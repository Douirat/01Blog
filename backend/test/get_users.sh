TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0NEBleGFtcGxlLmNvbSIsImlkIjo1LCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzY1MzEzNTU2LCJleHAiOjE3NjUzOTk5NTZ9.EAmaxuKCUl6jOw3CqjO5uM1ZZNqs8oodQfXHeWC_OP0"

curl -v http://localhost:8080/api/profiles?page=0 \
  -H "Authorization: Bearer $TOKEN"
