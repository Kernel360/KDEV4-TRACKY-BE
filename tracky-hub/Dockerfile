
# OpenJDK 17을 베이스 이미지로 사용
FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/libs/*.jar
COPY /${JAR_FILE} /app/app.jar

WORKDIR /app

# tzdata 패키지 설치 및 Asia/Seoul 시간대 설정
# RUN apt-get update && apt-get install -y tzdata && \
#     ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
#     echo Asia/Seoul > /etc/timezone

# ENV TZ=Asia/Seoul

ENTRYPOINT ["java", "-jar", "app.jar"]
