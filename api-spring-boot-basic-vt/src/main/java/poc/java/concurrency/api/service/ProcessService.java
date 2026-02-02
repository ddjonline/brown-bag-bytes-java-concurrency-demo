package poc.java.concurrency.api.service;

import org.owasp.encoder.Encode;
import org.springframework.stereotype.Service;

import poc.java.concurrency.api.client.SecondHalfFeignClient;
import poc.java.concurrency.api.persistence.repo.FirstHalfRepository;

@Service
public class ProcessService {
  
  private final FirstHalfRepository firstHalfRepository;
  private final SecondHalfFeignClient secondHalfFeignClient;
  
  public ProcessService(FirstHalfRepository firstHalfRepository, 
      SecondHalfFeignClient secondHalfFeignClient) {
    this.firstHalfRepository = firstHalfRepository;
    this.secondHalfFeignClient = secondHalfFeignClient;
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

    return secondHalfFeignClient.getLookup(encodedValue);
  }
}
