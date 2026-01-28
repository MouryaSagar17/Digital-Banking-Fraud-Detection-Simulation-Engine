package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private String accountId;

    @Column(nullable = false)
    private double transactionAmount;

    private String currency;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime txnTimestamp;

    private String channel;
    private String country;
    private String merchantId;
    private String merchantCategory;
    private String ipAddress;
    private int ipRiskScore;
    private int ruleRiskScore;
    private boolean isFraudLabel;
    private String status;
    private String riskRuleFlags;

    // New fields for advanced rules
    private String deviceFingerprint;
    private boolean isNewDevice;
    private String userAccountCountry;
    private long timeSinceLastTransaction; // in seconds
    private double dailyTotalAmount;
    private int dailyTransactionCount;
    private boolean isDormant;
    private boolean isBlacklisted;

    // Additional fields for 26 Rules
    private String email;
    private String phoneNumber;
    private String beneficiaryId;
    private int mfaFailures;
    private boolean kycVerified;
    private String accountHolderName;
    private String beneficiaryName; // For name mismatch check (simplified)
    private boolean isTrustedMerchant;
    private boolean isTrustedDevice;

    // ML Fields
    private String mlPrediction;
    private double mlScore;


    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRiskRuleFlags() { return riskRuleFlags; }
    public void setRiskRuleFlags(String riskRuleFlags) { this.riskRuleFlags = riskRuleFlags; }

    public String getDeviceFingerprint() { return deviceFingerprint; }
    public void setDeviceFingerprint(String deviceFingerprint) { this.deviceFingerprint = deviceFingerprint; }

    public boolean isNewDevice() { return isNewDevice; }
    public void setNewDevice(boolean aNew) { isNewDevice = aNew; }

    public String getUserAccountCountry() { return userAccountCountry; }
    public void setUserAccountCountry(String userAccountCountry) { this.userAccountCountry = userAccountCountry; }

    public long getTimeSinceLastTransaction() { return timeSinceLastTransaction; }
    public void setTimeSinceLastTransaction(long timeSinceLastTransaction) { this.timeSinceLastTransaction = timeSinceLastTransaction; }

    public double getDailyTotalAmount() { return dailyTotalAmount; }
    public void setDailyTotalAmount(double dailyTotalAmount) { this.dailyTotalAmount = dailyTotalAmount; }

    public int getDailyTransactionCount() { return dailyTransactionCount; }
    public void setDailyTransactionCount(int dailyTransactionCount) { this.dailyTransactionCount = dailyTransactionCount; }

    public boolean isDormant() { return isDormant; }
    public void setDormant(boolean dormant) { isDormant = dormant; }

    public boolean isBlacklisted() { return isBlacklisted; }
    public void setBlacklisted(boolean blacklisted) { this.isBlacklisted = blacklisted; }

    public String getMlPrediction() { return mlPrediction; }
    public void setMlPrediction(String mlPrediction) { this.mlPrediction = mlPrediction; }

    public double getMlScore() { return mlScore; }
    public void setMlScore(double mlScore) { this.mlScore = mlScore; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getBeneficiaryId() { return beneficiaryId; }
    public void setBeneficiaryId(String beneficiaryId) { this.beneficiaryId = beneficiaryId; }

    public int getMfaFailures() { return mfaFailures; }
    public void setMfaFailures(int mfaFailures) { this.mfaFailures = mfaFailures; }

    public boolean isKycVerified() { return kycVerified; }
    public void setKycVerified(boolean kycVerified) { this.kycVerified = kycVerified; }

    public String getAccountHolderName() { return accountHolderName; }
    public void setAccountHolderName(String accountHolderName) { this.accountHolderName = accountHolderName; }

    public String getBeneficiaryName() { return beneficiaryName; }
    public void setBeneficiaryName(String beneficiaryName) { this.beneficiaryName = beneficiaryName; }

    public boolean isTrustedMerchant() { return isTrustedMerchant; }
    public void setTrustedMerchant(boolean trustedMerchant) { isTrustedMerchant = trustedMerchant; }

    public boolean isTrustedDevice() { return isTrustedDevice; }
    public void setTrustedDevice(boolean trustedDevice) { isTrustedDevice = trustedDevice; }
}
