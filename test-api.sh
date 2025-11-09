#!/bin/bash

# SRRC Calendar API - Testing Script

set -e

API_URL="${API_URL:-http://localhost:8080}"

echo "🧪 SRRC Calendar API - Testing Suite"
echo "===================================="
echo "API URL: $API_URL"
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to test endpoint
test_endpoint() {
    local name="$1"
    local endpoint="$2"
    local method="${3:-GET}"
    
    echo -n "Testing $name... "
    
    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "\n%{http_code}" "$API_URL$endpoint")
    else
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$API_URL$endpoint")
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n-1)
    
    if [ "$http_code" -eq 200 ]; then
        echo -e "${GREEN}✓ OK${NC} (HTTP $http_code)"
        
        # Check if response has success field
        if echo "$body" | grep -q '"success":true'; then
            echo "  └─ Response: success=true"
        else
            echo -e "  └─ ${YELLOW}Warning: success field not true${NC}"
        fi
    else
        echo -e "${RED}✗ FAILED${NC} (HTTP $http_code)"
        echo "  └─ Response: $body"
        return 1
    fi
}

# Wait for API to be ready
echo "Waiting for API to be ready..."
for i in {1..30}; do
    if curl -s "$API_URL/actuator/health" > /dev/null 2>&1; then
        echo -e "${GREEN}✓ API is ready!${NC}"
        echo ""
        break
    fi
    if [ $i -eq 30 ]; then
        echo -e "${RED}✗ API not responding after 30 seconds${NC}"
        echo "Make sure the API is running:"
        echo "  ./gradlew bootRun"
        exit 1
    fi
    sleep 1
done

# Run tests
echo "Running API tests..."
echo ""

test_endpoint "Health Check (Actuator)" "/actuator/health"
test_endpoint "Health Check (Custom)" "/api/v1/health"
test_endpoint "Get All Events" "/api/v1/events"
test_endpoint "Get Upcoming Events" "/api/v1/events/upcoming"
test_endpoint "Manual Refresh" "/api/v1/events/refresh" "POST"

echo ""
echo "===================================="
echo -e "${GREEN}All tests passed! ✓${NC}"
echo ""

# Sample output
echo "Sample API Response:"
echo "-------------------"
curl -s "$API_URL/api/v1/events/upcoming" | head -c 500
echo "..."
echo ""
