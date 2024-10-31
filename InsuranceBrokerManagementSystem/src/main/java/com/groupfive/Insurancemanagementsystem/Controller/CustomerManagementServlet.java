package com.groupfive.Insurancemanagementsystem.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.groupfive.Insurancemanagementsystem.Model.Broker;
import com.groupfive.Insurancemanagementsystem.Model.Customer;
import com.groupfive.Insurancemanagementsystem.Repository.ICustomerRepository;
import com.groupfive.Insurancemanagementsystem.Repository.CustomerRepositoryImplementation;
import com.groupfive.Insurancemanagementsystem.Util.DatabaseUtility;

public class CustomerManagementServlet extends HttpServlet {

    private ICustomerRepository customerRepository = new CustomerRepositoryImplementation();
    private Connection connection;

    public CustomerManagementServlet() {
        try {
            this.connection = DatabaseUtility.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if ("fetchBrokers".equals(action)) {
            fetchBrokers(out);
            return;
        }

        String searchId = request.getParameter("searchId");
        String updateCustomerId = request.getParameter("updateCustomerId");
        String deleteCustomerId = request.getParameter("deleteCustomerId");

        synchronized (this) {
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

            if (updateCustomerId != null && !updateCustomerId.isEmpty()) {
                Customer existingCustomer = customerRepository.findCustomerById(updateCustomerId);
                if (existingCustomer != null) {
                    String updatedName = request.getParameter("name");
                    String updatedEmail = request.getParameter("email");
                    String updatedPhone = request.getParameter("phone");

                    existingCustomer.setName(updatedName);
                    existingCustomer.setEmail(updatedEmail);
                    existingCustomer.setPhone(updatedPhone);

                    customerRepository.updateCustomer(customerRepository.findAllCustomers(), updateCustomerId, existingCustomer);
                    out.print("{\"status\": \"success\", \"message\": \"Customer updated successfully\"}");
                } else {
                    out.print("{\"status\": \"error\", \"message\": \"Customer not found\"}");
                }
                out.flush();
                return;
            }

            if (deleteCustomerId != null && !deleteCustomerId.isEmpty()) {
                customerRepository.deleteCustomer(deleteCustomerId);
                out.print("{\"status\": \"success\", \"message\": \"Customer deleted successfully\"}");
                out.flush();
                return;
            }

            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String brokerEmail = request.getParameter("brokerEmail");

            if (customerRepository.findCustomerById(id) != null) {
                out.print("{\"status\": \"error\", \"message\": \"Customer with this ID already exists.\"}");
            } else {
                Customer newCustomer = new Customer(id, name, email, phone, brokerEmail);
                customerRepository.addCustomer(newCustomer);
                out.print("{\"status\": \"success\", \"message\": \"Customer added successfully\"}");
            }
            out.flush();
        }
    }

    private void fetchBrokers(PrintWriter out) throws IOException {
        List<Broker> brokers = new ArrayList<>();
        String sql = "SELECT name, email FROM brokers";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                brokers.add(new Broker(rs.getString("name"), rs.getString("email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        out.print("[");
        for (int i = 0; i < brokers.size(); i++) {
            Broker broker = brokers.get(i);
            out.print(String.format("{\"email\": \"%s\", \"name\": \"%s\"}", broker.getEmail(), broker.getName()));
            if (i < brokers.size() - 1) {
                out.print(",");
            }
        }
        out.print("]");
        out.flush();
    }
}
