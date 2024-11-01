package com.groupfive.Insurancemanagementsystem.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.AsyncContext;

import com.groupfive.Insurancemanagementsystem.Model.Claim;
import com.groupfive.Insurancemanagementsystem.Repository.ClaimRepository;

@WebServlet(urlPatterns = "/claimManagement", asyncSupported = true)
public class ClaimManagementServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ClaimRepository claimRepository = new ClaimRepository();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        AsyncContext asyncContext = request.startAsync();
        
        asyncContext.start(() -> {
            try {
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                
                List<Claim> claims = claimRepository.getAllClaims();
                
                StringBuilder jsonResponse = new StringBuilder("[");
                
                for (int i = 0; i < claims.size(); i++) {
                    Claim claim = claims.get(i);
                    jsonResponse.append(String.format(
                        "{\"id\":\"%s\",\"policyNumber\":\"%s\",\"description\":\"%s\",\"amount\":%.2f,\"dateOfClaim\":\"%s\",\"status\":\"%s\"}",
                        claim.getClaimNumber(),
                        claim.getPolicyNumber(),
                        claim.getPolicy_description(),
                        claim.getAmount(),
                        claim.getClaimDate().format(dateFormatter),
                        claim.getStatus()
                    ));
                    
                    if (i < claims.size() - 1) {
                        jsonResponse.append(",");
                    }
                }
                
                jsonResponse.append("]");
                out.print(jsonResponse.toString());
                
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                try (PrintWriter out = response.getWriter()) {
                    out.print("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
                }
            } finally {
                asyncContext.complete();
            }
        });
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        AsyncContext asyncContext = request.startAsync();
        
        asyncContext.start(() -> {
            String searchId = request.getParameter("searchId");
            String updateClaimId = request.getParameter("updateClaimId");
            String statusUpdateId = request.getParameter("claimIdStatus");
            
            response.setContentType("application/json");
            PrintWriter out = null;
            try {
                out = response.getWriter();
                
                if (searchId != null && !searchId.isEmpty()) {
                    Claim claim = claimRepository.getClaimByID(searchId);
                    if (claim != null) {
                        out.print(String.format(
                            "{\"claim\": {\"id\":\"%s\",\"policyNumber\":\"%s\",\"description\":\"%s\",\"amount\":%.2f,\"dateOfClaim\":\"%s\",\"status\":\"%s\"}}",
                            claim.getClaimNumber(), 
                            claim.getPolicyNumber(),
                            claim.getPolicy_description(),
                            claim.getAmount(),
                            claim.getClaimDate().format(dateFormatter),
                            claim.getStatus()
                        ));
                    } else {
                        out.print("{\"claim\": null}");
                    }
                    out.flush();
                    return;
                }
                
                if (updateClaimId != null && !updateClaimId.isEmpty()) {
                    Claim existingClaim = claimRepository.getClaimByID(updateClaimId);
                    String updatedDescription = request.getParameter("description");
                    double updatedAmount = Double.parseDouble(request.getParameter("amount"));
                    
                    existingClaim.setPolicy_description(updatedDescription);
                    existingClaim.setAmount(updatedAmount);
                    
                    claimRepository.updateClaim(existingClaim);
                    
                    out.print("{\"status\": \"success\", \"message\": \"Claim updated successfully\"}");
                    out.flush();
                    return;
                }
                
                if (statusUpdateId != null && !statusUpdateId.isEmpty()) {
                    String newStatus = request.getParameter("status").toUpperCase();
                    Claim existingClaim = claimRepository.getClaimByID(statusUpdateId);
                    
                    if (existingClaim != null) {
                        if (newStatus.equals("APPROVE") || newStatus.equals("REJECT")) {
                            existingClaim.setStatus(newStatus.equals("APPROVE") ? "Approved" : "Rejected");
                            claimRepository.updateClaim(existingClaim);
                            out.print("{\"status\": \"success\", \"message\": \"Claim " + 
                                    (newStatus.equals("APPROVE") ? "approved" : "rejected") + " successfully\"}");
                        } else {
                            out.print("{\"status\": \"error\", \"message\": \"Invalid status\"}");
                        }
                    } else {
                        out.print("{\"status\": \"error\", \"message\": \"Claim not found\"}");
                    }
                    out.flush();
                    return;
                }
                
                try {
                    String claimId = request.getParameter("claimId");
                    String policyNumber = request.getParameter("policyNumber");
                    String description = request.getParameter("description");
                    double amount = Double.parseDouble(request.getParameter("amount"));
                    LocalDate claimDate = LocalDate.parse(request.getParameter("dateOfClaim"), dateFormatter);
                    String status = "Pending";
                    
                    Claim newClaim = new Claim(claimId, policyNumber, amount, description, claimDate, status);
                    claimRepository.addClaim(newClaim);
                    
                    response.sendRedirect("Claims.html");
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out.flush();
                }
                asyncContext.complete();
            }
        });
    }
}
