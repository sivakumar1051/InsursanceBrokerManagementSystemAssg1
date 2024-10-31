package com.groupfive.Insurancemanagementsystem.Repository;

import com.groupfive.Insurancemanagementsystem.Model.Claim;
import com.groupfive.Insurancemanagementsystem.Util.DatabaseUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClaimRepository {
    
    public void addClaim(Claim claim) {
        String query = "INSERT INTO claims (claim_ID, policyNumber, amount, policyDescription, claimDate, status) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, claim.getClaimNumber());
            statement.setString(2, claim.getPolicyNumber());
            statement.setDouble(3, claim.getAmount());
            statement.setString(4, claim.getPolicy_description());
            statement.setDate(5, java.sql.Date.valueOf(claim.getClaimDate()));
            statement.setString(6, claim.getStatus());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateClaim(Claim claim) {
        String query = "UPDATE claims SET policyNumber = ?, amount = ?, policyDescription = ?, claimDate = ?, status = ? WHERE claim_ID = ?";
        
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, claim.getPolicyNumber());
            statement.setDouble(2, claim.getAmount());
            statement.setString(3, claim.getPolicy_description());
            statement.setDate(4, java.sql.Date.valueOf(claim.getClaimDate()));
            statement.setString(5, claim.getStatus());
            statement.setString(6, claim.getClaimNumber());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Claim getClaimByID(String claimNumber) {
        String query = "SELECT * FROM claims WHERE claim_ID = ?";
        
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, claimNumber);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return new Claim(
                    resultSet.getString("claim_ID"),
                    resultSet.getString("policyNumber"),
                    resultSet.getDouble("amount"),
                    resultSet.getString("policyDescription"),
                    resultSet.getDate("claimDate").toLocalDate(),
                    resultSet.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Claim> getAllClaims() {
        List<Claim> claims = new ArrayList<>();
        String query = "SELECT * FROM claims";
        
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                claims.add(new Claim(
                    resultSet.getString("claim_ID"),
                    resultSet.getString("policyNumber"),
                    resultSet.getDouble("amount"),
                    resultSet.getString("policyDescription"),
                    resultSet.getDate("claimDate").toLocalDate(),
                    resultSet.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claims;
    }

    public void deleteClaim(Claim claim) {
        String query = "DELETE FROM claims WHERE claim_ID = ?";
        
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, claim.getClaimNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateClaimStatus(String claimNumber, String status) {
        String query = "UPDATE claims SET status = ? WHERE claim_ID = ?";
        
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, status);
            statement.setString(2, claimNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
