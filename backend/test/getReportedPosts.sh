#!/bin/sh

# Configuration
BASE_URL="http://localhost:8080"  # Update with your actual base URL
JWT_TOKEN="your_jwt_token_here"   # Replace with actual JWT token
PAGE=0
USER_ID=123

# Test 1: Valid request (page >= 0)
echo "=== Testing valid request (page=0, userId=123) ==="
curl -X GET "${BASE_URL}/reports?page=${PAGE}&userId=${USER_ID}" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -H "Content-Type: application/json" \
  -v

echo -e "\n\n"

# Test 2: Invalid page (page < 0)
echo "=== Testing invalid page (page=-1) ==="
curl -X GET "${BASE_URL}/reports?page=-1&userId=${USER_ID}" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -H "Content-Type: application/json" \
  -v

echo -e "\n\n"

# Test 3: Page 1 with different user
echo "=== Testing page=1, different userId ==="
curl -X GET "${BASE_URL}/reports?page=1&userId=456" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -H "Content-Type: application/json" \
  -v
