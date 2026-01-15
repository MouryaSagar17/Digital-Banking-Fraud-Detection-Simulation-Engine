# 📊 Digital Banking Fraud Detection Simulation Engine

A comprehensive, real-time fraud detection system simulating a digital banking environment. It features rule-based and ML-driven fraud detection, a live interactive dashboard, and a complete authentication system.

---

## 🚀 Key Features

### 1. Fraud Detection Engine
*   **Hybrid Detection:** Combines a **Rule Engine** (25+ rules like High Amount, Velocity, IP Risk) with a **Machine Learning Model** (Random Forest) for high accuracy.
*   **Real-time Scoring:** Every transaction is instantly evaluated and assigned a risk score and status (`NORMAL`, `SUSPICIOUS`, `FRAUD`, `PENDING`).
*   **Automated Actions:** High-confidence fraud triggers automatic email alerts to admins.

### 2. Interactive Dashboard
*   **Live Monitoring:** Real-time updates via WebSockets (no page refresh needed).
*   **Visual Analytics:** Charts for fraud distribution and time-series analysis, plus a global threat map.
*   **Recent Transactions:** A paginated, filterable feed of all system activity.
*   **Alerts Review:** A dedicated workflow for analysts to review, confirm, or clear suspicious transactions.
*   **Account Blocking:** Ability to temporarily (24h) or permanently block suspicious accounts directly from the UI.

### 3. Simulation & Control
*   **Traffic Generator:** Built-in simulator to generate realistic traffic patterns (Normal, Fraud, Mixed).
*   **Manual Control:** Trigger simulations on-demand from the dashboard or API.
*   **Scenario Testing:** Pre-configured scenarios to test specific fraud rules (e.g., "High Amount", "Rapid Transactions").

### 4. Security & Authentication
*   **Dual Login:** Support for both **Password-based** and **OTP-based** login (via Email).
*   **User Registration:** Secure sign-up with password strength validation.
*   **Role-Based Access:** Dashboard is protected and requires authentication.

---

## 🛠️ Tech Stack
*   **Backend:** Java 17, Spring Boot 3.3 (Web, Security, Data JPA, Mail, WebSocket)
*   **Database:** MySQL 8.0
*   **Machine Learning:** Weka 3.8 (Random Forest)
*   **Frontend:** HTML5, Bootstrap 5, Chart.js, Leaflet.js, SockJS, Stomp.js
*   **Tools:** Maven, IntelliJ IDEA

---

## ⚙️ Setup Instructions

### 1. Clone & Configure
```bash
git clone https://github.com/MouryaSagar17/Digital-Banking-Fraud-Detection-Simulation-Engine
cd Digital-Banking-Fraud-Detection-Simulation-Engine
```

### 2. Database Setup
Create the database in MySQL:
```sql
CREATE DATABASE fraud_demo;
```
*Note: Tables are automatically created by the application.*

### 3. Application Properties
Update `src/main/resources/application.properties`:
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/fraud_demo
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD

# Email (Required for OTP & Alerts)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL@gmail.com
spring.mail.password=YOUR_APP_PASSWORD
app.admin.email=YOUR_EMAIL@gmail.com
```

### 4. Train ML Model (First Run Only)
Run the `FraudModelTrainer` class to generate the `fraud_rf.model` file:
*   Navigate to `src/main/java/org/example/ml/FraudModelTrainer.java`
*   Right-click -> **Run 'FraudModelTrainer.main()'**

### 5. Run Application
Run the main class: `src/main/java/org/example/FraudDetectionApplication.java`

---

## 🖥️ How to Use

### 1. Login / Register
*   Go to **http://localhost:8080/login**
*   **Register** a new account or use the default admin (created on first run if configured).
*   **Login** using Password or request an OTP to your email.

### 2. Dashboard Overview
*   **Overview Tab:** View live KPIs, charts, and the global risk map.
*   **Recent Transactions:** See a live feed of all transactions. Use the **"Refresh"** button to generate new simulated traffic.
*   **Alerts Review:** Manage suspicious activities.
    *   **Review:** Click a row to see details (ML Score, Rules).
    *   **Action:** Click "Success" to clear or "Block" to suspend the account.

### 3. API Testing
Use the included `api-test.http` file in IntelliJ to run specific test scenarios:
*   `POST /api/simulation/run?count=10` (Generate Mixed Traffic)
*   `POST /api/transactions` (Submit single transaction)
*   `GET /api/alerts` (Fetch active alerts)

---

## 📂 Project Structure
```
src/main/java/org/example
├── auth           # Login, OTP, Email services
├── client         # Transaction generators
├── config         # Security & WebSocket config
├── controller     # REST API endpoints
├── dto            # Data Transfer Objects
├── ml             # Weka ML training & prediction
├── model          # JPA Entities (Transaction, User, BlockedAccount)
├── repository     # Database access
├── service        # Business logic & Rule Engine
│   └── rules      # Individual fraud rules
└── FraudDetectionApplication.java
```
