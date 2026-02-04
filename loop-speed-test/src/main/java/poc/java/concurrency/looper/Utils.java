package poc.java.concurrency.looper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface Utils {
  
  static void timeTask(String description, Runnable task) {
    Instant instantNow = Instant.now();
    task.run();
    long duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.printf("%s duration %s%n", description, duration);
  }

  static ArrayList<String> arrayListStrings(int lineCount, int charCount) {
    ArrayList<String> arrayListStrings = new ArrayList<>();
    for (int i = 0; i < lineCount; i++) {
      StringBuilder sb = new StringBuilder();
      for (int j = 0; j < charCount; j++) {
        sb.append((char) (Math.random() * 94 + 33));
      }
      arrayListStrings.add(sb.toString());
    }
    return arrayListStrings;
  }

  static LinkedList<String> linkedListStrings(int lineCount, int charCount) {
    LinkedList<String> arrayListStrings = new LinkedList<>();
    for (int i = 0; i < lineCount; i++) {
      StringBuilder sb = new StringBuilder();
      for (int j = 0; j < charCount; j++) {
        sb.append((char) (Math.random() * 94 + 33));
      }
      arrayListStrings.add(sb.toString());
    }
    return arrayListStrings;
  }

  static ExecutorService standardExecutor(int threadCount) {
    return Executors.newFixedThreadPool(threadCount);
  }

  static ExecutorService virtualThreadExecutor() {
    return Executors.newVirtualThreadPerTaskExecutor();
  }
}
