package com.groupfive.Insurancemanagementsystem.Repository;

import com.groupfive.Insurancemanagementsystem.Model.Customer;
import com.groupfive.Insurancemanagementsystem.Util.DatabaseUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepositoryImplementation implements ICustomerRepository {
    
    private Connection connection;

    public CustomerRepositoryImplementation() {
        try {
            this.connection = DatabaseUtility.getConnection(); // Get the connection from DatabaseUtility
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCustomer(Customer customer) {
        String sql = "INSERT INTO customers (id, name, email, phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, customer.getId());
            pstmt.setString(2, customer.getName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPhone());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Customer findCustomerById(String id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if customer is not found
    }

    @Override
    public List<Customer> findAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(new Customer(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public void updateCustomer(List<Customer> customers, String custId, Customer updCustomer) {
        String sql = "UPDATE customers SET name = ?, email = ?, phone = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, updCustomer.getName());
            pstmt.setString(2, updCustomer.getEmail());
            pstmt.setString(3, updCustomer.getPhone());
            pstmt.setString(4, custId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCustomer(String id) {
        String sql = "DELETE FROM customers WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveAllCustomers(List<Customer> customers) {
        // This method is no longer required as individual CRUD operations are handled directly via the database.
    }
}
