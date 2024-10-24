package com.groupfive.Insurancemanagementsystem.Repository;

import com.groupfive.Insurancemanagementsystem.Model.Broker;
import com.groupfive.Insurancemanagementsystem.Util.DatabaseUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BrokerRepository {
    private Connection connection;

    public BrokerRepository() {
        try {
            // Establish a database connection using DatabaseUtility class
            this.connection = DatabaseUtility.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Save Broker to the database
    public void saveBroker(Broker broker) throws SQLException {
        String sql = "INSERT INTO brokers (name, email, phone, password) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, broker.getName());
            pstmt.setString(2, broker.getEmail());
            pstmt.setString(3, broker.getPhone());
            pstmt.setString(4, broker.getPassword());
            pstmt.executeUpdate();
        }
    }

    // Retrieve all brokers from the database
    public List<Broker> getAllBrokers() throws SQLException {
        List<Broker> brokers = new ArrayList<>();
        String sql = "SELECT * FROM brokers";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Broker broker = new Broker(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password")
                );
                brokers.add(broker);
            }
        }
        return brokers;
    }

    // Check if a broker exists with the given email and password
    public boolean isValidBroker(String email, String password) throws SQLException {
        String sql = "SELECT * FROM brokers WHERE email = ? AND password = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            return rs.next(); // Returns true if a record is found, false otherwise
        }
    }

    // Delete a broker by email
    public boolean deleteBroker(String email) throws SQLException {
        String sql = "DELETE FROM brokers WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Update broker details
    public void updateBroker(Broker broker) throws SQLException {
        String sql = "UPDATE brokers SET name = ?, phone = ?, password = ? WHERE email = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, broker.getName());
            pstmt.setString(2, broker.getPhone());
            pstmt.setString(3, broker.getPassword());
            pstmt.setString(4, broker.getEmail());
            pstmt.executeUpdate();
        }
    }
}
