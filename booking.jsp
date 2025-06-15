<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book Bus - Bus Reservation System</title>
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

        .booking-container {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 2rem;
        }

        .bus-details {
            background: white;
            padding: 2rem;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .bus-details h2 {
            color: #333;
            margin-bottom: 1.5rem;
        }

        .details-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 1.5rem;
        }

        .detail-item {
            color: #666;
        }

        .detail-item strong {
            color: #333;
            display: block;
            margin-bottom: 0.25rem;
        }

        .seat-selection {
            background: white;
            padding: 2rem;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .seat-selection h2 {
            color: #333;
            margin-bottom: 1.5rem;
        }

        .seat-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 1rem;
            margin-bottom: 2rem;
        }

        .seat {
            aspect-ratio: 1;
            border: 2px solid #ddd;
            border-radius: 5px;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .seat:hover {
            border-color: #667eea;
        }

        .seat.selected {
            background: #667eea;
            color: white;
            border-color: #667eea;
        }

        .seat.unavailable {
            background: #f8f9fa;
            color: #999;
            cursor: not-allowed;
            border-color: #ddd;
        }

        .seat-legend {
            display: flex;
            gap: 2rem;
            margin-bottom: 2rem;
        }

        .legend-item {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            color: #666;
        }

        .legend-color {
            width: 20px;
            height: 20px;
            border-radius: 3px;
        }

        .booking-summary {
            background: #f8f9fa;
            padding: 1.5rem;
            border-radius: 5px;
            margin-bottom: 2rem;
        }

        .summary-item {
            display: flex;
            justify-content: space-between;
            margin-bottom: 0.5rem;
            color: #666;
        }

        .summary-item.total {
            color: #333;
            font-weight: bold;
            font-size: 1.2rem;
            margin-top: 1rem;
            padding-top: 1rem;
            border-top: 1px solid #ddd;
        }

        .book-btn {
            width: 100%;
            background: #667eea;
            color: white;
            border: none;
            padding: 1rem;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1rem;
        }

        .book-btn:hover {
            background: #764ba2;
        }

        .book-btn:disabled {
            background: #ccc;
            cursor: not-allowed;
        }

        .error-message {
            color: #dc3545;
            margin-bottom: 1rem;
            display: none;
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <h1>Bus Reservation System</h1>
        <a href="search-results.jsp" class="back-btn">Back to Search Results</a>
    </nav>

    <div class="container">
        <div class="booking-container">
            <div class="bus-details">
                <h2>Bus Details</h2>
                <div class="details-grid">
                    <div class="detail-item">
                        <strong>Bus Name</strong>
                        ${schedule.busName}
                    </div>
                    <div class="detail-item">
                        <strong>Bus Number</strong>
                        ${schedule.busNumber}
                    </div>
                    <div class="detail-item">
                        <strong>From</strong>
                        ${schedule.source}
                    </div>
                    <div class="detail-item">
                        <strong>To</strong>
                        ${schedule.destination}
                    </div>
                    <div class="detail-item">
                        <strong>Departure</strong>
                        <fmt:formatDate value="${schedule.departureTime}" pattern="hh:mm a"/>
                    </div>
                    <div class="detail-item">
                        <strong>Arrival</strong>
                        <fmt:formatDate value="${schedule.arrivalTime}" pattern="hh:mm a"/>
                    </div>
                </div>
            </div>

            <div class="seat-selection">
                <h2>Select Your Seat</h2>
                
                <div class="seat-legend">
                    <div class="legend-item">
                        <div class="legend-color" style="background: white; border: 2px solid #ddd;"></div>
                        Available
                    </div>
                    <div class="legend-item">
                        <div class="legend-color" style="background: #667eea;"></div>
                        Selected
                    </div>
                    <div class="legend-item">
                        <div class="legend-color" style="background: #f8f9fa;"></div>
                        Unavailable
                    </div>
                </div>

                <div class="seat-grid" id="seatGrid">
                    <!-- Seats will be generated dynamically -->
                </div>

                <div class="booking-summary">
                    <div class="summary-item">
                        <span>Base Fare:</span>
                        <span>$${schedule.fare}</span>
                    </div>
                    <div class="summary-item">
                        <span>Taxes & Fees:</span>
                        <span>$0.00</span>
                    </div>
                    <div class="summary-item total">
                        <span>Total:</span>
                        <span>$${schedule.fare}</span>
                    </div>
                </div>

                <form id="bookingForm" action="book" method="POST">
                    <input type="hidden" name="scheduleId" value="${schedule.scheduleId}">
                    <input type="hidden" name="seatNumber" id="selectedSeat">
                    <div class="error-message" id="errorMessage"></div>
                    <button type="submit" class="book-btn" id="bookBtn" disabled>Book Now</button>
                </form>
            </div>
        </div>
    </div>

    <script>
        // Generate seats
        const seatGrid = document.getElementById('seatGrid');
        const totalSeats = ${schedule.totalSeats};
        const unavailableSeats = []; // This should be populated from the server

        for (let i = 1; i <= totalSeats; i++) {
            const seat = document.createElement('div');
            seat.className = 'seat' + (unavailableSeats.includes(i) ? ' unavailable' : '');
            seat.textContent = i;
            seat.dataset.seatNumber = i;
            
            if (!unavailableSeats.includes(i)) {
                seat.addEventListener('click', function() {
                    const selectedSeat = document.querySelector('.seat.selected');
                    if (selectedSeat) {
                        selectedSeat.classList.remove('selected');
                    }
                    this.classList.add('selected');
                    document.getElementById('selectedSeat').value = this.dataset.seatNumber;
                    document.getElementById('bookBtn').disabled = false;
                });
            }
            
            seatGrid.appendChild(seat);
        }

        // Form submission
        document.getElementById('bookingForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            const selectedSeat = document.getElementById('selectedSeat').value;
            const errorMessage = document.getElementById('errorMessage');
            
            if (!selectedSeat) {
                errorMessage.style.display = 'block';
                errorMessage.textContent = 'Please select a seat';
                return;
            }
            
            this.submit();
        });
    </script>
</body>
</html> 