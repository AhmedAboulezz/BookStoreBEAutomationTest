FROM maven:3.9.5-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .
RUN mvn -q dependency:resolve

COPY . .
RUN mvn -q clean test-compile

ARG BASE_URL=https://fakerestapi.azurewebsites.net/api/v1/
ENV BASE_URL=${BASE_URL}

ARG IGNORE_TEST_FAILURES=false
ENV IGNORE_TEST_FAILURES=${IGNORE_TEST_FAILURES}

CMD ["sh", "-c", "if [ \"$IGNORE_TEST_FAILURES\" = \"true\" ]; \
     then mvn -q clean test -Dmaven.test.failure.ignore=true ; \
     else mvn -q clean test ; \
     fi"]
