# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Add a non-root user to run the app
RUN addgroup --system --gid 1001 appuser && \
    adduser --system --uid 1001 --gid 1001 appuser

# Copy the pre-built executable JAR file (not the -plain.jar)
COPY build/libs/apigateway-0.0.1-SNAPSHOT.jar app.jar

# Change ownership to the non-root user
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose the port the app runs on
EXPOSE 8081

# Set health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]