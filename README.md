# 📊 Digital Banking Fraud Detection Simulation Engine

A real-time, rule-based fraud detection engine with a live simulation dashboard.

---

## Overview

This project provides an end-to-end simulation of a financial fraud detection system. It includes programmatic transaction generation, a robust rule-based evaluation engine, and a live web dashboard to monitor transactions in real-time.

- **Transaction Simulation**: Generate synthetic financial data via a REST API.
- **Real-time Rule Engine**: Each transaction is evaluated against a comprehensive set of 25+ fraud detection rules.
- **Live Dashboard**: A web-based UI visualizes transaction statuses (Normal vs. Fraud) and displays a live feed of incoming transactions using WebSockets.
- **RESTful Control**: The entire system is controlled and monitored via a clean REST API.

---

## Features

- **Comprehensive Rule Engine**: Implements over 25 rules, including high amount, velocity, IP risk, geolocation, and behavioral checks.
- **Live Web Dashboard**: A Chart.js and WebSocket-powered interface to monitor the system in real-time.
- **On-Demand Simulation**: Start and stop the transaction simulation via a dedicated API endpoint.
- **REST API for Control & Retrieval**: Full API suite to process transactions, retrieve data (all, by date, fraud-only), and control the simulation.
- **Input Validation**: Ensures data integrity for all incoming transactions.
- **Clear Status Management**: Transactions are tracked with `PENDING`, `NORMAL`, `SUSPICIOUS`, and `FRAUD` statuses.
- **Detailed API Testing Suite**: An `api-test.http` file is included for easy, one-click testing of all features.

---

## Architecture

```text
+----------------------+      +-------------------------+      +----------------------+
|   API Test Client    |----->|   Transaction Simulator |----->|  Live Web Dashboard  |
|  (api-test.http)     |      |    (Via REST API)       |      |      (index.html)    |
+----------------------+      +-------------------------+      +----------+-----------+
                                                                          | (WebSocket)
                                                                          |
          +---------------------------------------------------------------+
          |
          v
+--------------------+      +----------------------+      +----------------------+
|      REST API      |----->|  Fraud Rule Engine   |----->|       Database       |
|  (Spring Boot)     |      | (25+ Detection Rules)|      |        (MySQL)       |
+--------------------+      +----------------------+      +----------------------+
```

## Requirements
- Java 17+
- Maven
- MySQL

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/MouryaSagar17/Digital-Banking-Fraud-Detection-Simulation-Engine
cd Digital-Banking-Fraud-Detection-Simulation-Engine
```

### 2. Create the MySQL Database
```sql
CREATE DATABASE fraud_demo;
```
*Note: You do not need to create the `transactions` table manually. The application will do it for you.*

### 3. Configure Spring Boot
Open `src/main/resources/application.properties` and set your MySQL username and password:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fraud_demo
spring.datasource.username=root
spring.datasource.password=your_password

# This setting allows the application to create/update the database table automatically
spring.jpa.hibernate.ddl-auto=update
```

## How to Run

### 1. Run the Application
Start the Spring Boot application by running the `main` method in `FraudDetectionApplication.java`.

### 2. View the Live Dashboard
Open your web browser and navigate to:
**[http://localhost:8080](http://localhost:8080)**

The dashboard will load, but no transactions will appear initially.

### 3. Start the Simulation
To generate live data, you need to start the transaction simulator.
- Open the `api-test.http` file in your IDE.
- Find the request block labeled **MANUAL SIMULATION CONTROL**.
- Click the "Run" icon next to `POST http://localhost:8080/api/simulation/run`.

This will generate 10 new transactions, which will appear in real-time on the dashboard.

## API Endpoints

- `POST /api/simulation/run?count={n}`: Generates `n` random transactions.
- `POST /api/transactions`: Submits a single new transaction for processing.
- `GET /api/transactions`: Retrieves all transactions.
- `GET /api/transactions?startDate={...}&endDate={...}`: Retrieves transactions within a date range.
- `GET /api/alerts`: Retrieves only transactions flagged as `SUSPICIOUS` or `FRAUD`.
- `GET /api/transactions/{id}`: Retrieves a single transaction by its ID.

## Testing
The included `api-test.http` file provides a complete suite for testing every feature of the application. Use it to:
- Send valid, invalid, and fraudulent transactions.
- Test all data retrieval endpoints.
- Manually trigger the transaction simulator.
```