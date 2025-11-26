package poc.java.concurrency.resource;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  @Container
  private static final MariaDBContainer<?> mariadb = new MariaDBContainer<>("mariadb:latest")
      .withDatabaseName("my_database")
      .withUsername("user")
      .withPassword("my_awesome_password");

  private static JsonObject dbConfig = new JsonObject();

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    dbConfig.put("host", mariadb.getHost())
        .put("port", mariadb.getMappedPort(3306))
        .put("database", mariadb.getDatabaseName())
        .put("user", mariadb.getUsername())
        .put("password", mariadb.getPassword())
        .put("max_pool_size", 5); // Example pool option

    vertx.deployVerticle(new MainVerticle()).onComplete(testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }
}
