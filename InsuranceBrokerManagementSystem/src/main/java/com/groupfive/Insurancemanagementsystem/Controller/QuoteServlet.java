package com.groupfive.Insurancemanagementsystem.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import com.groupfive.Insurancemanagementsystem.Model.Quote;
import com.groupfive.Insurancemanagementsystem.Repository.QuoteRepository;

import java.io.IOException;

public class QuoteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters
        String customerName = request.getParameter("customerName");
        int age = Integer.parseInt(request.getParameter("age"));
        String policyType = request.getParameter("policyType");
        double coverageAmount = Double.parseDouble(request.getParameter("coverageAmount"));
        int termLength = Integer.parseInt(request.getParameter("termLength"));

        // Calculate premium based on policy type
        double baseRate = coverageAmount * (getBaseRate(policyType)) / 100;
        double termFactor = getTermFactor(termLength);
        double premium = baseRate * coverageAmount * termFactor;

        // Create a Quote object and save it using the repository
        Quote quote = new Quote(customerName, age, policyType, coverageAmount, termLength, premium);
        
        // Synchronize access to the repository if needed
        boolean isSaved;
        synchronized (this) {
            QuoteRepository quoteRepo = new QuoteRepository();
            isSaved = quoteRepo.saveQuote(quote);
        }

        // Set response type to JSON
        response.setContentType("application/json");

        if (isSaved) {
            JSONObject json = new JSONObject();
            json.put("customerName", customerName);
            json.put("age", age);
            json.put("policyType", policyType);
            json.put("coverageAmount", coverageAmount);
            json.put("termLength", termLength);
            json.put("premium", premium);

            response.getWriter().write(json.toString());
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving quote.");
        }
    }

    // Method to get base rate based on policy type
    private double getBaseRate(String policyType) {
        switch (policyType) {
            case "home": return 0.5;
            case "travel": return 1;
            case "life": return 1.5;
            case "pet": return 1;
            case "auto": return 5;
            case "disability": return 2;
            case "health": return 3;
            default: return 0.0;
        }
    }

    // Method to get term factor based on term length
    private double getTermFactor(int termLength) {
        return (termLength >= 5) ? 0.95 : 1.0;
    }
}
