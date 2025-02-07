FROM openjdk:17-alpine

WORKDIR /app
COPY target/remind_you-1.0-SNAPSHOT.jar /app/app.jar

ENV nacos_ip=host.docker.internal
EXPOSE 8888
CMD ["java", "-jar", "app.jar"]