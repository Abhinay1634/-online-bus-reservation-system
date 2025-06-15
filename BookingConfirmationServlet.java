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

@WebServlet("/booking-confirmation")
public class BookingConfirmationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookingDAO bookingDAO;

    public void init() {
        bookingDAO = new BookingDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String bookingId = request.getParameter("id");
        if (bookingId == null || bookingId.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user/my-bookings.jsp");
            return;
        }

        try {
            Booking booking = bookingDAO.getBookingById(Integer.parseInt(bookingId));
            if (booking != null && booking.getUserId() == user.getId()) {
                request.setAttribute("booking", booking);
                request.getRequestDispatcher("/user/booking-confirmation.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/user/my-bookings.jsp");
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/user/my-bookings.jsp");
        }
    }
} 