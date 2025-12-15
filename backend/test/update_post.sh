#!/bin/bash

# --- Configuration ---
URL="http://localhost:8080/api/posts/update"
JWT_TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImlkIjoxLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzY1NzY4MTg3LCJleHAiOjE3NjU4NTQ1ODd9.wQmaGzJTHLd3Kahrr07cYMNWkdbfHegiCFyj7d0OavY"  # replace with your actual JWT
TITLE="Updated Post Title"
CONTENT="This is the updated content."
MEDIA_TYPE="image/png"
MEDIA_FILE_PATH="./test-image.png"  # path to your test media file

# --- Send POST request with JWT ---
response=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST "$URL" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -F "title=$TITLE" \
  -F "content=$CONTENT" \
  -F "mediaType=$MEDIA_TYPE" \
  -F "media=@$MEDIA_FILE_PATH")

# --- Print response ---
echo "Response:"
echo "$response"
