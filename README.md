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

## Kubernetes Deployment

The application can be deployed to a Kubernetes cluster using the provided manifests.

1.  **Prerequisites:**
    *   A running Kubernetes cluster (e.g., Minikube, Docker Desktop).
    *   `kubectl` configured to connect to your cluster.
    *   An Ingress controller (e.g., NGINX) installed in your cluster.

2.  **Deployment:**

    Apply the manifests for each version of the microservice:

    ```bash
    # Deploy v1.0
    kubectl apply -f k8s/v1-deployment.yaml
    kubectl apply -f k8s/v1-hpa.yaml

    # Deploy v1.1
    kubectl apply -f k8s/v1.1-deployment.yaml
    kubectl apply -f k8s/v1.1-hpa.yaml

    # Deploy v2.0
    kubectl apply -f k8s/v2-deployment.yaml
    kubectl apply -f k8s/v2-hpa.yaml

    # Deploy the Ingress
    kubectl apply -f k8s/ingress.yaml
    ```

3.  **Accessing the Service:**

    Find the external IP of your Ingress controller and access the services at the following paths:
    *   `http://<ingress-ip>/v1/products`
    *   `http://<ingress-ip>/v1.1/products`
    *   `http://<ingress-ip>/v2/products`

## CI/CD Pipeline

This project uses GitHub Actions for CI/CD. The pipeline is defined in `.github/workflows/ci-cd.yaml` and has the following stages:

1.  **Build:** Builds the application and runs tests on every push to `main`.
2.  **Build and Push Docker Image:** Builds and pushes a Docker image to Docker Hub when a new tag is pushed.
3.  **Deploy to Kubernetes:** Deploys the new image to Kubernetes when a new tag is pushed.

### Secrets

The following secrets need to be configured in your GitHub repository for the pipeline to work:

*   `DOCKER_HUB_USERNAME`: Your Docker Hub username.
*   `DOCKER_HUB_ACCESS_TOKEN`: A Docker Hub access token with read/write permissions.
*   `KUBECONFIG`: Your Kubernetes configuration file, base64 encoded.