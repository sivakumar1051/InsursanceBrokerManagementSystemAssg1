package com.groupfive.Insurancemanagementsystem.Repository;

import com.groupfive.Insurancemanagementsystem.Util.DatabaseUtility;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClaimsHistoryRepository {

    // Retrieve all claims with policy names and descriptions
    public List<Map<String, Object>> getAllClaimsWithDetails() throws Exception {
        List<Map<String, Object>> claims = new ArrayList<>();
        String query = """
            SELECT c.policynumber, c.amount, c.policyDescription, c.claimDate, c.status, p.name AS policyName
            FROM claims c
            JOIN policies p ON c.policynumber = p.policy_number
            """;

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> claimData = new HashMap<>();
                claimData.put("policyName", rs.getString("policyName"));
                claimData.put("amount", rs.getDouble("amount"));
                claimData.put("description", rs.getString("policyDescription"));
                claimData.put("date", rs.getDate("claimDate"));
                claimData.put("status", rs.getString("status"));
                claims.add(claimData);
            }
        }
        return claims;
    }

    // Retrieve filtered claims based on date range and status, with policy names and descriptions
    public List<Map<String, Object>> getFilteredClaimsWithDetails(String startDate, String endDate, String status) throws Exception {
        List<Map<String, Object>> claims = new ArrayList<>();
        StringBuilder query = new StringBuilder("""
            SELECT c.policynumber, c.amount, c.policyDescription, c.claimDate, c.status, p.name AS policyName
            FROM claims c
            JOIN policies p ON c.policynumber = p.policy_number
            WHERE 1=1
            """);

        if (startDate != null && !startDate.isEmpty()) query.append(" AND c.claimDate >= ?");
        if (endDate != null && !endDate.isEmpty()) query.append(" AND c.claimDate <= ?");
        if (status != null && !"all".equals(status)) query.append(" AND c.status = ?");

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;
            if (startDate != null && !startDate.isEmpty()) stmt.setString(paramIndex++, startDate);
            if (endDate != null && !endDate.isEmpty()) stmt.setString(paramIndex++, endDate);
            if (status != null && !"all".equals(status)) stmt.setString(paramIndex, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> claimData = new HashMap<>();
                    claimData.put("policyName", rs.getString("policyName"));
                    claimData.put("amount", rs.getDouble("amount"));
                    claimData.put("description", rs.getString("policyDescription"));
                    claimData.put("date", rs.getDate("claimDate"));
                    claimData.put("status", rs.getString("status"));
                    claims.add(claimData);
                }
            }
        }
        return claims;
    }
}
