FROM gradle:jdk17 AS build
WORKDIR /app
COPY . /app
RUN gradle build

FROM openjdk:17.0.2-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "error-free-text-0.0.1-SNAPSHOT.jar"]