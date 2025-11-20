#!/bin/bash

TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0NEBleGFtcGxlLmNvbSIsImlkIjo1LCJpc0FkbWluIjpmYWxzZSwiaWF0IjoxNzYzNjY1MTgyLCJleHAiOjE3NjM3NTE1ODJ9.0tA7KaXX4Bkrkm_E9VgR50t1_h-QSAsP-05vhXzPDzI"

curl -v -X POST http://localhost:8080/api/posts \
  -H "Authorization: Bearer $TOKEN" \
  -F "title=my post" \
  -F "content=test body" \
  -F "mediaType=image" \
  -F "media=@image_test/Test-Logo.svg.png"
