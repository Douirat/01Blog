#!/bin/bash

# Path to the avatar image to upload
AVATAR_PATH="./image_test/Test-Logo.svg.png"

# API endpoint
URL="http://localhost:8080/api/users/register"

# User data
EMAIL="john@example.com"
PASSWORD="password123"
FIRST_NAME="John1"
LAST_NAME="Doe1"
NICKNAME="johnny1"
DATE_OF_BIRTH="1990-01-01"
IS_ADMIN="true"

# Send multipart/form-data request
curl -v -X POST "$URL" \
  -F "email=$EMAIL" \
  -F "password=$PASSWORD" \
  -F "firstName=$FIRST_NAME" \
  -F "lastName=$LAST_NAME" \
  -F "nickname=$NICKNAME" \
  -F "dateOfBirth=$DATE_OF_BIRTH" \
  -F "isAdmin=$IS_ADMIN" \
  -F "avatar=@$AVATAR_PATH" \
  -H "Accept: application/json"
