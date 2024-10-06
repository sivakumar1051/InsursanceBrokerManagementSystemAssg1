package com.groupfive.Insurancemanagementsystem.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.groupfive.Insurancemanagementsystem.Model.Customer;
import com.groupfive.Insurancemanagementsystem.Repository.ICustomerRepository;
import com.groupfive.Insurancemanagementsystem.Repository.CustomerRepositoryImplementation;

public class CustomerManagementServlet extends HttpServlet {

    private ICustomerRepository customerRepository = new CustomerRepositoryImplementation();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchId = request.getParameter("searchId");
        String updateCustomerId = request.getParameter("updateCustomerId");
        String deleteCustomerId = request.getParameter("deleteCustomerId");
        System.out.println("Customer Delete"+deleteCustomerId);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Use synchronized block for thread-safe operations
        synchronized (this) {
            // Search Customers
            if (searchId != null && !searchId.isEmpty()) {
                Customer customer = customerRepository.findCustomerById(searchId);
                if (customer != null) {
                    out.print(String.format("{\"customer\": {\"id\":\"%s\",\"name\":\"%s\",\"email\":\"%s\",\"phone\":\"%s\"}}",
                        customer.getId(), customer.getName(), customer.getEmail(), customer.getPhone()));
                } else {
                    out.print("{\"customer\": null}");
                }
                out.flush();
                return;
            }

            // Update Customers
            if (updateCustomerId != null && !updateCustomerId.isEmpty()) {
                Customer existingCustomer = customerRepository.findCustomerById(updateCustomerId);
                System.out.println(existingCustomer.getEmail() + "," + existingCustomer.getName());
                // Update the existing customer's details
                String updatedName = request.getParameter("name");
                String updatedEmail = request.getParameter("email");
                String updatedPhone = request.getParameter("phone");
                System.out.println(updatedName + "," + updatedEmail);

                existingCustomer.setName(updatedName);
                existingCustomer.setEmail(updatedEmail);
                existingCustomer.setPhone(updatedPhone);

                customerRepository.saveAllCustomers(customerRepository.findAllCustomers()); // Save the updated list
                customerRepository.updateCustomer(customerRepository.findAllCustomers(), updateCustomerId, existingCustomer);

                out.print("{\"status\": \"success\", \"message\": \"Customer updated successfully\"}");
                out.flush();
                return;
            }

            // Delete Customers
            if (deleteCustomerId != null && !deleteCustomerId.isEmpty()) {
                customerRepository.deleteCustomer(deleteCustomerId);
                out.print("{\"status\": \"success\", \"message\": \"Customer deleted successfully\"}");
                out.flush();
                return;
            }

            // Add New Customer
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");

            Customer newCustomer = new Customer(id, name, email, phone);
            customerRepository.addCustomer(newCustomer);

            response.sendRedirect("customerManagement.html");
        }
    }
}
