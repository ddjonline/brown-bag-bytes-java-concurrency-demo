// package poc.java.concurrency.resource;

// import io.vertx.core.DeploymentOptions;
// import io.vertx.core.Vertx;
// import io.vertx.core.json.JsonObject;
// import io.vertx.junit5.VertxExtension;
// import io.vertx.junit5.VertxTestContext;

// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.testcontainers.containers.PostgreSQLContainer;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;

// @Testcontainers
// @ExtendWith(VertxExtension.class)
// public class TestMainVerticle {

//   private WireMockServer wireMockServer;

//   @Container
//   private static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:13.3-alpine")
//       .withDatabaseName("mydatabase")
//       .withUsername("myuser")
//       .withPassword("my_awesome_password");
//   private static JsonObject dbConfig = new JsonObject();

//   @BeforeAll
//   static void setup(Vertx vertx, VertxTestContext testContext) {
//     postgresqlContainer.start();
//     testContext.completeNow();
//   }

//   @BeforeEach
//   void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
//     dbConfig.put("postgres.host", postgresqlContainer.getHost())
//         .put("postgres.port", postgresqlContainer.getMappedPort(5432))
//         .put("postgres.database", postgresqlContainer.getDatabaseName())
//         .put("postgres.user", postgresqlContainer.getUsername())
//         .put("postgres.password", postgresqlContainer.getPassword())
//         .put("max_pool_size", 5);

//     var deploymentOptions = new DeploymentOptions(dbConfig);
//     vertx.deployVerticle(new MainVerticle(), deploymentOptions)
//         .onComplete(testContext.succeeding(id -> testContext.completeNow()));
//   }

//   @Test
//   void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
//     testContext.completeNow();
//   }
// }
