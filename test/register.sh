curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "test",
    "lastName": "tester",
    "dateOfBirth": "1991-01-01",
    "nickname": "testuser"
  }'
