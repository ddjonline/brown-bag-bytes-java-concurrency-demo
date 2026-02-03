# loop-speed-test

## Develop

### Build it

```shell
mvn -U clean package
```

### Run it

```shell
java -jar target/*.jar
```

### Run it in Docker

```shell
docker run loop-speed-test
```

Use only one CPU (assuming OS docker install will respect request)
```shell
docker run --cpus="1.0" loop-speed-test
```
