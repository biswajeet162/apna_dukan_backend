# Apna Dukan Backend

Spring Boot backend application for Apna Dukan B2B platform.

## Database Configuration

The application supports multiple database configurations:

### Local Development (H2 - In-Memory)
For quick local testing without setting up PostgreSQL:
```bash
# Run with local profile
./gradlew bootRun --args='--spring.profiles.active=local'
```

- **Database**: H2 in-memory database
- **H2 Console**: Available at `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:apnadukan`
- **Username**: `sa`
- **Password**: (empty)
- **Note**: Data is lost on application restart

### Development (PostgreSQL)
For development with persistent data:
```bash
# Run with dev profile
./gradlew bootRun --args='--spring.profiles.active=dev'
```

- **Database**: PostgreSQL
- **Connection**: `jdbc:postgresql://localhost:5432/apnadukan`
- **Username**: `apnadukan`
- **Password**: `apnadukan`
- **DDL Mode**: `update` (auto-creates/updates schema)

### Production (PostgreSQL)
For production deployment:
```bash
# Run with prod profile
./gradlew bootRun --args='--spring.profiles.active=prod'
```

- **Database**: PostgreSQL (via Docker or external)
- **DDL Mode**: `validate` (no auto-updates)
- **Environment Variables**: 
  - `DATABASE_URL`
  - `DATABASE_USERNAME`
  - `DATABASE_PASSWORD`

## Docker Setup

To run with PostgreSQL using Docker:
```bash
cd docker
docker-compose up -d
```

This will start:
- PostgreSQL database on port 5432
- Application on port 8080

## Running the Application

### Local (H2):
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

### Development (PostgreSQL):
```bash
# Start PostgreSQL first (via Docker or locally)
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Production:
```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
```

## API Endpoints

### Authentication
- `POST /api/v1/auth/login` - Send OTP for login
- `POST /api/v1/auth/signup` - Send OTP for registration
- `POST /api/v1/auth/verify-otp` - Verify OTP and get tokens
- `POST /api/v1/auth/refresh-token` - Refresh access token
- `POST /api/v1/auth/logout` - Logout user
- `GET /api/v1/auth/me` - Get current user profile

## Database Migrations

For production, use Flyway or Liquibase for database migrations. Migration scripts should be placed in:
- `src/main/resources/db/migration/`

## Notes

- H2 database is only for local development/testing
- For production, always use PostgreSQL
- Database schema is auto-created in local/dev modes
- Use proper migrations for production deployments

