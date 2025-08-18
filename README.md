# POS Control System

A full-stack Point of Sale system with inventory management.

## Technologies
- Backend: Java Spring Boot, PostgreSQL
- Frontend: Angular 17+, Bootstrap

## Setup Instructions

### Prerequisites
- Java 21
- Node.js 18+
- PostgreSQL
- Maven

### Backend Setup
```bash
cd backend
./mvnw spring-boot:run
### 6. **Fix Commands to Run Everything**

Create a script to start both servers:

```bash
# Create start.sh in the root directory
cat > ~/posfolder/start.sh << 'EOF'
#!/bin/bash

echo "Starting POS Control System..."

# Start backend
echo "Starting backend server..."
cd backend && ./mvnw spring-boot:run &
BACKEND_PID=$!

# Wait for backend to start
sleep 10

# Start frontend
echo "Starting frontend server..."
cd frontend/order-system-frontend && ng serve &
FRONTEND_PID=$!

echo "Backend PID: $BACKEND_PID"
echo "Frontend PID: $FRONTEND_PID"
echo "System is running!"
echo "Frontend: http://localhost:4200"
echo "Backend: http://localhost:8080"

# Wait for user input to stop
read -p "Press Enter to stop all servers..."

# Kill both processes
kill $BACKEND_PID
kill $FRONTEND_PID
EOF

chmod +x ~/posfolder/start.sh
