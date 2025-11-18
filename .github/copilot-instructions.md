# WayFinder - AI Coding Assistant Instructions

## Project Overview
WayFinder is a Computer Networks course project implementing a social location tracker with Spring Boot backend, Vaadin frontend, and MongoDB Atlas integration.

## Architecture & Structure
- **Three-Tier System**: Standalone MongoDB module (`mongoDB/`), Spring Boot REST API backend (`backend/`), Vaadin frontend (`frontend/`)
- **Database**: MongoDB Atlas with three collections: `Users.UserDatabase`, `Users.Requests`, `Users.Friends`
- **Communication**: Frontend communicates with backend via REST API on port 8067, backend uses MongoDB service layer
- **Java Versions**: Backend (Java 17), Frontend (Java 21+), Legacy MongoDB module (package-less classes)

## Key Development Patterns

### Environment Configuration
- MongoDB URI stored in `.env` files (both `mongoDB/.env` and `backend/.env`)
- Spring Boot uses `spring.config.import=optional:dotenv` to load environment variables
- Backend connection: `spring.data.mongodb.uri=${MONGO_URI}` in `application.properties`

### Service Layer Architecture
**Backend (`backend/`):**
- `mongoDBFunctions` - Database operations component (injected via Spring)
- `connection` - Service layer with business logic (friend limits, validation)
- `userController` - REST endpoints using DTO pattern
- Pattern: Controller → Service → MongoDB Component

**Frontend (`frontend/`):**
- Vaadin views with `@Route` annotations (`LoginView`, `HomeView`, `FriendsView`, `ProfileView`)
- `backendClient` - HTTP client to communicate with backend REST API
- Session management via `VaadinSession.getAttribute("username")`

### Database Schema & Operations
**Collections:**
- `Users.UserDatabase` - User profiles (username, name, contact, lastLocation, password)
- `Users.Requests` - Friend requests (sender, receiver, requestStatus 0/1)
- `Users.Friends` - Bidirectional friendships (userName1, userName2)

**Business Rules:**
- Friend limit: 10 friends per user
- Bidirectional friendship records (both directions stored)
- Request acceptance automatically creates friendship records

## Critical Development Workflows

### Running the Application
1. **Backend**: `cd backend && mvn spring-boot:run` (port 8067)
2. **Frontend**: `cd frontend && mvn spring-boot:run` (Vaadin dev mode)
3. **Legacy MongoDB testing**: Direct execution of `mongoDBFunctions.java`

### Adding New Features
- **New API endpoints**: Add to `userController` with corresponding DTO
- **Frontend views**: Create Vaadin view with `@Route`, inject `backendClient`
- **Database operations**: Add to `mongoDBFunctions`, expose via `connection` service

## Project-Specific Conventions
- **DTO Pattern**: Public fields, constructor-based (no getters/setters)
- **Error Handling**: Simple string responses ("success"/"fail") for API calls
- **Authentication**: Basic username/password verification, no JWT/sessions
- **Location Storage**: String format coordinate pairs (e.g., "28.6139,77.2090")

## Integration Points
- **Frontend ↔ Backend**: REST calls via `backendClient` using Spring `RestTemplate`
- **Backend ↔ Database**: Spring Data MongoDB with custom service layer
- **Configuration**: Environment variables via dotenv, Spring profiles for development/production

## Critical Dependencies
```xml
<!-- Backend -->
spring-boot-starter-data-mongodb  // MongoDB integration
spring-dotenv:3.0.0              // Environment management

<!-- Frontend -->
vaadin-spring-boot-starter:24.9.5 // Vaadin framework
backend dependency               // Access to DTOs and types
```