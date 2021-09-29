FROM gradle:7.2.0-jdk11

WORKDIR /MercadilloLibreBack

ADD gradle /MercadilloLibreBack/gradle
COPY gradlew ./

RUN gradle -q bootRun