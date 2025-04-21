# BookQuik - Microservice Booking System

BookQuik is a robust and scalable booking service application built on a microservice architecture, designed to handle high-volume reservation operations with reliability and performance.

## Architecture Overview

BookQuik is composed of several independent microservices that communicate via Kafka event streams:

- **API Gateway**: Entry point for all client requests, handling routing, authentication, and load balancing
- **Booking Service**: Core service handling the creation, modification, and cancellation of bookings
- **Order Service**: Processes customer orders, manages order lifecycle and history
- **Inventory Service**: Manages available resources (rooms, equipment, vehicles, etc.)

## Future Services

- **User Service**: Handles user accounts, authentication, and profiles
- **Notification Service**: Manages communications via email, SMS, and push notifications
- **Payment Service**: Processes payments and refunds
- **Analytics Service**: Collects and processes booking data for business intelligence

## Tech Stack

- **Backend**: Java Spring Boot
- **Database**: MySQL (per service)
- **Message Broker**: Apache Kafka
- **API Gateway**: Spring Cloud Gateway
- **Service Discovery**: Eureka
- **Configuration**: Spring Cloud Config
- **Circuit Breaker**: Resilience4j

## Getting Started

### Prerequisites

- JDK 21+
- gradle 8.0+
- Docker and Docker Compose
- MySQL 8.0+
- Kafka 3.0+

### Setup Instructions

1. Clone the repository

   ```bash
   git clone https://github.com/yourorganization/bookquik.git
   cd bookquik
   ```

2. Build the service images:

   ```bash
   python build_docker_images.py
   ```

3. Start the containers

   ```bash
   docker-compose up -d
   ```

4. Check the status:
   ```bash
   docker-compose ps
   ```

## Service Details

### API Gateway

- Routes client requests to appropriate microservices
- Handles authentication and authorization with Keycloak
- Provides load balancing across service instances
- Implements request rate limiting and circuit breaking
- Serves as a unified entry point for all client applications

### Booking Service

- Manages booking lifecycle
- Publishes events: `booking-created`, `booking-updated`, `booking-cancelled`
- Subscribes to: `inventory-updated`, `payment-processed`

### Inventory Service

- Tracks resource availability
- Publishes events: `inventory-updated`, `inventory-reserved`
- Subscribes to: `booking-created`, `booking-cancelled`, `order-created`

### User Service

- Manages user accounts and authentication
- Provides JWT token-based authentication
- Publishes events: `user-registered`, `user-updated`

### Order Service

- Handles customer order processing
- Manages order lifecycle from creation to fulfillment
- Publishes events: `order-created`, `order-updated`, `order-fulfilled`, `order-cancelled`
- Subscribes to: `inventory-updated`, `payment-processed`

### Notification Service

- Handles all user communications
- Subscribes to: `booking-created`, `booking-updated`, `booking-cancelled`, `payment-processed`, `order-created`, `order-updated`

### Payment Service

- Processes financial transactions
- Publishes events: `payment-processed`, `payment-failed`, `refund-processed`
- Subscribes to: `booking-created`, `booking-cancelled`, `order-created`, `order-cancelled`

## Kafka Topics

- `bookings`: Booking-related events
- `inventory`: Inventory-related events
- `users`: User-related events
- `orders`: Order-related events
- `notifications`: Notification-related events
- `payments`: Payment-related events

## Configuration

Each service includes its own `application.yml` with service-specific configurations. Common configurations are stored in a central config server.

```yaml
spring:
  application:
    name: booking-service
  datasource:
    url: jdbc:mysql://localhost:3306/booking_db
    username: bookquik_user
    password: ${MYSQL_PASSWORD}
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

## Deployment

BookQuik supports deployment to Kubernetes with Helm charts provided in the `/deployment` directory.

```
kubectl apply -f deployment/k8s/
```

## Monitoring

- Prometheus for metrics collection
- Grafana for visualization
- Distributed tracing with Spring Cloud Sleuth and Zipkin

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
