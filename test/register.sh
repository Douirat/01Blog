curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test2@example.com",
    "password": "password123",
    "firstName": "test2",
    "lastName": "tester2",
    "dateOfBirth": "1991-01-01",
    "nickname": "testuser2"
  }'

# {"user":{"id":4,"email":"test2@example.com","firstName":"test2","lastName":"tester2","avatar":null,"nickname":"testuser2","dateOfBirth":"1991-01-01","admin":false},"token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MkBleGFtcGxlLmNvbSIsImlkIjo0LCJpc0FkbWluIjpmYWxzZSwiaWF0IjoxNzYxOTE4MjY0LCJleHAiOjE3NjIwMDQ2NjR9.3vJ2JGHE5bXvnliE5BddYtTxZBt-R56SF_vSNmmnhWw"}% 