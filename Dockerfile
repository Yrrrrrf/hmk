# File: hmk/Dockerfile

# --- Stage 1: Build ---
FROM gradle:jdk21-jammy AS builder
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
# Fix permissions for gradlew
RUN chmod +x gradlew
# Download dependencies
RUN ./gradlew dependencies --no-daemon
# Copy source and build
COPY src ./src
RUN ./gradlew bootJar --no-daemon

# --- Stage 2: Run ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]