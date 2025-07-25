FROM openjdk:17-jdk-slim

ARG JAR_FILE
ENV APP_JAR=${JAR_FILE}

WORKDIR /app

COPY target/${JAR_FILE} ./app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
