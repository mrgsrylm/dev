# Spring Boot Java Monolith Boilerplate

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

## Features

- [x] Authentication and Authorization with JWT
- [x] CRUD User

## Technology

- [x] Spring Boot v3.1 
- [x] Hibernate
- [x] PostgreSQL, Redis, Liquibase
- [x] JWT
- [x] Swagger
- [x] Testing (JUnit5, Mockito)
- [x] Docker

## How to work it

```bash
# Build container
docker-compose up --build -d

# Install dependencies
mvn clean install

# Run project
mvn spring-boot:run

# Build package
mvn clean package

# Skip Testing
mvn clean install -DskipITs=true
```
