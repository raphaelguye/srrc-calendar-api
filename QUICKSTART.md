# Quick Start Guide - SRRC Calendar API

## Prerequisites

**Install Java 17+**

macOS (using Homebrew):
```bash
brew install openjdk@17
```

Or download from: https://adoptium.net/

Verify installation:
```bash
java -version
# Should show version 17 or higher
```

## Setup & Run

### Option 1: Automated Setup (Recommended)

```bash
# Run the setup script
./setup.sh

# Start the application
./gradlew bootRun
```

### Option 2: Manual Setup

```bash
# Make Gradle wrapper executable
chmod +x gradlew

# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

### Option 3: Docker

```bash
# Build Docker image
docker build -t srrc-calendar-api .

# Run container
docker run -p 8080:8080 srrc-calendar-api
```

## Test the API

Once running, test with:

```bash
# Health check
curl http://localhost:8080/api/v1/health

# Get all events
curl http://localhost:8080/api/v1/events

# Get upcoming events only
curl http://localhost:8080/api/v1/events/upcoming
```

## Expected Response

```json
{
  "success": true,
  "data": [...],
  "message": "Successfully retrieved X events",
  "timestamp": "2024-11-07T19:30:00Z"
}
```

## Troubleshooting

### Port already in use
If port 8080 is busy, set a different port:
```bash
PORT=8081 ./gradlew bootRun
```

### Java not found
Make sure Java 17+ is installed and in your PATH:
```bash
java -version
```

### Build fails
Clean and rebuild:
```bash
./gradlew clean build
```

## Next Steps

- See [README.md](README.md) for full API documentation
- Deploy to Railway.app for production hosting
- Configure CORS settings for your domain

## Support

For issues, check the logs:
```bash
# Application logs are printed to console when running
./gradlew bootRun
```

The API will log:
- Startup information
- Event fetch operations
- Any errors encountered

---

**Ready to go! 🚀**
