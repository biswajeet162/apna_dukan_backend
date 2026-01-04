# Build stage
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies first (cached layer)
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render uses the PORT environment variable
ENV PORT 8080
EXPOSE ${PORT}

# Run with production profile
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
