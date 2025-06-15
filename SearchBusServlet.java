package com.busreservation.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.busreservation.dao.BusDAO;
import com.busreservation.model.Bus;

@WebServlet("/search-bus")
public class SearchBusServlet extends HttpServlet {
    private BusDAO busDAO;

    @Override
    public void init() throws ServletException {
        busDAO = new BusDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String source = request.getParameter("source");
        String destination = request.getParameter("destination");
        String journeyDate = request.getParameter("journeyDate");

        List<Bus> buses = busDAO.searchBuses(source, destination, journeyDate);

        request.setAttribute("buses", buses);
        request.setAttribute("source", source);
        request.setAttribute("destination", destination);
        request.setAttribute("journeyDate", journeyDate);

        request.getRequestDispatcher("user/search-results.jsp").forward(request, response);
    }
} 