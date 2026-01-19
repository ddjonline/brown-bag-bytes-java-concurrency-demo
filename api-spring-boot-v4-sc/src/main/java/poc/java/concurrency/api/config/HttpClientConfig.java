package poc.java.concurrency.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.service.registry.ImportHttpServices;

import poc.java.concurrency.api.client.SecondHalfLookupHttpClient;

@Configuration
@ImportHttpServices({ SecondHalfLookupHttpClient.class })
public class HttpClientConfig {

  // added to force order of operations in spring boot configuration instantiation
  @SuppressWarnings("unused")
  private final RestClientProperties restClientProperties;

  public HttpClientConfig(RestClientProperties restClientProperties) {
    this.restClientProperties = restClientProperties;
  }

  @Bean
  RestClient.Builder restClient() {
    return RestClient.builder();
  }

  @Bean
  HttpServiceProxyFactory proxyFactory(RestClient.Builder clientBuilder, RestClientProperties restClientProperties) {
    RestClient client = clientBuilder.baseUrl(restClientProperties.url()).build();
    return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(client)).build();
  }

  @Bean
  SecondHalfLookupHttpClient secondHalfLookupHttpClient(HttpServiceProxyFactory factory) {
    return factory.createClient(SecondHalfLookupHttpClient.class);
  }
}
