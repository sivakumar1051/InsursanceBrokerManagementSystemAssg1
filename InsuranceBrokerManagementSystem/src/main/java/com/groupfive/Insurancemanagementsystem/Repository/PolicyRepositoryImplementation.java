package com.groupfive.Insurancemanagementsystem.Repository;

import com.groupfive.Insurancemanagementsystem.Model.Customer;
import com.groupfive.Insurancemanagementsystem.Model.Policy;
import com.groupfive.Insurancemanagementsystem.Util.DatabaseUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PolicyRepositoryImplementation implements IPolicyRepository {
    private Connection connection;

    private final ICustomerRepository customerRepository;
    private final IPolicyRepository policyRepository;

    public PolicyRepositoryImplementation(IPolicyRepository policyRepository, ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
		this.policyRepository = policyRepository;
        try {
            this.connection = DatabaseUtility.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void savePolicy(Policy policy) {
        String sql = "INSERT INTO policies (policy_number, name, start_date, end_date, coverage_amount, premium_amount, policy_type) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, policy.getPolicyNumber());
            pstmt.setString(2, policy.getName());
            pstmt.setDate(3, Date.valueOf(policy.getStartDate()));
            pstmt.setDate(4, Date.valueOf(policy.getEndDate()));
            pstmt.setDouble(5, policy.getCoverageAmount());
            pstmt.setDouble(6, policy.getPremiumAmount());
            pstmt.setString(7, policy.getPolicyType());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Policy> findAll() {
        List<Policy> policies = new ArrayList<>();
        String sql = "SELECT * FROM policies";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Policy policy = new Policy();
                policy.setPolicyNumber(rs.getString("policy_number"));
                policy.setName(rs.getString("name"));
                policy.setStartDate(rs.getDate("start_date").toString());
                policy.setEndDate(rs.getDate("end_date").toString());
                policy.setCoverageAmount(rs.getDouble("coverage_amount"));
                policy.setPremiumAmount(rs.getDouble("premium_amount"));
                policy.setPolicyType(rs.getString("policy_type"));
                policies.add(policy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return policies;
    }

    @Override
    public List<Policy> findByIdOrName(String query) {
        List<Policy> matchingPolicies = new ArrayList<>();
        String sql = "SELECT * FROM policies WHERE policy_number = ? OR name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, query);
            pstmt.setString(2, query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Policy policy = new Policy();
                policy.setPolicyNumber(rs.getString("policy_number"));
                policy.setName(rs.getString("name"));
                policy.setStartDate(rs.getDate("start_date").toString());
                policy.setEndDate(rs.getDate("end_date").toString());
                policy.setCoverageAmount(rs.getDouble("coverage_amount"));
                policy.setPremiumAmount(rs.getDouble("premium_amount"));
                policy.setPolicyType(rs.getString("policy_type"));
                matchingPolicies.add(policy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matchingPolicies;
    }

    @Override
    public void updatePolicy(Policy updatedPolicy) {
        String sql = "UPDATE policies SET name = ?, start_date = ?, end_date = ?, coverage_amount = ?, premium_amount = ?, policy_type = ? WHERE policy_number = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, updatedPolicy.getName());
            pstmt.setDate(2, Date.valueOf(updatedPolicy.getStartDate()));
            pstmt.setDate(3, Date.valueOf(updatedPolicy.getEndDate()));
            pstmt.setDouble(4, updatedPolicy.getCoverageAmount());
            pstmt.setDouble(5, updatedPolicy.getPremiumAmount());
            pstmt.setString(6, updatedPolicy.getPolicyType());
            pstmt.setString(7, updatedPolicy.getPolicyNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deletePolicy(String policyNumber) {
        String sql = "DELETE FROM policies WHERE policy_number = ?";
        boolean isDeleted = false;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, policyNumber);
            int rowsAffected = pstmt.executeUpdate();
            isDeleted = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    @Override
    public List<String> getPolicyNames() {
        List<String> policyNames = new ArrayList<>();
        String sql = "SELECT name FROM policies";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                policyNames.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return policyNames;
    }

    @Override
    public List<String> getCustomers() {
        List<Customer> custList = customerRepository.findAllCustomers();
        return custList.stream()
            .map(Customer::getName) 
            .collect(Collectors.toList());
    }

    @Override
    public boolean assignPolicyToCustomer(String customerName, String policyName) {
        // Placeholder for assignPolicyToCustomer logic
        return false;
    }

    // Assign a policy to a customer
    public boolean assignPolicy(String policyNumber, String customerId) {
        String sql = "INSERT INTO customer_policies (customer_id, policy_number) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, customerId);
            pstmt.setString(2, policyNumber);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
