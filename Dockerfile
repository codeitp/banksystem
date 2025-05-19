# Using the official OpenJDK 21 base image (Alpine for small footprint)
FROM eclipse-temurin:21-jdk-alpine as builder

WORKDIR /app

# Copy the Maven wrapper and project files
COPY .mvn .mvn
COPY mvnw pom.xml ./
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# ------------------------------
# Use a clean runtime image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
