FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Copy default avatar so it's always available
RUN mkdir -p /app/uploads
COPY uploads/default-avatar.jpg /app/uploads/default-avatar.jpg

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]