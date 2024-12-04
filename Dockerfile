# Этап сборки
FROM maven:3.9.9-eclipse-temurin-22-jammy AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Сначала копируем только файл pom.xml и запускаем скачивание зависимостей
COPY pom.xml .

# Используем отдельную команду для загрузки зависимостей
RUN mvn dependency:go-offline

# Копируем исходный код проекта
COPY src ./src

# Сборка приложения
RUN mvn clean package

# Этап выполнения
FROM openjdk:22

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем скомпилированный jar из этапа сборки
COPY --from=build /app/target/ITU-v1.jar .

# Указываем команду для запуска приложения
CMD ["java", "-jar", "ITU-v1.jar"]

# Открываем порт приложения
EXPOSE 8080
