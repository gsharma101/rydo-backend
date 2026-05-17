# Rydo Backend

A scalable ride-booking backend system built using Java, Spring Boot, and PostgreSQL.

## Features

* User Signup API
* Role-based user system (RIDER, DRIVER, ADMIN)
* Password encryption using BCrypt
* Spring Security integration
* DTO-based clean architecture
* PostgreSQL database integration
* Layered backend architecture

---

## Tech Stack

* Java 21
* Spring Boot 3
* Spring Security
* Spring Data JPA
* PostgreSQL
* Maven
* Lombok

---

## Project Structure

```text
src/main/java/com/gaurav/rydo
│
├── controller
├── service
├── repository
├── entity
├── dto
├── security
├── config
└── exception
```

---

## Current APIs

### Signup API

```http
POST /auth/signup
```

### Sample Request

```json
{
  "firstName": "Gaurav",
  "lastName": "Sharma",
  "email": "gaurav@example.com",
  "phoneNumber": "9876543210",
  "password": "password123",
  "role": "RIDER"
}
```

---

## Setup Instructions

### Clone Repository

```bash
git clone <your-repo-url>
```

### Configure PostgreSQL

Create a database named:

```text
rydo
```

Update `application.properties`:

```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Run Application

```bash
mvn spring-boot:run
```

---

## Future Plans

* JWT Authentication
* Login API
* Driver APIs
* Ride Booking APIs
* Redis Integration
* Kafka Integration
* Real-time Driver Tracking
* Docker Deployment

---

## Author

Gaurav Sharma
