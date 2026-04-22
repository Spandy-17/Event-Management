FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw && ./mvnw clean package -DskipTests

CMD ["java", "-jar", "target/event-management-0.0.1-SNAPSHOT.jar"]