package poc.java.concurrency.api.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.owasp.encoder.Encode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import poc.java.concurrency.api.persistence.entity.FirstHalfEntityProjection;
import poc.java.concurrency.api.persistence.repo.FirstHalfRepository;

@Service
public class ProcessService {
  
  private final FirstHalfRepository firstHalfRepository;
  private final RestClient restClient;
  
  public ProcessService(FirstHalfRepository firstHalfRepository, RestClient restClient) {
    this.firstHalfRepository = firstHalfRepository;
    this.restClient = restClient;
  }

  @Async("taskExecutor")
  public CompletableFuture<String> processValue(String value) throws InterruptedException, ExecutionException {
    CompletableFuture<FirstHalfEntityProjection> futureFirstHalf = getFirstHalf(value);
    CompletableFuture<String> futureSecondHalf = getSecondHalf(value);

    CompletableFuture<String> combinedFuture = futureFirstHalf.thenCombine(futureSecondHalf, (result1, result2) -> String.format("%s%s",result1.getFirstHalf(), result2));

    return combinedFuture;
  }

  private CompletableFuture<FirstHalfEntityProjection> getFirstHalf(String value) {
    var encodedValue = Encode.forJava(value);
    var firstHalfProjectionFuture = firstHalfRepository.findFirstHalfEntityProjectionByPro(encodedValue);
    return firstHalfProjectionFuture;
  }

  private CompletableFuture<String> getSecondHalf(String value) {
    var encodedValue = Encode.forUriComponent(value);
    var uri = String.format("/lookup/%s", encodedValue);

    return CompletableFuture.supplyAsync(() -> {
      String secondHalf = restClient.get()
        .uri(uri)
        .retrieve()
        .onStatus(status -> status.value() == 404, (request, response) -> {
          throw new RuntimeException("value found: " + encodedValue);
        })
        .body(String.class);

      if (secondHalf == null) {
        throw new RuntimeException("value found: " + encodedValue);
      }
      return secondHalf;
    });
  }
}
