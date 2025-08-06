FROM maven:3.9.5-eclipse-temurin-17 AS builder

WORKDIR /app

COPY . .

ENV BASE_URL=https://fakerestapi.azurewebsites.net/api/v1/

RUN mvn clean test || true
