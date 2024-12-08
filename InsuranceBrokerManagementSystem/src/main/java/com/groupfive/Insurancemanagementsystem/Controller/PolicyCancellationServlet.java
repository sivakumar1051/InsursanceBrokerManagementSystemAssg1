package com.groupfive.Insurancemanagementsystem.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import com.groupfive.Insurancemanagementsystem.Model.Customer;
import com.groupfive.Insurancemanagementsystem.Model.CustomerPolicies;
import com.groupfive.Insurancemanagementsystem.Repository.PolicyCancellationRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class PolicyCancellationServlet extends HttpServlet {

    private final PolicyCancellationRepository policyRepository = new PolicyCancellationRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {

            if ("loadCustomers".equals(action)) {
                // Synchronize customer data loading
                List<Customer> customers;
                synchronized (policyRepository) {
                    customers = policyRepository.getAllCustomers();
                }
                
                JSONArray jsonArray = new JSONArray();
                for (Customer customer : customers) {
                    JSONObject jsonCustomer = new JSONObject();
                    jsonCustomer.put("name", customer.getName());
                    jsonArray.put(jsonCustomer);
                }
                out.print(jsonArray.toString());

            } else if ("fetchPolicies".equals(action)) {
                // Synchronize fetching policies for a customer
                String customerName = request.getParameter("customerName");
                List<CustomerPolicies> policies;
                synchronized (policyRepository) {
                    policies = policyRepository.getPoliciesForCustomer(customerName);
                }

                JSONArray jsonArray = new JSONArray();
                for (CustomerPolicies policy : policies) {
                    JSONObject jsonPolicy = new JSONObject();
                    jsonPolicy.put("customerName", policy.getCustomerName());
                    jsonPolicy.put("policyName", policy.getPolicyName());
                    jsonArray.put(jsonPolicy);
                }
                out.print(jsonArray.toString());

            } else if ("cancelPolicy".equals(action)) {
                // Synchronize policy cancellation
                String customerName = request.getParameter("customerName");
                String policyName = request.getParameter("policyName");
                String reason = request.getParameter("reason");

                boolean isCancelled;
                synchronized (policyRepository) {
                    isCancelled = policyRepository.cancelPolicy(customerName, policyName, reason);
                }

                if (isCancelled) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print("{\"message\": \"Policy canceled successfully.\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"message\": \"Error canceling policy.\"}");
                }
            }
        }
    }
}
