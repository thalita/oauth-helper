FROM maven:3.8.5-openjdk-18-slim AS build

WORKDIR /build

# Build dependency offline to streamline build
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn package -Dmaven.test.skip=true

FROM openjdk:18-ea-11-jdk-alpine

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/app/target/oauth-helper-0.0.1-SNAPSHOT.jar", "--server.port=8080" ]

COPY --from=build /build/target/oauth-helper-0.0.1-SNAPSHOT.jar /app/target/oauth-helper-0.0.1-SNAPSHOT.jar
