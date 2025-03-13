# OpenJDK 17  as base image
FROM openjdk:17

# Create a non-root user and group
RUN groupadd --system appgroup && useradd --system --gid appgroup appuser

# Set working directory in the image
WORKDIR /app

# Switch to the new user
USER appuser

# Copy the project local jar to the container
COPY --chown=appgroup:appuser build/libs/spring-gym-app-1.0-SNAPSHOT.jar main-service.jar

# Command to run the application
ENTRYPOINT ["sh", "-c", "echo 'Running as:' $(whoami) && java -jar main-service.jar"]