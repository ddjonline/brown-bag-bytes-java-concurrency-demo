package poc.java.concurrency.looper;

import java.util.ArrayList;
import java.util.List;

public interface Looper {



  // This method iterates through a list of strings using a for-each loop.
  public static List<String> forLooper(List<String> strings) {
    List<String> upperCasedStrings = new ArrayList<>();
    for (String s : strings) {
      String reversed = new StringBuilder(s).reverse().toString();
      upperCasedStrings.add(reversed.toUpperCase());
      upperCasedStrings.add(s.toUpperCase());
    }
    return upperCasedStrings;
  }






  // This method iterates through a list of strings using a Spliterator.
  public static List<String> spliteratorLooper(List<String> strings) {
    List<String> upperCasedStrings = new ArrayList<>();
    strings.spliterator().forEachRemaining(s -> { 
      String reversed = new StringBuilder(s).reverse().toString();
      upperCasedStrings.add(reversed.toUpperCase());
      upperCasedStrings.add(s.toUpperCase());
    });
    return upperCasedStrings;
  }






  // This method iterates through a list of strings using a standard Java Stream
  public static List<String> streamLooper(List<String> strings) {
    return strings.stream()
        .map(s -> new StringBuilder(s).reverse().toString())
        .map(String::toUpperCase) 
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
  }






  // This method iterates through a list of strings using a parallel Stream using default Spliterator.
  public static List<String> streamParallelLooper(List<String> strings) {
    return strings.parallelStream()
        .map(s -> new StringBuilder(s).reverse().toString())
        .map(String::toUpperCase)
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
  }
}
