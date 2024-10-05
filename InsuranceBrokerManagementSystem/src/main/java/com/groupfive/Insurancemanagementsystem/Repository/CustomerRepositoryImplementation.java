package com.groupfive.Insurancemanagementsystem.Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.groupfive.Insurancemanagementsystem.Model.Customer;

public class CustomerRepositoryImplementation implements ICustomerRepository {
    private static final String JSON_FILE_PATH = "C:\\JavaData\\customers.json";

    @Override
    public void addCustomer(Customer customer) {
        List<Customer> customers = findAllCustomers();
        customers.add(customer);
        saveAllCustomers(customers);
    }

    @Override
    public Customer findCustomerById(String id) {
        List<Customer> customers = findAllCustomers();
        return customers.stream()
                        .filter(customer -> customer.getId().equals(id))
                        .findFirst()
                        .orElse(null);
    }

    @Override
    public List<Customer> findAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        File file = new File(JSON_FILE_PATH);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String jsonString = reader.lines().reduce("", String::concat);
                if (jsonString.startsWith("[") && jsonString.endsWith("]")) {
                    String[] customerArray = jsonString.substring(1, jsonString.length() - 1).trim().split("},\\s*\\{");
                    for (String customerData : customerArray) {
                        customerData = customerData.replaceAll("[{}]", "").trim();
                        String[] attributes = customerData.split(",\\s*");
                        String id = null, name = null, email = null, phone = null;
                        for (String attribute : attributes) {
                            String[] keyValue = attribute.split(":");
                            if (keyValue.length == 2) {
                                String key = keyValue[0].replaceAll("\"", "").trim();
                                String value = keyValue[1].replaceAll("\"", "").trim();
                                switch (key) {
                                    case "id": id = value; break;
                                    case "name": name = value; break;
                                    case "email": email = value; break;
                                    case "phone": phone = value; break;
                                }
                            }
                        }
                        customers.add(new Customer(id, name, email, phone));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return customers;
    }

    public void saveAllCustomers(List<Customer> customers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(JSON_FILE_PATH))) {
            writer.write("[");
            for (int i = 0; i < customers.size(); i++) {
                Customer customer = customers.get(i);
                writer.write(String.format(
                    "{\"id\":\"%s\",\"name\":\"%s\",\"email\":\"%s\",\"phone\":\"%s\"}",
                    customer.getId(), customer.getName(), customer.getEmail(), customer.getPhone()
                ));
                if (i < customers.size() - 1) {
                    writer.write(",");
                }
            }
            writer.write("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCustomer(List<Customer> customers, String custId, Customer updCustomer) {
        for (Customer customer : customers) {
            if (customer.getId().equals(custId)) {
                customer.setEmail(updCustomer.getEmail());
                customer.setName(updCustomer.getName());
                customer.setPhone(updCustomer.getPhone());
                break;
            }
        }
        saveAllCustomers(customers);
    }

    @Override
    public void deleteCustomer(String id) {
        List<Customer> customers = findAllCustomers();
        customers.removeIf(customer -> customer.getId().equals(id));
        if (customers.isEmpty()) {
            File file = new File(JSON_FILE_PATH);
            file.delete();
        } else {
            saveAllCustomers(customers);
        }
    }
}
