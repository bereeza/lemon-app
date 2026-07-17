FROM ghcr.io/graalvm/native-image-community:21 AS builder
LABEL authors="bereeeza"

RUN microdnf install -y maven findutils && microdnf clean all

WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package native:compile -Pnative -DskipTests

FROM debian:bookworm-slim
WORKDIR /app

COPY --from=builder /build/target/lemon-app /app/lemon-app

ENV IP="192.168.178.54"
ENV BIGTABLE_EMULATOR_HOST="localhost:8086"

EXPOSE 8080
ENTRYPOINT ["./lemon-app"]