
POS Control

A Point of Sale (POS) and Order Management System designed to help small businesses streamline sales, order tracking, and stock management. This project demonstrates full-stack development skills by combining a responsive Angular frontend with a robust Java Spring Boot backend and a relational SQL database.

Features

Order Management – Create, edit, and process customer orders

Inventory Control – Manage products and track stock levels

POS System – Simulate real-world sales transactions

Authentication – Secure login and role-based access

Reporting – Basic sales and inventory reporting

Tech Stack
Frontend: Angular
Backend: Java Spring Boot
Database: MySQL / Postgres
Other Tools: Git/GitHub, Docker (optional for deployment)

Project Structure
poscontrol/
│── backend/ Spring Boot API (Orders, Products, Auth)
│── frontend/ Angular app (UI, Components, Services)
│── docs/ Documentation and mockups
│── README.txt Project documentation

Setup & Installation

Clone the repository
git clone https://github.com/s1d1sek/Poscontrol.git

cd poscontrol

Run the backend (Spring Boot)
cd backend
mvn spring-boot:run

Run the frontend (Angular)
cd frontend/order-system-frontend
npm install
ng serve --open

Access the app
Open http://localhost:4200
 in your browser

Roadmap

Advanced reporting and analytics

Multi-user roles and permissions

Cloud deployment (Docker + AWS)

UI/UX improvements
