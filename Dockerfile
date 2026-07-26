FROM --platform=$BUILDPLATFORM maven:3.9-eclipse-temurin-21-alpine AS builder
LABEL authors="bereeeza"
WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM --platform=linux/arm64 eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /build/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]