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

1.  **Prerequisites:**
    *   A running Kubernetes cluster (e.g., Minikube, Docker Desktop).
    *   `kubectl` configured to connect to your cluster.
    *   [Helm](https://helm.sh/) installed.
    *   An Ingress controller (e.g., NGINX) installed in your cluster.

2.  **Deployment:**

    Deploy each version using Helm (replace `<docker-hub-username>` with your Docker Hub username if needed):

    ```sh
    # Deploy v1.0.0
    helm install v1 ./product-service-chart \
      --set app.versionLabel=v1 \
      --set app.namespace=v1 \
      --set image.tag=v1.0.0 \
      --namespace v1 \
      --create-namespace

    # Deploy v1.1.0
    helm install v1-1 ./product-service-chart \
      --set app.versionLabel=v1-1 \
      --set app.namespace=v1-1 \
      --set image.tag=v1.1.0 \
      --namespace v1-1 \
      --create-namespace

    # Deploy v2.0.0
    helm install v2 ./product-service-chart \
      --set app.versionLabel=v2 \
      --set app.namespace=v2 \
      --set image.tag=v2.0.0 \
      --namespace v2 \
      --create-namespace
    ```

    > **Note:** If you get an error about resources already existing, delete the namespace or resource before re-installing.

3.  **Accessing the Service:**

    Find the external IP of your Ingress controller and access the services at the following paths:
    *   `http://<ingress-ip>/v1/products`
    *   `http://<ingress-ip>/v1.1/products`
    *   `http://<ingress-ip>/v2/products`

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