# Use an official OpenJDK runtime as base image
FROM openjdk:17-jdk-slim

# Set working directory it Create the /app directory inside the container (if it doesn't already exist)
WORKDIR /app

# Copy JAR file into the container
COPY target/*.jar api-gateway.jar

# Expose the port (must match your app's server.port)
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]
