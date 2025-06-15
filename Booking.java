package com.busreservation.model;

import java.util.Date;

public class Booking {
    private int id;
    private int userId;
    private int busId;
    private int seatNumber;
    private Date bookingDate;
    private Date journeyDate;
    private double totalFare;
    private String status;
    private String paymentStatus;

    public Booking() {}

    public Booking(int id, int userId, int busId, int seatNumber, Date bookingDate, 
                  Date journeyDate, double totalFare, String status, String paymentStatus) {
        this.id = id;
        this.userId = userId;
        this.busId = busId;
        this.seatNumber = seatNumber;
        this.bookingDate = bookingDate;
        this.journeyDate = journeyDate;
        this.totalFare = totalFare;
        this.status = status;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Date getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(Date journeyDate) {
        this.journeyDate = journeyDate;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(double totalFare) {
        this.totalFare = totalFare;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
} 