package com.groupfive.Insurancemanagementsystem.Repository;


import com.groupfive.Insurancemanagementsystem.Model.Policy;
import com.groupfive.Insurancemanagementsystem.Util.DatabaseUtility;

import java.lang.System.Logger.Level;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PolicyRenewalRepository {

    private static final Logger logger = Logger.getLogger(PolicyRenewalRepository.class.getName());

    public List<Policy> getPoliciesExpiringByDate(LocalDate endDate) {
        List<Policy> policies = new ArrayList<>();
        String query = "SELECT * FROM policies WHERE end_date BETWEEN ? AND ?";
        
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDate(1, java.sql.Date.valueOf(endDate));
            statement.setDate(2, java.sql.Date.valueOf(endDate.plusDays(15)));

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                policies.add(mapResultSetToPolicy(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return policies;
    }

    public List<Policy> getExpiredPolicies(LocalDate today) {
        List<Policy> policies = new ArrayList<>();
        String query = "SELECT * FROM policies WHERE end_date < ?";
        
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDate(1, java.sql.Date.valueOf(today));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                policies.add(mapResultSetToPolicy(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return policies;
    }

    private Policy mapResultSetToPolicy(ResultSet resultSet) throws Exception {
        Policy policy = new Policy();
        policy.setPolicyNumber(resultSet.getString("policy_number"));
        policy.setName(resultSet.getString("name"));
        policy.setStartDate(resultSet.getDate("start_date").toLocalDate().toString());
        policy.setEndDate(resultSet.getDate("end_date").toLocalDate().toString());
        policy.setCoverageAmount(resultSet.getDouble("coverage_amount"));
        policy.setPremiumAmount(resultSet.getDouble("premium_amount"));
        policy.setPolicyType(resultSet.getString("policy_type"));
        return policy;
    }
    
    public boolean updatePolicy(String policyNumber, LocalDate newEndDate, double coverageAmount, double premiumAmount) {
        String updateQuery = "UPDATE policies SET end_date = ?, coverage_amount = ?, premium_amount = ? WHERE policy_number = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setDate(1, java.sql.Date.valueOf(newEndDate));
            preparedStatement.setDouble(2, coverageAmount);
            preparedStatement.setDouble(3, premiumAmount);
            preparedStatement.setString(4, policyNumber);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0; // Returns true if at least one row was updated

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

