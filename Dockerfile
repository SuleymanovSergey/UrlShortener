# Используем базовый образ Java 17
FROM openjdk:17-jdk-alpine

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR-файл вашего приложения в контейнер
COPY build/libs/UrlShortener-0.0.1-SNAPSHOT.jar /app/app.jar

# Установка и настройка PostgreSQL
RUN apk update && apk add postgresql-client
ENV POSTGRES_URL=jdbc:postgresql://postgres:5432/your_database
ENV POSTGRES_USERNAME=your_username
ENV POSTGRES_PASSWORD=your_password

# Установка и настройка Redis
RUN apk add --update redis
ENV REDIS_HOST=redis
ENV REDIS_PORT=6379

# Установка и настройка Kafka
RUN apk add --update bash
RUN wget https://archive.apache.org/dist/kafka/3.1.0/kafka_2.13-3.1.0.tgz
RUN tar -xzf kafka_2.13-3.1.0.tgz
ENV KAFKA_HOME=/app/kafka_2.13-3.1.0

# Запускаем приложение при старте контейнера
CMD ["java", "-jar", "app.jar"]

