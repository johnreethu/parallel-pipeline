FROM maven:3-jdk-11-slim as build

COPY ./src ./src
COPY ./pom.xml ./pom.xml

RUN mvn clean install -U

FROM openjdk:11-jre-slim

WORKDIR /BMIApp

COPY --from=build target/test-framework-0.0.1-SNAPSHOT.jar ./BMIDocker/test-framework-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "./BMIDocker/test-framework-0.0.1-SNAPSHOT.jar"]
