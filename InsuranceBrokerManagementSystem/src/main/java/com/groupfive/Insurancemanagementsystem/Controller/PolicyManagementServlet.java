package com.groupfive.Insurancemanagementsystem.Controller;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupfive.Insurancemanagementsystem.Model.Policy;
import com.groupfive.Insurancemanagementsystem.Repository.CustomerRepositoryImplementation;
import com.groupfive.Insurancemanagementsystem.Repository.ICustomerRepository;
import com.groupfive.Insurancemanagementsystem.Repository.IPolicyRepository;
import com.groupfive.Insurancemanagementsystem.Repository.PolicyRepositoryImplementation;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/policy-management")
public class PolicyManagementServlet extends HttpServlet {
    private IPolicyRepository policyRepository;
    private ICustomerRepository customerRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Initialize repository with customer repository implementation
        customerRepository = new CustomerRepositoryImplementation(); // Assuming this is implemented elsewhere
        policyRepository = new PolicyRepositoryImplementation(policyRepository, customerRepository);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        String action = request.getParameter("action");
        ObjectMapper objectMapper = new ObjectMapper();

        synchronized (this) {
            if ("assign".equals(action)) {
                // Handling policy assignment to customer
                JSONObject jsonObject = new JSONObject(reader.lines().collect(Collectors.joining()));
                String customerName = jsonObject.getString("customerName");
                String policyName = jsonObject.getString("policyName");

                // Assign policy to customer
                boolean assigned = policyRepository.assignPolicyToCustomer(customerName, policyName);
                if (assigned) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Policy assigned successfully!");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Failed to assign policy.");
                }
                return;
            }

            // Handling policy creation
            Policy policy = objectMapper.readValue(reader, Policy.class);
            List<Policy> existingPolicies = policyRepository.findByIdOrName(policy.getPolicyNumber());

            // Prevent adding a policy that already exists
            if (!existingPolicies.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Policy already exists!");
                return;
            }

            // Save new policy
            policyRepository.savePolicy(policy);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Policy added successfully!");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String query = request.getParameter("query");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        synchronized (this) {
            if ("search".equals(action)) {
                // Search policies by id or name
                List<Policy> policies = policyRepository.findByIdOrName(query);
                out.print(new ObjectMapper().writeValueAsString(policies));
            } else if ("viewAll".equals(action)) {
                // View all policies
                List<Policy> allPolicies = policyRepository.findAll();
                out.print(new ObjectMapper().writeValueAsString(allPolicies));
            } else if ("getCustomers".equals(action)) {
                // Get customer names for dropdown
                List<String> customers = policyRepository.getCustomers();
                JSONArray customerJsonArray = new JSONArray(customers);
                out.print(customerJsonArray.toString());
            } else if ("getPolicies".equals(action)) {
                // Get policy names for dropdown
                List<String> policies = policyRepository.getPolicyNames();
                out.print(new ObjectMapper().writeValueAsString(policies));
            }
            out.flush();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        synchronized (this) {
            // Handle policy updates
            Policy updatedPolicy = new ObjectMapper().readValue(request.getInputStream(), Policy.class);
            policyRepository.updatePolicy(updatedPolicy);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Policy updated successfully.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        synchronized (this) {
            // Handle policy deletion
            String policyNumber = request.getParameter("policyNumber");
            boolean isDeleted = policyRepository.deletePolicy(policyNumber);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (isDeleted) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Policy deleted successfully.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"message\": \"Policy not found.\"}");
            }
        }
    }
}
