package poc.java.concurrency.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@EnableFeignClients(basePackages = "poc.java.concurrency.api.client")
@Configuration
public class AppConfig {
  
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

}
