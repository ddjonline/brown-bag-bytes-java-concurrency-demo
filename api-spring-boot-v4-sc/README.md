# api-spring-boot-v4-sc

Prerequisite: Java 25

## Run as a Jar

Structured Concurrency still requires preview enable Structured Concurrency.

```shell
java --enable-preview -jar target/api-spring-boot-v4-sc*.jar
```

## Summary: Java Structured Concurrency

This project demonstrates the usage and benefits of **Structured Concurrency**, a preview feature in modern Java.

### Usage
Structured Concurrency treats multiple tasks running in different threads as a single unit of work. It is primarily used via the `StructuredTaskScope` API.
- **`StructuredTaskScope`**: A try-with-resources block ensures that a scope is opened and closed correctly.
- **`fork()`**: Subtasks are forked within the scope.
- **`join()`**: The scope waits for all subtasks to complete.
- **`throwIfFailed()`**: Propagates exceptions if any subtask fails.

### Benefits
- **Error Handling**: If a subtask fails, the scope can automatically cancel other running subtasks, preventing resource leaks and inconsistent states.
- **Cancellation**: When the parent task is cancelled, all subtasks are automatically cancelled.
- **Observability**: Thread dumps show the hierarchy of tasks, making it easier to debug concurrent code.
- **Readability**: Code looks sequential and is easier to reason about compared to nested callbacks or complex `CompletableFuture` chains.
