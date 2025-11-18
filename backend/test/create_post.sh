#!/bin/bash

TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0NEBleGFtcGxlLmNvbSIsImlkIjo1LCJpc0FkbWluIjpmYWxzZSwiaWF0IjoxNzYzNDk5MzM5LCJleHAiOjE3NjM1ODU3Mzl9.xEVrVu54didB-D38bMgrPeMCDRapTWlsGBOqA95hpAY"

curl -v -X POST http://localhost:8080/api/posts \
  -H "Authorization: Bearer $TOKEN" \
  -F "title=my post" \
  -F "content=test body" \
  -F "mediaType=image" \
  -F "media=@image_test/Test-Logo.svg.png"
