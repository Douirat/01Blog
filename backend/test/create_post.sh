#!/bin/bash

TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWQiOjYsImlzQWRtaW4iOmZhbHNlLCJpYXQiOjE3NjM5MDg2NDMsImV4cCI6MTc2Mzk5NTA0M30.NimXUm9j4yxu8rv8wkOCccImNW-86WluOTmomnYdkCM"

curl -v -X POST http://localhost:8080/api/posts \
  -H "Authorization: Bearer $TOKEN" \
  -F "title=my post" \
  -F "content=test body" \
  -F "mediaType=image" \
  -F "media=@image_test/Test-Logo.svg.png"
