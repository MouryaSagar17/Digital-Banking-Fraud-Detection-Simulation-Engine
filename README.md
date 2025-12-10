# 📊 Fraud Detection Pipeline

A simple REST API + MySQL workflow for simulating and validating financial transactions.

---

## Overview

This project demonstrates an end-to-end fraud detection flow:

- A transaction is generated  
- Sent to a REST API  
- Validated using rule checks  
- Stored in MySQL if valid  

Designed for learning:

- Backend development with Spring Boot  
- API validation  
- Fraud rule simulation  

---

## Features

- Generate sample transactions  
- REST API to receive and validate data  
- Field-level and rule-based validation  
- Store valid transactions in MySQL  
- Clear validation errors  
- IntelliJ HTTP Client support  
- Clean and readable structure  

---

## Architecture

```text
          +---------------------------+
          |   Transaction Generator   |
          |       (Java Program)      |
          +-------------+-------------+
                        |
                        | POST /api/transactions
                        v
                +--------------------+
                |      REST API      |
                |  Spring Boot App   |
                +---------+----------+
                          |
                          | Validation + Rules
                          v
                +----------------------+
                |      Database        |
                |        MySQL         |
                +----------------------+
```
## Requirements
Java 24

Spring Boot 3.3

Maven

MySQL

IntelliJ IDEA

## Setup Instructions

### 1. Clone the repository
``` bash

git clone https://github.com/MouryaSagar17/Digital-Banking-Fraud-Detection-Simulation-Engine
cd Fraud_Detection
``` 
### 2. Create the MySQL database

``` sql
CREATE DATABASE fraud_demo;

USE fraud_demo;

CREATE TABLE transactions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_id VARCHAR(50),
  account_id VARCHAR(50),
  transaction_amount DECIMAL(12,2),
  currency CHAR(3),
  txn_timestamp DATETIME,
  channel VARCHAR(20),
  country VARCHAR(50),
  merchant_id VARCHAR(50),
  merchant_category VARCHAR(50),
  ip_address VARCHAR(45),
  ip_risk_score INT,
  rule_risk_score INT,
  status VARCHAR(20),
  is_fraud_label BOOLEAN,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```
### 3. Configure Spring Boot
``` properties

spring.datasource.url=jdbc:mysql://localhost:3306/fraud_demo
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=none
``` 
### 4. Run the application
``` bash

mvn spring-boot:run
API endpoint:
``` 

``` text
http://localhost:8080
``` 
## Testing with IntelliJ HTTP Client
Create api-test.http:

``` http

POST http://localhost:8080/api/transactions
```
Content-Type: application/json
```
{
  "customerId": "CUST1001",
  "accountId": "ACC2001",
  "transactionAmount": 1500.75,
  "currency": "INR",
  "txnTimestamp": "2025-12-06T12:15:00",
  "channel": "WEB",
  "country": "IN",
  "merchantId": "M3001",
  "merchantCategory": "GROCERY",
  "ipAddress": "192.168.1.10",
  "ipRiskScore": 20,
  "ruleRiskScore": 25,
  "isFraudLabel": false
}
```
## Error Handling & Validation Techniques
### Validation Types
```
Required fields

Format validation

Pattern rules

Allowed channel values

Amount range checks

Risk score thresholds
```

### Example Errors
``` 
Problem	Message
Missing field	customerId must not be blank
Illegal amount	Transaction amount is invalid
Wrong channel	Invalid channel
Bad ID format	Customer ID must start with CUST
``` 
## Project Structure
``` text

src/main/java
 ├── controller          → REST API endpoint
 ├── service             → Validation and rule logic
 ├── entity              → Transaction model
 ├── repository          → Spring Data JPA
 └── client              → Transaction generator
```
## Milestones

``` ### Milestone 1
Project setup

Database design

API creation

Core validation rules
``` 

``` ### Milestone 2
Transaction generator

Simulation testing

Extended error handling

Documentation cleanup

``` 
