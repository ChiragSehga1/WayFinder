# WayFinder - AI Coding Assistant Instructions

## Project Overview
WayFinder is a Computer Networks course project with a multi-tier architecture featuring Java backend with MongoDB integration and separate frontend/backend components.

## Architecture & Structure
- **Hybrid Build System**: Uses both Maven (`pom.xml`) and manual compilation with JAR dependencies (`lib/`)
- **Multi-component**: Frontend, backend, and main Java application are separate but related components
- **Database**: MongoDB Atlas cloud database with connection string in `.env` (excluded from git)
- **Java Version**: Targets Java 11 with Maven compiler plugin

## Key Development Patterns

### Environment Configuration
- MongoDB connection string stored in `.env` file using `dotenv-java` library
- Pattern: `Dotenv.load()` then `dotenv.get("MONGO_URI")` for secure credential access
- Always validate environment variables with null/empty checks before use

### Build & Run Workflows
Two execution methods available:
1. **Maven**: `mvn exec:java` (uses exec-maven-plugin with mainClass: MongoClientConnectionExample)
2. **Manual**: Use provided scripts
   - Windows: `run.bat` or `run.ps1` 
   - Compiles with: `javac -cp "lib\*" src\main\java\*.java -d .`
   - Runs with: `java -cp ".;lib\*" MongoClientConnectionExample`

### MongoDB Integration
- Uses MongoDB Java Driver 4.11.1 with ServerAPI V1
- Pattern: Create `MongoClientSettings` with `ConnectionString` and `ServerApi`
- Always use try-with-resources for `MongoClient` connections
- Database operations wrapped in try-catch for `MongoException`
- Test connectivity with `database.runCommand(new Document("ping", 1))`

## Project-Specific Conventions
- Main class directly in default package (no package declaration)
- JAR dependencies manually managed in `lib/` directory alongside Maven
- Hardcoded paths in run scripts point to specific development machine
- `.env` file ignored by git but required for local development
- Compiled `.class` files committed to repository (unusual but intentional)

## Critical Dependencies
```xml
mongodb-driver-sync:4.11.1  // Core MongoDB connectivity
dotenv-java:3.0.0          // Environment variable management
```

## Development Setup Requirements
1. Java 11+ installed and configured
2. Maven for dependency management (optional - JARs provided)
3. `.env` file with valid `MONGO_URI` connection string
4. MongoDB Atlas cluster accessible from development environment

## File Patterns
- Configuration: `pom.xml` (Maven), `.env` (secrets), `lib/` (manual JARs)
- Source: `src/main/java/` (standard Maven structure)
- Scripts: `run.bat`/`run.ps1` (platform-specific execution)
- Documentation: Minimal READMEs in each component directory

## Integration Points
- **Database**: MongoDB Atlas via connection string authentication
- **Frontend/Backend**: Separate components (implementation details in respective directories)
- **Environment**: Dotenv pattern for configuration management