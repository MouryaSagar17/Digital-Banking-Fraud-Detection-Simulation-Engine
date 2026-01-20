# 🧪 Functional Test Cases: Digital Banking Fraud Detection System

## 📋 Test Suite Overview
**Module:** Transaction Processing & Fraud Detection Engine  
**Objective:** Verify the accuracy of rule-based and ML-driven fraud detection, alert generation, and transaction status management.

---

## 🟢 1. Happy Path Scenarios (Normal Operations)

| Test ID | Scenario Title | Pre-conditions | Step-by-Step Instructions | Test Data | Expected Result | Priority |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC-001** | **Process Valid Low-Risk Transaction** | System is running; Account is active. | 1. Send a POST request to `/api/transactions`.<br>2. Use valid data with low amount and safe location. | `amount: 50.00`, `country: "USA"`, `channel: "WEB"` | Status: `NORMAL` (Success). No alerts generated. | **High** |
| **TC-002** | **Approve Transaction via Analyst Review** | A transaction exists in `PENDING` state. | 1. Login to Dashboard as Admin.<br>2. Navigate to "Alerts Review".<br>3. Locate pending transaction.<br>4. Click "Success" / "Approve". | `txnId: 101` | Status updates to `CLEARED`. Transaction moves to "Success" tab. | **High** |
| **TC-003** | **Generate Normal Traffic Simulation** | Dashboard is accessible. | 1. Go to "Recent Transactions" tab.<br>2. Click "Refresh" button.<br>3. Observe new rows. | N/A | A batch of mixed transactions appears. Majority are `NORMAL` (Green). | **Medium** |

---

## 🔴 2. Negative Scenarios (Error Handling)

| Test ID | Scenario Title | Pre-conditions | Step-by-Step Instructions | Test Data | Expected Result | Priority |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC-004** | **Reject Invalid Transaction Data** | API is up. | 1. Send POST request with missing `customerId`. | `customerId: null`, `amount: 100` | API returns `400 Bad Request`. Error message: "Customer ID is required". | **Medium** |
| **TC-005** | **Reject Negative Amount Transaction** | API is up. | 1. Send POST request with negative amount. | `amount: -50.00` | API returns `400 Bad Request`. Error message: "Transaction amount must be positive". | **Medium** |
| **TC-006** | **Block Transaction from Blacklisted Account** | Account `ACC_BLOCKED_USER` exists. | 1. Send POST request using blocked account ID. | `accountId: "ACC_BLOCKED_USER"` | Status: `BLOCKED_ACCOUNT`. Transaction rejected automatically. | **High** |

---

## ⚠️ 3. Fraud Detection Rules (Business Logic)

| Test ID | Scenario Title | Pre-conditions | Step-by-Step Instructions | Test Data | Expected Result | Priority |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC-007** | **Detect High Amount Fraud** | Rule: Amount > 50,000 is Fraud. | 1. Send transaction with amount 60,000. | `amount: 60000.00` | Status: `FRAUD`. Reason: `HighAmountRule`. Email alert sent. | **High** |
| **TC-008** | **Detect Country Mismatch** | Account country is "USA". | 1. Send transaction from "RUS" (Russia). | `userCountry: "USA"`, `txnCountry: "RUS"` | Status: `PENDING` or `SUSPICIOUS`. Reason: `CountryMismatchRule`. | **Medium** |
| **TC-009** | **Detect Invalid Channel** | Allowed: WEB, MOBILE, POS. | 1. Send transaction with channel "ATM_LEGACY". | `channel: "ATM_LEGACY"` | Status: `SUSPICIOUS`. Reason: `ChannelValidationRule`. | **Medium** |
| **TC-010** | **Detect Rapid Velocity (Flood)** | Velocity Rule enabled. | 1. Send 10 transactions for same account in < 1 sec. | `accountId: "ACC_VELOCITY"` | First few `NORMAL`, subsequent ones `FRAUD` or `SUSPICIOUS` due to `VelocityRule`. | **High** |

---

## 🤖 4. Machine Learning Integration

| Test ID | Scenario Title | Pre-conditions | Step-by-Step Instructions | Test Data | Expected Result | Priority |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC-011** | **ML Escalation of Pending Transaction** | ML Model loaded. | 1. Create transaction that triggers 1 rule (usually Pending).<br>2. Ensure ML inputs predict high fraud probability (>80%). | `ruleRiskScore: 20`, `mlScore: 0.85` | Status escalates from `PENDING` to `FRAUD` due to high ML confidence. | **High** |
| **TC-012** | **ML Confirmation of Safe Transaction** | ML Model loaded. | 1. Create transaction with borderline rule score.<br>2. Ensure ML predicts very low fraud probability (<10%). | `ruleRiskScore: 30`, `mlScore: 0.05` | Status remains `NORMAL` or `PENDING` (does not escalate to Fraud). | **Medium** |

---

## 🛡️ 5. Security & Analyst Workflow

| Test ID | Scenario Title | Pre-conditions | Step-by-Step Instructions | Test Data | Expected Result | Priority |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC-013** | **Block Account for 24 Hours** | Suspicious transaction exists. | 1. Open Dashboard -> Alerts Review.<br>2. Click "Block 24h" on a transaction.<br>3. Confirm dialog. | `txnId: 205` | UI Status updates to `BLOCKED (24h)`. Account is added to temporary block list. | **High** |
| **TC-014** | **Permanent Account Block** | Fraud transaction exists. | 1. Open Dashboard -> Alerts Review.<br>2. Click "Block" (Red button).<br>3. Confirm dialog. | `txnId: 206` | UI Status updates to `BLOCKED`. Future transactions from this account are auto-rejected. | **High** |
| **TC-015** | **Verify Email Alert Delivery** | SMTP configured. | 1. Trigger a `FRAUD` transaction (e.g., High Amount).<br>2. Check configured Admin email inbox. | `amount: 75000` | Email received with Subject: "Important Alert: Suspicious Transaction...". | **Medium** |

---

## 📉 6. Boundary Value Analysis

| Test ID | Scenario Title | Pre-conditions | Step-by-Step Instructions | Test Data | Expected Result | Priority |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC-016** | **Transaction Amount Boundary (Max Safe)** | Threshold is 50,000. | 1. Send transaction with amount 49,999.99. | `amount: 49999.99` | Status: `NORMAL` (assuming no other rules trigger). | **Low** |
| **TC-017** | **Transaction Amount Boundary (Min Fraud)** | Threshold is 50,000. | 1. Send transaction with amount 50,000.01. | `amount: 50000.01` | Status: `FRAUD` (HighAmountRule triggered). | **Low** |
