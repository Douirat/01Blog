TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0NEBleGFtcGxlLmNvbSIsImlkIjo1LCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzY1MDE4ODQyLCJleHAiOjE3NjUxMDUyNDJ9.Dyg8l2VHi3_9mzo6Qt1AG0R6ot4WCVTcieNyBuAK7LE"

curl -X POST "http://localhost:8080/api/votes" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
        "postId": 1,
        "userId": 5,
        "value": false
      }' \
  -o vote.txt

echo "Response saved to vote.txt"
cat vote.txt
