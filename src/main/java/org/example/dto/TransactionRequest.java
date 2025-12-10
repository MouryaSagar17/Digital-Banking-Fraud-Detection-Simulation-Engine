package org.example.dto;

import java.time.LocalDateTime;

public class TransactionRequest {
    private String customerId;
    private String accountId;
    private double transactionAmount;
    private String currency;
    private String channel;
    private String country;
    private String merchantId;
    private String merchantCategory;
    private String ipAddress;

    // Getters and Setters

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public double getTransactionAmount() { return transactionAmount; }
    public void setTransactionAmount(double transactionAmount) { this.transactionAmount = transactionAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

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
}
