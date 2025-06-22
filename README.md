# Product Catalogue Microservice

This is a sample Spring Boot application that provides a REST API for managing a product catalogue.

## Local Development

To run the application locally, you will need Java 8 and Maven installed.

```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:9191`.

## Containerization

The application can be containerized using Docker.

```bash
# Build the Docker image
docker build -t product-service .

# Run the Docker container
docker run -p 9191:9191 product-service
```

## Kubernetes Deployment (with Helm)

The application can be deployed to a Kubernetes cluster using Helm charts.

### Prerequisites
- A running Kubernetes cluster (e.g., Docker Desktop, Minikube)
- `kubectl` configured to connect to your cluster
- [Helm](https://helm.sh/) installed
- An NGINX Ingress controller installed in your cluster

### Deployment

Deploy each version using Helm (replace `<docker-hub-username>` with your Docker Hub username if needed):

```sh
# Deploy v1.0.0
helm upgrade --install product-v1 ./product-service-chart \
  -n v1 --create-namespace \
  --set app.versionLabel=v1 --set app.namespace=v1 --set image.tag="v1.0.0"

# Deploy v1.1.0
helm upgrade --install product-v1-1 ./product-service-chart \
  -n v1-1 --create-namespace \
  --set app.versionLabel=v1-1 --set app.namespace=v1-1 --set image.tag="v1.1.0"

# Deploy v2.0.0
helm upgrade --install product-v2 ./product-service-chart \
  -n v2 --create-namespace \
  --set app.versionLabel=v2 --set app.namespace=v2 --set image.tag="v2.0.0"
```

> **Note:** If you get an error about resources already existing, delete the namespace or resource before re-installing.

### Ingress and Path Routing

The NGINX Ingress controller is configured with regex paths and rewrite rules to support versioned routing:
- `/v1/products` → v1 namespace
- `/v1-1/products` → v1-1 namespace
- `/v2/products` → v2 namespace

The Ingress template uses:
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

### Accessing the Service

Access the services at the following paths (assuming Ingress is on localhost):
- http://localhost/v1/products
- http://localhost/v1-1/products
- http://localhost/v2/products

### Troubleshooting

- **404 Not Found (nginx):**
  - Check that the Ingress path and rewrite annotation are correct.
  - Make sure the Ingress is in the same namespace as the service.
  - Try both with and without a trailing slash.
- **502 Bad Gateway (nginx):**
  - Check that pods are running and READY.
  - Check service endpoints: `kubectl get endpoints -n <namespace>`
  - Check pod logs for errors or OOMKilled status.
- **503 Service Unavailable (nginx):**
  - No healthy endpoints for the service (pods not ready or service selector mismatch).

## CI/CD Pipeline

This project uses GitHub Actions for CI/CD. The pipeline is defined in `.github/workflows/ci-cd.yaml` and has the following stages:

1.  **Build:** Builds the application and runs tests on every push to `main`.
2.  **Build and Push Docker Image:** Builds and pushes a Docker image to Docker Hub when a new tag is pushed.
3.  **Deploy to Kubernetes (with Helm):** Deploys the new image to Kubernetes using Helm when a new tag is pushed.

### Secrets

The following secrets need to be configured in your GitHub repository for the pipeline to work:

*   `DOCKER_HUB_USERNAME`: Your Docker Hub username.
*   `DOCKER_HUB_ACCESS_TOKEN`: A Docker Hub access token with read/write permissions.
*   `KUBECONFIG`: Your Kubernetes configuration file, base64 encoded.