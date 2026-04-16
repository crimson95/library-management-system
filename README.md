# Library Management System (LMS)

A robust, full-stack Library Management System built with **Java Servlets, JSP, and MySQL**. Designed with a strong emphasis on data integrity and software architecture, this system features role-based access control, the MVC design pattern, and **manual ACID transaction management** to ensure safe database operations.

## Key Features

### Core Architecture & Security
* **Role-Based Access Control (RBAC):** Implemented Servlet Filters to strictly isolate routing between Admins and regular Members.
* **ACID Transaction Management:** Engineered manual database transaction boundaries (Commit/Rollback) using JDBC `Connection` injection. This guarantees atomicity during complex operations, such as cascading deletes (Books and Copies) and synchronized inventory creations, preventing "ghost data".
* **PRG Pattern (Post-Redirect-Get):** Implemented across all forms to prevent duplicate submissions and provide clean, state-independent URL routing with success notification feedback.

### Admin Dashboard
* **Transaction-Protected CRUD:** Full management of Books, Physical Copies, Authors, Publishers, and Members.
* **Circulation Desk:** Streamlined process for checking out and returning physical book copies based on unique User IDs and Copy IDs.
* **Late Fee Calculation:** Automated status tracking and late fee assignment for overdue returns.

### User Portal
* **User Authentication:** Secure registration and login flows with unified error handling.
* **Personal Dashboard:** Users can track their active borrowed books, past reading history, and outstanding late fees.
* **Profile Management:** Update personal information seamlessly.

---
## Tech Stack

* **Backend:** Java 17, Jakarta EE (Servlets & JSP)
* **Database:** MySQL 8.x, JDBC API 
* **Frontend:** HTML5, CSS3 (Custom responsive styling), JavaScript
* **Architecture Patterns:** MVC (Model-View-Controller), DAO (Data Access Object), DTO (Data Transfer Object)
* **Server:** Apache Tomcat 10.x

---
## System Design & Architecture

### Entity-Relationship (ER) Diagram
![[lms.png]]
### Highlight: Manual Transaction Control
To ensure 100% data consistency, critical business logic in the `Service` layer dictates the lifecycle of database connections. For instance, when adding a new book:
1. Auto-commit is disabled (`conn.setAutoCommit(false)`).
2. The catalog record is created (`BookDAO`).
3. The initial physical copy inventory is created (`BookInfoDAO`).
4. If both succeed, `conn.commit()` is executed. If any `SQLException` occurs, `conn.rollback()` guarantees the database remains untainted.

---
## How to Run Locally

1. **Clone the repository:**
```
git clone https://github.com/crimson95/Library-Management-System.git
```

2. Database Setup:

* Ensure MySQL is running on port `3306`.
* Create a database named `lms`.
* Run the provided SQL script located at `src/main/java/lms.sql` to initialize tables and insert mock data.

3. Configure Credentials:

* Navigate to `src/main/resources/database.properties`.
* Update the `db.url`, `db.user`, and `db.password` to match your local MySQL credentials.

4. Deploy:

* Build the project using your IDE (IntelliJ IDEA / Eclipse) or Maven/Gradle.
* Deploy the generated artifact to an Apache Tomcat server.
* Access the application at `http://localhost:8080/lms`.

---

## Future Enhancements

* Implement Book Search and Pagination for large catalogs.
* Integrate password hashing (e.g., BCrypt) for enhanced security.
* Add email notifications for overdue books.