package com.busreservation.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.busreservation.model.User;
import com.busreservation.util.DBUtil;

@WebServlet("/cancel")
public class CancelBookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        
        try (Connection conn = DBUtil.getConnection()) {
            // Verify that the booking belongs to the user
            String verifySql = "SELECT COUNT(*) FROM bookings WHERE booking_id = ? AND user_id = ? AND status = 'CONFIRMED'";
            try (PreparedStatement verifyStmt = conn.prepareStatement(verifySql)) {
                verifyStmt.setInt(1, bookingId);
                verifyStmt.setInt(2, user.getUserId());
                
                if (verifyStmt.executeQuery().next()) {
                    // Cancel the booking
                    String cancelSql = "UPDATE bookings SET status = 'CANCELLED' WHERE booking_id = ?";
                    try (PreparedStatement cancelStmt = conn.prepareStatement(cancelSql)) {
                        cancelStmt.setInt(1, bookingId);
                        cancelStmt.executeUpdate();
                    }
                    
                    request.setAttribute("message", "Booking cancelled successfully");
                } else {
                    request.setAttribute("error", "Invalid booking or already cancelled");
                }
            }
            
            response.sendRedirect("dashboard.jsp");
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error cancelling booking");
            response.sendRedirect("dashboard.jsp");
        }
    }
} 