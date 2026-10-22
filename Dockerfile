# Stage 1: build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml first to cache dependencies
COPY pom.xml .

# Download dependencies
RUN mvn -B dependency:go-offline

# Copy all source code
COPY . .

# Package the app
RUN mvn -B package -DskipTests

# Stage 2: runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the JAR built in stage 1
COPY --from=build /app/target/*.jar app.jar

# Expose the gateway port (server.port)
EXPOSE 8081

# Run the JAR
CMD ["java", "-jar", "app.jar"]