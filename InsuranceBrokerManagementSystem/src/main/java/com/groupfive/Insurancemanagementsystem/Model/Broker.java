package com.groupfive.Insurancemanagementsystem.Model;

public class Broker {
    private String name;
    private String email;
    private String phone;
    private String password;

    public Broker(String name, String email) {
        this.name = name;
        this.email = email;
       
    }
    
    
 
    

	public Broker(String name, String email, String phone, String password) {
		super();
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.password = password;
	}





	public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }
}