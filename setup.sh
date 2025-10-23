curl https://start.spring.io/starter.zip \
  -d type=maven-project \
  -d language=java \
  -d javaVersion=17 \
  -d dependencies=web,data-jpa,h2 \
  -d name=01blog \
  -d groupId=com.blog \
  -d artifactId=backend \
  -d packageName=com.blog.backend \
  -o 01blog-backend.zip