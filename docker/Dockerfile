# OpenJDK 17을 베이스 이미지로 사용
FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/libs/*.jar
COPY /${JAR_FILE} /app/app.jar

WORKDIR /app

ENTRYPOINT ["java", "-jar", "app.jar"]