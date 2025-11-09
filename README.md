# SRRC Calendar API

A production-ready Kotlin Spring Boot REST API for serving SRRC (Swiss Rock'n'Roll Confederation) events data.

## Features

- 🚀 **Spring Boot 3.x** with Kotlin
- 📅 **Event Data**: Fetches from GitHub Releases API
- 💾 **In-Memory Caching**: 1-hour cache to reduce API calls
- ⏰ **Auto-Refresh**: Scheduled hourly updates
- 🌐 **CORS Support**: Ready for web app integration
- 🐳 **Docker Ready**: Containerized deployment
- 🔍 **Health Checks**: Built-in monitoring endpoints
- 🛡️ **Error Handling**: Graceful failure recovery

## API Endpoints

### Events

#### Get All Events
```http
GET /api/v1/events
```

Returns all events with metadata.

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": "20241107-event-title",
      "title": "Event Title",
      "dateDisplay": "07 nov. (jeu.)",
      "startDate": "2024-11-07T10:00:00",
      "endDate": "2024-11-07T18:00:00",
      "location": "City, Country",
      "organizer": "Organization Name",
      "description": "Event description...",
      "url": "https://...",
      "isUpcoming": true
    }
  ],
  "message": "Successfully retrieved 42 events",
  "timestamp": "2024-11-07T19:30:00Z"
}
```

#### Get Upcoming Events
```http
GET /api/v1/events/upcoming
```

Returns only future events (based on `startDate`).

**Response:** Same format as above, filtered for upcoming events only.

### Health & Monitoring

#### Health Check
```http
GET /api/v1/health
```

Returns API health status and cache information.

**Response:**
```json
{
  "success": true,
  "data": {
    "status": "UP",
    "service": "SRRC Calendar API",
    "cache": {
      "totalEvents": 42,
      "upcomingEvents": 15,
      "lastRefresh": "2024-11-07T19:00:00Z",
      "cacheDurationHours": 1
    }
  },
  "message": "Service is healthy",
  "timestamp": "2024-11-07T19:30:00Z"
}
```

#### Actuator Health
```http
GET /actuator/health
```

Spring Boot Actuator health endpoint for deployment platforms.

### Admin

#### Manual Refresh
```http
POST /api/v1/events/refresh
```

Triggers immediate cache refresh from GitHub API.

**Response:**
```json
{
  "success": true,
  "data": "Refresh initiated",
  "message": "Events refresh completed successfully",
  "timestamp": "2024-11-07T19:30:00Z"
}
```

## Error Response Format

All errors follow a consistent format:

```json
{
  "success": false,
  "error": "Error message describing what went wrong",
  "timestamp": "2024-11-07T19:30:00Z"
}
```

## Getting Started

### Prerequisites

- **Java 17** or higher
- **Gradle 8.5** (or use included wrapper)

### Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/srrc-calendar-api.git
   cd srrc-calendar-api
   ```

2. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

3. **Test the API**
   ```bash
   curl http://localhost:8080/api/v1/health
   curl http://localhost:8080/api/v1/events
   ```

### Building

#### Build JAR
```bash
./gradlew build
```

The JAR will be created in `build/libs/srrc-calendar-api-1.0.0.jar`

#### Run JAR
```bash
java -jar build/libs/srrc-calendar-api-1.0.0.jar
```

## Docker Deployment

### Build Docker Image
```bash
docker build -t srrc-calendar-api .
```

### Run Container
```bash
docker run -p 8080:8080 srrc-calendar-api
```

### Using Docker Compose
```bash
docker-compose up
```

## Railway Deployment

This API is ready for deployment on [Railway.app](https://railway.app).

### Automatic Deployment

1. Connect your GitHub repository to Railway
2. Railway will auto-detect the Dockerfile
3. Set environment variables if needed (optional):
   - `PORT` - Railway sets this automatically
4. Deploy!

### Health Check Configuration

Railway will use the `/actuator/health` endpoint for health checks.

**Health Check Path:** `/actuator/health`

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `PORT` | Server port | `8080` |
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `default` |

### Application Configuration

Edit `src/main/resources/application.yml`:

```yaml
github:
  repository: raphaelguye/srrc-calendar-scraper
  asset-name: srrc_events.json
```

## Architecture

```
src/main/kotlin/ch/srrc/events/
├── SrrcCalendarApiApplication.kt     # Main application
├── controller/
│   └── EventController.kt            # REST endpoints
├── service/
│   ├── EventService.kt               # Business logic & caching
│   └── GitHubService.kt              # GitHub API integration
├── model/
│   ├── Event.kt                      # Event data classes
│   ├── ApiResponse.kt                # Response wrapper
│   └── GitHubRelease.kt              # GitHub API models
├── config/
│   ├── CorsConfig.kt                 # CORS configuration
│   └── WebClientConfig.kt            # HTTP client config
└── exception/
    └── GlobalExceptionHandler.kt     # Error handling
```

## Data Flow

1. **Startup**: API fetches latest events from GitHub on initialization
2. **Caching**: Events stored in memory for 1 hour
3. **Auto-Refresh**: Scheduled task refreshes every hour
4. **API Calls**: Serve from cache (fast response)
5. **Failure Handling**: Keep old cache if refresh fails

## GitHub Data Source

Events are fetched from:
- **Repository**: `raphaelguye/srrc-calendar-scraper`
- **Source**: Latest GitHub Release
- **Asset**: `srrc_events.json`

The scraper publishes new releases with updated event data.

## Development

### Project Structure
- **Language**: Kotlin 1.9.20
- **Framework**: Spring Boot 3.2.0
- **Build Tool**: Gradle (Kotlin DSL)
- **Java Version**: 17

### Key Dependencies
- Spring Boot Starter Web
- Spring Boot Starter Actuator
- Spring Boot Starter WebFlux (for WebClient)
- Jackson Kotlin Module
- Kotlin Coroutines

### Running Tests
```bash
./gradlew test
```

## Monitoring & Logging

### Logging Levels
- **Application**: `DEBUG` for `ch.srrc.events` package
- **Spring**: `INFO` level

### Key Logged Events
- API startup and initialization
- Event refresh operations
- GitHub API calls
- Errors and exceptions

### Actuator Endpoints
- `/actuator/health` - Health status
- `/actuator/info` - Application info

## Production Considerations

### Current POC Features
✅ In-memory caching (1 hour)  
✅ Scheduled refresh (hourly)  
✅ CORS enabled (all origins)  
✅ Error handling  
✅ Health checks  

### Future Enhancements
- [ ] Database persistence
- [ ] Redis caching
- [ ] Authentication/Authorization
- [ ] Rate limiting
- [ ] Metrics & monitoring (Prometheus)
- [ ] Event search & filtering
- [ ] Pagination
- [ ] WebSocket support for real-time updates

## API Client Examples

### JavaScript (Fetch)
```javascript
// Get all events
const response = await fetch('http://localhost:8080/api/v1/events');
const data = await response.json();
console.log(data.data); // Array of events
```

### cURL
```bash
# Get upcoming events
curl http://localhost:8080/api/v1/events/upcoming

# Manual refresh
curl -X POST http://localhost:8080/api/v1/events/refresh
```

### Python
```python
import requests

response = requests.get('http://localhost:8080/api/v1/events')
events = response.json()['data']
```

## License

MIT License - feel free to use this project as needed.

## Contact

For questions or issues, please open an issue on GitHub.

---

**Built with ❤️ for the Swiss Rock'n'Roll Community**
