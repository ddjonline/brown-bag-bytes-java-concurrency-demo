FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=target/*-fat.jar
COPY conf/config.properties config.properties
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-XX:+UseZGC", "-jar","/app.jar","--conf=/config.properties"]