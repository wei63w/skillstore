FROM eclipse-temurin:21-jre
WORKDIR /app
COPY harness/backend/target/agent-dev-harness-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
