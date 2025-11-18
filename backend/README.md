# WayFinder Backend 🚀

## Technical Overview
The WayFinder backend is a **Spring Boot REST API** that serves as the central hub for all application logic, implementing key Computer Networks principles including client-server architecture, HTTP protocol handling, and distributed data management.

## Computer Networks Concepts Implemented

### 1. **Client-Server Architecture**
- **Centralized server** handling multiple client requests
- **Stateless HTTP communication** between frontend and backend
- **Load balancing ready** - Spring Boot can be easily scaled horizontally

### 2. **RESTful API Design**
- **HTTP methods** (POST) for standardized communication
- **JSON data exchange** for platform-independent messaging
- **Resource-based URLs** (`/api/login`, `/api/addFriend`, etc.)
- **Status codes** and error handling for reliable communication

### 3. **Network Protocol Stack**
- **Application Layer**: REST API endpoints
- **Transport Layer**: TCP for reliable data transmission
- **Network Layer**: IP routing handled by infrastructure
- **Data Link/Physical**: Ethernet/WiFi abstraction

### 4. **Distributed Database Architecture**
- **Cloud-hosted MongoDB Atlas** - geographically distributed
- **Connection pooling** for efficient network resource usage
- **Persistent connections** via MongoDB driver
- **Data replication** across MongoDB cluster nodes

## Technical Architecture

### Service Layer Pattern
```
HTTP Request → Controller → Service → Database Component → MongoDB Atlas
```

1. **userController** - REST endpoint handlers
2. **connection** - Business logic and validation
3. **mongoDBFunctions** - Database operations
4. **MongoDB Atlas** - Cloud data persistence

### Key Components

#### REST Controllers (`userController.java`)
- Handles HTTP requests from frontend clients
- Implements standard REST patterns
- Returns JSON responses for cross-platform compatibility

#### Service Layer (`connection.java`)
- Business logic implementation
- Friend limit enforcement (max 10 friends)
- Request validation and security checks

#### Database Layer (`mongoDBFunctions.java`)
- MongoDB connection management
- CRUD operations with error handling
- Connection pooling for network efficiency

## Network Communication Patterns

### Request-Response Cycle
1. **Frontend** sends HTTP POST to `localhost:8067/api/*`
2. **Spring Boot** deserializes JSON to DTO objects
3. **Controller** routes to appropriate service method
4. **Service** validates and processes business logic
5. **Database** executes MongoDB operations
6. **Response** serialized back to JSON and returned

### Networking Optimizations
- **Connection reuse** via HTTP keep-alive
- **JSON compression** for reduced bandwidth
- **Database connection pooling** to minimize network overhead
- **Async processing** where applicable

## Environment Configuration
```properties
# application.properties
spring.config.import=optional:dotenv
spring.data.mongodb.uri=${MONGO_URI}
server.port=8067
```

## API Endpoints
| Endpoint | Purpose | Network Pattern |
|----------|---------|----------------|
| `/api/login` | User authentication | Request-Response |
| `/api/signup` | User registration | Request-Response |
| `/api/addFriend` | Friend management | Request-Response |
| `/api/updateLocation` | Location sharing | Request-Response |
| `/api/getFriends` | Data retrieval | Request-Response |

## Scalability & Performance
- **Stateless design** enables horizontal scaling
- **Database connection pooling** optimizes network resources
- **JSON serialization** minimizes network payload
- **Cloud database** provides geographic distribution

## Security Considerations
- **Environment variables** for sensitive configuration
- **Input validation** at service layer
- **Password verification** before data access
- **Friend limit enforcement** prevents network abuse

## Running the Backend
```bash
cd backend
mvn spring-boot:run
# Server starts on http://localhost:8067
```

---
*Demonstrates distributed systems, HTTP protocol implementation, and cloud database integration.*