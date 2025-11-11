# SRRC Calendar API

A Kotlin Spring Boot REST API serving Swiss Rock'n'Roll Confederation events data.

**Live API:** https://srrc-calendar-api-production-4112.up.railway.app

## Features

- 📅 Fetches events from GitHub Releases API
- 💾 1-hour in-memory cache
- ⏰ Auto-refresh every hour
- 🌐 CORS enabled
- 🐳 Docker ready

## Quick Start

### Test the Live API

```bash
# Health check
curl https://srrc-calendar-api-production-4112.up.railway.app/api/v1/health

# Get all events
curl https://srrc-calendar-api-production-4112.up.railway.app/api/v1/events

# Get upcoming events only
curl https://srrc-calendar-api-production-4112.up.railway.app/api/v1/events/upcoming
```

### Run Locally

```bash
# Prerequisites: Java 17+
./gradlew bootRun

# Test locally
curl http://localhost:8080/api/v1/events
```

### Run with Docker

```bash
docker-compose up
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/events` | All events |
| `GET` | `/api/v1/events/upcoming` | Future events only |
| `GET` | `/api/v1/health` | Health check with cache info |
| `POST` | `/api/v1/events/refresh` | Trigger manual refresh |

## Response Format

**Success:**
```json
{
  "success": true,
  "data": [
    {
      "id": "event-123",
      "title": "Event Name",
      "dateDisplay": "07 nov. (jeu.)",
      "startDate": "2024-11-07T10:00:00",
      "endDate": "2024-11-07T18:00:00",
      "location": "Zurich, Switzerland",
      "organizer": "SRRC",
      "description": "Event details...",
      "url": "https://...",
      "isUpcoming": true
    }
  ],
  "message": "Successfully retrieved 42 events",
  "timestamp": "2024-11-07T19:30:00Z"
}
```

**Error:**
```json
{
  "success": false,
  "error": "Error message",
  "timestamp": "2024-11-07T19:30:00Z"
}
```

## Development

**Tech Stack:**
- Kotlin 1.9.20
- Spring Boot 3.2.0
- Java 17
- Gradle 8.5

**Build:**
```bash
./gradlew build
```

## Data Source

Events fetched from: [`raphaelguye/srrc-calendar-scraper`](https://github.com/raphaelguye/srrc-calendar-scraper/releases/latest)

## License

MIT
