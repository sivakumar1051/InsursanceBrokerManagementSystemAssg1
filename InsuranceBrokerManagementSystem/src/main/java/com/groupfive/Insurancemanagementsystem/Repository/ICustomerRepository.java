package com.groupfive.Insurancemanagementsystem.Repository;

import java.util.List;
import com.groupfive.Insurancemanagementsystem.Model.Customer;

public interface ICustomerRepository {
    void addCustomer(Customer customer);
    Customer findCustomerById(String id);
    List<Customer> findAllCustomers();
    void updateCustomer(List<Customer> customers,String custId,Customer updCustomer);
	void saveAllCustomers(List<Customer> customers);
	void deleteCustomer(String id);
}