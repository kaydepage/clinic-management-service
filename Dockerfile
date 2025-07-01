FROM eclipse-temurin:17-jdk-alpine AS base

FROM ubuntu
RUN apt-get update
RUN apt-get install dos2unix

FROM base AS builder
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN dos2unix mvnw

RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean install

FROM base AS runner
WORKDIR /app
EXPOSE 8080
COPY --from=builder /app/target/*.jar /app/*.jar
ENTRYPOINT ["java", "-jar", "/app/*.jar" ]
