# Online Bus Reservation System

A full-stack Java web application for managing bus reservations.

## Technologies Used
- Frontend: HTML, CSS, JavaScript
- Backend: Java EE, Servlets, JSP
- Database: MySQL
- JDBC for database connectivity

## Project Structure
```
bus-reservation/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   └── busreservation/
│   │   │   │       ├── controller/
│   │   │   │       ├── dao/
│   │   │   │       ├── model/
│   │   │   │       └── util/
│   │   ├── webapp/
│   │   │   ├── WEB-INF/
│   │   │   ├── css/
│   │   │   ├── js/
│   │   │   └── pages/
│   └── resources/
└── pom.xml
```

## Features
- User Registration and Login
- Bus Search and Booking
- Seat Selection
- Booking History
- Admin Panel for Bus Management
- Payment Integration

## Setup Instructions
1. Install Java JDK 8 or higher
2. Install MySQL Server
3. Configure database connection in `src/main/resources/db.properties`
4. Build the project using Maven
5. Deploy on Tomcat Server

## Database Schema
The database includes tables for:
- Users
- Buses
- Routes
- Bookings
- Seats
- Payments 