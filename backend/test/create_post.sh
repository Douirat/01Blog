#!/bin/bash

TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0NEBleGFtcGxlLmNvbSIsImlkIjo1LCJpc0FkbWluIjpmYWxzZSwiaWF0IjoxNzY0MDc3MzAwLCJleHAiOjE3NjQxNjM3MDB9.KZAkinino2h8z3Ms-y1sJiQT8_nFQU60uAn3EXTY2MM"

curl -v -X POST http://localhost:8080/api/comments \
  -H "Authorization: Bearer $TOKEN" \
  -F {
  "title=my comment" \
   "content=test body" \
  }
