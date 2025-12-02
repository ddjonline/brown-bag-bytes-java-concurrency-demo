package poc.java.concurrency.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import poc.java.concurrency.api.config.RestClientProperties;

@SpringBootApplication
@EnableConfigurationProperties({RestClientProperties.class})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
