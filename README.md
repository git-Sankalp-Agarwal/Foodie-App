# Food Delivery Order Service

A scalable backend service for processing food delivery orders built with Java, Spring Boot, MySQL, and message queuing.

## Features

✅ RESTful API for order management  
✅ Asynchronous order processing with in-memory queue
✅ MySQL database with optimized queries and indexing  
✅ Pagination support for fetching orders  
✅ Comprehensive validation and exception handling  
✅ Service layer abstraction with DTOs  
✅ Background consumer for automatic order processing
✅ Spring Boot best practices and clean architecture  

## Tech Stack

- Java 17
- Spring Boot 3.2.0
- MySQL 8.0
- Spring Data JPA
- Lombok
- Maven

## Project Structure
```
food-delivery-service/
├── src/main/java/com/foodorderservice/Foodie
│   ├── controller/       # REST controllers
│   ├── service/          # Business logic
│   ├── repository/       # Data access layer
│   ├── entity/           # JPA entities
│   ├── dtos/             # Data transfer objects
│   ├── mapper/           # Entity-DTO mappers
│   ├── config/           # Configurations
│   ├── config/           # Configurations
│   ├── advices/           # Response advices and Global exception handler
│   ├── exception/        # Custom exceptions
│   ├         # Configuration classes
│   └── FoodApplication.java
├── src/main/resources/
│   └── application.properties   # Application configuration
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
git clone https://github.com/git-Sankalp-Agarwal/Foodie-App
cd Foodie-App
```

### 2. Configure MySQL Database
Create a MySQL database and update the credentials in `application.yml`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/foodie_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username = your_username
spring.datasource.password = your_password
```

### 2. Build the Application
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```
Or run the JAR:
```bash
java -jar target/foodie-app-1.0.0.jar
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

## Sample API payloads & responses

Base URL: `http://localhost:8080` (adjust port if different)

## Create Order

**POST** `/api/orders` (example path; adapt to your controller)

**Request JSON:**
```json
{
  "customerName": "Alice Johnson",
  "items": [
    {
      "itemName": "Burger",
      "quantity": 2,
      "price": 8.99
    },
    {
      "itemName": "Fries",
      "quantity": 1,
      "price": 3.99
    },
    {
      "itemName": "Coke",
      "quantity": 2,
      "price": 2.50
    }
  ],
  "totalAmount": 26.97
}
```

**Successful response (201 Created)**
```json
{
  "success": true,
  "message": "Order created successfully",
  "timeStamp": "2025-09-14T11:17:33.3697473",
  "data": {
    "id": 4,
    "customerName": "Alice Johnson",
    "totalAmount": 26.97,
    "status": "PENDING",
    "orderTime": "2025-09-14T11:17:33.215756",
    "processedTime": null,
    "items": [
      {
        "itemName": "Burger",
        "quantity": 2,
        "price": 8.99
      },
      {
        "itemName": "Fries",
        "quantity": 1,
        "price": 3.99
      },
      {
        "itemName": "Coke",
        "quantity": 2,
        "price": 2.50
      }
    ]
  }
}
```

**Validation error (400 Bad Request)**
```json
{
  "success": false,
  "message": "Invalid order state",
  "timeStamp": "2025-09-14T11:15:15.2112686",
  "error": {
    "status": "BAD_REQUEST",
    "message": "Invalid order state",
    "subErrors": [
      "Total amount does not match sum of items"
    ]
  }
}
```

---

## Get Order by ID

**GET** `/api/orders/{id}`

**Successful response (200)**
```json
{
  "success": true,
  "message": "Order retrieved successfully",
  "timeStamp": "2025-09-14T11:18:10.4862606",
  "data": {
    "id": 3,
    "customerName": "Bob Johnson",
    "totalAmount": 42.00,
    "status": "DELIVERED",
    "orderTime": "2025-09-14T14:47:00",
    "processedTime": null,
    "items": [
      {
        "itemName": "Pasta",
        "quantity": 2,
        "price": 12.99
      },
      {
        "itemName": "Garlic Bread",
        "quantity": 1,
        "price": 4.99
      },
      {
        "itemName": "Ice Cream",
        "quantity": 2,
        "price": 5.50
      }
    ]
  }
}
```

---

## Get Orders (paged)

**GET** `/api/orders?page=0&size=10`

**Sample response (200)**
```json
{
  "success": true,
  "message": "Orders retrieved successfully",
  "timeStamp": "2025-09-14T11:18:38.9136543",
  "data": {
    "content": [
      {
        "id": 1,
        "customerName": "John Doe",
        "totalAmount": 25.99,
        "status": "PENDING",
        "orderTime": "2025-09-14T16:47:00",
        "processedTime": null,
        "items": [
          {
            "itemName": "Burger",
            "quantity": 2,
            "price": 8.99
          },
          {
            "itemName": "Fries",
            "quantity": 1,
            "price": 3.99
          },
          {
            "itemName": "Coke",
            "quantity": 2,
            "price": 2.50
          }
        ]
      },
      {
        "id": 2,
        "customerName": "Jane Smith",
        "totalAmount": 35.50,
        "status": "PROCESSED",
        "orderTime": "2025-09-14T15:47:00",
        "processedTime": null,
        "items": [
          {
            "itemName": "Pizza",
            "quantity": 1,
            "price": 15.99
          },
          {
            "itemName": "Salad",
            "quantity": 1,
            "price": 7.99
          },
          {
            "itemName": "Juice",
            "quantity": 2,
            "price": 3.50
          }
        ]
      },
      {
        "id": 3,
        "customerName": "Bob Johnson",
        "totalAmount": 42.00,
        "status": "DELIVERED",
        "orderTime": "2025-09-14T14:47:00",
        "processedTime": null,
        "items": [
          {
            "itemName": "Pasta",
            "quantity": 2,
            "price": 12.99
          },
          {
            "itemName": "Garlic Bread",
            "quantity": 1,
            "price": 4.99
          },
          {
            "itemName": "Ice Cream",
            "quantity": 2,
            "price": 5.50
          }
        ]
      },
      {
        "id": 4,
        "customerName": "Alice Johnson",
        "totalAmount": 26.97,
        "status": "PROCESSED",
        "orderTime": "2025-09-14T11:17:33",
        "processedTime": "2025-09-14T11:17:35",
        "items": [
          {
            "itemName": "Burger",
            "quantity": 2,
            "price": 8.99
          },
          {
            "itemName": "Fries",
            "quantity": 1,
            "price": 3.99
          },
          {
            "itemName": "Coke",
            "quantity": 2,
            "price": 2.50
          }
        ]
      }
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 4,
    "totalPages": 1,
    "first": true,
    "last": true
  }
}
```

---

## Update Order Status

**PATCH** `/api/orders/{id}/status`

**Request**
```json
{ "status": "CANCELLED" }
```

**Response (200)**
```json
{
  "success": true,
  "message": "Order status updated successfully",
  "timeStamp": "2025-09-14T11:25:35.3492948",
  "data": {
    "id": 10,
    "customerName": "Ravi Johnson",
    "totalAmount": 26.97,
    "status": "CANCELLED",
    "orderTime": "2025-09-14T11:25:30",
    "processedTime": null,
    "items": [
      {
        "itemName": "Pizze",
        "quantity": 2,
        "price": 8.99
      },
      {
        "itemName": "Roll",
        "quantity": 1,
        "price": 3.99
      },
      {
        "itemName": "Sprite",
        "quantity": 2,
        "price": 2.50
      }
    ]
  }
}
```

---


## Asynchronous Processing

- Orders are added to an **in-memory queue**.
- A background consumer processes them, moving them from `PENDING → PROCESSING → PROCESSED`.
- A delay of ~5 seconds simulates like real-world processing.

### Testing the Queue

- Create multiple orders rapidly.
- Check status → should be `PENDING`.
- Wait a few seconds.
- Status should move to `PROCESSED`.

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
  "message": "An unexpected error occurred",
  "timeStamp": "2025-09-14T11:26:19.292407",
  "error": {
    "status": "INTERNAL_SERVER_ERROR",
    "message": "An unexpected error occurred",
    "subErrors": [
      "JSON parse error: Cannot deserialize value of type `com.foodorderservice.Foodie.entity.enums.OrderStatus` from String \"CANCsELLED\": not one of the values accepted for Enum class: [CANCELLED, PROCESSING, DELIVERED, PROCESSED, PENDING]"
    ]
  }
}
```

---

## Contact

- Name: Sankalp
- Email - sankalpagarwal1304@gmail.com
- Link: [https://github.com/git-Sankalp-Agarwal/Foodie-App](https://github.com/git-Sankalp-Agarwal/Foodie-App)