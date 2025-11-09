# Makefile for SRRC Calendar API
# Provides convenient shortcuts for common tasks

.PHONY: help build run test clean docker-build docker-run docker-stop deploy-check

# Default target
help:
	@echo "SRRC Calendar API - Available Commands"
	@echo "======================================="
	@echo ""
	@echo "Development:"
	@echo "  make build        - Build the application"
	@echo "  make run          - Run the application locally"
	@echo "  make test         - Run unit tests"
	@echo "  make test-api     - Test API endpoints (requires running app)"
	@echo "  make clean        - Clean build artifacts"
	@echo ""
	@echo "Docker:"
	@echo "  make docker-build - Build Docker image"
	@echo "  make docker-run   - Run Docker container"
	@echo "  make docker-stop  - Stop Docker container"
	@echo "  make docker-clean - Remove Docker image"
	@echo ""
	@echo "Production:"
	@echo "  make deploy-check - Verify deployment readiness"
	@echo "  make build-prod   - Build for production"
	@echo ""

# Build the application
build:
	@echo "Building SRRC Calendar API..."
	./gradlew build

# Run the application
run:
	@echo "Starting SRRC Calendar API..."
	./gradlew bootRun

# Run tests
test:
	@echo "Running unit tests..."
	./gradlew test

# Test API endpoints
test-api:
	@echo "Testing API endpoints..."
	./test-api.sh

# Clean build artifacts
clean:
	@echo "Cleaning build artifacts..."
	./gradlew clean

# Build Docker image
docker-build:
	@echo "Building Docker image..."
	docker build -t srrc-calendar-api:latest .

# Run Docker container
docker-run:
	@echo "Running Docker container..."
	docker run -d --name srrc-calendar-api -p 8080:8080 srrc-calendar-api:latest
	@echo "Container started! API available at http://localhost:8080"

# Stop Docker container
docker-stop:
	@echo "Stopping Docker container..."
	docker stop srrc-calendar-api || true
	docker rm srrc-calendar-api || true

# Clean Docker image
docker-clean: docker-stop
	@echo "Removing Docker image..."
	docker rmi srrc-calendar-api:latest || true

# Compose up
compose-up:
	@echo "Starting with Docker Compose..."
	docker-compose up -d

# Compose down
compose-down:
	@echo "Stopping Docker Compose..."
	docker-compose down

# Build for production
build-prod:
	@echo "Building for production..."
	SPRING_PROFILES_ACTIVE=prod ./gradlew build -x test

# Check deployment readiness
deploy-check:
	@echo "Checking deployment readiness..."
	@echo ""
	@echo "✓ Dockerfile exists"
	@test -f Dockerfile || (echo "✗ Dockerfile missing" && exit 1)
	@echo "✓ docker-compose.yml exists"
	@test -f docker-compose.yml || (echo "✗ docker-compose.yml missing" && exit 1)
	@echo "✓ README.md exists"
	@test -f README.md || (echo "✗ README.md missing" && exit 1)
	@echo ""
	@echo "All checks passed! Ready for deployment 🚀"

# Quick start (setup + build + run)
quickstart:
	@echo "Quick start SRRC Calendar API..."
	./setup.sh
	@echo ""
	@echo "Now run: make run"
