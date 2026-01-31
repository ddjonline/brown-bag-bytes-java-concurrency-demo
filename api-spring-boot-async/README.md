# api-spring-boot-async

## Develop

Build the project

```shell
mvn -U clean verify
```

Run the project (Spring Boot dev mode)

```shell
mvn spring-boot:run
```

Run the project (Jvm Uber/Fat/Runner Jar)

```shell
mvn package
java -jar target/*.jar
```

## Summary: Java Async with CompletableFuture

This project demonstrates the usage and benefits of asynchronous programming in Java using `CompletableFuture` for parallel workflows.

### Usage
`CompletableFuture` provides a powerful API for composing asynchronous operations.
- **`supplyAsync()`**: Initiates an asynchronous computation, typically running on a separate thread (e.g., via `ForkJoinPool`).
- **Chaining**: Methods like `thenApply`, `thenAccept`, and `thenCompose` allow you to chain processing steps that execute once the previous stage completes.
- **Composition**: `CompletableFuture.allOf()` or `anyOf()` can be used to wait for multiple independent futures to complete, enabling parallel execution of tasks.
- **Exception Handling**: Methods like `exceptionally` or `handle` provide a way to manage errors in the asynchronous pipeline.

### Benefits
- **Non-blocking**: The main thread is not blocked waiting for I/O or long-running tasks, improving application responsiveness.
- **Parallelism**: Independent tasks can run concurrently, significantly reducing the total processing time for complex workflows.
- **Resource Efficiency**: Threads are released back to the pool while waiting for external resources (in non-blocking I/O scenarios), allowing better scalability under load.
- **Functional Style**: The API encourages a functional programming style, making it easier to express complex data flow transformations.
