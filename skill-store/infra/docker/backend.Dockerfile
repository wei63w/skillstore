FROM eclipse-temurin:21-jre
WORKDIR /app
COPY skill-store/backend/target/openclaw-skill-store-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
