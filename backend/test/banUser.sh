TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huMUBleGFtcGxlLmNvbSIsImlkIjoyLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzc0Njk5MDY4LCJleHAiOjE3NzQ3ODU0Njh9.PSS4cdlhJMgvUF0DDalowBv-A6cPFDr5dosxYmwGvC8"

curl -X PATCH \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     "http://localhost:8080/api/users?id=2"