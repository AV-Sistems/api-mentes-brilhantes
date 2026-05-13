# Stage 1: Build
FROM eclipse-temurin:25-jdk-alpine AS build

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline -q

COPY src ./src
RUN ./mvnw package -DskipTests -q

# Stage 2: Runtime
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

ENV APP_UPLOAD_ROOT_DIR=/data/uploads

COPY --from=build /app/target/quarkus-app/lib/ ./lib/
COPY --from=build /app/target/quarkus-app/*.jar ./
COPY --from=build /app/target/quarkus-app/app/ ./app/
COPY --from=build /app/target/quarkus-app/quarkus/ ./quarkus/

RUN mkdir -p ${APP_UPLOAD_ROOT_DIR}

VOLUME ["/data/uploads"]

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]
