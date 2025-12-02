#!/bin/bash

# URL of your endpoint
URL="http://localhost:8080/api/posts"

# Example fields
TITLE="My Test Post 1"
CONTENT="This is the content of my test post1."
MEDIA_TYPE="image/png"
MEDIA_FILE_PATH="./image_test/Test-Logo.svg.png"  # Change this to a real file path

# JWT token (if your endpoint is secured)
TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0NEBleGFtcGxlLmNvbSIsImlkIjo1LCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzY0NTk4NDI2LCJleHAiOjE3NjQ2ODQ4MjZ9.BGcT2thtJHb8rYzHPFGEU5SqhWUpYD77bPf533yQwHM"

# Send the POST request
curl -X POST "$URL" \
  -H "Authorization: Bearer $TOKEN" \
  -F "title=$TITLE" \
  -F "content=$CONTENT" \
  -F "mediaType=$MEDIA_TYPE" \
  -F "media=@$MEDIA_FILE_PATH"
