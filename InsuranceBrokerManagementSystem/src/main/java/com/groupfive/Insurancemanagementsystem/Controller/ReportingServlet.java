package com.groupfive.Insurancemanagementsystem.Controller;

import com.groupfive.Insurancemanagementsystem.Repository.ReportsRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import org.json.JSONObject;
import org.json.JSONArray;

@WebServlet("/reporting")
public class ReportingServlet extends HttpServlet {

    private ReportsRepository reportsRepository = new ReportsRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        JSONObject reportData = new JSONObject();

        switch (action) {
            case "customerReport":
                JSONArray brokersData = reportsRepository.getBrokerCustomerCounts();
                reportData.put("brokers", brokersData);
                break;

            case "customerDetails":
                String brokerEmail = request.getParameter("brokerEmail");
                JSONArray customersData = reportsRepository.getCustomersByBroker(brokerEmail);
                reportData.put("customers", customersData);
                break;

            case "claimsReport":
                JSONObject claimsCount = reportsRepository.getProcessedClaimsCount();
                reportData.put("claimsCount", claimsCount);
                break;

            case "claimsDetails":
                JSONArray claimsDetails = reportsRepository.getProcessedClaimsDetails();
                reportData.put("claimsDetails", claimsDetails);
                break;

            case "policiesReport":
                JSONObject policiesCount = reportsRepository.getPoliciesCount();
                reportData.put("policiesCount", policiesCount);
                break;

            case "policiesDetails":
                JSONArray policiesDetails = reportsRepository.getPoliciesDetails();
                reportData.put("policiesDetails", policiesDetails);
                break;

            case "approvalRejectionRates":
                JSONObject approvalRejection = reportsRepository.getApprovalRejectionRates();
                reportData.put("approvalRejection", approvalRejection);
                break;

            case "approvedClaimsDetails":
                JSONArray approvedClaims = reportsRepository.getApprovedClaimsDetails();
                reportData.put("approvedClaims", approvedClaims);
                break;

            case "rejectedClaimsDetails":
                JSONArray rejectedClaims = reportsRepository.getRejectedClaimsDetails();
                reportData.put("rejectedClaims", rejectedClaims);
                break;

            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                reportData.put("error", "Invalid action specified.");
        }

        response.setContentType("application/json");
        response.getWriter().write(reportData.toString());
    }
}
