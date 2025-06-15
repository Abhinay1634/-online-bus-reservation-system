package com.busreservation.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.busreservation.model.BusSchedule;
import com.busreservation.util.DBUtil;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String date = request.getParameter("date");
        
        List<BusSchedule> schedules = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT s.*, b.bus_name, b.bus_number, b.total_seats, " +
                        "r.source_city, r.destination_city " +
                        "FROM schedules s " +
                        "JOIN buses b ON s.bus_id = b.bus_id " +
                        "JOIN routes r ON s.route_id = r.route_id " +
                        "WHERE r.source_city = ? AND r.destination_city = ? " +
                        "AND DATE(s.departure_time) = ? " +
                        "AND s.status = 'SCHEDULED'";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, from);
                pstmt.setString(2, to);
                pstmt.setString(3, date);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
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
                        
                        // Get available seats
                        int bookedSeats = getBookedSeats(conn, schedule.getScheduleId());
                        schedule.setAvailableSeats(schedule.getTotalSeats() - bookedSeats);
                        
                        schedules.add(schedule);
                    }
                }
            }
            
            request.setAttribute("schedules", schedules);
            request.setAttribute("from", from);
            request.setAttribute("to", to);
            request.setAttribute("date", date);
            request.getRequestDispatcher("search-results.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error searching for buses");
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        }
    }
    
    private int getBookedSeats(Connection conn, int scheduleId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings WHERE schedule_id = ? AND status = 'CONFIRMED'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, scheduleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
} 