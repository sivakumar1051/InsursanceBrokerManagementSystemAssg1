CREATE DATABASE insurance;
USE insurance;
CREATE TABLE customer_policies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    policy_number VARCHAR(50) NOT NULL
   
);
CREATE TABLE claims (
    claim_ID VARCHAR(50) UNIQUE NOT NULL,
    policynumber VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
	policyDescription VARCHAR(200) NOT NULL,
    claimDate DATE NOT NULL,
    status ENUM('Pending', 'Approved', 'Rejected') NOT NULL
);

CREATE TABLE customers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    brokerEmail VARCHAR(100)
);
CREATE TABLE policies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    policy_number VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    coverage_amount DOUBLE NOT NULL,
    premium_amount DOUBLE NOT NULL,
    policy_type VARCHAR(50) NOT NULL
);
CREATE TABLE brokers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL
);
ALTER TABLE customer_policies ADD COLUMN status VARCHAR(20) DEFAULT 'active';
CREATE TABLE cancelledPolicies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    policy_name VARCHAR(255) NOT NULL,
    reason TEXT,
    cancellation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    
);
CREATE TABLE Quote (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    customerName VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    policyType VARCHAR(50) NOT NULL,
    coverageAmount DECIMAL(15, 2) NOT NULL,
    termLength INT NOT NULL,
    premium DECIMAL(15, 2) NOT NULL
);

