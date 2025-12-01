// package poc.java.concurrency.api;

// import org.junit.jupiter.api.AfterAll;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
// import org.springframework.context.annotation.Import;
// import org.testcontainers.containers.PostgreSQLContainer;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;
// import org.testcontainers.utility.DockerImageName;

// // @Import(TestcontainersConfiguration.class)
// @SpringBootTest
// @Testcontainers
// class ApplicationTests {

//   static WireMockServer wireMockServer = new WireMockServer(options().dynamicPort());

//   @Container
//   @ServiceConnection
//   static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

//   @BeforeAll
//   static void setupWireMock() {
//     wireMockServer.start();
//     wireMockServer.stubFor(get(urlEqualTo("/lookup/09876543210"))
//                 .willReturn(aResponse().withBody("543210").withStatus(200)));
//   }

//   @AfterAll
//   static void tearDownWireMock() {
//     wireMockServer.stop();
//   }

//   @Test
//   void contextLoads() {
//   }

// }
