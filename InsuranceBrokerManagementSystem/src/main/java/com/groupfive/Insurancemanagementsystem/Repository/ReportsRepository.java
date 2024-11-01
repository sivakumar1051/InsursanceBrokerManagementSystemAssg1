package com.groupfive.Insurancemanagementsystem.Repository;

import com.groupfive.Insurancemanagementsystem.Util.DatabaseUtility;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportsRepository {

    public JSONArray getBrokerCustomerCounts() {
        String query = "SELECT brokers.email, brokers.name, COUNT(customers.id) AS customer_count " +
                       "FROM brokers LEFT JOIN customers ON brokers.email = customers.brokerEmail " +
                       "GROUP BY brokers.email, brokers.name";
        JSONArray brokersArray = new JSONArray();

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                JSONObject broker = new JSONObject();
                broker.put("brokerEmail", resultSet.getString("email"));
                broker.put("brokerName", resultSet.getString("name"));
                broker.put("customerCount", resultSet.getInt("customer_count"));
                brokersArray.put(broker);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brokersArray;
    }

    public JSONArray getCustomersByBroker(String brokerEmail) {
        String query = "SELECT name, phone FROM customers WHERE brokerEmail = ?";
        JSONArray customersArray = new JSONArray();

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, brokerEmail);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                JSONObject customer = new JSONObject();
                customer.put("customerName", resultSet.getString("name"));
                customer.put("contactDetails", resultSet.getString("phone"));
                customersArray.put(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customersArray;
    }
    
    public JSONObject getProcessedClaimsCount() {
        String query = "SELECT COUNT(*) AS total_claims, SUM(CASE WHEN status = 'approved' THEN 1 ELSE 0 END) AS processed_claims FROM claims";
        JSONObject claimsCount = new JSONObject();

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                claimsCount.put("totalClaims", resultSet.getInt("total_claims"));
                claimsCount.put("processedClaims", resultSet.getInt("processed_claims"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claimsCount;
    }

    public JSONArray getProcessedClaimsDetails() {
        String query = "SELECT policynumber, policyDescription, amount FROM claims WHERE status = 'approved'";
        JSONArray claimsDetails = new JSONArray();

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                JSONObject claim = new JSONObject();
                claim.put("policyNumber", resultSet.getString("policynumber"));
                claim.put("description", resultSet.getString("policyDescription"));
                claim.put("amount", resultSet.getDouble("amount"));
                claimsDetails.put(claim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claimsDetails;
    }

    public JSONObject getPoliciesCount() {
        String query = "SELECT COUNT(DISTINCT p.name) AS used_policies, " +
                       "(SELECT COUNT(*) FROM policies) AS total_policies " +
                       "FROM customer_policies cp " +
                       "JOIN policies p ON cp.policy_number = p.name";
        JSONObject policiesCount = new JSONObject();

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                policiesCount.put("totalPolicies", resultSet.getInt("total_policies"));
                policiesCount.put("usedPolicies", resultSet.getInt("used_policies"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return policiesCount;
    }

    public JSONArray getPoliciesDetails() {
        String query = "SELECT p.name as policy_name, c.name AS customer_name " +
                       "FROM customer_policies cp " +
                       "JOIN policies p ON cp.policy_number = p.name " +
                       "JOIN customers c ON cp.customer_id = c.name";
        JSONArray policiesDetails = new JSONArray();

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                JSONObject policyDetail = new JSONObject();
                policyDetail.put("policyName", resultSet.getString("policy_name"));
                policyDetail.put("customerName", resultSet.getString("customer_name"));
                policiesDetails.put(policyDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return policiesDetails;
    }

    public JSONObject getApprovalRejectionRates() {
        String query = "SELECT COUNT(*) AS total_claims, " +
                       "SUM(CASE WHEN status = 'approved' THEN 1 ELSE 0 END) AS approved_claims, " +
                       "SUM(CASE WHEN status = 'rejected' THEN 1 ELSE 0 END) AS rejected_claims " +
                       "FROM claims";
        JSONObject rates = new JSONObject();

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                rates.put("totalClaims", resultSet.getInt("total_claims"));
                rates.put("approvedClaims", resultSet.getInt("approved_claims"));
                rates.put("rejectedClaims", resultSet.getInt("rejected_claims"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rates;
    }

    public JSONArray getApprovedClaimsDetails() {
        String query = "SELECT policynumber, policyDescription, amount FROM claims WHERE status = 'approved'";
        JSONArray approvedDetails = new JSONArray();

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                JSONObject claim = new JSONObject();
                claim.put("policyNumber", resultSet.getString("policynumber"));
                claim.put("description", resultSet.getString("policyDescription"));
                claim.put("amount", resultSet.getDouble("amount"));
                approvedDetails.put(claim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return approvedDetails;
    }

    public JSONArray getRejectedClaimsDetails() {
        String query = "SELECT policynumber, policyDescription, amount FROM claims WHERE status = 'rejected'";
        JSONArray rejectedDetails = new JSONArray();

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                JSONObject claim = new JSONObject();
                claim.put("policyNumber", resultSet.getString("policynumber"));
                claim.put("description", resultSet.getString("policyDescription"));
                claim.put("amount", resultSet.getDouble("amount"));
                rejectedDetails.put(claim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rejectedDetails;
    }
}
