#!/bin/bash

# SRRC Calendar API - Quick Start Script

set -e

echo "🚀 SRRC Calendar API - Setup & Build"
echo "===================================="
echo ""

# Check for Java
if ! command -v java &> /dev/null; then
    echo "❌ Java not found!"
    echo ""
    echo "Please install Java 17 or higher:"
    echo "  - macOS: brew install openjdk@17"
    echo "  - Or download from: https://adoptium.net/"
    echo ""
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java version $JAVA_VERSION found, but Java 17+ is required"
    exit 1
fi

echo "✅ Java $JAVA_VERSION detected"
echo ""

# Make gradlew executable
chmod +x gradlew

# Build the project
echo "📦 Building project..."
./gradlew build -x test

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Build successful!"
    echo ""
    echo "Next steps:"
    echo "  1. Run the application:"
    echo "     ./gradlew bootRun"
    echo ""
    echo "  2. Test the API:"
    echo "     curl http://localhost:8080/api/v1/health"
    echo "     curl http://localhost:8080/api/v1/events"
    echo ""
    echo "  3. Or run with Docker:"
    echo "     docker build -t srrc-calendar-api ."
    echo "     docker run -p 8080:8080 srrc-calendar-api"
    echo ""
else
    echo ""
    echo "❌ Build failed. Please check the error messages above."
    exit 1
fi
