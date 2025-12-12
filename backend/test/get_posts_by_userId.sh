#!/bin/bash

# Assign your token to a variable
TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImlkIjoxLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzY1NTM1MTk0LCJleHAiOjE3NjU2MjE1OTR9.lv6yUhHxGJ1GsSQtDQ8u-rwe-x8mVdzraomNY036me0"

# Make the GET request with curl
curl -v -X GET "http://localhost:8080/api/posts/profile?page=0&userId=1" \
-H "Authorization: Bearer $TOKEN"
