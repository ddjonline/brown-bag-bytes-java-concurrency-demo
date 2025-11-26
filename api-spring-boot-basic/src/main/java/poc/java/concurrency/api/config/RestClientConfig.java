package poc.java.concurrency.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  // added to force order of operations in spring boot configuration instantiation
  @SuppressWarnings("unused")
  private final RestClientProperties restClientProperties;

  public RestClientConfig(RestClientProperties restClientProperties) {
    this.restClientProperties = restClientProperties;
  }

  public RestClient myRestClient(RestClientProperties restClientProperties) {
    return RestClient.builder()
        .baseUrl(restClientProperties.url()) 
        // .defaultHeader("Accept", "application/json")
        .build();
  }
}
