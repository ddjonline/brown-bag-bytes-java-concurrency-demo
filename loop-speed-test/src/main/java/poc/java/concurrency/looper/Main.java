package poc.java.concurrency.looper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

  public static final int LIST_COUNT = 100000;

  public static void main(String[] args) {

    List<String> arrayListStrings = arrayListStrings();
    List<String> linkedListStrings = linkedListStrings();

    System.out.println("--- Running benchmarks sequentially ---");
    timeTask("forLooper ArrayList", () -> Looper.forLooper(arrayListStrings));
    timeTask("spliteratorLooper ArrayList", () -> Looper.spliteratorLooper(arrayListStrings));
    timeTask("streamLooper ArrayList", () -> Looper.streamLooper(arrayListStrings));
    timeTask("streamParallelLooper ArrayList", () -> Looper.streamParallelLooper(arrayListStrings));
    timeTask("forLooper LinkedList", () -> Looper.forLooper(linkedListStrings));
    timeTask("spliteratorLooper LinkedList", () -> Looper.spliteratorLooper(linkedListStrings));
    timeTask("streamLooper LinkedList", () -> Looper.streamLooper(linkedListStrings));
    timeTask("streamParallelLooper LinkedList", () -> Looper.streamParallelLooper(linkedListStrings));

    System.out.println("\n--- Running benchmarks concurrently with Standard Thread Executor ---");
    try (ExecutorService stdExecutor = standardExecutor()) {
      stdExecutor.submit(() -> timeTask("forLooper ArrayList (Std Executor)", () -> Looper.forLooper(arrayListStrings)));
      stdExecutor.submit(() -> timeTask("spliteratorLooper ArrayList (Std Executor)", () -> Looper.spliteratorLooper(arrayListStrings)));
      stdExecutor.submit(() -> timeTask("streamLooper ArrayList (Std Executor)", () -> Looper.streamLooper(arrayListStrings)));
      stdExecutor.submit(() -> timeTask("streamParallelLooper ArrayList (Std Executor)", () -> Looper.streamParallelLooper(arrayListStrings)));
      stdExecutor.submit(() -> timeTask("forLooper LinkedList (Std Executor)", () -> Looper.forLooper(linkedListStrings)));
      stdExecutor.submit(() -> timeTask("spliteratorLooper LinkedList (Std Executor)", () -> Looper.spliteratorLooper(linkedListStrings)));
      stdExecutor.submit(() -> timeTask("streamLooper LinkedList (Std Executor)", () -> Looper.streamLooper(linkedListStrings)));
      stdExecutor.submit(() -> timeTask("streamParallelLooper LinkedList (Std Executor)", () -> Looper.streamParallelLooper(linkedListStrings)));
    }

    System.out.println("\n--- Running benchmarks concurrently with Virtual Threads ---");
    try (ExecutorService vtExecutor = virtualThreadExecutor()) {
      vtExecutor.submit(() -> timeTask("forLooper ArrayList (VT)", () -> Looper.forLooper(arrayListStrings)));
      vtExecutor.submit(() -> timeTask("spliteratorLooper ArrayList (VT)", () -> Looper.spliteratorLooper(arrayListStrings)));
      vtExecutor.submit(() -> timeTask("streamLooper ArrayList (VT)", () -> Looper.streamLooper(arrayListStrings)));
      vtExecutor.submit(() -> timeTask("streamParallelLooper ArrayList (VT)", () -> Looper.streamParallelLooper(arrayListStrings)));
      vtExecutor.submit(() -> timeTask("forLooper LinkedList (VT)", () -> Looper.forLooper(linkedListStrings)));
      vtExecutor.submit(() -> timeTask("spliteratorLooper LinkedList (VT)", () -> Looper.spliteratorLooper(linkedListStrings)));
      vtExecutor.submit(() -> timeTask("streamLooper LinkedList (VT)", () -> Looper.streamLooper(linkedListStrings)));
      vtExecutor.submit(() -> timeTask("streamParallelLooper LinkedList (VT)", () -> Looper.streamParallelLooper(linkedListStrings)));
    }
  }

  private static void timeTask(String description, Runnable task) {
    Instant instantNow = Instant.now();
    task.run();
    long duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.println(String.format("%s duration %s", description, duration));
  }

  public static List<String> arrayListStrings() {
    List<String> arrayListStrings = new ArrayList<>();
    for (int i = 0; i < LIST_COUNT; i++) {
      StringBuilder sb = new StringBuilder();
      for (int j = 0; j < 64; j++) {
        sb.append((char) (Math.random() * 94 + 33));
      }
      arrayListStrings.add(sb.toString());
    }
    return arrayListStrings;
  }

  public static List<String> linkedListStrings() {
    List<String> arrayListStrings = new LinkedList<>();
    for (int i = 0; i < LIST_COUNT; i++) {
      StringBuilder sb = new StringBuilder();
      for (int j = 0; j < 64; j++) {
        sb.append((char) (Math.random() * 94 + 33));
      }
      arrayListStrings.add(sb.toString());
    }
    return arrayListStrings;
  }

  public static ExecutorService standardExecutor() {
    return Executors.newFixedThreadPool(50);
  }

  public static ExecutorService virtualThreadExecutor() {
    return Executors.newVirtualThreadPerTaskExecutor();
  }
}