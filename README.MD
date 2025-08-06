# BookStoreAPI Tests — Quick Start

This repository contains automated API tests for the BookStoreAPI. The tests are written in Java, use Maven for build management, REST Assured for API testing, TestNG for test orchestration, and ExtentReports for reporting.

---

## Local Setup
**Prerequisites:**
- Java 17 (or 11+)
- Maven 3.8+

**Optional Configuration:**
- Edit `src/test/resources/config.properties` to set environment variables or API endpoints.

## Running Tests
To execute all tests and generate reports:
```bash
mvn clean test
```

## Test Reports
- **ExtentReports HTML:** After running tests, open `test-output/ExtentReport.html` for a detailed report.
- **TestNG Reports:** See `test-output/emailable-report.html` and `test-output/index.html` for standard TestNG results.

## Project Structure
- `src/main/java/` — Main source code (if any)
- `src/test/java/` — Test classes
- `src/test/resources/` — Test resources and config files
- `test-output/` — Generated reports
- `target/` — Maven build output

## Docker Support
A `Dockerfile` is included for containerized test execution. Build and run with:
```bash
docker build -t bookstore-api-tests .
docker run --rm bookstore-api-tests
```

## Customization
- Update `config.properties` for environment-specific settings.
- Add new tests in `src/test/java/`.

## Troubleshooting
- Ensure Java and Maven versions match prerequisites.
- Clean previous builds with `mvn clean` if you encounter issues.

---


