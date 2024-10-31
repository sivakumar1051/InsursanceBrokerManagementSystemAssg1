package com.groupfive.Insurancemanagementsystem.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtility {
    
    private static final String DB_URL ="jdbc:mysql://localhost:3306/insurance"; // Update with your DB URL
    private static final String USER ="root"; // Update with your DB username
    private static final String PASS ="Siva@7567"; // Update with your DB password

    static {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading MySQL Driver: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}
