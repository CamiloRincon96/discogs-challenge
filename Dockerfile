FROM ubuntu:latest
LABEL authors="juancamilo.rincon"

ENTRYPOINT ["top", "-b"]

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY build/libs/DiscogsChallenge-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]