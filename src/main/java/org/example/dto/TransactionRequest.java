package org.example.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class TransactionRequest {

    @NotBlank
    private String customerId;

    @NotBlank
    private String accountId;

    @NotNull
    @Positive
    private Double transactionAmount;

    @NotBlank
    @Size(min = 3, max = 3)
    private String currency;        // "INR"

    @NotNull
    private LocalDateTime txnTimestamp;

    @NotBlank
    private String channel;         // WEB, MOBILE_APP, UPI...

    @NotBlank
    private String country;

    private String merchantId;
    private String merchantCategory;
    private String ipAddress;

    @Min(0) @Max(100)
    private Integer ipRiskScore;

    @Min(0) @Max(100)
    private Integer ruleRiskScore;

    private Boolean isFraudLabel;

    // getters and setters
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public Double getTransactionAmount() { return transactionAmount; }
    public void setTransactionAmount(Double transactionAmount) { this.transactionAmount = transactionAmount; }

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

    public Integer getIpRiskScore() { return ipRiskScore; }
    public void setIpRiskScore(Integer ipRiskScore) { this.ipRiskScore = ipRiskScore; }

    public Integer getRuleRiskScore() { return ruleRiskScore; }
    public void setRuleRiskScore(Integer ruleRiskScore) { this.ruleRiskScore = ruleRiskScore; }

    public Boolean getIsFraudLabel() { return isFraudLabel; }
    public void setIsFraudLabel(Boolean fraudLabel) { isFraudLabel = fraudLabel; }
}
