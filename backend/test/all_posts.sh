#!/bin/bash

# Base URL
BASE_URL="http://localhost:8080/api/posts"

echo "=== Testing GET Posts ==="
echo ""

# Test 1: Get first page (page=0)
echo "Test 1: Get first page (page=0)"

curl -X GET "${BASE_URL}?page=0" \
     -H "Content-Type: application/json" \
     -w "\nStatus Code: %{http_code}\n" \
     -s | jq '.'

echo ""
