FROM eclipse-temurin:21
ARG JAR_FILE=target/*-fat.jar
COPY conf/config.properties config.properties
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar","--conf=/config.properties"]