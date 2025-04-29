# TMS

This project is an example of a corporate API for task management that I developed as a test assignment.
It incorporates most of the technologies in demand in the job market.

## Table of Contents

- [Installation and Launch](#installation-and-launch)
- [Usage](#usage)
- [Technologies](#technologies)
- [License](#license)

## Installation and Launch

### Prerequisites

Before starting, ensure the following tools are installed:
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [JDK 17 или выше](https://adoptium.net/temurin/releases/)

### Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/ваш-username/ваш-репозиторий.git
    cd TMS
    ```
   
2. Start the containers:

    ```bash
    docker compose up -d
    ```
    
3. Run the project to create database tables:

    ```bash
   ./gradlew build

   java -jar build/libs/tms-plain.jar
    ```

4. Populate the database with initial data:

   ```bash
    docker cp ./src/main/resources/fill_db.sql tms_postgres:/tmp/fill_db.sql

    docker exec -it tms_postgres bash

    psql -U postgres -d tms -f /tmp/fill_db.sql
   
    exit
    ```

   Installation is now complete.

### Launch

Run the project using the built JAR file:

   ```bash
   java -jar build/libs/tms-plain.jar
   ```

## Usage

### Swagger

A full description of the API endpoints is available in Swagger UI at:

   ```url
   http://localhost:8080/swagger-ui/index.html
   ```

Brief endpoint overview:

### taskEntity-controller

1. PUT /api/tasks/{id}
2. DELETE /api/tasks/{id}
3. GET /api/tasks
4. POST /api/tasks
5. PATCH /api/tasks/{taskId}/status
6. PATCH /api/tasks/{taskId}/priority
7. PATCH /api/tasks/{taskId}/executor
8. GET /api/tasks/{taskId}

### commentEntity-controller

1. POST /api/tasks/{taskId}/comments
2. DELETE /api/tasks/{taskId}/comments/{commentId}
3. PATCH /api/tasks/{taskId}/comments/{commentId}

### userEntity-controller

1. POST /api/auth/register
2. POST /api/auth/login
3. GET /api/auth/profile

## Technologies

Technologies used in the project:
- Java 17
- Spring Boot
- Spring Data
- Spring Validation
- Spring Security
- Spring Cache
- PostgresSQL
- Redis
- JWT
- Swagger

Planned additions:
- Elastic search
- Junit
- Mockito 
- Csrf

## License

This project is licensed under the MIT License. Details can be found in the LICENSE.md file.