# Use an official OpenJDK 17 runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container to /app
WORKDIR /app

# Add the current directory contents into the container at /app
ADD target/*.jar app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the application when the container launches
ENTRYPOINT ["java", "-jar", "app.jar"]