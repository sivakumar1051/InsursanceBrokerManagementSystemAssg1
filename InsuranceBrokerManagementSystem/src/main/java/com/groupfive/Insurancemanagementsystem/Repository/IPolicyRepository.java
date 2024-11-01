package com.groupfive.Insurancemanagementsystem.Repository;

import java.util.List;

import com.groupfive.Insurancemanagementsystem.Model.Policy;

public interface IPolicyRepository {

	void savePolicy(Policy policy);
	
	List<Policy> findByIdOrName(String search);
    List<Policy> findAll();
    public void updatePolicy(Policy updatedPolicy);

	boolean deletePolicy(String policyNumber);

	List<String> getCustomers();

	List<String> getPolicyNames();

	boolean assignPolicy(String customerName, String policyName);

}
