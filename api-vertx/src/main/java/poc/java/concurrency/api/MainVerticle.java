package poc.java.concurrency.api;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.owasp.encoder.Encode;

import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.micrometer.MicrometerMetricsFactory;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.PrometheusScrapingHandler;
import io.vertx.micrometer.VertxPrometheusOptions;
import io.vertx.micrometer.backends.BackendRegistries;
import io.vertx.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;

public class MainVerticle extends AbstractVerticle {

  private SqlClient dbClient;
  private WebClient webClient;
  private HttpServer httpServer;
  private static final Logger logger = Logger.getLogger(MainVerticle.class.getName());

  @Override
  public void start(Promise<Void> startPromise) {

    PrometheusMeterRegistry registry = (PrometheusMeterRegistry) BackendRegistries.getDefaultNow();

    var envStore = new ConfigStoreOptions()
        .setType("env");

    var fileStore = new ConfigStoreOptions()
        .setType("file")
        .setFormat("properties")
        .setConfig(new JsonObject().put("path", "config.properties"));

    ConfigRetriever retriever = ConfigRetriever.create(vertx,
        new ConfigRetrieverOptions().addStore(fileStore).addStore(envStore));

    retriever.getConfig()
        .onSuccess(config -> {

          // Set up Postgres DB Connection
          var postgresDBConnectionOptions = new PgConnectOptions()
              .setPort(config.getInteger("postgres.port"))
              .setHost(config.getString("postgres.host"))
              .setDatabase(config.getString("postgres.database"))
              .setUser(config.getString("postgres.user"))
              .setPassword(config.getString("postgres.password"));

          var poolOptions = new PoolOptions().setMaxSize(5);

          dbClient = PgBuilder
              .client()
              .with(poolOptions)
              .connectingTo(postgresDBConnectionOptions)
              .using(vertx)
              .build();

          // Set up the HTTP WebClient for Lookup
          webClient = WebClient.create(vertx);

          // Set up HTTP Server
          var router = Router.router(vertx);

          router.route("/metrics").handler(PrometheusScrapingHandler.create());

          router.get("/lookup/:value").handler(routingContext -> {
            var value = Encode.forUriComponent(routingContext.pathParam("value"));
            // fetch the first half
            var firstHalfFutrue = getFirstHalf(value);

            // fetch the second half
            var secondHalfFuture = getSecondHalf(value, config);

            // combine the results and return the routing context
            Future.all(List.of(firstHalfFutrue, secondHalfFuture))
                .onSuccess(results -> {
                  var firstHalf = results.resultAt(0);
                  var secondHalf = results.resultAt(1);

                  var result = String.format("%s%s", firstHalf, secondHalf);

                  routingContext.response()
                      .putHeader("content-type", "text/plain")
                      .end(result);
                }).onFailure(throwable -> {
                  routingContext.fail(500);
                });
          });

          router.errorHandler(404, routingContext -> {
            routingContext.response().setStatusCode(404).end("Item not found!");
          });

          router.errorHandler(500, routingContext -> {
            routingContext.response().setStatusCode(500).end("Something went wrong on our end.");
          });

          var allowedOriginsRegex = "^(http|https)://localhost:\\d+|^(http|https)://api-.*:\\d+";
          var corsHandler = CorsHandler.create()
              .addOriginWithRegex(allowedOriginsRegex)
              .allowedMethod(HttpMethod.GET)
              .allowedMethod(HttpMethod.OPTIONS);
          router.route().handler(corsHandler);

          vertx.createHttpServer()
              .requestHandler(router)
              .listen(config.getInteger("http_port"))
              .onSuccess(server -> {
                logger.info("HTTP server started on port " + server.actualPort());
                startPromise.complete();
              })
              .onFailure(startPromise::fail);
        });
  }

  private Future<String> getFirstHalf(String value) {
    Promise<String> promise = Promise.promise();
    String sql = "SELECT firsthalf from mytable where pro = $1";

    dbClient.preparedQuery(sql).execute(Tuple.of(value))
        .onSuccess(rowSet -> {
          if (rowSet.iterator().hasNext()) {
            Row row = rowSet.iterator().next();
            var firstHalf = row.getString("firsthalf");
            promise.complete(firstHalf); // Complete the promise with the result
          } else {
            promise.fail(new NoSuchElementException("firstHalf not found"));
          }
        })
        .onFailure(promise::fail); // Fail the promise if the DB operation fails

    return promise.future();
  }

  private Future<String> getSecondHalf(String value, JsonObject config) {
    return webClient.get(config.getInteger("lookup.port"), config.getString("lookup.host"), "/lookup/" + value)
        .send()
        .compose(response -> {
          if (response.statusCode() == 200) {
            return Future.succeededFuture(response.bodyAsString());
          } else {
            return Future.failedFuture("API request failed: " + response.statusCode());
          }
        });
  }

  @Override
  public void stop(Promise<Void> stopPromise) {
    if (dbClient != null) {
      dbClient.close();
    }
    if (httpServer != null) {
      httpServer.close();
    }
    stopPromise.complete();
  }

  public static void main(String[] args) {
    PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    registry.config().meterFilter(
        new MeterFilter() {
          @Override
          public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
            return DistributionStatisticConfig.builder()
                .percentilesHistogram(true)
                .build()
                .merge(config);
          }
        });

    Vertx vertx = Vertx.builder()
        .with(new VertxOptions().setMetricsOptions(new MicrometerMetricsOptions()
            .setEnabled(true)
            .setPrometheusOptions(new VertxPrometheusOptions()
                .setEnabled(true))))
        .withMetrics(new MicrometerMetricsFactory(registry))
        .build();

    vertx.deployVerticle(new MainVerticle()).await();
    logger.info("Verticle started");
  }
}
