package org.example;

import java.time.LocalDateTime;

public class Transaction {

    private String customerId;
    private String accountId;
    private double transactionAmount;
    private String currency;
    private LocalDateTime txnTimestamp;
    private String channel;
    private String country;
    private String merchantId;
    private String merchantCategory;
    private String ipAddress;
    private int ipRiskScore;
    private int ruleRiskScore;
    private boolean isFraudLabel;

    public Transaction() {}

    public Transaction(String customerId, String accountId, double transactionAmount,
                       String currency, LocalDateTime txnTimestamp, String channel,
                       String country, String merchantId, String merchantCategory,
                       String ipAddress, int ipRiskScore, int ruleRiskScore,
                       boolean isFraudLabel) {

        this.customerId = customerId;
        this.accountId = accountId;
        this.transactionAmount = transactionAmount;
        this.currency = currency;
        this.txnTimestamp = txnTimestamp;
        this.channel = channel;
        this.country = country;
        this.merchantId = merchantId;
        this.merchantCategory = merchantCategory;
        this.ipAddress = ipAddress;
        this.ipRiskScore = ipRiskScore;
        this.ruleRiskScore = ruleRiskScore;
        this.isFraudLabel = isFraudLabel;
    }

    // Getters and setters
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public double getTransactionAmount() { return transactionAmount; }
    public void setTransactionAmount(double transactionAmount) { this.transactionAmount = transactionAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public LocalDateTime getTxnTimestamp() { return txnTimestamp; }
    public void setTxnTimestamp(LocalDateTime txnTimestamp) { this.txnTimestamp = txnTimestamp; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }

    public String getMerchantCategory() { return merchantCategory; }
    public void setMerchantCategory(String merchantCategory) { this.merchantCategory = merchantCategory; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public int getIpRiskScore() { return ipRiskScore; }
    public void setIpRiskScore(int ipRiskScore) { this.ipRiskScore = ipRiskScore; }

    public int getRuleRiskScore() { return ruleRiskScore; }
    public void setRuleRiskScore(int ruleRiskScore) { this.ruleRiskScore = ruleRiskScore; }

    public boolean isFraudLabel() { return isFraudLabel; }
    public void setFraudLabel(boolean fraudLabel) { isFraudLabel = fraudLabel; }
}
