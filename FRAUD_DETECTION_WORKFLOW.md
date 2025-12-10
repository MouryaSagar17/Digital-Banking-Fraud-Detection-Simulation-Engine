# Digital Banking Fraud Detection Simulation Workflow

This document outlines the architectural flow of the fraud detection engine, from data generation to anomaly detection and alerting.

## 1. Simulation Initialization & Data Generation
The process begins with the simulation of user activity.
*   **Account Pool:** The system initializes with a set of predefined user accounts (e.g., valid Customer IDs and Account IDs).
*   **Random Transaction Generator:** A simulation component (like `RandomTransactionGenerator.java`) acts as the "world." It continuously generates synthetic transactions for these accounts.
    *   *Attributes generated:* Amount, Merchant, Location (IP/Country), Timestamp, Channel (Mobile/Web).
    *   *Behavior:* Most transactions are normal, but the generator occasionally injects specific patterns designed to look suspicious (e.g., high amounts, rapid location changes).

## 2. API Ingestion & Storage
Generated transactions are not processed internally immediately; they mimic real-world external traffic.
*   **API Layer:** The generator sends these transactions via REST API (e.g., `POST /api/transactions`) to the banking system.
*   **Persistence:** The system receives the request and immediately saves the raw transaction details into the Database (MySQL). This ensures a record exists before processing begins.

## 3. The Rule Engine
Once a transaction is successfully stored, the core Fraud Detection logic is triggered.
*   **Predefined Rules Repository:** The system maintains a registry of specific fraud rules. Examples include:
    *   *High Amount Rule:* Transaction exceeds $10,000.
    *   *Velocity Rule:* More than 3 transactions from the same account in 5 minutes.
    *   *Geolocational Anomaly:* Transaction occurs in a different country than the previous one within an impossible timeframe.
    *   *Merchant Blacklist:* Transaction involves a known high-risk merchant category.
*   **Comprehensive Evaluation:** Every single incoming transaction is passed through **ALL** active rules. The system does not stop at the first violation; it aggregates findings from all rules to build a complete risk profile.

## 4. Anomaly Detection & Alerting
Based on the output of the Rule Engine, the system determines the final status of the transaction.
*   **Risk Scoring:** Each rule violation contributes to a risk score (e.g., High Amount = 20 points, Blacklist = 100 points).
*   **Triggering Alerts:**
    *   If a specific "Critical" rule is violated (e.g., Stolen Card List), an immediate **BLOCK** alert is raised.
    *   If the cumulative risk score crosses a threshold (e.g., Score > 50), a **SUSPICIOUS** alert is raised for manual review.
*   **Outcome:** The transaction record in the database is updated with:
    *   `isFraud`: true/false
    *   `riskReason`: "High Velocity, Foreign IP"
    *   `status`: BLOCKED / FLAGGED / CLEARED

## Summary Flowchart
`[Accounts]` -> `[Random Generator]` -> `(HTTP POST)` -> `[API Layer]` -> `[Database]` -> `[Rule Engine]` -> `[Alert System]`
