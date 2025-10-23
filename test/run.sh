#!/bin/bash
# ===========================
# Simple script to run Spring Boot app
# ===========================

# 1️⃣ Make sure we are in the project root
cd "$(dirname "$0")"

# 2️⃣ Build the app using Maven Wrapper
./mvnw clean package -DskipTests

# 3️⃣ Run the generated JAR
java -jar target/*.jar
# Compile your Spring Boot project.

# Package it into a JAR (target/Application-*.jar).

# Run the JAR using Java.