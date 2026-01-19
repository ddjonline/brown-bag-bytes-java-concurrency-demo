package poc.java.concurrency.api.service;

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

  public String processValue(String value) {
    // get first half from database
    var firstHalf = getFirstHalf(value);

    // get second half from REST API
    var secondHalf = getSecondHalf(value);

    // return combined result
    return String.format("%s%s", firstHalf, secondHalf);
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
