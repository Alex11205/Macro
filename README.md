# MacroTracker

[![Backend CI](https://github.com/Alex11205/Macro/actions/workflows/backend-ci.yaml/badge.svg)](https://github.com/Alex11205/Macro/actions/workflows/backend-ci.yaml)

[![Frontend CI](https://github.com/Alex11205/Macro/actions/workflows/frontend-ci.yaml/badge.svg)](https://github.com/Alex11205/Macro/actions/workflows/frontend-ci.yaml)

MacroTracker is a full-stack macronutrition tracking application, focused on backend secure REST API development, relational data modeling, automated testing, GitHub Actions CI, and production deployment.

Live Production: [View Site](https://macro-xi-lime.vercel.app)

API Documentation: [Swagger UI Endpoint](https://macro-production-b20a.up.railway.app/swagger-ui/index.html)

## Why I develop it

It's essential for people working out or on a diet to track their daily macros and calories. So I developed this web app to make it easier. 

## Screenshots & GIFs

User Profile:

![User Profile](assets/images/user_profile.png)

Login:

![Login](assets/images/Login.gif)

Tracking Foods:

![Tracking Foods](assets/images/tracking_foods_git.gif)

## Engineering Highlights

- Stateless JWT authentication with USER and ADMIN authorization
- BCrypt password hashing and validated DTO-based request handling
- Global Exception handling with SLF4J Logging
- PostgreSQL persistence with versioned Liquibase migrations
- Unit, controller slice, Testcontainers repository, integration, and E2E testing
- Automated backend and frontend verification through GitHub Actions
- Independently deployed frontend and backend services

## Features

- User registration and login with JWT authentication
- Role-based authorization for USER and ADMIN endpoints
- Custom food creation and browsing
- Personal favorite food management
- Daily macro tracking with automated caloric breakdown
- Global exception handling with consistent error responses
- SLF4J Logging
- PostgreSQL persistence with Spring Data JPA
- Version-controlled database migrations with Liquibase
- OpenAPI/Swagger API documentation
- Backend unit, controller slice, repository, integration, and E2E tests
- GitHub Actions CI for backend and frontend

## Tech Stack

### Backend
- Java 21
- Spring Boot 4
- Spring Security
- Spring Data JPA
- PostgreSQL
- Liquibase
- Testcontainers
- JUnit 5 / Mockito
- springdoc-openapi

### Frontend
- Next.js
- React
- TypeScript/JavaScript
- Playwright smoke test
- CSS/Tailwind

### DevOps
- Docker Compose
- GitHub Actions CI
- Liquibase database version control
- Documentation: OpenAPI/Swagger

### Hosting
- Frontend: Vercel
- Backend: Railway

## Architecture

The backend currently uses a layered structure:

- `controller`
- `service`
- `repository`
- `model`
- `dto`
- `security`
- `exceptions`
- `config`

```text
+---src
|   +---main
|   |   +---java
|   |   |   \---com
|   |   |       \---alex
|   |   |           \---macro
|   |   |               +---config
|   |   |               +---controller
|   |   |               +---dto
|   |   |               +---exceptions
|   |   |               +---model
|   |   |               +---repository
|   |   |               +---security
|   |   |               \---service
|   |   \---resources
|   |       +---db
|   |       |   \---changelog
|   |       |       \---changes
|   |       +---static
|   |       \---templates
|   \---test
|       +---java
|       |   \---com
|       |       \---alex
|       |           \---macro
|       |               +---controller
|       |               +---e2e
|       |               +---integration
|       |               +---repository
|       |               \---service
|       \---resources
\---pom.xml
```

## API Documentation


```text
https://macro-production-b20a.up.railway.app/swagger-ui.html
https://macro-production-b20a.up.railway.app/v3/api-docs
```

## Local Setup
Follow these steps to get a local development environment running on your machine.

### Prerequisites
- Java 21
- Maven
- Docker Desktop
- Node.js 24
- npm

### Setup Backend

Clone this repository and navigate to the backend directory

```bash
git clone https://github.com/Alex11205/Macro.git
cd Macro/backend
```

### Backend Environment Variables

Create 'backend/secret.env' based on 'backend/secret.env.example':

```bash
cp secret.env.example secret.env
```

Example:

```properties
DB_URL=jdbc:postgresql://localhost:5432/macro
DB_USER=postgres
DB_PASSWORD=your_password
JWT_SECRET_KEY=your_base64_secret
JWT_EXPIRATION=3600000
```

Create a db_password.txt in /backend containing your database password only, and it must match DB_PASSWORD in your secret.env
```txt
your_password
```

### Start PostgreSQL

```bash
docker compose up -d database
```

### Run Backend Tests

```bash
./mvnw clean verify
```

This runs:
- Unit tests: isolated service business logic
- Controller slice tests: HTTP methods, validation, and exception responses
- Repository tests: JPA mappings and PostgreSQL behavior through Testcontainers
- Integration tests: security, service, persistence, and API layers together
- E2E tests: complete user workflows
- Frontend smoke test: critical UI flow and application availability


### Run Backend

```bash
./mvnw spring-boot:run
```

### Run Frontend

Navigate to the frontend directory

```bash
cd ../frontend
```

```bash
npm ci
npm run dev
```

### Run Frontend Checks

```bash
npm run lint
npm run build
npm run test:smoke
```

The application now should be accessible locally at [`http://localhost:3000`](http://localhost:3000)

## CI
GitHub Actions runs:
- Backend build and test with Maven
- Frontend install, lint, build and smoke test

## Deployment
Real live deployment links:
- Frontend: https://macro-xi-lime.vercel.app
- Backend: https://macro-production-b20a.up.railway.app
- Swagger UI: https://macro-production-b20a.up.railway.app/swagger-ui/index.html

## Challenges & Solutions

- Challenge: All the integration tests passed individually, but some failed when I run ./mvnw clean verify.  
  Cause: The outcome of previous tests may have impact on the following tests.  
  Solution: Add a sql block to truncate all tables after each tests.  
    

- Challenge: GitHub Actions CI automated tests couldn't pass and the error message was too long to locate the cause.  
  Solution: Add a step in CI to print the failsafe report, and found the root cause and resolved it.  
  Cause: GitHub Actions couldn't read my JWT secret.  
  Solution: Add a fake JWT secret in application-test.properties



- Challenge: Everything worked fine in local development but failed deploying on Railway.  
  Cause: It was because Railway didn't support Java 25 which I was using.  
  Solution: I switched to Java 21.

 
## Future Improvements

- Actuator
- Pagination and sorting
- Filter and search bar
- Rate limiting
- Refresh token flow
- More frontend E2E tests coverage
- Caching
- Feature-based package restructuring




