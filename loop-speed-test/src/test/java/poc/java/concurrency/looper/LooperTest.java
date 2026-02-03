package poc.java.concurrency.looper;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LooperTest {

  private ArrayList<String> arrayListStrings;
  private LinkedList<String> linkedListStrings;

  @BeforeAll
  void setUp() {
    arrayListStrings = Utils.arrayListStrings(100000, 64);
    linkedListStrings = Utils.linkedListStrings(100000, 64);

    System.out.println(String.format("Number of CPUs: %s", Runtime.getRuntime().availableProcessors()));
  }

  // ArrayList Tests
  @RepeatedTest(5)
  void forLooper_arrayList() {
    Utils.timeTask("forLooper ArrayList", () -> Looper.forLooper(arrayListStrings));
  }

  @RepeatedTest(5)
  void spliteratorLooper_arrayList() {
    Utils.timeTask("spliteratorLooper ArrayList", () -> Looper.spliteratorLooper(arrayListStrings));
  }

  @RepeatedTest(5)
  void streamLooper_arrayList() {
    Utils.timeTask("streamLooper ArrayList", () -> Looper.streamLooper(arrayListStrings));
  }

  @RepeatedTest(5)
  void streamParallelLooper_arrayList() {
    Utils.timeTask("streamParallelLooper ArrayList", () -> Looper.streamParallelLooper(arrayListStrings));
  }

  // LinkedList Tests
  @RepeatedTest(5)
  void forLooper_linkedList() {
    Utils.timeTask("forLooper LinkedList", () -> Looper.forLooper(linkedListStrings));
  }

  @RepeatedTest(5)
  void spliteratorLooper_linkedList() {
    Utils.timeTask("spliteratorLooper LinkedList", () -> Looper.spliteratorLooper(linkedListStrings));
  }

  @RepeatedTest(5)
  void streamLooper_linkedList() {
   Utils.timeTask("streamLooper LinkedList", () -> Looper.streamLooper(linkedListStrings));
  }

  @RepeatedTest(5)
  void streamParallelLooper_linkedList() {
    Utils.timeTask("streamParallelLooper LinkedList", () -> Looper.streamParallelLooper(linkedListStrings));
  }

}
