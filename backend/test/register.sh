curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test1@example.com",
    "password": "password123",
    "firstName": "test1",
    "lastName": "tester1",
    "dateOfBirth": "1991-01-01",
    "nickname": "testuser1"
  }' > response_register.txt
echo "Register Response:"
cat response_register.txt
echo "check response_register.txt for JWT token and user details."
