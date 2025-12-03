TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0NEBleGFtcGxlLmNvbSIsImlkIjo1LCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzY0Nzk5Njk0LCJleHAiOjE3NjQ4ODYwOTR9.goPudkLI7t-QJUbqcz-ZIcKVg4Jo4aUacA1pc0uwxNU"

curl -v -X GET http://localhost:8080/api/comments?postId=1 \
  -H "Authorization: Bearer $TOKEN" \