# Docker Compose Summary

## Services Overview

The setup orchestrates a set of monitoring tools, databases, a resource service, and multiple API implementations demonstrating different concurrency models.

### Monitoring & Observability
*   **`prometheus`**:
    *   **Image**: `prom/prometheus`
    *   **Port**: `9090`
    *   **Config**: Mounts `prometheus.yml` from `./env/prometheus/`.
    *   **Role**: Collects metrics from the services.
*   **`grafana`**:
    *   **Image**: `grafana/grafana-oss:latest`
    *   **Port**: `3000`
    *   **Config**: Uses `grafana.env` and mounts provisioning configs.
    *   **Role**: Visualizes metrics collected by Prometheus.

### Databases
*   **`postgres`**:
    *   **Image**: `postgres:latest`
    *   **Port**: `5432`
    *   **Credentials**: `myuser` / `my_awesome_password` / `mydatabase`
    *   **Init**: Scripts from `./env/postgres/init-scripts`.
    *   **Healthcheck**: Checks if the database is ready to accept connections.
    *   **Role**: Primary data store for the API services.
*   **`maria`**:
    *   **Image**: `mariadb:latest`
    *   **Port**: `3306`
    *   **Credentials**: `user` / `my_awesome_password` / `my_database`
    *   **Init**: Schema and seed data from `./env/mariadb/init-scripts`.
    *   **Healthcheck**: Simple ping check.
    *   **Role**: Backend database for the `resource-vertx` service.

### Resource Service
*   **`resource-vertx`**:
    *   **Build**: `./resource-vertx`
    *   **Port**: `9080` (mapped to internal `8080`)
    *   **Dependencies**: Depends on `maria`.
    *   **Role**: Acts as an external resource/upstream service that the API services communicate with.

### API Services (Concurrency Demos)
All API services depend on `resource-vertx` (started) and `postgres` (healthy). They are configured with the same database and resource URL environment variables.

*   **`api-spring-boot-basic`**:
    *   **Build**: `./api-spring-boot-basic`
    *   **Port**: `8081`
    *   **Description**: Standard Spring Boot implementation.
*   **`api-spring-boot-basic-vt`**:
    *   **Build**: `./api-spring-boot-basic-vt`
    *   **Port**: `8082`
    *   **Description**: Spring Boot with Virtual Threads.
*   **`api-spring-boot-async`**:
    *   **Build**: `./api-spring-boot-async`
    *   **Port**: `8083`
    *   **Description**: Spring Boot using asynchronous programming (`CompletableFuture`).
*   **`api-spring-boot-v4-basic`**:
    *   **Build**: `./api-spring-boot-v4-basic`
    *   **Port**: `8084`
    *   **Description**: Spring Boot v4 basic implementation.
*   **`api-spring-boot-v4-sc`**:
    *   **Build**: `./api-spring-boot-v4-sc`
    *   **Port**: `8085`
    *   **Description**: Spring Boot v4 using Structured Concurrency.
*   **`api-vertx`**:
    *   **Build**: `./api-vertx`
    *   **Port**: `8090`
    *   **Description**: Vert.x implementation.

## Network & Interactions

*   **Network**: All services reside on the `poc-concurrency-testing-network` (bridge driver), allowing them to communicate using service names as hostnames.
*   **Flow**:
    1.  **Databases** (`postgres`, `maria`) start and initialize.
    2.  **Resource Service** (`resource-vertx`) starts, connecting to `maria`.
    3.  **API Services** start, connecting to `postgres` for data and `resource-vertx` for external calls.
    4.  **Monitoring** (`prometheus`) scrapes metrics from the services, which can be viewed in `grafana`.

## Environment Variables
Common environment variables used across API services:
*   `POSTGRESQL_URL`: `jdbc:postgresql://postgres:5432/mydatabase`
*   `POSTGRESQL_USER`: `myuser`
*   `POSTGRESQL_PWD`: `my_awesome_password`
*   `RESOURCE_URL`: `http://resource-vertx:8080`
