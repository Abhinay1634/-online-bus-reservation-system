package com.busreservation.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.busreservation.model.Booking;
import com.busreservation.util.DBUtil;

public class BookingDAO {
    
    public boolean createBooking(Booking booking) {
        String query = "INSERT INTO bookings (user_id, bus_id, seat_number, journey_date, total_fare, status, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, booking.getUserId());
            pstmt.setInt(2, booking.getBusId());
            pstmt.setInt(3, booking.getSeatNumber());
            pstmt.setDate(4, new java.sql.Date(booking.getJourneyDate().getTime()));
            pstmt.setDouble(5, booking.getTotalFare());
            pstmt.setString(6, booking.getStatus());
            pstmt.setString(7, booking.getPaymentStatus());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Booking getBookingById(int id) {
        String query = "SELECT * FROM bookings WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractBookingFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE user_id = ? ORDER BY booking_date DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(extractBookingFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    
    public List<Booking> getBookingsByBusId(int busId) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE bus_id = ? ORDER BY booking_date DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, busId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(extractBookingFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    
    public boolean updateBookingStatus(int bookingId, String status) {
        String query = "UPDATE bookings SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, bookingId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updatePaymentStatus(int bookingId, String paymentStatus) {
        String query = "UPDATE bookings SET payment_status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, paymentStatus);
            pstmt.setInt(2, bookingId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Integer> getBookedSeats(int busId, Date journeyDate) {
        List<Integer> bookedSeats = new ArrayList<>();
        String query = "SELECT seat_number FROM bookings WHERE bus_id = ? AND journey_date = ? AND status = 'CONFIRMED'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, busId);
            pstmt.setDate(2, new java.sql.Date(journeyDate.getTime()));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookedSeats.add(rs.getInt("seat_number"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookedSeats;
    }
    
    public boolean updateBooking(Booking booking) {
        String sql = "UPDATE bookings SET status = ?, payment_status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, booking.getStatus());
            stmt.setString(2, booking.getPaymentStatus());
            stmt.setInt(3, booking.getId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Booking extractBookingFromResultSet(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setBusId(rs.getInt("bus_id"));
        booking.setSeatNumber(rs.getInt("seat_number"));
        booking.setBookingDate(rs.getTimestamp("booking_date"));
        booking.setJourneyDate(rs.getDate("journey_date"));
        booking.setTotalFare(rs.getDouble("total_fare"));
        booking.setStatus(rs.getString("status"));
        booking.setPaymentStatus(rs.getString("payment_status"));
        return booking;
    }
} 