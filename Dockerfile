# Stage 1: Build and Test
FROM maven:3.9.5-eclipse-temurin-17 AS builder

WORKDIR /app
COPY . .

# Run tests but don't fail the Docker build on test failure
RUN mvn clean test || true

# At this point, ExtentReport.html should exist at /app/test-output/
