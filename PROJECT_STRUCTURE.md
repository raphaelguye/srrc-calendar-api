# SRRC Calendar API - Project Structure

## Complete File Tree

```
srrc-calendar-api/
├── .dockerignore                   # Docker ignore rules
├── .gitignore                      # Git ignore rules
├── Dockerfile                      # Multi-stage Docker build
├── QUICKSTART.md                   # Quick start guide
├── README.md                       # Complete documentation
├── build.gradle.kts                # Gradle build configuration (Kotlin DSL)
├── docker-compose.yml              # Docker Compose configuration
├── gradlew                         # Gradle wrapper script (Unix)
├── gradlew.bat                     # Gradle wrapper script (Windows)
├── settings.gradle.kts             # Gradle settings
├── setup.sh                        # Automated setup script
│
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
│
└── src/
    ├── main/
    │   ├── kotlin/
    │   │   └── ch/srrc/events/
    │   │       ├── SrrcCalendarApiApplication.kt    # Main Spring Boot app
    │   │       │
    │   │       ├── config/
    │   │       │   ├── CorsConfig.kt                # CORS configuration
    │   │       │   └── WebClientConfig.kt           # HTTP client config
    │   │       │
    │   │       ├── controller/
    │   │       │   └── EventController.kt           # REST API endpoints
    │   │       │
    │   │       ├── exception/
    │   │       │   └── GlobalExceptionHandler.kt    # Global error handling
    │   │       │
    │   │       ├── model/
    │   │       │   ├── ApiResponse.kt               # API response wrapper
    │   │       │   ├── Event.kt                     # Event data models
    │   │       │   └── GitHubRelease.kt             # GitHub API models
    │   │       │
    │   │       └── service/
    │   │           ├── EventService.kt              # Business logic & caching
    │   │           └── GitHubService.kt             # GitHub API integration
    │   │
    │   └── resources/
    │       ├── application.yml                      # Development configuration
    │       ├── application-prod.yml                 # Production configuration
    │       └── banner.txt                           # Custom startup banner
    │
    └── test/
        └── kotlin/
            └── ch/srrc/events/
                └── controller/
                    └── EventControllerTest.kt       # Controller unit tests
```

## Key Components

### 1. Configuration Files
- **build.gradle.kts**: Kotlin DSL build script with all dependencies
- **application.yml**: Runtime configuration (ports, GitHub settings, logging)
- **Dockerfile**: Production-ready multi-stage build
- **docker-compose.yml**: Easy local deployment

### 2. Application Entry Point
- **SrrcCalendarApiApplication.kt**: Main class with `@SpringBootApplication` and `@EnableScheduling`

### 3. Data Models
- **Event.kt**: Clean event model + transformation from raw data
- **RawEvent.kt**: GitHub JSON structure mapping
- **ApiResponse.kt**: Generic API response wrapper
- **GitHubRelease.kt**: GitHub API response models

### 4. Services
- **GitHubService.kt**: Fetches events from GitHub Releases API
- **EventService.kt**: Business logic, caching, and scheduled refresh

### 5. Controllers
- **EventController.kt**: REST endpoints for events, health, and manual refresh

### 6. Configuration
- **CorsConfig.kt**: CORS settings for web app integration
- **WebClientConfig.kt**: HTTP client configuration
- **GlobalExceptionHandler.kt**: Centralized error handling

### 7. Tests
- **EventControllerTest.kt**: Unit tests using MockMvc

## Technology Stack

### Core
- **Language**: Kotlin 1.9.20
- **Framework**: Spring Boot 3.2.0
- **Java**: 17
- **Build Tool**: Gradle 8.5 (Kotlin DSL)

### Dependencies
- Spring Boot Starter Web
- Spring Boot Starter Actuator
- Spring Boot Starter WebFlux (for WebClient)
- Jackson Kotlin Module
- Kotlin Coroutines
- JUnit 5 + Mockito (testing)

## Architecture Pattern

**Layered Architecture**:
1. **Controller Layer**: HTTP request handling and response formatting
2. **Service Layer**: Business logic and data transformation
3. **External Integration**: GitHub API client
4. **Configuration Layer**: Cross-cutting concerns (CORS, error handling)

## Data Flow

```
GitHub Release API
        ↓
GitHubService (fetch & download)
        ↓
EventService (transform & cache)
        ↓
EventController (serve via REST)
        ↓
Client Application
```

## Deployment Options

1. **Local Development**: `./gradlew bootRun`
2. **JAR**: `java -jar build/libs/srrc-calendar-api-1.0.0.jar`
3. **Docker**: `docker build -t srrc-calendar-api .`
4. **Docker Compose**: `docker-compose up`
5. **Railway.app**: Push to GitHub → Deploy automatically

## Environment Variables

| Variable | Purpose | Default |
|----------|---------|---------|
| PORT | Server port | 8080 |
| SPRING_PROFILES_ACTIVE | Active profile | default |

## API Endpoints Summary

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/events` | GET | All events |
| `/api/v1/events/upcoming` | GET | Future events only |
| `/api/v1/health` | GET | Health check |
| `/api/v1/events/refresh` | POST | Manual refresh |
| `/actuator/health` | GET | Spring Boot health |

## Features Implemented

✅ GitHub Releases API integration  
✅ In-memory caching (1 hour)  
✅ Scheduled auto-refresh (hourly)  
✅ CORS support (all origins)  
✅ Global error handling  
✅ Health check endpoints  
✅ Docker support  
✅ Railway.app ready  
✅ Comprehensive logging  
✅ Graceful shutdown  
✅ Unit tests  

## Production Checklist

Before deploying to production:

- [ ] Review CORS settings in `CorsConfig.kt`
- [ ] Set `SPRING_PROFILES_ACTIVE=prod`
- [ ] Configure monitoring/alerting
- [ ] Set up log aggregation
- [ ] Test health check endpoints
- [ ] Verify GitHub API rate limits
- [ ] Test graceful shutdown
- [ ] Load test the API
- [ ] Set up CI/CD pipeline

---

**Project Status**: ✅ Production Ready
