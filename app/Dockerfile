# Use the official Kotlin JDK base image
FROM gradle:8.7.0-jdk17 AS builder

WORKDIR /app
COPY . .
RUN gradle clean shadowJar --no-daemon

# Runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /app/app/build/libs/app-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]