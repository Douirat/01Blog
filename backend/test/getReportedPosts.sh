#!/bin/bash

BASE_URL="http://localhost:8080"
TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiZW5AZ21haWwuY29tIiwiaWQiOjEsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzc0NDYzNjc4LCJleHAiOjE3NzQ1NTAwNzh9.sWIFhxSisdZYognMsES_IzeHxsQAgx7fW3MG6PAmVaI"
USER_ID=1
PAGE=0

echo "=== Testing valid request (page=0, userId=3) ==="
curl -X GET "${BASE_URL}/api/posts/reports?page=${PAGE}&userId=${USER_ID}" \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -v

echo -e "\n\n"
