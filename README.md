# Graduation System

Graduation System is a Spring Boot REST backend for managing the graduation process at a school or university. It handles students, graduation-related records, and role-based access, with secure JWT authentication and data stored in MySQL.

## Features

- Secure registration and login using JSON Web Tokens (JWT)
- Role-based access control (e.g. student, teacher, administrator)
- Manage students and their graduation data (thesis/diploma projects, supervisors, status)
- Input validation on all incoming data
- REST API for integration with a front-end client
- Persistent storage in a relational database

## Technology

- **Language:** Java 21
- **Framework:** Spring Boot 3.4.0 (Spring Web, Spring Data JPA)
- **Security:** Spring Security with JWT (jjwt 0.11.5)
- **Database:** MySQL (H2 in-memory database for tests)
- **Validation:** Jakarta Validation / Hibernate Validator
- **Utilities:** Lombok
- **Build tool:** Gradle (with Gradle Wrapper)
- **Testing:** Spring Boot Starter Test, JUnit 5

## Getting Started

### Prerequisites

- JDK 21
- A running MySQL server

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/GeorgiPetrov8/GraduationSystem.git
   cd GraduationSystem
   ```
2. Create a MySQL database and set the connection details and JWT secret in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/graduation_system
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```
3. Run the application:
   ```bash
   ./gradlew bootRun
   ```
   On Windows use `gradlew.bat bootRun`. The API starts on `http://localhost:8080`.

### Run tests

```bash
./gradlew test
```
