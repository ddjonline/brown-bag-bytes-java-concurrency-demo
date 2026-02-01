package poc.java.concurrency.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "resource-client", url = "${rest.client.url}")
public interface SecondHalfFeignClient {

    @GetMapping("/lookup/{value}")
    String getLookup(@PathVariable("value") String value);
}

