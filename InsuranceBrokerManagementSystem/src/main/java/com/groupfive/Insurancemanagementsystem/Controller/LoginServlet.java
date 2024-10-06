package com.groupfive.Insurancemanagementsystem.Controller;

import com.groupfive.Insurancemanagementsystem.Repository.BrokerRepository;
import jakarta.servlet.ServletContext;
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
        // Initialize BrokerRepository once to be reused by all requests
        ServletContext context = getServletContext();
        String fileStoragePath = context.getInitParameter("fileStoragePath");
        this.brokerRepository = new BrokerRepository(fileStoragePath);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Data from form
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Use synchronized block to ensure thread-safe file access
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
                // File not found
                response.sendRedirect("loginPage.html?error=Broker data not found.");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("loginPage.html");
    }
}
