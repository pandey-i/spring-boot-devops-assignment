# System Design

This document outlines the system design for the product catalogue microservice.

## Architecture

The system is designed as a microservice architecture, with the product catalogue service being a standalone component. This approach allows for independent development, deployment, and scaling of the service.

### Components

-   **Product Service:** A Spring Boot application that provides a REST API for managing products.
-   **H2 Database:** An in-memory database used for storing product data. This was chosen to simplify the development and containerization process, as it removes the need for an external database.
-   **Docker:** Used for containerizing the application, ensuring a consistent and portable runtime environment.
-   **Kubernetes:** Used for orchestrating the deployment, scaling, and management of the containerized application.
-   **Helm:** Used for templating and managing Kubernetes resources, enabling versioned, repeatable, and parameterized deployments. Each version is deployed as a separate Helm release in its own namespace.
-   **NGINX Ingress Controller:** Used for routing external traffic to the different versions of the microservice based on the URL path. Regex paths and rewrite rules are used for robust versioned routing.
-   **GitHub Actions:** Used for automating the CI/CD pipeline, from building and testing to deploying the application.

### Versioning

The microservice is versioned using semantic versioning. Each version is deployed as a separate Helm release in its own Kubernetes namespace to ensure isolation. The Ingress controller routes traffic to the appropriate version based on the URL path (`/v1`, `/v1-1`, `/v2`).

#### Ingress Path Routing
- Regex paths and rewrite rules are used in the Ingress to support versioned endpoints:
  - `/v1/products` → v1 namespace
  - `/v1-1/products` → v1-1 namespace
  - `/v2/products` → v2 namespace
- The Ingress template uses:
  ```yaml
  - path: /{{ .Values.app.versionLabel }}(/|$)(.*)
    pathType: ImplementationSpecific
    backend:
      service:
        name: {{ .Values.app.name }}-{{ .Values.app.versionLabel }}
        port:
          number: {{ .Values.service.port }}
  ```
  With annotation:
  ```yaml
  nginx.ingress.kubernetes.io/rewrite-target: /$2
  ```

### Scalability

The system is designed to be scalable. Helm charts template the deployment and Horizontal Pod Autoscaler (HPA) resources, allowing for easy adjustment of scaling policies per version.

### Security

-   **Secrets Management:** Environment variables and secrets are managed securely using GitHub secrets for the CI/CD pipeline and will be injected into the Kubernetes pods.
-   **Resource Management:** CPU and memory limits are defined in the Kubernetes deployment manifests to prevent resource exhaustion.
-   **Network Policies:** (Future work) Network policies can be added to restrict traffic between pods and namespaces.
-   **TLS:** (Future work) TLS can be configured on the Ingress to secure communication with the microservice.

### Troubleshooting
- If you encounter 404, 502, or 503 errors from NGINX, check that:
  - The Ingress path and rewrite annotation are correct and match the version label.
  - The service selector matches the pod labels.
  - Pods are running and READY.
  - Endpoints are present for the service.