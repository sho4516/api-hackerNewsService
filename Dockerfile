# Start with a base image containing Java 17 and Maven
FROM openjdk:17-jdk-slim as build

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build the application
RUN mvn clean install

# Start a new stage with a base image containing Java runtime and Redis
FROM openjdk:17-jdk-slim

# Install Redis
RUN apt-get update && apt-get install -y redis-server

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/target/hackerNewsService-0.0.1-SNAPSHOT.jar .

# Copy the entrypoint script
COPY entrypoint.sh entrypoint.sh

# Make the entrypoint script executable
RUN chmod +x entrypoint.sh

# Set the entrypoint script as the container's entrypoint
ENTRYPOINT ["./entrypoint.sh"]

# Expose the default Redis port
EXPOSE 6379

# Expose the H2 console port
EXPOSE 8080
