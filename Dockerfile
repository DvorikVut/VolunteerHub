# Используем официальный образ OpenJDK 17 в качестве базового
FROM openjdk:22-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файл jar в контейнер
COPY target/Pastebin-0.0.1-SNAPSHOT.jar /app/Pastebin.jar

# Указываем команду для запуска Spring Boot приложения
CMD ["java", "-jar", "Pastebin.jar"]

# Открываем порт приложения
EXPOSE 8000