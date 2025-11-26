package poc.java.concurrency.resource;

import org.owasp.encoder.Encode;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.mysqlclient.MySQLBuilder;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;

public class MainVerticle extends AbstractVerticle {

  private SqlClient dbClient;
  private HttpServer httpServer;

  @Override
  public void start(Promise<Void> startPromise) {

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

          var connectOptions = new MySQLConnectOptions()
              .setHost(config.getString("mariadb.host"))
              .setPort(config.getInteger("mariadb.port"))
              .setDatabase(config.getString("mariadb.database"))
              .setUser(config.getString("mariadb.user"))
              .setPassword(config.getString("mariadb.password"));

          var poolOptions = new PoolOptions().setMaxSize(5);

          dbClient = MySQLBuilder
              .client()
              .using(vertx)
              .with(poolOptions)
              .connectingTo(connectOptions)
              .build();

          var router = Router.router(vertx);
          router.get("/lookup/:value").handler(routingContext -> {
            var value = Encode.forUriComponent(routingContext.pathParam("value"));

            dbClient
                .preparedQuery("SELECT secondhalf from mytable where pro=?")
                .execute(Tuple.of(value))
                .onComplete(asyncResult -> {
                  if (asyncResult.succeeded()) {
                    RowSet<Row> rows = asyncResult.result();

                    var iter = rows.iterator();
                    var row = iter.next();
                    var secondHalf = row.getString("secondhalf");

                    routingContext.response()
                        .putHeader("content-type", "text/plain")
                        .end(secondHalf);
                  } else {
                    routingContext.fail(400);
                  }
                })
                .onFailure(throwable -> {
                  routingContext.fail(500);
                });
          });

          vertx.createHttpServer()
              .requestHandler(router)
              .listen(config.getInteger("http_port"))
              .onSuccess(server -> {
                System.out.println("HTTP server started on port " + server.actualPort());
                startPromise.complete();
              })
              .onFailure(startPromise::fail);
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
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }
}
