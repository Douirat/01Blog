#!/bin/sh

# Store your JWT token here (replace with the real one)
TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0NEBleGFtcGxlLmNvbSIsImlkIjo1LCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzY0NTk4NDI2LCJleHAiOjE3NjQ2ODQ4MjZ9.BGcT2thtJHb8rYzHPFGEU5SqhWUpYD77bPf533yQwHM"
# Make the GET request to check status
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -o response_status.txt

# Print the response
echo "Response saved to response_status.txt"
cat response_status.txt
