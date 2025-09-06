# Use an official OpenJDK runtime as base image
FROM openjdk:17-jdk-slim

# Set working directory it Create the /app directory inside the container (if it doesn't already exist)
WORKDIR /app

# Copy JAR file into the container
COPY target/*.jar auth-service.jar

# Expose the port (must match your app's server.port)
EXPOSE 8081

# Run the JAR
ENTRYPOINT ["java", "-jar", "auth-service.jar"]
