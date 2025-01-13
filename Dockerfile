FROM postgres:15

# Устанавливаем переменные окружения для создания базы данных
ENV POSTGRES_DB=tms \
    POSTGRES_USER=postgres \
    POSTGRES_PASSWORD=pass