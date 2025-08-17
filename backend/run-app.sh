#!/bin/bash

echo "Starting Order System Backend..."
echo "================================"

# Navigate to backend directory
cd "$(dirname "$0")"

# Run with skip tests flag
./mvnw spring-boot:run -DskipTests -Dspring-boot.run.fork=false

