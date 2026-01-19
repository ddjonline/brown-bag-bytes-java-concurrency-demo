package poc.java.concurrency.api.service;

import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;

import org.owasp.encoder.Encode;
import org.springframework.stereotype.Service;

import poc.java.concurrency.api.client.SecondHalfLookupHttpClient;
import poc.java.concurrency.api.persistence.repo.FirstHalfRepository;

@Service
public class ProcessService {

  private final FirstHalfRepository firstHalfRepository;
  private final SecondHalfLookupHttpClient secondHalfLookupHttpClient;

  public ProcessService(FirstHalfRepository firstHalfRepository,
      SecondHalfLookupHttpClient secondHalfLookupHttpClient) {
    this.firstHalfRepository = firstHalfRepository;
    this.secondHalfLookupHttpClient = secondHalfLookupHttpClient;
  }

  public String processValue(String value) throws InterruptedException {
    try (var scope = StructuredTaskScope.open())  {
      var firstHalfFuture = scope.fork(() -> getFirstHalf(value));
      var secondHalfFuture = scope.fork(() -> getSecondHalf(value));

      // Guarantees all succeeded or throws an exception and cancels all tasks
      scope.join();
      
      // return combined result
      return String.format("%s%s", firstHalfFuture.get(), secondHalfFuture.get());
    }
  }

  private String getFirstHalf(String value) {
    var encodedValue = Encode.forJava(value);
    var firstHalfProjection = firstHalfRepository.findFirstHalfEntityProjectionByPro(encodedValue);
    return firstHalfProjection.getFirstHalf();
  }

  private String getSecondHalf(String value) {
    var encodedValue = Encode.forUriComponent(value);

    String secondHalf = secondHalfLookupHttpClient.lookup(encodedValue);

    if (secondHalf == null) {
      throw new RuntimeException("value found: " + encodedValue);
    }

    return secondHalf;
  }
}
