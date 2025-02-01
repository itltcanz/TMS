# TMS

Этот проект является api для работы с задачами, которое я делал как тестовое задание.
Здесь применены технологии, востребованные на ранке труда. Некоторые были для меня новыми, некоторые - нет.

## Оглавление

- [Установка и запуск](#установка-и-запуск)
- [Использование](#использование)
- [Технологии](#технологии)
- [Лицензия](#лицензия)

## Установка и запуск

### Требования

Перед началом работы убедитесь, что у вас установлены следующие инструменты:
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [JDK 17 или выше](https://adoptium.net/temurin/releases/)

### Установка

1. Клонируйте репозиторий:

    ```bash
    git clone https://github.com/ваш-username/ваш-репозиторий.git
    cd TMS
    ```
   
2. Запустите контейнеры:

    ```bash
    docker compose up -d
    ```
    
3. Запустите проект, чтобы в БД создались таблицы

    ```bash
   ./gradlew build

   java -jar build/libs/TMS-0.0.1-SNAPSHOT.jar
    ```

4. Заполните БД базовой информацией:

   ```bash
    docker cp ./src/main/resources/fill_db.sql tms_postgres:/tmp/fill_db.sql

    docker exec -it tms_postgres bash

    psql -U postgres -d tms -f /tmp/fill_db.sql
   
    exit
    ```
   
   На этом установка закончена.

### Запуск

Запуск проекта осуществляется через собранный jar файл:

   ```bash
   java -jar build/libs/TMS-0.0.1-SNAPSHOT.jar
   ```

## Использование

### Swagger

Полное описание Endpoint-ов api можно увидеть в swagger-ui по url:

   ```url
   http://localhost:8080/swagger-ui/index.html
   ```

Краткое описание Endpoint-ов api:

### task-controller

1. PUT /api/tasks/{id}
2. DELETE /api/tasks/{id}
3. GET /api/tasks
4. POST /api/tasks
5. PATCH /api/tasks/{taskId}/status
6. PATCH /api/tasks/{taskId}/priority
7. PATCH /api/tasks/{taskId}/executor
8. GET /api/tasks/{taskId}

### comment-controller

1. POST /api/tasks/{taskId}/comments
2. DELETE /api/tasks/{taskId}/comments/{commentId}
3. PATCH /api/tasks/{taskId}/comments/{commentId}

### account-controller

1. POST /api/auth/register
2. POST /api/auth/login
3. GET /api/auth/profile

## Технологии

Используемые в проекте технологии:
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

Планируется добавить:
- Logback
- Elastic search
- Junit
- Mockito 
- Csrf

## Лицензия

Этот проект распространяется под лицензией MIT. Подробности смотрите в файле LICENSE.md.