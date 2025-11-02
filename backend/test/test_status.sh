#!/bin/sh

# Store your JWT token here (replace with the real one)
TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWQiOjEsImlzQWRtaW4iOmZhbHNlLCJpYXQiOjE3NjIwMDIwNzUsImV4cCI6MTc2MjA4ODQ3NX0.I7AfWN6k8Y6nWR2qV-NX32NZ1mFlapoUVMJwIk2v3Zs"
# Make the GET request to check status
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -o response_status.txt

# Print the response
echo "Response saved to response_status.txt"
cat response_status.txt
