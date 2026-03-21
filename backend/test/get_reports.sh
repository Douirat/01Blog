#!/bin/bash
TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiZW5AZ21haWwuY29tIiwiaWQiOjEsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzc0MTA5NjM5LCJleHAiOjE3NzQxOTYwMzl9.8f6Rs13LiZR8kaEgfVbDGjtP4IPwKktKDcFYn-cwgmY"

BASE_URL="http://localhost:8080/api/reports"

echo "=== Testing GET reports ==="
echo "Authorization: Bearer $TOKEN"

echo "Test 1: Get first page (page=0)"

# curl -X GET "${BASE_URL}?page=0" \
curl -X GET "${BASE_URL}/user?userId=3&page=0" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -s | jq '.' \
  && echo ""  # newline after JSON
