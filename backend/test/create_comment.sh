curl -v -X POST http://localhost:8080/api/comments \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "my comment",
    "content": "test body",
    "postId": 1
  }'
