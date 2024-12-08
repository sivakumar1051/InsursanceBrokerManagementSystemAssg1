package com.groupfive.Insurancemanagementsystem.Model;

import java.time.LocalDate;

import javax.persistence.Id;

import org.hibernate.annotations.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Table;


@Entity
@Table(name = "claims")
public class Claim {
	
	
	@Id
    @Column(name = "claim_ID", unique = true)
    private String claim_ID;

    @Column(name = "policyNumber")
    private String policyNumber;

    @Column(name = "claim_date")
    private LocalDate claimDate;  
    
    @Column(name = "policyDescription")
    private String policyDescription;
    

	@Column(name = "amount")
    private double amount;

    @Column(name = "status")
    private String status;

    // Constructor with claimDate parameter
    public Claim(String claim_ID,  String policyNumber, double amount, String policy_description, LocalDate claimDate, String status) {
        this.claim_ID = claim_ID;
        this.policyNumber = policyNumber;
        this.amount = amount;
        this. policyDescription = policy_description;
        this.claimDate = claimDate;  // Properly set claimDate
        this.status = status;
    }
    public Claim() {
        // Default constructor required by JPA/Hibernate
    }
   

    public String getClaimNumber() {
        return claim_ID;
    }

    public void setClaimNumber(String claimID) {
        this.claim_ID = claimID;
    }

    public String getPolicy_description() {
		return policyDescription;
	}



	public void setPolicy_description(String policy_description) {
		this.policyDescription = policy_description;
	}

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
