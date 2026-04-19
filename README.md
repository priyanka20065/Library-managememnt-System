# 📚 Library Management System

A **Spring Boot** application for managing library books, members, and transactions. It supports **role-based access (Admin/User)**, **JWT authentication**, a modern **Thymeleaf UI**, and **MySQL database integration**.

---

## 🚀 Live Demo
Currently, no public demo is available. You can deploy and run the project locally or on your own server.

---

## 🏗️ Architecture

This is a **monolithic Spring Boot application** consisting of:

- **Authentication Service**
  - JWT-based authentication
  - Role management (Admin/User)

- **Book Management**
  - CRUD operations
  - Search & category filtering

- **Member Management**
  - Admin-only access

- **Transaction Management**
  - Borrow/return books
  - Admin approval/rejection

- **REST API**
  - Endpoints for all core operations

- **Web Frontend**
  - Thymeleaf-based UI

---

## ✨ Features

### 🔐 Authentication & Security
- JWT-based authentication
- Role-based authorization (Admin/User)
- Password encryption using BCrypt

### 🌐 Web & API
- Full RESTful API support
- Complete Thymeleaf web interface

### 👨‍💼 Admin Dashboard
- Manage books (Add/Edit/Delete)
- Manage members
- Approve/reject transactions
- View all users and transactions

### 👤 User Dashboard
- Search and borrow books
- Return borrowed books
- Update profile

### 💾 Persistence
- MySQL database
- JPA/Hibernate ORM

### ⚙️ Backend
- Spring Boot REST API
- Dependency Injection
- Transaction management

---

## 🧩 Application Overview

- **Port:** `8080`
- **Core Capabilities:**
  - JWT authentication
  - Role-based access control
  - REST API endpoints
  - Thymeleaf frontend
  - MySQL integration

---

## 🛠️ Getting Started

### ✅ Prerequisites
- Java 17+
- MySQL 8.0+
- Maven 3.6+

---

### ⚙️ Setup Instructions

#### 1. Clone Repository
```bash
git clone https://github.com/priyanka20065/Library-managememnt-System.git
cd Library-managememnt-System
```

#### 2. Configure Database
Create database:
```sql
CREATE DATABASE library_db;
```

Update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library_db
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD
```

---

#### 3. Build Project

**Windows**
```bash
.\apache-maven-3.9.14\bin\mvn.cmd clean install
```

**Linux/Mac**
```bash
./apache-maven-3.9.14/bin/mvn clean install
```

Or:
```bash
mvn clean install
```

---

#### 4. Run Application

**Windows**
```bash
.\apache-maven-3.9.14\bin\mvn.cmd spring-boot:run
```

**Linux/Mac**
```bash
./apache-maven-3.9.14/bin/mvn spring-boot:run
```

Or:
```bash
mvn spring-boot:run
```

---

#### 5. Access Application
- Main App: http://localhost:8080  
- Login Page: http://localhost:8080/login  

---

## 🔄 Usage

### 🔑 Authentication Flow
- Register:
  - `/signup` (User)
  - `/admin-signup` (Admin, if enabled)
- Login: `/login` → returns JWT in HTTP-only cookie
- Access: Secured endpoints require JWT
- Logout: `/logout`

---

### 🖥️ Web Interface

| Page | URL |
|------|-----|
| Dashboard | `/dashboard` |
| Books | `/books` |
| Members (Admin) | `/members` |
| Transactions | `/transactions` |
| Profile | `/profile` |

---

### 🔗 Sample API Endpoints

| Method | Endpoint | Description |
|--------|---------|------------|
| POST | `/api/auth/login` | Authenticate user |
| POST | `/api/auth/register` | Register user |
| GET | `/api/books` | Get all books |
| POST | `/api/books` | Add book (Admin) |
| GET | `/api/members` | Get members (Admin) |
| POST | `/api/transactions/borrow` | Borrow book |
| POST | `/api/transactions/return/{id}` | Return book |

---

## ⚙️ Configuration

Edit `src/main/resources/application.properties`:

```properties
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/library_db
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD

spring.jpa.hibernate.ddl-auto=update

jwt.secret=your_jwt_secret_key
jwt.expiration=86400000
```

---

## 📁 Project Structure

```
src/main/java/com/library/library_management_system/
├── config/        # App & security config
├── controller/    # REST & MVC controllers
├── entity/        # JPA entities
├── exception/     # Global exception handling
├── repository/    # JPA repositories
├── security/      # JWT & security setup
├── service/       # Business logic
└── Application.java

src/main/resources/
├── application.properties
├── static/        # CSS/JS
└── templates/     # Thymeleaf HTML
```

---

## 🔒 Security Features

- JWT Authentication
- Role-based Authorization
- BCrypt Password Encryption

---

## 📄 Web Pages

| File | Purpose | URL |
|------|--------|-----|
| login.html | Login page | `/login` |
| signup.html | User registration | `/signup` |
| dashboard.html | Dashboard | `/dashboard` |
| books.html | Book management | `/books` |
| members.html | Admin member management | `/members` |
| transactions.html | Borrow/Return books | `/transactions` |
| profile.html | User profile | `/profile` |
| error.html | Error page | `/error` |

---

## 🚧 Future Enhancements

- Swagger / OpenAPI documentation
- Docker Compose setup
- Email notifications for overdue books
- Pagination & advanced search
- Unit & integration testing

---

## 🛠️ Troubleshooting

- **Database Issues**
  - Ensure MySQL is running
  - Verify credentials in `application.properties`

- **JWT Issues**
  - Check secret key and expiration config

- **Build Errors**
  - Ensure Java 17+ and Maven 3.6+

---

## 📜 License
MIT License

---

## 📌 Summary
This project demonstrates a **modern Spring Boot application** with:
- JWT authentication  
- Role-based access control  
- REST API + Web UI  
- Full library management functionality  
