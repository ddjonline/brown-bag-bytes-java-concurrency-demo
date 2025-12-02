package poc.java.concurrency.api.service;

import org.owasp.encoder.Encode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import poc.java.concurrency.api.persistence.repo.FirstHalfRepository;

@Service
public class ProcessService {
  
  private final FirstHalfRepository firstHalfRepository;
  private final RestClient restClient;
  
  public ProcessService(FirstHalfRepository firstHalfRepository, RestClient restClient) {
    this.firstHalfRepository = firstHalfRepository;
    this.restClient = restClient;
  }

  public String processValue(String value) {
    // get first half from database
    var firstHalf = getFirstHalf(value);

    // get second half from REST API
    var secondHalf = getSecondHalf(value);

    // return combined result
    return String.format("%s%s",firstHalf, secondHalf);
  }

  private String getFirstHalf(String value) {
    var encodedValue = Encode.forJava(value);
    var firstHalfProjection = firstHalfRepository.findFirstHalfEntityProjectionByPro(encodedValue);
    return firstHalfProjection.getFirstHalf();
  }

  private String getSecondHalf(String value) {
    var encodedValue = Encode.forUriComponent(value);
    var uri = String.format("/lookup/%s", encodedValue);

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
  }
}
