package poc.java.concurrency.looper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {

  public static final int LIST_COUNT = 100000;

  public static void main(String[] args) {

    List<String> arrayListStrings = arrayListStrings();
    List<String> linkedListStrings = linkedListStrings();
    
    Instant instantNow = Instant.now();
    Looper.forLooper(arrayListStrings);
    long duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.println(String.format("forLooper ArrayList duration %s", duration));


    instantNow = Instant.now();
    Looper.spliteratorLooper(arrayListStrings);
    duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.println(String.format("spliteratorLooper ArrayList duration %s", duration));

    instantNow = Instant.now();
    Looper.streamLooper(arrayListStrings);
    duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.println(String.format("streamLooper ArrayList duration %s", duration));

    instantNow = Instant.now();
    Looper.streamParallelLooper(arrayListStrings);
    duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.println(String.format("streamParallelLooper ArrayList duration %s", duration));


    instantNow = Instant.now();
    Looper.forLooper(linkedListStrings);
    duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.println(String.format("forLooper LinkedList duration %s", duration));

    instantNow = Instant.now();
    Looper.spliteratorLooper(linkedListStrings);
    duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.println(String.format("spliteratorLooper LinkedList duration %s", duration));

    instantNow = Instant.now();
    Looper.streamLooper(linkedListStrings);
    duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.println(String.format("streamLooper LinkedList duration %s", duration));

    instantNow = Instant.now();
    Looper.streamParallelLooper(linkedListStrings);
    duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.println(String.format("streamParallelLooper LinkedList duration %s", duration));
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

}