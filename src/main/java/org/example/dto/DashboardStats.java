package org.example.dto;

import java.util.Map;

public class DashboardStats {
    private long totalTransactions;
    private double totalVolume;
    private Map<String, Long> riskCounts;
    private Map<String, Double> riskVolumes;
    private Map<String, Map<String, Long>> countryRiskStats;

    // Getters and Setters
    public long getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(long totalTransactions) { this.totalTransactions = totalTransactions; }

    public double getTotalVolume() { return totalVolume; }
    public void setTotalVolume(double totalVolume) { this.totalVolume = totalVolume; }

    public Map<String, Long> getRiskCounts() { return riskCounts; }
    public void setRiskCounts(Map<String, Long> riskCounts) { this.riskCounts = riskCounts; }

    public Map<String, Double> getRiskVolumes() { return riskVolumes; }
    public void setRiskVolumes(Map<String, Double> riskVolumes) { this.riskVolumes = riskVolumes; }

    public Map<String, Map<String, Long>> getCountryRiskStats() { return countryRiskStats; }
    public void setCountryRiskStats(Map<String, Map<String, Long>> countryRiskStats) { this.countryRiskStats = countryRiskStats; }
}
