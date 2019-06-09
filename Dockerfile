FROM openjdk:8-jdk-alpine

COPY target/tomcat-ssl-example-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD java -jar /app/app.jar

USER 1001
