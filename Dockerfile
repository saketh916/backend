# ---------- Build stage ----------
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY . .

# Fix permission issue for mvnw
RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests


# ---------- Run stage ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
