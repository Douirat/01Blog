#!/bin/bash
TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWQiOjIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NzQwMzQ2NzMsImV4cCI6MTc3NDEyMTA3M30.LTO2onB5C2Gn1MKZr69ctdP9OMeM3jOCyhxQ9BDizcc"

response=$(curl -s -w "\n%{http_code}" \
  -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/reports)

body=$(echo "$response" | sed '$d')
status=$(echo "$response" | tail -n 1)

echo "Status Code: $status"
echo "Body: $body"

if [ "$status" = "200" ]; then
  echo "✅ OK"
else
  echo "❌ FAILED"
fi