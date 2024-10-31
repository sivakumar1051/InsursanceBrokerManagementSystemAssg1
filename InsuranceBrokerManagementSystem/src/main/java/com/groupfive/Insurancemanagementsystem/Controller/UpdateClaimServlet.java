package com.groupfive.Insurancemanagementsystem.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.groupfive.Insurancemanagementsystem.Model.Claim;
import com.groupfive.Insurancemanagementsystem.Repository.ClaimRepository;

/**
 * Servlet implementation class UpdateClaimServlet
 */
public class UpdateClaimServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String claimId = request.getParameter("claimIdUpdate");
        String newDescription = request.getParameter("descriptionUpdate");
        String newAmountStr = request.getParameter("amountUpdate");

        // Retrieve the claim by ID
        ClaimRepository claimRepo = new ClaimRepository();
        Claim claim = claimRepo.getClaimByID(claimId);

        // Update claim fields
        if (claim != null) {
            if (!newDescription.isEmpty()) {
                claim.setPolicy_description(newDescription);
            }
            if (newAmountStr != null && !newAmountStr.isEmpty()) {
                double newAmount = Double.parseDouble(newAmountStr);
                claim.setAmount(newAmount);
            }
            claimRepo.updateClaim(claim);  // Save changes
        }

        response.sendRedirect("claims.html");
    }
}
