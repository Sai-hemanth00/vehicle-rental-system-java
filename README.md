# Vehicle Rental System (Java + JDBC + MySQL)

## Overview
The **Vehicle Rental System** is a console-based Java application developed using **Core Java, JDBC, and MySQL**.  
It allows users to manage **vehicles, customers, rentals, returns, and payments** efficiently.

This project follows a **layered architecture** using **Model, DAO, Service, Utility, and Exception packages**, making the code modular, maintainable, and easy to understand.

---

## Features
- Add new vehicles
- View all vehicles
- View available vehicles
- Add customers
- Rent vehicles
- Return vehicles
- Process payments
- View rentals with customer details
- View all payments

### Advanced Validations
- Validate customer ID before renting
- Validate vehicle ID before renting
- Validate rental ID before payment
- Prevent duplicate payment for the same rental
- Prevent returning an already returned vehicle
- Prevent renting unavailable vehicles
- Input validation for menu choice, numbers, and date format
- Late fee calculation for overdue returns

---

## Technologies Used
- **Java**
- **JDBC**
- **MySQL**
- **SQL**
- **Eclipse IDE**
- **OOP Concepts**
- **DAO Design Pattern**
- **Exception Handling**
- **Transaction Management**

---

## Project Structure
```text
VehicleRentalSystem/
├── src/
│   └── com/example/vehiclerental/
│       ├── dao/
│       │   ├── VehicleDAO.java
│       │   ├── CustomerDAO.java
│       │   ├── RentalDAO.java
│       │   └── PaymentDAO.java
│       ├── model/
│       │   ├── Vehicle.java
│       │   ├── Customer.java
│       │   ├── Rental.java
│       │   └── Payment.java
│       ├── service/
│       │   ├── VehicleService.java
│       │   ├── CustomerService.java
│       │   ├── RentalService.java
│       │   └── PaymentService.java
│       ├── util/
│       │   └── DatabaseConnection.java
│       ├── exception/
│       │   └── DatabaseException.java
│       └── main/
│           └── VehicleRentalApp.java
├── .classpath
├── .project
└── README.md



Database Schema
1. Vehicles Table
CREATE TABLE vehicles (
    vehicle_id INT PRIMARY KEY AUTO_INCREMENT,
    model VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    registration_number VARCHAR(20) UNIQUE NOT NULL,
    daily_rental_price DECIMAL(8,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'Available'
);
2. Customers Table
CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    license_number VARCHAR(50) UNIQUE NOT NULL
);
3. Rentals Table
CREATE TABLE rentals (
    rental_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    rental_date DATE NOT NULL,
    return_date DATE,
    status VARCHAR(20) DEFAULT 'Rented',
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)
);
4. Payments Table
CREATE TABLE payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    rental_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(50),
    FOREIGN KEY (rental_id) REFERENCES rentals(rental_id)
);
Relationships Implemented
One-to-Many
One customer can have multiple rentals
One vehicle can have multiple rentals over time
Many-to-Many
Customers and vehicles are connected through the rentals table
Payments
Each payment is linked to a rental for billing and tracking
How to Run the Project
1. Clone the Repository
git clone https://github.com/Sai-hemanth00/vehicle-rental-system-java.git
2. Open in Eclipse
Open Eclipse IDE
Import the project as a Java Project
3. Add MySQL Connector JAR
Right click project → Properties
Go to Java Build Path
Select Classpath
Click Add External JARs
Add mysql-connector-j-x.x.x.jar
4. Create Database in MySQL

Run the SQL script in MySQL Workbench:

CREATE DATABASE vehicle_rental_db;
USE vehicle_rental_db;

CREATE TABLE vehicles (
    vehicle_id INT PRIMARY KEY AUTO_INCREMENT,
    model VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    registration_number VARCHAR(20) UNIQUE NOT NULL,
    daily_rental_price DECIMAL(8,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'Available'
);

CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    license_number VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE rentals (
    rental_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    rental_date DATE NOT NULL,
    return_date DATE,
    status VARCHAR(20) DEFAULT 'Rented',
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)
);

CREATE TABLE payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    rental_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(50),
    FOREIGN KEY (rental_id) REFERENCES rentals(rental_id)
);
5. Update Database Credentials

In DatabaseConnection.java, update:

private static final String URL = "jdbc:mysql://localhost:3306/vehicle_rental_db";
private static final String USER = "root";
private static final String PASSWORD = "your_mysql_password";
6. Run the Application
Open VehicleRentalApp.java
Right click → Run As → Java Application
Sample Console Menu
===== Vehicle Rental System =====
1. Add Vehicle
2. View All Vehicles
3. View Available Vehicles
4. Add Customer
5. Rent Vehicle
6. Return Vehicle
7. Process Payment
8. View Rentals with Customer Details
9. View All Payments
0. Exit
Key Functionalities Implemented
Vehicle Management
Add and view vehicles
Track availability status
Customer Management
Register and manage customer data
Rental Management
Rent available vehicles
Return rented vehicles
Prevent invalid rentals
Prevent duplicate returns
Payment Management
Process rental payments
Validate rental before payment
Prevent duplicate payments
Late Fee Handling
Calculate late fee for overdue returns
Example rule: Rs. 500 per late day
Error Handling and Data Integrity
Used custom DatabaseException
Added input validation for:
Menu choice
Numeric inputs
Date format (yyyy-mm-dd)
Used JDBC transactions (commit / rollback) for:
Renting a vehicle
Returning a vehicle
Prevented invalid database operations using validations before insert/update
Future Enhancements
Update/Delete Vehicle
Update/Delete Customer
Revenue reporting
Most rented vehicle report
Top customer by rentals
GUI version using JavaFX or Swing
Web version using Spring Boot
Resume Highlights

This project demonstrates:

Core Java programming
JDBC integration with MySQL
SQL joins and foreign key relationships
DAO design pattern
Exception handling
Transaction management
Input validation and business rule enforcement
Author

Sai Hemanth


---

# ✅ How to use it
1. Open **README.md** in GitHub
2. Click **Edit (✏️)**
3. Remove old content
4. Paste the above full content
5. Commit message:
```text
Update README with project documentation
Click Commit changes
