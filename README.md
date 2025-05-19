# Bank System API

A Spring Boot-based RESTful Banking System with JWT authentication, Swagger UI documentation, and modular support for managing **Customers**, **Accounts**, and **Cards**.

---

## Features

- JWT-based authentication
- Swagger UI documentation
- Modular REST APIs for:
  - Customers
  - Accounts (with filtering)
  - Cards
- Unit and Integration Tests (JUnit 5, Mockito)
- Docker & Docker Compose support
- CORS enabled for frontends (e.g., Swagger UI or Postman)

---

## Tech Stack

- Java 21
- Spring Boot 3.4.5
- Spring Security
- Spring Data JPA (Hibernate)
- PostgreSQL
- Docker
- Swagger/OpenAPI
- JUnit 5 + Mockito

---

## Project Structure
src/
├── main/
│ ├── java/com.bank.banksystem/
│ │ ├── controller/
│ │ ├── service/
│ │ ├── entity/
│ │ ├── dto/
│ │ ├── mapper/
│ │ └── security/
│ └── resources/
│ └── application.yml
├── test/
│ └── ... (unit tests)
Dockerfile
docker-compose.yml

## Setup Instructions

### 1. Clone the repo
```
git clone https://github.com/codeitp/banksystem.git
cd banksystem
```
### 2. Run using Docker Compose
```
docker-compose up --build
```
Access app at: http://localhost:8080

Swagger UI: http://localhost:8080/swagger-ui.html

PostgreSQL DB: localhost:5432, user: postgres, password: admin

### 3. Running Tests
```
./mvnw test
```
### Authentication
Generate a token using:
```
POST http://localhost:8080/auth/token
Content-Type: application/json

{
  "username": "your-username"
}
```
Response:
```
"eyJhbGciOiJIUzI1NiIsInR..."
```
### Use the token in your requests:
```
Authorization: Bearer <token>

```
### API Endpoints Overview
Resource		Method		Endpoint				Description
Customers		GET			/api/v1/customers		List customers
Customers		POST		/api/v1/customers		Create customer
Customers		GET			/api/v1/customers/{id}	Get customer by ID
Accounts		GET			/api/v1/accounts		List accounts (filter)
Cards			GET			/api/v1/cards			List cards
...				...			...						More in Swagger UI


### 👤 Author
Patroba Oteko
📧 patrobaoteko@gmail.com
🌍 Nairobi, Kenya

