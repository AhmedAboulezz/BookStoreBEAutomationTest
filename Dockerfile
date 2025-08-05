# Stage 1: Build and Test
FROM maven:3.9.5-eclipse-temurin-17 AS builder

WORKDIR /app
COPY . .
RUN mvn clean test

# Stage 2: Export artifacts
FROM alpine:latest AS final
WORKDIR /output
COPY --from=builder /app/test-output/ExtentReport.html ./ExtentReport.html
