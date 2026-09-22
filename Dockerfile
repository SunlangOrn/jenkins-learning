FROM eclipse-temurin:21.0.1_12-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 1200
ENTRYPOINT ["java", "-jar", "app.jar"]
