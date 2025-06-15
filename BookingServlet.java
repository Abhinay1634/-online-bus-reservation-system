package com.busreservation.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.busreservation.model.BusSchedule;
import com.busreservation.model.User;
import com.busreservation.util.DBUtil;

@WebServlet("/book")
public class BookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
        
        try (Connection conn = DBUtil.getConnection()) {
            // Get schedule details
            BusSchedule schedule = getScheduleDetails(conn, scheduleId);
            
            if (schedule != null) {
                request.setAttribute("schedule", schedule);
                request.getRequestDispatcher("booking.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Schedule not found");
                response.sendRedirect("dashboard.jsp");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error processing booking");
            response.sendRedirect("dashboard.jsp");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
        int seatNumber = Integer.parseInt(request.getParameter("seatNumber"));
        
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Check if seat is available
                if (!isSeatAvailable(conn, scheduleId, seatNumber)) {
                    request.setAttribute("error", "Selected seat is not available");
                    request.getRequestDispatcher("booking.jsp").forward(request, response);
                    return;
                }
                
                // Get schedule details for fare
                BusSchedule schedule = getScheduleDetails(conn, scheduleId);
                
                // Create booking
                String sql = "INSERT INTO bookings (user_id, schedule_id, seat_number, total_fare, status) " +
                           "VALUES (?, ?, ?, ?, 'CONFIRMED')";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, user.getUserId());
                    pstmt.setInt(2, scheduleId);
                    pstmt.setInt(3, seatNumber);
                    pstmt.setDouble(4, schedule.getFare());
                    
                    pstmt.executeUpdate();
                }
                
                conn.commit();
                response.sendRedirect("dashboard.jsp");
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error processing booking");
            request.getRequestDispatcher("booking.jsp").forward(request, response);
        }
    }
    
    private BusSchedule getScheduleDetails(Connection conn, int scheduleId) throws SQLException {
        String sql = "SELECT s.*, b.bus_name, b.bus_number, b.total_seats, " +
                    "r.source_city, r.destination_city " +
                    "FROM schedules s " +
                    "JOIN buses b ON s.bus_id = b.bus_id " +
                    "JOIN routes r ON s.route_id = r.route_id " +
                    "WHERE s.schedule_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, scheduleId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    BusSchedule schedule = new BusSchedule();
                    schedule.setScheduleId(rs.getInt("schedule_id"));
                    schedule.setBusName(rs.getString("bus_name"));
                    schedule.setBusNumber(rs.getString("bus_number"));
                    schedule.setSource(rs.getString("source_city"));
                    schedule.setDestination(rs.getString("destination_city"));
                    schedule.setDepartureTime(rs.getTimestamp("departure_time"));
                    schedule.setArrivalTime(rs.getTimestamp("arrival_time"));
                    schedule.setFare(rs.getDouble("fare"));
                    schedule.setTotalSeats(rs.getInt("total_seats"));
                    return schedule;
                }
            }
        }
        return null;
    }
    
    private boolean isSeatAvailable(Connection conn, int scheduleId, int seatNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings " +
                    "WHERE schedule_id = ? AND seat_number = ? AND status = 'CONFIRMED'";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, scheduleId);
            pstmt.setInt(2, seatNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return false;
    }
} 