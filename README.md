# brown-bag-bytes-java-concurrency-demo

This project is meant as a demonstration of comparing and testing multiple options for basic preformance improvements for Java-based applicaiton patterns primarily by means of concurrency support.

## Docker

### Docker Compose Start/Up

Option1: Docker compose start and view logs

```shell
docker compose up
```

Option2: Docker compose start in detached mode (view the logs via Docker Desktop or VSCode Docker extension)

```shell
docker compose up -d
```

### Docker Compose Stop/Down

```shell
docker compose down
```

### Prometheus

The Prometheus dashboards can be viewed on http://localhost:9090 

### Grafana Dashboard

The Grafana dashboards can be viewed on http://localhost:3000