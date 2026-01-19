package poc.java.concurrency.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("rest.client")
public record RestClientProperties(String url) { }
