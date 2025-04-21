# Use Java 21 base image
FROM eclipse-temurin:21-jdk

# Set working directory inside container
WORKDIR /app

# Copy everything into container
COPY . /app

# Give execute permission to mvnw
RUN chmod +x ./mvnw

# Build the Spring Boot app
RUN ./mvnw clean package -DskipTests

# Expose the port your app uses
EXPOSE 8080

# Start the app (replace * if needed)
CMD ["java", "-jar", "target/Flight_Ticket_Booking_System-0.0.1-SNAPSHOT.jar"]
