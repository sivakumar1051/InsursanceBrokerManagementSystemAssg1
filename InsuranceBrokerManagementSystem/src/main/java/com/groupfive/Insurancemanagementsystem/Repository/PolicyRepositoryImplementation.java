package com.groupfive.Insurancemanagementsystem.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupfive.Insurancemanagementsystem.Model.Customer;
import com.groupfive.Insurancemanagementsystem.Model.Policy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.Writer;

public  class PolicyRepositoryImplementation implements IPolicyRepository {
    private List<Policy> policies = new ArrayList<>();
    private List<Policy> tempPolicies = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    String custFilePath="C:/JavaData/customer.json";

    private List<String> tempPolicynames = new ArrayList<>();
    private List<String> tempCustomernames = new ArrayList<>();

    

    // Sample storage; consider using a database

    private final String filePath;
    public boolean isDeleted;
	private String customerPoliciesFilePath;
    

    public PolicyRepositoryImplementation(String filePath) {
        this.filePath = filePath;
        this.customerPoliciesFilePath = "C:/JavaData/customerPolicies.json"; // Specify your path

    }

    @Override
    public void savePolicy(Policy policy) {
        List<Policy> policies = findAll();
        policies.add(policy);
        writePoliciesToFile(policies);
    }

    @Override
    public List<Policy> findAll() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), new TypeReference<List<Policy>>() {});
        } catch (IOException e) {
            return new ArrayList<>(); // Return an empty list if the file doesn't exist
        }
    }

    @Override
    public List<Policy> findByIdOrName(String query) {
        List<Policy> allPolicies = findAll();
        List<Policy> matchingPolicies = new ArrayList<>();
        for (Policy policy : allPolicies) {
            if (policy.getPolicyNumber().equalsIgnoreCase(query) || policy.getName().equalsIgnoreCase(query)) {
                matchingPolicies.add(policy);
            }
        }
        return matchingPolicies;
    }

    public void updatePolicy(Policy updatedPolicy) {
        List<Policy> policies = getPolicies();
        boolean updated = false;

        for (int i = 0; i < policies.size(); i++) {
            if (policies.get(i).getPolicyNumber().equals(updatedPolicy.getPolicyNumber())) {
                policies.set(i, updatedPolicy); // Update the existing policy
                updated = true;
                break;
            }
        }

        if (updated) {
            writePoliciesToFile(policies); // Write the updated list back to the file
        }
    }

    private void writePoliciesToFile(List<Policy> policies) {
        try (Writer writer = new BufferedWriter(new FileWriter(filePath))) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(writer, policies);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<Policy> getPolicies() {
        List<Policy> policies = new ArrayList<>();
        try {
            String json = new String(Files.readAllBytes(Paths.get(filePath)));
            ObjectMapper objectMapper = new ObjectMapper();
            Policy[] policyArray = objectMapper.readValue(json, Policy[].class);
            for (Policy policy : policyArray) {
                policies.add(policy);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return policies;
    }
    
    public List<Customer> loadCustomers() {
        try {
            String json = new String(Files.readAllBytes(Paths.get(custFilePath)));
            ObjectMapper objectMapper = new ObjectMapper();
            Customer[] custArray = objectMapper.readValue(json, Customer[].class);
            for (Customer cust : custArray) {
            	customers.add(cust);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customers;
    }
    
    
 // Method to load policies from JSON file
    private void loadPoliciesFromFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Load policies from the JSON file
            Policy[] policyArray = objectMapper.readValue(new File(filePath), Policy[].class);
            for (Policy policy : policyArray) {
                policies.add(policy);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as needed
        }
    }
    
 // Method to save policies to JSON file, replacing existing data
    private void savePoliciesToFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Write the updated policies list to the JSON file, replacing the existing data
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), policies);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as needed
        }
    }
    
    @Override
    public boolean deletePolicy(String policyNumber) {
        System.out.println("Received DELETE request for policy repo number: " + policyNumber);
        
        loadPoliciesFromFile();
        boolean isDeleted = false; // Initialize the deletion flag
        Iterator<Policy> iterator = policies.iterator();
        

        while (iterator.hasNext()) {
            Policy policy = iterator.next();
            System.out.println(policy.getPolicyNumber() + "," + policy.getName());
            tempPolicies.add(policy);
            if (policy.getPolicyNumber().equals(policyNumber)) {
                iterator.remove(); // Remove policy from the list
                System.out.println("Policy removed: " + policy.getPolicyNumber());
                isDeleted = true; // Set flag to true if policy is found and removed
                break; // Exit the loop since we've found and removed the policy
            }
        }
        writePoliciesToFile(policies);
        System.out.println(isDeleted);
        
        return isDeleted; // Return true if a policy was deleted, false otherwise
    }

	public List<String> getCustomers() {
		// TODO Auto-generated method stub
		loadCustomers();
		tempCustomernames = customers.stream().map(e -> e.getName()).collect(Collectors.toList());
		policies.clear();
		return tempCustomernames;
	}

	@Override
	public List<String> getPolicyNames() {
		loadPoliciesFromFile();
		System.out.println(policies.size());
		tempPolicynames = policies.stream().map(e -> e.getName()).collect(Collectors.toList());
		// TODO Auto-generated method stub
		return tempPolicynames;
	}

	@Override
    public boolean assignPolicyToCustomer(String customerName, String policyName) {
        try {
            // Create a map to hold customer and their assigned policies
            Map<String, List<String>> customerPolicies = getCustomerPoliciesMap();

            // Add the policy name to the customer's list
            customerPolicies.computeIfAbsent(customerName, k -> new ArrayList<>()).add(policyName);
            
            // Save the updated map to file
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(customerPoliciesFilePath), customerPolicies);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	private Map<String, List<String>> getCustomerPoliciesMap() {
        Map<String, List<String>> customerPolicies = new HashMap<>();
        try {
            if (new File(customerPoliciesFilePath).exists()) {
                String content = new String(Files.readAllBytes(Paths.get(customerPoliciesFilePath)));
                customerPolicies = new ObjectMapper().readValue(content, HashMap.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customerPolicies;
    }
     
    
    
   

    
    
}

