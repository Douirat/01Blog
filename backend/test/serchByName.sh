#!/bin/bash

TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huMUBleGFtcGxlLmNvbSIsImlkIjoyLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzc1MDQ3NzQ5LCJleHAiOjE3NzUxMzQxNDl9.YX1KS0iLCggObNK9342owkLVDvYB9e8e51YaMcz0PYc"

curl -H "Authorization: Bearer $TOKEN" \
"http://localhost:8080/api/profiles/search?page=0&value=jo"