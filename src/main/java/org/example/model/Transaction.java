package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerId;
    private String accountId;
    private Double transactionAmount;
    private String currency;
    private LocalDateTime txnTimestamp;
    private String channel;
    private String country;
    private String merchantId;
    private String merchantCategory;
    private String ipAddress;
    private Integer ipRiskScore;
    private Integer ruleRiskScore;
    private String status;
    private Boolean isFraudLabel;

    public Transaction() {
    }

    // getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getIsFraudLabel() { return isFraudLabel; }
    public void setIsFraudLabel(Boolean fraudLabel) { isFraudLabel = fraudLabel; }
}
