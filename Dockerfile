FROM amazoncorretto:21-alpine
VOLUME /tmp
COPY src/main/resources/server.jks server.jks
COPY target/PhotoAppAPIConfigServer-0.0.1-SNAPSHOT.jar ConfigServer.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "ConfigServer.jar"]

