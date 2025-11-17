TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0NEBleGFtcGxlLmNvbSIsImlkIjo1LCJpc0FkbWluIjpmYWxzZSwiaWF0IjoxNzYzMzg5MTQ5LCJleHAiOjE3NjM0NzU1NDl9.aAfdXpebM9SKxkSGxIhURSTs4KIRvYoendKmYqT5YOk"

curl -v -X POST http://localhost:8080/api/posts \
  -H "Authorization: Bearer $TOKEN" \
  -F "title=my post" \
  -F "content=test body" \
  -F "mediaType=image" \
  -F "media=@./image_test/Test-Logo.svg.png"
