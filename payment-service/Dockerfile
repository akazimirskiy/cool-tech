FROM amazoncorretto:21-alpine
LABEL authors="kazimir"

WORKDIR /app

COPY target/payment-service-1.0.0-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]