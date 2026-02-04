package poc.java.concurrency.looper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;

public class Main {

  public static final int LIST_COUNT = 100000;
  public static final int CHARACTER_COUNT = 64;
  // public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors(); // this is would make sense
  public static final int THREAD_COUNT = 48; // let's try to force more than there are CPUs/cores available

  public static void main(String[] args) {

    ArrayList<String> arrayListStrings = Utils.arrayListStrings(LIST_COUNT, CHARACTER_COUNT);
    LinkedList<String> linkedListStrings = Utils.linkedListStrings(LIST_COUNT, CHARACTER_COUNT);
    
    System.out.printf("Number of CPUs: %s%n", Runtime.getRuntime().availableProcessors());

    
    Utils.timeTask( "** sequential loopers total", () -> runSequentialLoopers(arrayListStrings, linkedListStrings));
    Utils.timeTask( "** standard threadconcurrent loopers total", () -> runConcurrentLoopers(arrayListStrings, linkedListStrings));
    Utils.timeTask( "** virtual thread concurrent loopers total", () -> runVirtualThreadLoopers(arrayListStrings, linkedListStrings));


  }
 
  private static void runSequentialLoopers(ArrayList<String> arrayListStrings, LinkedList<String> linkedListStrings) {
    System.out.println("--- Running benchmarks sequentially ---");
    Utils.timeTask("forLooper ArrayList", () -> Looper.forLooper(arrayListStrings));
    Utils.timeTask("spliteratorLooper ArrayList", () -> Looper.spliteratorLooper(arrayListStrings));
    Utils.timeTask("streamLooper ArrayList", () -> Looper.streamLooper(arrayListStrings));
    Utils.timeTask("streamParallelLooper ArrayList", () -> Looper.streamParallelLooper(arrayListStrings));
    Utils.timeTask("forLooper LinkedList", () -> Looper.forLooper(linkedListStrings));
    Utils.timeTask("spliteratorLooper LinkedList", () -> Looper.spliteratorLooper(linkedListStrings));
    Utils.timeTask("streamLooper LinkedList", () -> Looper.streamLooper(linkedListStrings));
    Utils.timeTask("streamParallelLooper LinkedList", () -> Looper.streamParallelLooper(linkedListStrings));
  }

  private static void runConcurrentLoopers(ArrayList<String> arrayListStrings, LinkedList<String> linkedListStrings) {
    System.out.println("\n--- Running benchmarks concurrently with Standard Thread Executor ---");
    try (ExecutorService stdExecutor = Utils.standardExecutor(THREAD_COUNT)) {
      stdExecutor.submit(() -> Utils.timeTask("forLooper ArrayList (Std Executor)", () -> Looper.forLooper(arrayListStrings)));
      stdExecutor.submit(() -> Utils.timeTask("spliteratorLooper ArrayList (Std Executor)", () -> Looper.spliteratorLooper(arrayListStrings)));
      stdExecutor.submit(() -> Utils.timeTask("streamLooper ArrayList (Std Executor)", () -> Looper.streamLooper(arrayListStrings)));
      stdExecutor.submit(() -> Utils.timeTask("streamParallelLooper ArrayList (Std Executor)", () -> Looper.streamParallelLooper(arrayListStrings)));
      stdExecutor.submit(() -> Utils.timeTask("forLooper LinkedList (Std Executor)", () -> Looper.forLooper(linkedListStrings)));
      stdExecutor.submit(() -> Utils.timeTask("spliteratorLooper LinkedList (Std Executor)", () -> Looper.spliteratorLooper(linkedListStrings)));
      stdExecutor.submit(() -> Utils.timeTask("streamLooper LinkedList (Std Executor)", () -> Looper.streamLooper(linkedListStrings)));
      stdExecutor.submit(() -> Utils.timeTask("streamParallelLooper LinkedList (Std Executor)", () -> Looper.streamParallelLooper(linkedListStrings)));
    }
  }

  private static void runVirtualThreadLoopers(ArrayList<String> arrayListStrings, LinkedList<String> linkedListStrings) {
    System.out.println("\n--- Running benchmarks concurrently with Virtual Threads ---");
    try (ExecutorService vtExecutor = Utils.virtualThreadExecutor()) {
      vtExecutor.submit(() -> Utils.timeTask("forLooper ArrayList (VT)", () -> Looper.forLooper(arrayListStrings)));
      vtExecutor.submit(() -> Utils.timeTask("spliteratorLooper ArrayList (VT)", () -> Looper.spliteratorLooper(arrayListStrings)));
      vtExecutor.submit(() -> Utils.timeTask("streamLooper ArrayList (VT)", () -> Looper.streamLooper(arrayListStrings)));
      vtExecutor.submit(() -> Utils.timeTask("streamParallelLooper ArrayList (VT)", () -> Looper.streamParallelLooper(arrayListStrings)));
      vtExecutor.submit(() -> Utils.timeTask("forLooper LinkedList (VT)", () -> Looper.forLooper(linkedListStrings)));
      vtExecutor.submit(() -> Utils.timeTask("spliteratorLooper LinkedList (VT)", () -> Looper.spliteratorLooper(linkedListStrings)));
      vtExecutor.submit(() -> Utils.timeTask("streamLooper LinkedList (VT)", () -> Looper.streamLooper(linkedListStrings)));
      vtExecutor.submit(() -> Utils.timeTask("streamParallelLooper LinkedList (VT)", () -> Looper.streamParallelLooper(linkedListStrings)));
    }
  }

}