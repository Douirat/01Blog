#!/bin/bash

TOKEN="john1@example.com","firstName":"John1","lastName":"Doe1","avatar":"http://localhost:8080/uploads/avatars/d5f42823-de33-4187-acc1-c0dfa39ee070.png","nickname":"johnny1","dateOfBirth":"1990-01-01","isAdmin":false,"admin":false},"token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huMUBleGFtcGxlLmNvbSIsImlkIjo0LCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzczOTMyNzM1LCJleHAiOjE3NzQwMTkxMzV9.9QHVg8KTb6tZ07qW2VodbDN-GASYcYVdps_7glc36mc"

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