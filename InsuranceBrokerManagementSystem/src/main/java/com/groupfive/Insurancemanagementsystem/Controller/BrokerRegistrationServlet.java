package com.groupfive.Insurancemanagementsystem.Controller;

import com.groupfive.Insurancemanagementsystem.Model.Broker;
import com.groupfive.Insurancemanagementsystem.Repository.BrokerRepository;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;

public class BrokerRegistrationServlet extends HttpServlet {

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        // Create a new Broker object
        Broker broker = new Broker(name, email, phone, password);

        // Use synchronized block to ensure thread-safe file writing
        synchronized (this) {
            try {
                // Write the broker data to the file within the synchronized block
                brokerRepository.saveBroker(broker);

                // Redirect to login page after successful registration
                response.sendRedirect("loginPage.html");
            } catch (IOException e) {
                e.printStackTrace(); // Print the stack trace for debugging

                // Redirect back to the registration page with an error message
                String errorMessage = "Error registering broker: " + e.getMessage();
                response.sendRedirect("brokerRegistration.html?error=" + URLEncoder.encode(errorMessage, "UTF-8"));
            }
        }
    }
}
