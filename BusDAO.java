package com.busreservation.dao;

import com.busreservation.model.Bus;
import com.busreservation.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BusDAO {
    
    public boolean addBus(Bus bus) {
        String query = "INSERT INTO buses (bus_number, bus_name, bus_type, total_seats, fare, source, destination, departure_time, arrival_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, bus.getBusNumber());
            pstmt.setString(2, bus.getBusName());
            pstmt.setString(3, bus.getBusType());
            pstmt.setInt(4, bus.getTotalSeats());
            pstmt.setDouble(5, bus.getFare());
            pstmt.setString(6, bus.getSource());
            pstmt.setString(7, bus.getDestination());
            pstmt.setString(8, bus.getDepartureTime());
            pstmt.setString(9, bus.getArrivalTime());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Bus getBusById(int id) {
        String query = "SELECT * FROM buses WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractBusFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Bus> getAllBuses() {
        List<Bus> buses = new ArrayList<>();
        String query = "SELECT * FROM buses";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                buses.add(extractBusFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buses;
    }
    
    public List<Bus> searchBuses(String source, String destination, String date) {
        List<Bus> buses = new ArrayList<>();
        String query = "SELECT * FROM buses WHERE source = ? AND destination = ? AND status = 'ACTIVE'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, source);
            pstmt.setString(2, destination);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    buses.add(extractBusFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buses;
    }
    
    public boolean updateBus(Bus bus) {
        String query = "UPDATE buses SET bus_number = ?, bus_name = ?, bus_type = ?, total_seats = ?, fare = ?, source = ?, destination = ?, departure_time = ?, arrival_time = ?, status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, bus.getBusNumber());
            pstmt.setString(2, bus.getBusName());
            pstmt.setString(3, bus.getBusType());
            pstmt.setInt(4, bus.getTotalSeats());
            pstmt.setDouble(5, bus.getFare());
            pstmt.setString(6, bus.getSource());
            pstmt.setString(7, bus.getDestination());
            pstmt.setString(8, bus.getDepartureTime());
            pstmt.setString(9, bus.getArrivalTime());
            pstmt.setString(10, bus.getStatus());
            pstmt.setInt(11, bus.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteBus(int id) {
        String query = "DELETE FROM buses WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Bus extractBusFromResultSet(ResultSet rs) throws SQLException {
        Bus bus = new Bus();
        bus.setId(rs.getInt("id"));
        bus.setBusNumber(rs.getString("bus_number"));
        bus.setBusName(rs.getString("bus_name"));
        bus.setBusType(rs.getString("bus_type"));
        bus.setTotalSeats(rs.getInt("total_seats"));
        bus.setFare(rs.getDouble("fare"));
        bus.setSource(rs.getString("source"));
        bus.setDestination(rs.getString("destination"));
        bus.setDepartureTime(rs.getString("departure_time"));
        bus.setArrivalTime(rs.getString("arrival_time"));
        bus.setStatus(rs.getString("status"));
        return bus;
    }
} 