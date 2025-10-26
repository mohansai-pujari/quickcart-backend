# Quickcart Backend - A Microservices Project

This project comprises three services: **Order Service**, **Product Service**, and **Notification Service**, all integrated via **Kafka** for real-time messaging. The services communicate through REST APIs and Kafka topics.

---

## Table of Contents

1. [Services Overview](#services-overview)
2. [Setup](#setup)
3. [Kafka Configuration](#kafka-configuration)
4. [APIs](#apis)

   * [Order Service](#order-service)
   * [Product Service](#product-service)
   * [Notification Service](#notification-service)
5. [Notes](#notes)

---

## Services Overview

| Service Name         | Port | Responsibility                                                   |
| -------------------- | ---- | ---------------------------------------------------------------- |
| Order Service        | 8080 | Manages orders, validates stock, publishes order events to Kafka |
| Product Service      | 8081 | Manages product catalog and stock levels                         |
| Notification Service | 8083 | Consumes order events from Kafka and stores notifications        |

---

## Setup

1. Start **Kafka** (port 9092) and **Zookeeper**.
2. Start **Product Service** (port 8081).
3. Start **Order Service** (port 8080).
4. Start **Notification Service** (port 8083).
5. Make sure Kafka topic `order-events` exists.

---

## Kafka Configuration

**Kafka Topic:** `order-events`

**Order Service (Producer)**

```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.topic.order-events=order-events
```

**Notification Service (Consumer)**

```properties
spring.kafka.consumer.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=notification-service
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
spring.kafka.consumer.properties.spring.deserializer.value.delegate.class=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*
spring.kafka.consumer.properties.spring.json.value.default.type=org.notification_service.dto.OrderCreatedEvent
spring.kafka.consumer.properties.spring.json.use.type.headers=false
spring.kafka.topic.order-events=order-events
```

---

## APIs

### Order Service

**Base URL:** `http://localhost:8080/api/orders`

| Method | Endpoint                 | Request Body                                                       | Response             | Description                                                                              |
| ------ | ------------------------ | ------------------------------------------------------------------ | -------------------- | ---------------------------------------------------------------------------------------- |
| POST   | `/`                      | `{ "productId": "UUID", "quantity": int, "customerId": "string" }` | Created Order object | Create a new order. Validates stock, decreases product stock, and publishes Kafka event. |
| GET    | `/{orderId}`             | N/A                                                                | Order object         | Get details of a specific order by ID                                                    |
| GET    | `/`                      | N/A                                                                | List of Orders       | Get all orders                                                                           |
| GET    | `/customer/{customerId}` | N/A                                                                | List of Orders       | Get all orders for a specific customer                                                   |

**Order Object Example:**

```json
{
        "id": "UUID",
        "productId": "UUID",
        "productName": "String",
        "unitPrice": 899.0,
        "quantity": 6,
        "totalPrice": 5394.0,
        "customerId": "1008",
        "createdAt": "2025-10-26T17:49:08.316+00:00"
}
```

---

### Product Service

**Base URL:** `http://localhost:8081/api/products`

| Method | Endpoint       | Request Body                                           | Response                | Description                      |
| ------ | -------------- | ------------------------------------------------------ | ----------------------- | -------------------------------- |
| GET    | `/{productId}` | N/A                                                    | Product object          | Get product details              |
| GET    | `/`            | N/A                                                    | Product object          | Get product details              |
| POST   | `/`            | `{ "name": "string", "price": double, "stock": int }`  | Product object          | Create a product object          |
| POST   | `/multiple`    | `[{ "name": "string", "price": double, "stock": int }]`| List of Product objects | Create multiple product objects  |
| PUT    | `/{productId}` | `{ "name": "string", "price": double, "stock": int }`  | Updated Product object  | Update product stock and details |
| DELETE | `/{productId}` | N/A                                                    | Successfully Deleted    | To Delete a product object       |

**Product Object Example:**

```json
{
        "id": "UUID",
        "name": "String",
        "sku": "WM-001",
        "price": 999.0,
        "stock": 150,
        "activeStatus": true,
        "createdOn": "2025-10-26T17:47:29.022+00:00",
        "updatedOn": "2025-10-26T17:47:29.022+00:00"
}
```

---

### Notification Service

**Base URL:** `http://localhost:8083/api/notifications`

| Method | Endpoint | Request Body | Response                  | Description                               |
| ------ | -------- | ------------ | ------------------------- | ----------------------------------------- |
| GET    | `/`      | N/A          | List of OrderCreatedEvent | Get all notifications received from Kafka |

**OrderCreatedEvent Example:**

```json
{
        "orderId": "UUID",
        "productId": "UUID",
        "productName": "String",
        "quantity": 6,
        "totalPrice": 5394.0,
        "customerId": "1008",
        "createdAt": "2025-10-26T17:52:33.928+00:00"
}
```
* Notification details are in-memory storage and are not permanent.
---

## Notes

1. **Kafka Serialization/Deserialization Issues:**
   * Ensure **notification-service** has its own copy of `OrderCreatedEvent` in the `org.notification_service.dto` package.
   * Old messages serialized with the `order_service` package may fail.


2. **Error Handling:**
   * Use of `ErrorHandlingDeserializer` in the consumer is to prevent crashes on bad messages.
   * Can customize logging or implement dead-letter topics.


3. **Persistence:**

   * **Order Service** now uses a repository instead of in-memory storage.
   * We can use JPA (H2/MySQL) for `Orders` persistence.

---

## Future Enhancements & Scalability Improvements

1. **Hybrid Database Architecture:**
  * Combine a relational database (such as MySQL/PostgreSQL) with Elasticsearch, or a NoSQL database (like MongoDB), for optimized data management.
  * Use the primary database (SQL/MongoDB) for fast write and transactional consistency.
  * Use Elasticsearch asynchronously for pattern-based or full-text product search.
  * A background sync job or Kafka stream can handle data propagation to ES to maintain near real-time consistency.


2. **Enhanced Notification Infrastructure:**
  * Integrate Email and SMS notification pipelines for real-time order status updates using tools like Twilio, AWS SNS, or SendGrid.
  * Use message queues (Kafka topics) for asynchronous delivery.
  * Add retry and failure tracking to ensure reliable notifications.


3. **Order Service Optimization:**
  * Store order data in MongoDB for write efficiency and scalability.
  * Ideal for handling large numbers of concurrent order placements.
  * Use Kafka streams or batch sync jobs to replicate essential data to analytics or SQL layers.


4. **Caching Layer for Hot Data:**
  * Introduce Redis or Hazelcast as a caching layer to store frequently accessed product and order details.
  * Reduces latency for API reads.
  * Useful for handling surge traffic during high-load events (e.g., flash sales).


5. **API Gateway and Service Registry:**
  * Add Spring Cloud Gateway and Eureka/Nacos for centralized routing, monitoring, and service discovery.
  * Enables smoother scaling and traffic management for all microservices.


6. **Centralized Logging and Monitoring:**
  * Implement ELK (Elasticsearch, Logstash, Kibana) or Prometheus + Grafana for real-time logging, metrics, and alerting.
  * Helps trace inter-service communication and identify performance bottlenecks.


7. **Authentication and Authorization:**
  * Integrate JWT-based authentication or OAuth2 for secure API access across services.
  * Protects order and customer information from unauthorized access.


8. **CI/CD and Containerization:**
  * Use Docker + Kubernetes for deployment automation, scalability, and fault tolerance.
  * CI/CD pipelines with GitHub Actions or Jenkins ensure faster delivery and rollback capability.

---
