package com.groupfive.Insurancemanagementsystem.Controller;

import com.groupfive.Insurancemanagementsystem.Repository.BrokerRepository;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Data from form
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Storage path from xml
        ServletContext context = getServletContext();
        String fileStoragePath = context.getInitParameter("fileStoragePath");

        // Initialize the repository
        BrokerRepository brokerRepository = new BrokerRepository(fileStoragePath);

        try {
            // Validate Login
            if (brokerRepository.isValidBroker(email, password)) {
                response.sendRedirect("landingPage.html");
            } else {
                // Error Page
                response.sendRedirect("loginPage.html?error=Invalid email or password. Please try again.");
            }
        } catch (Exception e) {
            // File not found
            response.sendRedirect("loginPage.html?error=Broker data not found.");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("loginPage.html");
    }
}
