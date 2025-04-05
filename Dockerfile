FROM openjdk:17-jdk-slim

WORKDIR /app

COPY ./target/users-service-0.0.1-SNAPSHOT.jar .

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "users-service-0.0.1-SNAPSHOT.jar"]