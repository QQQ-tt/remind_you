FROM openjdk:17-alpine

WORKDIR /app
COPY target/remind_you-1.0-SNAPSHOT.jar /app/app.jar
# docker compose --project-name server -f docker-compose-server.yaml up -d
CMD ["java", "-jar", "app.jar"]