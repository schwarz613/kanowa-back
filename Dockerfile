# Stage 1: Build the Spring Boot Application
FROM gradle:8.10.2-jdk21-alpine AS builder
WORKDIR /app

# Copy gradle configuration and source code
COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src

# Build production JAR without daemon
RUN gradle bootJar --no-daemon

# Stage 2: Lightweight JRE runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Cloud providers dynamically inject the PORT environment variable
ENV PORT=8080
EXPOSE 8080

# Run with container-optimized memory limits
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
