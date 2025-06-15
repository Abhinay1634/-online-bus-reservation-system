<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Search Results - Bus Reservation System</title>
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

        .back-btn {
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

        .search-info {
            background: white;
            padding: 1.5rem;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
        }

        .search-info h2 {
            color: #333;
            margin-bottom: 1rem;
        }

        .search-details {
            display: flex;
            gap: 2rem;
            color: #666;
        }

        .bus-list {
            display: grid;
            gap: 1.5rem;
        }

        .bus-card {
            background: white;
            padding: 1.5rem;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 2rem;
        }

        .bus-info h3 {
            color: #333;
            margin-bottom: 0.5rem;
        }

        .bus-details {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 1rem;
            margin-top: 1rem;
        }

        .detail-item {
            color: #666;
        }

        .detail-item strong {
            color: #333;
            display: block;
            margin-bottom: 0.25rem;
        }

        .booking-section {
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            align-items: flex-end;
        }

        .price {
            font-size: 1.5rem;
            color: #28a745;
            font-weight: bold;
        }

        .seats-available {
            color: #666;
            margin: 0.5rem 0;
        }

        .book-btn {
            background: #667eea;
            color: white;
            border: none;
            padding: 0.8rem 1.5rem;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1rem;
            text-decoration: none;
            text-align: center;
        }

        .book-btn:hover {
            background: #764ba2;
        }

        .no-results {
            text-align: center;
            padding: 3rem;
            background: white;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .no-results h3 {
            color: #666;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <h1>Bus Reservation System</h1>
        <a href="dashboard.jsp" class="back-btn">Back to Dashboard</a>
    </nav>

    <div class="container">
        <div class="search-info">
            <h2>Search Results</h2>
            <div class="search-details">
                <div>
                    <strong>From:</strong> ${from}
                </div>
                <div>
                    <strong>To:</strong> ${to}
                </div>
                <div>
                    <strong>Date:</strong> ${date}
                </div>
            </div>
        </div>

        <div class="bus-list">
            <c:choose>
                <c:when test="${empty schedules}">
                    <div class="no-results">
                        <h3>No buses found for the selected route and date.</h3>
                        <p>Please try different search criteria.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${schedules}" var="schedule">
                        <div class="bus-card">
                            <div class="bus-info">
                                <h3>${schedule.busName}</h3>
                                <p>Bus Number: ${schedule.busNumber}</p>
                                <div class="bus-details">
                                    <div class="detail-item">
                                        <strong>Departure</strong>
                                        <fmt:formatDate value="${schedule.departureTime}" pattern="hh:mm a"/>
                                    </div>
                                    <div class="detail-item">
                                        <strong>Arrival</strong>
                                        <fmt:formatDate value="${schedule.arrivalTime}" pattern="hh:mm a"/>
                                    </div>
                                    <div class="detail-item">
                                        <strong>Duration</strong>
                                        ${schedule.duration}
                                    </div>
                                    <div class="detail-item">
                                        <strong>Bus Type</strong>
                                        ${schedule.busType}
                                    </div>
                                </div>
                            </div>
                            <div class="booking-section">
                                <div class="price">
                                    $${schedule.fare}
                                </div>
                                <div class="seats-available">
                                    ${schedule.availableSeats} seats available
                                </div>
                                <a href="book?scheduleId=${schedule.scheduleId}" class="book-btn">
                                    Book Now
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html> 