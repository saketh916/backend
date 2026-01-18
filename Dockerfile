# ---------- Build stage ----------
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Copy everything
COPY . .

# 🔥 THIS LINE IS MANDATORY AND MUST COME BEFORE mvnw RUN
RUN chmod +x mvnw

# Build Spring Boot app
RUN ./mvnw clean package -DskipTests


# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
