FROM eclipse-temurin:23-noble AS builder

WORKDIR /src

COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

RUN chmod a+x ./mvnw && ./mvnw clean package -Dmaven.test.skip=true


FROM eclipse-temurin:23-jre-noble

WORKDIR /app

COPY --from=builder /src/target/mini-project-0.0.1-SNAPSHOT.jar community_platform.jar

RUN apt update && apt install -y curl

ENV PORT=8080
ENV SPRING_DATA_REDIS_HOST=
ENV SPRING_DATA_REDIS_PORT=
ENV SPRING_DATA_REDIS_USERNAME=
ENV SPRING_DATA_REDIS_PASSWORD=

ENV ADMIN_EMAIL=
ENV GOOGLE_API_KEY=

EXPOSE ${PORT}


ENTRYPOINT SERVER_PORT=${PORT} java -jar community_platform.jar
