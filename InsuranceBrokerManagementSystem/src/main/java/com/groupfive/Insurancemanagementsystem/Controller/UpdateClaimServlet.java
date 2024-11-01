package com.groupfive.Insurancemanagementsystem.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.AsyncContext;
import java.io.IOException;

import com.groupfive.Insurancemanagementsystem.Model.Claim;
import com.groupfive.Insurancemanagementsystem.Repository.ClaimRepository;

@WebServlet(urlPatterns = "/updateClaim", asyncSupported = true)
public class UpdateClaimServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AsyncContext asyncContext = request.startAsync();
        
        asyncContext.start(() -> {
            try {
                String claimId = request.getParameter("claimIdUpdate");
                String newDescription = request.getParameter("descriptionUpdate");
                String newAmountStr = request.getParameter("amountUpdate");

                ClaimRepository claimRepo = new ClaimRepository();
                Claim claim = claimRepo.getClaimByID(claimId);

                if (claim != null) {
                    if (newDescription != null && !newDescription.isEmpty()) {
                        claim.setPolicy_description(newDescription);
                    }
                    if (newAmountStr != null && !newAmountStr.isEmpty()) {
                        double newAmount = Double.parseDouble(newAmountStr);
                        claim.setAmount(newAmount);
                    }
                    claimRepo.updateClaim(claim);
                }

                response.sendRedirect("claims.html");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                asyncContext.complete();
            }
        });
    }
}
