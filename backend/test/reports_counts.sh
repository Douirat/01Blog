#!/bin/bash
TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huMUBleGFtcGxlLmNvbSIsImlkIjo0LCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzczOTMzOTEwLCJleHAiOjE3NzQwMjAzMTB9.u33WGKHOAJk6oxpuDNyuRPgul1-QEadlFZf4FeuZrvg"

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