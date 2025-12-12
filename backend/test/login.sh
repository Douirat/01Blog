#!/bin/bash

# API URL
LOGIN_URL="http://localhost:8080/api/users/login"

# JSON payload
read -r -d '' PAYLOAD << EOM
{
  "emailOrUsername": "test@gmail.com",
  "password": "TEST123*"
}
EOM

# File to save the response
RESPONSE_FILE="response_login.txt"

# Make the POST request
curl -s -X POST "$LOGIN_URL" \
     -H "Content-Type: application/json" \
     -d "$PAYLOAD" \
     | tee "$RESPONSE_FILE"

# Optional: Extract the token using jq
if command -v jq &> /dev/null
then
    TOKEN=$(jq -r '.token' < "$RESPONSE_FILE")
    echo "Extracted token: $TOKEN"
else
    echo "jq not installed, cannot extract token automatically"
fi
