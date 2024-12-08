package com.groupfive.Insurancemanagementsystem.Repository;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.groupfive.Insurancemanagementsystem.Model.Customer;
import com.groupfive.Insurancemanagementsystem.Model.CustomerPolicies;
import com.groupfive.Insurancemanagementsystem.Util.DatabaseUtility;

public class PolicyCancellationRepository {

    // Fetch all customers from the customers table
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT name FROM customers";

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setName(rs.getString("name"));
                customers.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    // Fetch policies assigned to the specified customerName from the customer_policies table
    public List<CustomerPolicies> getPoliciesForCustomer(String customerName) {
        List<CustomerPolicies> policies = new ArrayList<>();
        String query = "SELECT customer_id, policy_number FROM customer_policies WHERE customer_id = ? and status='active'";

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, customerName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
            	CustomerPolicies policy = new CustomerPolicies();
                policy.setCustomerName(rs.getString("customer_id"));
                policy.setPolicyName(rs.getString("policy_number"));
                policies.add(policy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return policies;
    }
    
    public boolean cancelPolicy(String customerName, String policyName, String reason) {
        String updateStatusQuery = "UPDATE customer_policies SET status = 'cancelled' WHERE customer_id = ? AND policy_number = ?";
        String insertCancellationQuery = "INSERT INTO cancelledPolicies (customer_name, policy_name, reason, cancellation_date) VALUES (?, ?, ?, NOW())";

        try (Connection conn = DatabaseUtility.getConnection()) {
            // Update policy status in customer_policies table
            try (PreparedStatement updateStmt = conn.prepareStatement(updateStatusQuery)) {
                updateStmt.setString(1, customerName);
                updateStmt.setString(2, policyName);
                int rowsUpdated = updateStmt.executeUpdate();

                // If the status update was successful, proceed to insert into cancelledPolicies
                if (rowsUpdated > 0) {
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertCancellationQuery)) {
                        insertStmt.setString(1, customerName);
                        insertStmt.setString(2, policyName);
                        insertStmt.setString(3, reason);
                        int rowsInserted = insertStmt.executeUpdate();

                        return rowsInserted > 0; // Return true if cancellation was successful
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Return false if any operation failed
    }
}
