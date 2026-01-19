package poc.java.concurrency.api.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
// import org.springframework.web.service.annotation.HttpExchange

// @HttpExchange(url = "/lookup", accept = "text/plain")
public interface SecondHalfLookupHttpClient {
  
  @GetExchange("/lookup/{value}")
  String lookup(@PathVariable("value") String value);
}
