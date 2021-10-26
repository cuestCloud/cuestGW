FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/gateway*.jar
COPY ${JAR_FILE} app.jar
COPY application.properties application.properties
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar","--spring.config.location=/application.properties"]

