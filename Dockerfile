FROM openjdk:17-jdk-slim

WORKDIR /app

COPY . .

RUN chmod +x mvnw && ./mvnw clean package

CMD ["java", "-jar", "target/event-management-0.0.1-SNAPSHOT.jar"]