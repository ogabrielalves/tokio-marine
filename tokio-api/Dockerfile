FROM maven:3.9.2-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:11-jdk-slim
WORKDIR /app
COPY --from=build /app/target/tokio-api-0.0.1-SNAPSHOT.jar ./tokio-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "tokio-api.jar"]
