TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0NEBleGFtcGxlLmNvbSIsImlkIjo1LCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzY1MTAxMDIwLCJleHAiOjE3NjUxODc0MjB9.op9fCumdaSmXXWnpwZuWb6aiOZjxWdOmw5NKLBVQcSw"

curl -X POST "http://localhost:8080/api/votes" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
        "postId": 2,
        "userId": 5,
        "value": true
      }' \
  -o vote.txt

echo "Response saved to vote.txt"
cat vote.txt
