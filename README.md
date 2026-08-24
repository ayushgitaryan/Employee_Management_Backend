# Employee Management System

A production-style backend REST API for managing employee records, built with Java and Spring Boot. The system supports full CRUD operations, search and filtering, pagination, and JWT-based authentication with role-based access control (ADMIN, HR, EMPLOYEE), and is deployed on Render backed by a managed PostgreSQL database.

## Features

- Employee CRUD operations (create, read, update, delete)
- Search and filtering by department, first name, and minimum salary
- Pagination and sorting on employee listings
- Request validation using Bean Validation (`@NotBlank`, `@Email`, `@Positive`, etc.)
- Centralized/global exception handling with consistent JSON error responses
- DTO-based API design, decoupling the database schema from the API contract
- JWT-based authentication (register/login)
- Role-based authorization with three roles: `ADMIN`, `HR`, `EMPLOYEE`
- Password hashing with BCrypt
- Deployed as a Docker container on Render, with a managed PostgreSQL database

## Tech Stack

| Technology       | Purpose                            |
|------------------|-------------------------------------|
| Java             | Backend programming language        |
| Spring Boot      | Application framework               |
| Spring Web       | REST API layer                      |
| Spring Data JPA  | Database access abstraction         |
| Hibernate        | ORM implementation                  |
| PostgreSQL       | Relational database                 |
| Spring Security  | Authentication and authorization    |
| JWT (jjwt)       | Token-based authentication          |
| Maven            | Dependency and build management     |
| Docker           | Containerization for deployment     |
| Render           | Hosting (web service + PostgreSQL)  |

## Architecture

The application follows a standard layered architecture:

```
Client
  │
  ▼
Security (JWT Filter)
  │
  ▼
Controller
  │
  ▼
DTO
  │
  ▼
Service (business logic)
  │
  ▼
Repository (Spring Data JPA)
  │
  ▼
Hibernate / JPA
  │
  ▼
PostgreSQL
```

**Layer responsibilities:**

- **Security** – A custom JWT filter (`JwtAuthFilter`) intercepts every request, validates the token, and populates the Spring Security context before the request reaches any controller.
- **Controller** – Exposes REST endpoints, delegates work to the service layer, and returns appropriate HTTP status codes. Contains no business logic.
- **DTO** – Defines the shape of data exchanged over the API (`EmployeeDTO`, `RegisterRequest`, `LoginRequest`, `AuthResponse`), keeping the database entity structure hidden from API consumers.
- **Service** – Contains business rules (e.g. duplicate email checks on employee creation), and handles entity-to-DTO mapping.
- **Repository** – Spring Data JPA interfaces providing CRUD operations and derived query methods (e.g. `findByDepartment`, `findByFirstNameContainingIgnoreCase`).
- **Entity/JPA/Hibernate** – `@Entity` classes (`Employee`, `User`) mapped to PostgreSQL tables, with schema managed automatically by Hibernate.

Validation is applied at the controller boundary using `@Valid` on DTOs, so invalid requests are rejected before reaching the service layer. Exceptions (such as `EmployeeNotFoundException`) are caught centrally by a `GlobalExceptionHandler` (`@RestControllerAdvice`), which returns consistent, structured JSON error responses instead of raw stack traces.

## Project Structure

```text
src/
└── main/
    ├── java/
    │   └── com/example/Emp/
    │       ├── Controller/     → EmployeeController, AuthController
    │       ├── Service/        → EmployeeService, EmployeeServiceImpl
    │       ├── Repository/     → EmployeeRepository, UserRepository
    │       ├── Entity/         → Employee, User, Role
    │       ├── dto/            → EmployeeDTO, RegisterRequest, LoginRequest, AuthResponse
    │       ├── exception/      → EmployeeNotFoundException, GlobalExceptionHandler, ErrorResponse
    │       ├── security/       → JwtUtil, JwtAuthFilter, SecurityConfig, CustomUserDetailsService
    │       └── EmpApplication.java
    └── resources/
        └── application.properties
```

## API Endpoints

### Authentication APIs

| Method | Endpoint             | Description             | Authentication |
|--------|-----------------------|--------------------------|----------------|
| POST   | `/api/auth/register`  | Register a new user      | Public         |
| POST   | `/api/auth/login`     | Log in and receive a JWT | Public         |

### Employee APIs

| Method | Endpoint              | Description                                  | Authorization           |
|--------|------------------------|-----------------------------------------------|--------------------------|
| GET    | `/api/employee`        | Get all employees (supports search, pagination, sorting) | ADMIN, HR, EMPLOYEE |
| GET    | `/api/employee/{id}`   | Get a single employee by ID                   | ADMIN, HR, EMPLOYEE      |
| POST   | `/api/employee`        | Create a new employee                         | ADMIN, HR                |
| PUT    | `/api/employee/{id}`   | Update an existing employee                   | ADMIN, HR                |
| DELETE | `/api/employee/{id}`   | Delete an employee                            | ADMIN                    |

## Authentication & Authorization

1. **Registration** – A user registers with a username, password, and role (`ADMIN`, `HR`, or `EMPLOYEE`) via `POST /api/auth/register`. The password is hashed using BCrypt before being persisted; it is never stored in plain text.
2. **Login** – On `POST /api/auth/login`, Spring Security's `AuthenticationManager` verifies the submitted credentials against the stored hash.
3. **JWT generation** – On successful login, `JwtUtil` issues a signed JWT containing the username and role as claims, with a defined expiration time.
4. **JWT validation** – On every subsequent request, `JwtAuthFilter` extracts the token from the `Authorization: Bearer <token>` header, validates its signature and expiry, and sets the authenticated user (with their role) in the Spring Security context.
5. **Role-based access** – `SecurityConfig` defines method-and-path-specific access rules: `ADMIN` can perform all operations including delete; `HR` can create and update but not delete; `EMPLOYEE` has read-only access.
6. **Protected endpoints** – All `/api/employee/**` endpoints require a valid JWT. `/api/auth/**` endpoints are public, since a token cannot be required to obtain one.

## Database Design

The application uses PostgreSQL with two main tables, managed automatically by Hibernate based on the JPA entity definitions:

- **`employees`** – Stores employee records: `id`, `first_name`, `last_name`, `email`, `phone`, `department`, `salary`, `joining_date`.
- **`users`** – Stores login credentials and role: `id`, `username`, `password` (hashed), `role` (`ADMIN` / `HR` / `EMPLOYEE`).

```text
User
  |
  | authenticates and is authorized to act on
  ↓
Employee
```

`User` and `Employee` are currently independent tables; a `User` is not linked to a specific `Employee` record.

## Validation & Exception Handling

Request payloads are validated using Bean Validation annotations on DTOs (`@NotBlank`, `@Email`, `@NotNull`, `@Positive`, `@PastOrPresent`), enforced via `@Valid` in the controller layer. Invalid requests are rejected with `400 Bad Request` and a field-by-field error message before reaching the service layer.

A `GlobalExceptionHandler` centralizes error handling:

| Scenario                              | HTTP Status | Handled By                        |
|----------------------------------------|-------------|-------------------------------------|
| Employee not found                     | 404         | `EmployeeNotFoundException`         |
| Duplicate email on employee creation    | 409         | `IllegalArgumentException`          |
| Validation failure (`@Valid`)          | 400         | `MethodArgumentNotValidException`   |
| Unhandled/unexpected error             | 500         | Generic `Exception` handler         |

## Pagination, Sorting & Filtering

`GET /api/employee` supports optional query parameters for filtering, pagination, and sorting, backed by Spring Data JPA's `Pageable`/`Page` abstraction:

```
GET /api/employee?page=0&size=10
GET /api/employee?sort=salary,desc
GET /api/employee?department=IT
GET /api/employee?firstName=John
GET /api/employee?minSalary=50000
GET /api/employee?department=IT&page=0&size=5&sort=salary,desc
```

Responses include pagination metadata (`totalElements`, `totalPages`, `first`, `last`, etc.) alongside the `content` array.

## Getting Started

### Prerequisites

- Java 21
- Maven (or the included Maven Wrapper, `mvnw`)
- PostgreSQL
- Git

### Clone Repository

```bash
git clone https://github.com/ayushgitaryan/Employee_Management_Backend.git
cd Employee_Management_Backend
```

### Database Setup

Create a PostgreSQL database locally:

```sql
CREATE DATABASE employee_management;
```

### Configuration

Set the following in `src/main/resources/application.properties` (or as environment variables):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/employee_management
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=YOUR_JWT_SECRET
```

### Run Application

Using the Maven Wrapper:

```bash
./mvnw spring-boot:run
```

Or build and run the packaged JAR:

```bash
./mvnw clean package -DskipTests
java -jar target/*.jar
```

The application starts on `http://localhost:8080` by default.

## API Testing

The API can be tested with Postman. Example flow:

**Register:**
```
POST /api/auth/register
Content-Type: application/json

{
  "username": "admin1",
  "password": "adminpass123",
  "role": "ADMIN"
}
```

**Login:**
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin1",
  "password": "adminpass123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Use the returned token as a Bearer token in the `Authorization` header for all `/api/employee/**` requests.

## Deployment

The application is containerized with Docker and deployed on **Render**:

- **Web Service**: Deployed from a multi-stage `Dockerfile` (Maven build stage + slim JRE runtime stage).
- **Database**: A managed PostgreSQL instance, also hosted on Render.
- **Configuration**: Database credentials and the JWT signing secret are injected via environment variables (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `JWT_SECRET`) — no secrets are hardcoded in the repository.
- **Production URL**: `https://employee-management-backend-mgrh.onrender.com`

## Future Improvements

The following are potential enhancements, not currently implemented:

- Unit and integration test coverage (JUnit, Mockito, `MockMvc`)
- Refresh token support (current JWTs are short-lived with no renewal mechanism)
- Linking `User` accounts to specific `Employee` records, to support an "view own profile" endpoint for the `EMPLOYEE` role
- CI/CD pipeline (e.g. GitHub Actions) for automated build and deployment
- API documentation via Swagger/OpenAPI
- Redis caching for frequently accessed data
- Structured audit logging for employee record changes

## Author

### Author

Ayush Gitaryan

GitHub: https://github.com/ayushgitaryan
LinkedIn: <your LinkedIn URL>
