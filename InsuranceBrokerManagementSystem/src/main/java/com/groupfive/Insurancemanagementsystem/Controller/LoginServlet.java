package com.groupfive.Insurancemanagementsystem.Controller;

import com.groupfive.Insurancemanagementsystem.Repository.BrokerRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private BrokerRepository brokerRepository; // Class-level variable for shared repository

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize BrokerRepository without any parameters (it uses the default constructor)
        this.brokerRepository = new BrokerRepository();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Data from form
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Use synchronized block to ensure thread-safe database access
        synchronized (this) {
            try {
                // Validate Login within synchronized block
                if (brokerRepository.isValidBroker(email, password)) {
                    response.sendRedirect("landingPage.html");
                } else {
                    // Error Page
                    response.sendRedirect("loginPage.html?error=Invalid email or password. Please try again.");
                }
            } catch (Exception e) {
                // Database error
                response.sendRedirect("loginPage.html?error=Error validating login. Please try again.");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("loginPage.html");
    }
}
