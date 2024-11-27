# Order Management System

This project implements an **Order Management System** built using a microservices architecture with the **Spring ecosystem**. It focuses on creating a modular, highly efficient, and scalable application. The system is composed of various services for managing customers, products, orders, and delivery logistics. The design emphasizes autonomy of microservices, effective communication, and isolated data persistence.

---

## Objectives

1. Develop an efficient **Order Management System** using a **microservices architecture**.
2. Manage **Customers**, **Products**, and **Orders**, including:
   - Customer CRUD operations.
   - Product catalog with stock control and bulk import capabilities.
   - Full order processing lifecycle.
3. Implement **Delivery Logistics**, including:
   - Driver assignment.
   - Real-time tracking.
   - Optimized routes and estimated delivery times.
   - Status updates for customers.
4. Ensure robust communication between microservices and isolated data persistence.
5. Automate tests and provide API documentation for maintainability and quality assurance.

---

## Microservices Overview

### 1. Customer Management Service
- **Purpose**: Manages customer lifecycle and their associated addresses.
- **Technologies**: 
  - Spring Boot, Spring Data JPA, PostgreSQL, Flyway, Swagger.
- **API Endpoints**:
  - **Customers**:
    - `POST /api/customers`: Create a new customer.
    - `GET /api/customers`: Retrieve all customers.
    - `GET /api/customers/{id}`: Retrieve a specific customer.
    - `PUT /api/customers/{id}`: Update a specific customer.
    - `DELETE /api/customers/{id}`: Delete a specific customer.
  - **Addresses**:
    - `POST /api/customers/{customerId}/addresses`: Add a new address for a customer.
    - `GET /api/customers/{customerId}/addresses`: Retrieve all addresses of a customer.
    - `GET /api/customers/{customerId}/addresses/{addressId}`: Retrieve a specific address of a customer.
    - `PUT /api/customers/{customerId}/addresses/{addressId}`: Update a specific address of a customer.
    - `DELETE /api/customers/{customerId}/addresses/{addressId}`: Delete an address of a customer.

---

### 2. Product Management Service
- **Purpose**: Manages the product catalog, including detailed product information and stock control.
- **Technologies**: 
  - Spring Boot, Spring Data JPA, PostgreSQL, Flyway, Swagger.
- **API Endpoints**:
  - **Products**:
    - `POST /api/products`: Create a new product.
    - `PUT /api/products/{productId}`: Update an existing product.
    - `PUT /api/products/{productId}/enable`: Enable a disabled product.
    - `PUT /api/products/{productId}/disable`: Disable an active product.
    - `GET /api/products/{productId}`: Retrieve product details by ID.
    - `GET /api/products/all/[{productIds}]`: Retrieve all products by provided IDs.
    - `GET /api/products`: Retrieve products by name with pagination support.
  - **Product Stock**:
    - `PUT /api/products/stock/increase`: Increase product stock.
    - `PUT /api/products/stock/decrease`: Decrease product stock.

---

### 3. Product Import Service
- **Purpose**: Handles bulk product data import from external sources, such as CSV files.
- **Technologies**: 
  - Spring Boot, Spring Batch, PostgreSQL, Swagger.
- **API Endpoints**:
  - `POST /api/product-import`: Initiates the product data import process from a CSV file.

---

### 4. Order Management Service
- **Purpose**: Manages the lifecycle of orders, including customer and product validation, and order status updates.
- **Technologies**: 
  - Spring Boot, Spring Data MongoDB, MongoDB, Spring Integration, Swagger.
- **API Endpoints**:
  - `POST /api/orders`: Create a new order.
  - `GET /api/orders/{orderId}`: Retrieve specific order details.
  - `PUT /api/orders/{orderId}/cancel`: Cancel an order.
  - `DELETE /api/orders/{orderId}`: Delete an order.

---

### 5. Delivery Logistics Service
- **Purpose**: Manages driver assignments, delivery tracking, status updates, and optimized routes.
- **Technologies**: 
  - Spring Boot, Spring Data JPA, PostgreSQL, Flyway, Feign Client, Swagger.
- **API Endpoints**:
  - `POST /api/deliveries`: Create a new delivery.
  - `GET /api/deliveries/{deliveryId}`: Retrieve delivery details.
  - `PUT /api/deliveries/{deliveryId}/assign-driver`: Assign a driver to a delivery.
  - `PUT /api/deliveries/{deliveryId}/update-status`: Update the status of a delivery.
  - `GET /api/deliveries/track/{deliveryId}`: Track delivery status in real-time.

---

### 6. Cloud Gateway Service
- **Purpose**: Acts as a central entry point, routing client requests to the appropriate microservices.
- **Technologies**: 
  - Spring Cloud Gateway.
- **API Features**:
  - Centralized routing.
  - Load balancing.

---

### 7. Eureka Server
- **Purpose**: Facilitates dynamic discovery and registration of microservices in a distributed architecture.
- **Technologies**: 
  - Spring Cloud Netflix Eureka.

---

## Key Features

1. **Scalability**: Modular design with independent microservices.
2. **Efficiency**: Optimized APIs for fast processing and data handling.
3. **Maintainability**: Clean architecture and automated testing.
4. **Documentation**: Fully documented APIs with Swagger.
5. **Data Integrity**: Isolated persistence for each service.

---

## Getting Started

1. Clone the repository.
2. Configure the environment variables for each microservice.
3. Run each microservice using your preferred IDE or `mvn spring-boot:run`.
4. Access the APIs via Swagger UI at `/swagger-ui.html`.

---
