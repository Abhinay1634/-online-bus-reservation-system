package com.busreservation.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.busreservation.dao.BookingDAO;
import com.busreservation.model.Booking;
import com.busreservation.model.User;

@WebServlet("/book-seat")
public class BookSeatServlet extends HttpServlet {
    private BookingDAO bookingDAO;

    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int busId = Integer.parseInt(request.getParameter("busId"));
        int seatNumber = Integer.parseInt(request.getParameter("seatNumber"));
        double totalFare = Double.parseDouble(request.getParameter("totalFare"));
        String journeyDate = request.getParameter("journeyDate");

        Booking booking = new Booking();
        booking.setUserId(user.getId());
        booking.setBusId(busId);
        booking.setSeatNumber(seatNumber);
        booking.setJourneyDate(new Date(journeyDate));
        booking.setTotalFare(totalFare);
        booking.setStatus("CONFIRMED");
        booking.setPaymentStatus("PENDING");

        boolean success = bookingDAO.createBooking(booking);

        if (success) {
            response.sendRedirect("payment.jsp?bookingId=" + booking.getId());
        } else {
            request.setAttribute("error", "Booking failed. Please try again.");
            request.getRequestDispatcher("select-seats.jsp").forward(request, response);
        }
    }
} 