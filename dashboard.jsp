<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Bus Reservation System</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background: #f5f6fa;
        }

        .navbar {
            background: #667eea;
            padding: 1rem 2rem;
            color: white;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .navbar h1 {
            font-size: 1.5rem;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 1rem;
        }

        .logout-btn {
            background: transparent;
            border: 1px solid white;
            color: white;
            padding: 0.5rem 1rem;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
        }

        .container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 1rem;
        }

        .search-section {
            background: white;
            padding: 2rem;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
        }

        .search-form {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem;
        }

        .form-group {
            display: flex;
            flex-direction: column;
            gap: 0.5rem;
        }

        .form-group label {
            color: #666;
        }

        .form-group input, .form-group select {
            padding: 0.8rem;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1rem;
        }

        .search-btn {
            background: #667eea;
            color: white;
            border: none;
            padding: 0.8rem;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1rem;
            align-self: end;
        }

        .bookings-section {
            background: white;
            padding: 2rem;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .bookings-section h2 {
            margin-bottom: 1.5rem;
            color: #333;
        }

        .bookings-table {
            width: 100%;
            border-collapse: collapse;
        }

        .bookings-table th, .bookings-table td {
            padding: 1rem;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        .bookings-table th {
            background: #f8f9fa;
            color: #666;
        }

        .status-confirmed {
            color: #28a745;
        }

        .status-cancelled {
            color: #dc3545;
        }

        .status-completed {
            color: #6c757d;
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <h1>Bus Reservation System</h1>
        <div class="user-info">
            <span>Welcome, ${user.fullName}</span>
            <a href="logout" class="logout-btn">Logout</a>
        </div>
    </nav>

    <div class="container">
        <section class="search-section">
            <h2>Search Buses</h2>
            <form class="search-form" action="search" method="GET">
                <div class="form-group">
                    <label for="from">From</label>
                    <input type="text" id="from" name="from" required>
                </div>
                <div class="form-group">
                    <label for="to">To</label>
                    <input type="text" id="to" name="to" required>
                </div>
                <div class="form-group">
                    <label for="date">Date</label>
                    <input type="date" id="date" name="date" required>
                </div>
                <button type="submit" class="search-btn">Search Buses</button>
            </form>
        </section>

        <section class="bookings-section">
            <h2>Your Bookings</h2>
            <table class="bookings-table">
                <thead>
                    <tr>
                        <th>Booking ID</th>
                        <th>Bus</th>
                        <th>From</th>
                        <th>To</th>
                        <th>Date</th>
                        <th>Seat</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${bookings}" var="booking">
                        <tr>
                            <td>${booking.bookingId}</td>
                            <td>${booking.busName}</td>
                            <td>${booking.source}</td>
                            <td>${booking.destination}</td>
                            <td>${booking.journeyDate}</td>
                            <td>${booking.seatNumber}</td>
                            <td class="status-${booking.status.toLowerCase()}">${booking.status}</td>
                            <td>
                                <c:if test="${booking.status == 'CONFIRMED'}">
                                    <a href="cancel?bookingId=${booking.bookingId}" 
                                       onclick="return confirm('Are you sure you want to cancel this booking?')"
                                       class="cancel-btn">Cancel</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </section>
    </div>

    <script>
        // Set minimum date to today for the date input
        const dateInput = document.getElementById('date');
        const today = new Date().toISOString().split('T')[0];
        dateInput.min = today;
    </script>
</body>
</html> 