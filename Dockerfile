FROM maven:3-jdk-11-slim as build

COPY ./src ./src
COPY ./pom.xml ./pom.xml

RUN mvn clean install -U

FROM openjdk:11-jre-slim

WORKDIR /BMIApp

COPY --from=build target/test-framework-*.jar ./apps/test-framework-*.jar
CMD ["java", "-jar", "./apps/test-framework-*.jar"]
