FROM openjdk:8-jre-alpine

EXPOSE 8080

RUN mkdir -p /usr/app

COPY ./target/java-mvn-app-0.0.1-SNAPSHOT.jar /usr/app

WORKDIR /usr/app

ENTRYPOINT ["java","-jar","java-mvn-app-0.0.1-SNAPSHOT.jar"]
