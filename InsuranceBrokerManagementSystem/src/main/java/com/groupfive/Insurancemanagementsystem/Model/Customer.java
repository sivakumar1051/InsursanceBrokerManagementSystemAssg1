package com.groupfive.Insurancemanagementsystem.Model;

public class Customer {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String brokerEmail;  // Field to store selected broker's email

    public Customer(String id, String name, String email, String phone, String brokerEmail) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.brokerEmail = brokerEmail;
    }

    public Customer() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBrokerEmail() {
        return brokerEmail;
    }

    public void setBrokerEmail(String brokerEmail) {
        this.brokerEmail = brokerEmail;
    }

    @Override
    public String toString() {
        return String.format("Customer{id='%s', name='%s', email='%s', phone='%s', brokerEmail='%s'}", id, name, email, phone, brokerEmail);
    }
}
