TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0NEBleGFtcGxlLmNvbSIsImlkIjo1LCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzY0NTk4NDI2LCJleHAiOjE3NjQ2ODQ4MjZ9.BGcT2thtJHb8rYzHPFGEU5SqhWUpYD77bPf533yQwHM"
curl -v -X POST http://localhost:8080/api/comments \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "my comment to post 1",
    "content": "test comment to post 1",
    "postId": 13
  }'
