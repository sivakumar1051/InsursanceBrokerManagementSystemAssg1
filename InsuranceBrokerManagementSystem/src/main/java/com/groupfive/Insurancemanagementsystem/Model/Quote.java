package com.groupfive.Insurancemanagementsystem.Model;

public class Quote {
    private String customerName;
    private int age;
    private String policyType;
    private double coverageAmount;
    private int termLength;
    private double premium;

    public Quote(String customerName, int age, String policyType, double coverageAmount, int termLength, double premium) {
        this.customerName = customerName;
        this.age = age;
        this.policyType = policyType;
        this.coverageAmount = coverageAmount;
        this.termLength = termLength;
        this.premium = premium;
    }

    // Getters and setters for all fields
    public String getCustomerName() { return customerName; }
    public int getAge() { return age; }
    public String getPolicyType() { return policyType; }
    public double getCoverageAmount() { return coverageAmount; }
    public int getTermLength() { return termLength; }
    public double getPremium() { return premium; }
    
    public void setPremium(double premium) { this.premium = premium; }
}
