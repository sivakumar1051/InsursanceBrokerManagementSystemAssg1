package com.groupfive.Insurancemanagementsystem.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import com.groupfive.Insurancemanagementsystem.Model.Claim;
import com.groupfive.Insurancemanagementsystem.Repository.ClaimsHistoryRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class ClaimsHistoryServlet extends HttpServlet {

    private final ClaimsHistoryRepository claimsRepository = new ClaimsHistoryRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {

            if ("loadClaims".equals(action)) {
                // Synchronized block for loading all claims
                List<Map<String, Object>> claims;
                synchronized (claimsRepository) {
                    claims = claimsRepository.getAllClaimsWithDetails();
                }
                JSONArray claimsArray = convertClaimsToJSON(claims);
                out.print(claimsArray.toString());

            } else if ("filterClaims".equals(action)) {
                String startDate = request.getParameter("startDate");
                String endDate = request.getParameter("endDate");
                String status = request.getParameter("status");

                // Synchronized block for filtered claims retrieval
                List<Map<String, Object>> claims;
                synchronized (claimsRepository) {
                    claims = claimsRepository.getFilteredClaimsWithDetails(startDate, endDate, status);
                }
                JSONArray claimsArray = convertClaimsToJSON(claims);
                out.print(claimsArray.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"error\":\"Error fetching claims data.\"}");
            }
        }
    }

    private JSONArray convertClaimsToJSON(List<Map<String, Object>> claims) {
        JSONArray claimsArray = new JSONArray();
        for (Map<String, Object> claim : claims) {
            JSONObject claimJson = new JSONObject();
            claimJson.put("policyName", claim.get("policyName"));
            claimJson.put("amount", claim.get("amount"));
            claimJson.put("description", claim.get("description"));
            claimJson.put("date", claim.get("date"));
            claimJson.put("status", claim.get("status"));
            claimsArray.put(claimJson);
        }
        return claimsArray;
    }
}
