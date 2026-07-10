# MacroTracker

MacroTracker is a full-stack macronutrition tracking app focused on backend API design, authentication, persistence, testing, and deployment readiness.

Live Production: [View Site](Vercel placeholder)

API Documentation: [Swagger UI Endpoint](Swagger placeholder)

User Profile:

![User Profile](assets/images/user_profile.png)

Login:

![Login](assets/images/Login.gif)

Tracking Foods:

![Tracking Foods](assets/images/tracking_foods_git.gif)



## Features

- User registration and login with JWT authentication
- Role-based authorization for USER and ADMIN endpoints
- Custom food creation and browsing
- Personal favorite food management
- Daily macro tracking with automated caloric breakdown
- Global exception handling with consistent error responses
- PostgreSQL persistence with Spring Data JPA
- Robust schema evolution managed seamlessly via Liquibase
- OpenAPI/Swagger API documentation
- Backend unit, controller slice, repository, integration, and E2E tests
- GitHub Actions CI for backend and frontend

## Tech Stack

### Backend
- Java 25
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
├─src
│  ├─main
│  │  ├─java
│  │  │  └─com
│  │  │      └─alex
│  │  │          └─macro
│  │  │              ├─config
│  │  │              ├─controller
│  │  │              ├─dto
│  │  │              ├─exceptions
│  │  │              ├─model
│  │  │              ├─repository
│  │  │              ├─security
│  │  │              └─service
│  │  └─resources
│  │      ├─db
│  │      │  └─changelog
│  │      │      └─changes
│  │      ├─static
│  │      └─templates
│  └─test
│      ├─java
│      │  └─com
│      │      └─alex
│      │          └─macro
│      │              ├─controller
│      │              ├─e2e
│      │              ├─integration
│      │              ├─repository
│      │              └─service
│      └─resources
└─target
```

This keeps the project simple and readable at its current size.

## API Documentation

When the backend is running locally:

```text
http://localhost:8080/swagger-ui.html
http://localhost:8080/v3/api-docs
```

## Local Setup
Follow these steps to get a local development environment running on your machine.

### Prerequisite
- Java 25
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
DB_URL=jdbc:postgresql://localhost:5432/database
DB_USER=database_user
DB_PASSWORD=your_password
JWT_SECRET_KEY=your_base64_secret
JWT_EXPIRATION=3600000
```

Create a db_password.txt in /backend containing your database password only
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
- Unit Tests
- Controller slice tests
- Repository slice tests with Testcontainers
- Integration tests
- Backend End to End tests


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
GitHub Action runs:
- Backend build and test with Maven
- Frontend install, lint, build and smoke test

## Deployment
Real live deployment links will be added after release
- Frontend:
- Backend:
- PostgreSQL:
- Swagger UI:

## Future Improvements

- Pagination and sorting
- Filter and search bar
- Rate limiting
- Refresh token flow
- More frontend E2E tests coverage
- Kafka
- Feature-based package restructuring



