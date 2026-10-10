FROM gradle:jdk17 AS build
WORKDIR /app
COPY . /app
RUN gradle build -s

FROM openjdk:17.0.2-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "error-free-text-1.0.jar"]