TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huMUBleGFtcGxlLmNvbSIsImlkIjo0LCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzc1NzQwNzQ2LCJleHAiOjE3NzU4MjcxNDZ9.xWDdg64PqwmjGVJEN1jkJ3deyaYG3GRRwin05egUsl4"

curl -X POST \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     "http://localhost:8080/api/subscription?followedId=3"
