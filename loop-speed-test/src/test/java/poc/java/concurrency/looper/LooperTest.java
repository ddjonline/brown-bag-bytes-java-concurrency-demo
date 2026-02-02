package poc.java.concurrency.looper;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;
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
  void setUp() throws IOException, URISyntaxException {
    arrayListStrings = readArrayList();
    linkedListStrings = readLinkedList();
  }

  @RepeatedTest(5)
  void forLooper_arrayList() {
    Instant instantNow = Instant.now();
    List<String> actual = Looper.forLooper(arrayListStrings);
    long duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.println(String.format("forLooper ArrayList duration %s", duration));
    assertNotNull(actual);
  }

  @RepeatedTest(5)
  void spliteratorLooper_arrayList() {
    Instant instantNow = Instant.now();
    List<String> actual = Looper.spliteratorLooper(arrayListStrings);
    long duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.println(String.format("spliteratorLooper ArrayList duration %s", duration));
    assertNotNull(actual);
  }

  @RepeatedTest(5)
  void streamLooper_arrayList() {
    Instant instantNow = Instant.now();
    List<String> actual = Looper.streamLooper(arrayListStrings);
    long duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.println(String.format("streamLooper ArrayList duration %s", duration));
    assertNotNull(actual);
  }

  @RepeatedTest(5)
  void streamParallelLooper_arrayList() {
    Instant instantNow = Instant.now();
    List<String> actual = Looper.streamParallelLooper(arrayListStrings);
    long duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.println(String.format("streamParallelLooper ArrayList duration %s", duration));
    assertNotNull(actual);
  }

  @RepeatedTest(5)
  void forLooper_linkedList() {
    Instant instantNow = Instant.now();
    List<String> actual = Looper.forLooper(linkedListStrings);
    long duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.println(String.format("forLooper LinkedList duration %s", duration));
    assertNotNull(actual);
  }

  @RepeatedTest(5)
  void spliteratorLooper_linkedList() {
    Instant instantNow = Instant.now();
    List<String> actual = Looper.spliteratorLooper(linkedListStrings);
    long duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.println(String.format("spliteratorLooper LinkedList duration %s", duration));
    assertNotNull(actual);
  }

  @RepeatedTest(5)
  void streamLooper_linkedList() {
    Instant instantNow = Instant.now();
    List<String> actual = Looper.streamLooper(linkedListStrings);
    long duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.println(String.format("streamLooper LinkedList duration %s", duration));
    assertNotNull(actual);
  }

  @RepeatedTest(5)
  void streamParallelLooper_linkedList() {
    Instant instantNow = Instant.now();
    List<String> actual = Looper.streamParallelLooper(linkedListStrings);
    long duration = Instant.now().toEpochMilli() - instantNow.toEpochMilli();
    System.out.println(String.format("streamParallelLooper LinkedList duration %s", duration));
    assertNotNull(actual);
  }

  private ArrayList<String> readArrayList() throws IOException, URISyntaxException {
    return java.nio.file.Files.readAllLines(
        java.nio.file.Paths.get(
            getClass().getClassLoader().getResource("strings.txt").toURI()))
        .stream().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
  }

  private LinkedList<String> readLinkedList() throws IOException, URISyntaxException {
    return java.nio.file.Files.readAllLines(
        java.nio.file.Paths.get(
            getClass().getClassLoader().getResource("strings.txt").toURI()))
        .stream().collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
  }
}
