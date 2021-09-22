FROM openjdk:12-jdk-alpine

RUN apk add --no-cache bash

WORKDIR /MercadilloLibreBack

ADD gradle /MercadilloLibreBack/gradle
COPY gradlew ./
COPY gradlew.bat ./

RUN ./gradlew run