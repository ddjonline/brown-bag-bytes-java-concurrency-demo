# api-vertx

## Develop

Build the project

```shell
mvn -U clean verify
```

Run the project (Vertx dev mode)

```shell
mvn exec:java
```

Run the project (Jvm Uber/Fat/Runner Jar)

```shell
mvn clean package
java -jar target/*-fat.jar
```

## Docker

### Build docker image

```shell
mvn -U clean package && docker build  -t api-vertx .
```

### Docker run the container

#### Docker run without resource limits

```shell
docker run -p 9080:8080 -p 10084:10084  api-vertx
```

#### Docker run with CPU limits (higher than proxy containers)

```shell
docker run -p 9080:8080 -p 10084:10084 --cpus=4  api-vertx
```
