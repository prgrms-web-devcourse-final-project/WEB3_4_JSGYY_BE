FROM gradle:8.11.1-jdk-21-and-23 AS build

WORKDIR /app

COPY . /app

RUN gradle clean build --no-daemon

FROM  openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/mock.jar

EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-jar","mock.jar"]