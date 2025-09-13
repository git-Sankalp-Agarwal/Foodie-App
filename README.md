# Food Delivery Order Service

A scalable backend service for processing food delivery orders built with Java, Spring Boot, MySQL, and message queuing.

## Features

✅ RESTful API for order management  
✅ Asynchronous order processing with in-memory queue (AWS SQS ready)  
✅ MySQL database with optimized queries and indexing  
✅ Pagination support for fetching orders  
✅ Comprehensive validation and exception handling  
✅ Service layer abstraction with DTOs  
✅ Background consumer for automatic order processing  
✅ Swagger UI documentation  
✅ Spring Boot best practices and clean architecture  

## Tech Stack

- Java 17
- Spring Boot 3.2.0
- MySQL 8.0
- Spring Data JPA
- Lombok
- SpringDoc OpenAPI (Swagger)
- Maven

## Project Structure
```
food-delivery-service/
├── src/main/java/com/fooddelivery/
│   ├── controller/       # REST controllers
│   ├── service/          # Business logic
│   ├── repository/       # Data access layer
│   ├── entity/           # JPA entities
│   ├── dto/              # Data transfer objects
│   ├── mapper/           # Entity-DTO mappers
│   ├── exception/        # Custom exceptions
│   ├── config/           # Configuration classes
│   └── FoodDeliveryApplication.java
├── src/main/resources/
│   └── application.yml   # Application configuration
├── pom.xml
└── README.md
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/food-delivery-service.git
cd food-delivery-service
```

### 2. Configure MySQL Database
Create a MySQL database and update the credentials in `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/food_delivery_db?createDatabaseIfNotExist=true
    username: your_username
    password: your_password
```

Or run the SQL schema script:
```bash
mysql -u root -p < schema.sql
```

### 3. Build the Application
```bash
mvn clean install
```

### 4. Run the Application
```bash
mvn spring-boot:run
```
Or run the JAR:
```bash
java -jar target/food-delivery-service-1.0.0.jar
```

The application will start on `http://localhost:8080`

## API Documentation
Access Swagger UI at: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Available Endpoints

| Method | Endpoint             | Description            |
|--------|----------------------|------------------------|
| POST   | /api/orders          | Create a new order     |
| GET    | /api/orders          | Get all orders (paginated) |
| GET    | /api/orders/{id}     | Get order by ID        |
| GET    | /api/orders/{id}/status | Get order status    |
| PATCH  | /api/orders/{id}/status | Update order status |

... (Detailed examples as provided earlier) ...

## Order Status Flow

```
PENDING → PROCESSING → PROCESSED → DELIVERED
    ↓                      ↓
CANCELLED            CANCELLED
```

Valid status transitions:  
- PENDING → PROCESSING, CANCELLED  
- PROCESSING → PROCESSED, CANCELLED  
- PROCESSED → DELIVERED  
- DELIVERED → (no transitions)  
- CANCELLED → (no transitions)  

## Asynchronous Processing

- Orders are added to an **in-memory queue**.
- A background consumer processes them, moving them from `PENDING → PROCESSING → PROCESSED`.
- A delay of ~2 seconds simulates real-world processing.

### Testing the Queue

- Create multiple orders rapidly.
- Check status → should be `PENDING`.
- Wait a few seconds.
- Status should move to `PROCESSED`.

---

## Switching to AWS SQS

To use AWS SQS instead of in-memory queue, update `application.yml`:

```yaml
aws:
  sqs:
    enabled: true
    region: us-east-1
    queue-name: food-delivery-orders
    access-key: your-access-key
    secret-key: your-secret-key
```

Then implement `SQSQueueService` extending the current queue service.

---

## Database Optimization

- Indexes on frequently queried columns (`status`, `orderTime`, `customerName`).
- Lazy loading for order items.
- Batch processing for bulk operations.
- Query optimization with custom JPQL queries.
- HikariCP connection pooling.

---

## Error Handling

- **400 Bad Request** → Validation errors, invalid transitions.
- **404 Not Found** → Order not found.
- **500 Internal Server Error** → Unexpected errors.

All errors follow consistent JSON response format:
```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "timestamp": "2024-01-15T10:30:00"
}
```

---

## Monitoring

- Health: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- Metrics: [http://localhost:8080/actuator/metrics](http://localhost:8080/actuator/metrics)

---

## Performance Considerations

- Connection pooling with HikariCP.
- Add Redis caching for performance.
- Implement rate limiting on APIs.
- Async processing improves response time.
- Proper database indexing.

---

## Future Enhancements

- Add Redis caching
- WebSocket for real-time updates
- Authentication & Authorization
- GPS-based order tracking
- Payment integration
- Notification service (Email/SMS)
- Analytics & reporting
- Circuit breaker pattern
- Docker & Kubernetes support

---

## Contributing

1. Fork the repo
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

---

## License

This project is licensed under the **MIT License**.

---

## Contact

Name: Sankalp
Email - sankalpagarwal1304@gmail.com
Project Link: [https://github.com/git-Sankalp-Agarwal/Foodie-App](https://github.com/git-Sankalp-Agarwal/Foodie-App)