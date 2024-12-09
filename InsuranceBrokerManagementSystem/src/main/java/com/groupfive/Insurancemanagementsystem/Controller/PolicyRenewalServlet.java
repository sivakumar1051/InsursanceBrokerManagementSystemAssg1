package com.groupfive.Insurancemanagementsystem.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import com.groupfive.Insurancemanagementsystem.Model.Policy;
import com.groupfive.Insurancemanagementsystem.Repository.PolicyRenewalRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class PolicyRenewalServlet extends HttpServlet {

    private final PolicyRenewalRepository policyRepository = new PolicyRenewalRepository();
    private final Object lock = new Object(); // Dedicated lock object for synchronization


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        LocalDate endDate = LocalDate.now();

        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");

            if ("expiring".equals(action)) {
                // Synchronize to fetch policies expiring in the next 15 days
                List<Policy> expiringPolicies;
                synchronized (lock) {
                    expiringPolicies = policyRepository.getPoliciesExpiringByDate(endDate);
                }
                JSONArray jsonArray = convertPoliciesToJson(expiringPolicies);
                out.write(jsonArray.toString());

            } else if ("expired".equals(action)) {
                // Synchronize to fetch policies that have already expired
                List<Policy> expiredPolicies;
                synchronized (lock) {
                    expiredPolicies = policyRepository.getExpiredPolicies(endDate);
                }
                JSONArray jsonArray = convertPoliciesToJson(expiredPolicies);
                out.write(jsonArray.toString());

            } else if ("renew".equals(action)) {
                // Handle policy renewal update
                String policyNumber = request.getParameter("policyNumber");
                String newEndDateStr = request.getParameter("endDate");
                String coverageAmountStr = request.getParameter("coverageAmount");
                String premiumAmountStr = request.getParameter("premiumAmount");

                if (policyNumber == null || newEndDateStr == null || coverageAmountStr == null || premiumAmountStr == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
                    return;
                }

                LocalDate newEndDate = LocalDate.parse(newEndDateStr);
                double coverageAmount = Double.parseDouble(coverageAmountStr);
                double premiumAmount = Double.parseDouble(premiumAmountStr);

                boolean isUpdated;
                synchronized (policyRepository) {
                    isUpdated = policyRepository.updatePolicy(policyNumber, newEndDate, coverageAmount, premiumAmount);
                }

                JSONObject responseJson = new JSONObject();
                if (isUpdated) {
                    responseJson.put("status", "success");
                    responseJson.put("message", "Policy renewed successfully.");
                } else {
                    responseJson.put("status", "failure");
                    responseJson.put("message", "Failed to renew policy.");
                }
                out.write(responseJson.toString());

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing policies.");
        }
    }

    private JSONArray convertPoliciesToJson(List<Policy> policies) {
        JSONArray jsonArray = new JSONArray();
        for (Policy policy : policies) {
            JSONObject policyJson = new JSONObject();
            policyJson.put("policyNumber", policy.getPolicyNumber());
            policyJson.put("name", policy.getName());
            policyJson.put("startDate", policy.getStartDate().toString());
            policyJson.put("endDate", policy.getEndDate().toString());
            policyJson.put("coverageAmount", policy.getCoverageAmount());
            policyJson.put("premiumAmount", policy.getPremiumAmount());
            policyJson.put("policyType", policy.getPolicyType());
            policyJson.put("action", "Expired");
            jsonArray.put(policyJson);
        }
        return jsonArray;
    }
}
