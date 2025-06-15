package com.busreservation.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.busreservation.dao.BookingDAO;
import com.busreservation.model.Booking;
import com.busreservation.model.User;

@WebServlet("/process-payment")
public class ProcessPaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookingDAO bookingDAO;

    public void init() {
        bookingDAO = new BookingDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String bookingId = request.getParameter("bookingId");
        String cardNumber = request.getParameter("cardNumber");
        String expiryDate = request.getParameter("expiryDate");
        String cvv = request.getParameter("cvv");
        String cardName = request.getParameter("cardName");

        // Validate payment details
        if (!isValidPaymentDetails(cardNumber, expiryDate, cvv, cardName)) {
            request.setAttribute("error", "Invalid payment details. Please check and try again.");
            request.getRequestDispatcher("/user/payment.jsp").forward(request, response);
            return;
        }

        try {
            // Update booking status
            Booking booking = bookingDAO.getBookingById(Integer.parseInt(bookingId));
            if (booking != null && booking.getUserId() == user.getId()) {
                booking.setPaymentStatus("PAID");
                boolean updated = bookingDAO.updateBooking(booking);
                
                if (updated) {
                    // Payment successful
                    response.sendRedirect(request.getContextPath() + "/user/booking-confirmation.jsp?id=" + bookingId);
                } else {
                    request.setAttribute("error", "Failed to process payment. Please try again.");
                    request.getRequestDispatcher("/user/payment.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("error", "Invalid booking.");
                request.getRequestDispatcher("/user/payment.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred while processing payment.");
            request.getRequestDispatcher("/user/payment.jsp").forward(request, response);
        }
    }

    private boolean isValidPaymentDetails(String cardNumber, String expiryDate, String cvv, String cardName) {
        // Basic validation
        if (cardNumber == null || cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
            return false;
        }

        if (expiryDate == null || !expiryDate.matches("(0[1-9]|1[0-2])/([0-9]{2})")) {
            return false;
        }

        if (cvv == null || cvv.length() != 3 || !cvv.matches("\\d+")) {
            return false;
        }

        if (cardName == null || cardName.trim().isEmpty()) {
            return false;
        }

        return true;
    }
} 