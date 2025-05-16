# Build stage
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-Dserver.port=8081", "-Dspring.profiles.active=prod", "-Dlogging.level.org.springframework.security=DEBUG", "-Dlogging.level.com.anonymousibex.Agents.of.Revature=DEBUG", "-jar", "app.jar"] 