package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class TransactionSimulator {

    private static final String URL = "jdbc:mysql://localhost:3306/fraud_demo";
    private static final String USER = "root";
    private static final String PASSWORD = "Sagar@#707";

    private static final Random random = new Random();

    public static void main(String[] args) {
        int totalTxns = 50; // change to 1 if you only want a single transaction

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            for (int i = 0; i < totalTxns; i++) {
                boolean isFraud = random.nextDouble() < 0.15; // 15% fraud rate
                insertRandomTransaction(conn, isFraud);
            }
            System.out.println("Inserted " + totalTxns + " simulated transactions.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertRandomTransaction(Connection conn, boolean isFraud) throws Exception {
        // Simple random IDs
        String customerId = "CUST" + (1000 + random.nextInt(50));
        String accountId = "ACC" + (2000 + random.nextInt(100));
        String merchantId = "M" + (3000 + random.nextInt(100));

        // Base profile for the customer
        double avgTxnAmount = 2000 + random.nextInt(8000);  // 2000–10000
        double typicalMin = avgTxnAmount * 0.4;
        double typicalMax = avgTxnAmount * 1.6;

        // Time info
        LocalDateTime now = LocalDateTime.now().minusMinutes(random.nextInt(60 * 24));
        int dayOfWeek = now.getDayOfWeek().getValue();
        int timeOfDayMin = now.getHour() * 60 + now.getMinute();

        // Normal vs fraud logic
        double amount;
        String country;
        String region;
        double distanceFromLastKm;
        boolean isNewDevice;
        int ipRiskScore;
        boolean deviationLocation;
        boolean deviationTime;
        int highValueTxn1h;
        boolean blacklistHit;
        int ruleRiskScore;
        String status;
        String riskFlags;

        if (!isFraud) {
            // Normal transaction
            amount = clamp(
                    random.nextDouble() * (typicalMax - typicalMin) + typicalMin,
                    100,
                    30000
            );
            country = "IN";
            region = "KA"; // example
            distanceFromLastKm = random.nextDouble() * 5.0; // close by
            isNewDevice = random.nextDouble() < 0.05;
            ipRiskScore = random.nextInt(30); // low
            deviationLocation = false;
            deviationTime = false;
            highValueTxn1h = random.nextDouble() < 0.1 ? 1 : 0;
            blacklistHit = false;
            ruleRiskScore = random.nextInt(30);
            status = "AUTHORIZED";
            riskFlags = "NORMAL";
        } else {
            // Fraud transaction
            amount = random.nextBoolean()
                    ? 40000 + random.nextDouble() * 100000 // very high
                    : 1 + random.nextInt(200);             // strange small amount
            country = randomCountryForFraud();
            region = "UNKNOWN";
            distanceFromLastKm = 500 + random.nextDouble() * 5000; // far away
            isNewDevice = true;
            ipRiskScore = 70 + random.nextInt(30); // high
            deviationLocation = true;
            deviationTime = random.nextBoolean();
            highValueTxn1h = 1 + random.nextInt(5);
            blacklistHit = random.nextDouble() < 0.3;
            ruleRiskScore = 70 + random.nextInt(30);
            status = random.nextDouble() < 0.6 ? "DECLINED" : "AUTHORIZED";
            riskFlags = "SUSPECT;ANOMALY;RULE_HIT";
        }

        // Engineered features
        double deviationAmount = Math.abs(amount - avgTxnAmount);
        boolean isRoundAmount = (Math.round(amount) == amount) &&
                ((long) amount) % 100 == 0;
        boolean isRecurring = random.nextDouble() < 0.2;
        boolean isSplitPayment = random.nextDouble() < (isFraud ? 0.3 : 0.05);

        int txnCount24h = isFraud
                ? 5 + random.nextInt(10)
                : 1 + random.nextInt(5);
        double txnAmount24h = txnCount24h * avgTxnAmount * (isFraud ? 1.5 : 0.8);

        int accountAgeDays = 30 + random.nextInt(2000);
        int prevFraudFlags = isFraud ? random.nextInt(3) : random.nextInt(1);
        int chargebackCount = isFraud ? random.nextInt(5) : random.nextInt(2);
        String creditScoreBand = isFraud ? "C" : (random.nextBoolean() ? "A" : "B");

        String deviceId = "DEV" + (5000 + random.nextInt(500));
        String deviceOs = randomOS();
        String deviceChannel = randomChannel();
        String channel = deviceChannel;

        String ipAddress = randomIp();
        int failedLogin1h = isFraud ? random.nextInt(6) : random.nextInt(2);
        int sessionDurationSec = (int) (30 + random.nextInt(900));
        boolean isFraudLabel = isFraud;

        String sql = "INSERT INTO transactions (" +
                "customer_id, account_id, transaction_amount, currency, txn_timestamp," +
                "day_of_week, time_of_day_min, merchant_id, merchant_category, terminal_type," +
                "channel, status, risk_rule_flags," +
                "account_type, product_type, avg_spend_daily, typical_amount_min, typical_amount_max," +
                "txn_count_24h, txn_amount_24h, account_age_days, prev_fraud_flags, chargeback_count, credit_score_band," +
                "country, region, distance_from_last_km, device_id, device_os, device_channel, ip_address, ip_risk_score," +
                "is_new_device, failed_login_1h, session_duration_sec, deviation_amount, deviation_location, deviation_time," +
                "high_value_txn_1h, is_recurring, is_round_amount, is_split_payment, rule_risk_score, blacklist_hit, is_fraud_label" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            ps.setString(idx++, customerId);
            ps.setString(idx++, accountId);
            ps.setDouble(idx++, round2(amount));
            ps.setString(idx++, "INR");
            ps.setTimestamp(idx++, java.sql.Timestamp.valueOf(now.truncatedTo(ChronoUnit.SECONDS)));

            ps.setInt(idx++, dayOfWeek);
            ps.setInt(idx++, timeOfDayMin);
            ps.setString(idx++, merchantId);
            ps.setString(idx++, randomMerchantCategory());
            ps.setString(idx++, randomTerminalType());
            ps.setString(idx++, channel);
            ps.setString(idx++, status);
            ps.setString(idx++, riskFlags);

            ps.setString(idx++, "SAVINGS");
            ps.setString(idx++, "DEBIT_CARD");
            ps.setDouble(idx++, round2(avgTxnAmount));
            ps.setDouble(idx++, round2(typicalMin));
            ps.setDouble(idx++, round2(typicalMax));

            ps.setInt(idx++, txnCount24h);
            ps.setDouble(idx++, round2(txnAmount24h));
            ps.setInt(idx++, accountAgeDays);
            ps.setInt(idx++, prevFraudFlags);
            ps.setInt(idx++, chargebackCount);
            ps.setString(idx++, creditScoreBand);

            ps.setString(idx++, country);
            ps.setString(idx++, region);
            ps.setDouble(idx++, round2(distanceFromLastKm));
            ps.setString(idx++, deviceId);
            ps.setString(idx++, deviceOs);
            ps.setString(idx++, deviceChannel);
            ps.setString(idx++, ipAddress);
            ps.setInt(idx++, ipRiskScore);

            ps.setBoolean(idx++, isNewDevice);
            ps.setInt(idx++, failedLogin1h);
            ps.setInt(idx++, sessionDurationSec);
            ps.setDouble(idx++, round2(deviationAmount));
            ps.setBoolean(idx++, deviationLocation);
            ps.setBoolean(idx++, deviationTime);

            ps.setInt(idx++, highValueTxn1h);
            ps.setBoolean(idx++, isRecurring);
            ps.setBoolean(idx++, isRoundAmount);
            ps.setBoolean(idx++, isSplitPayment);
            ps.setInt(idx++, ruleRiskScore);
            ps.setBoolean(idx++, blacklistHit);
            ps.setBoolean(idx++, isFraudLabel);

            ps.executeUpdate();
        }
    }

    private static String randomCountryForFraud() {
        String[] badCountries = {"US", "GB", "RU", "NG", "CN", "BR", "DE"};
        return badCountries[random.nextInt(badCountries.length)];
    }

    private static String randomOS() {
        String[] os = {"Android", "iOS", "Windows", "Linux"};
        return os[random.nextInt(os.length)];
    }

    private static String randomChannel() {
        String[] channels = {"WEB", "MOBILE_APP", "CARD_PRESENT", "UPI", "WALLET"};
        return channels[random.nextInt(channels.length)];
    }

    private static String randomMerchantCategory() {
        String[] mcc = {"GROCERY", "ELECTRONICS", "TRAVEL", "FOOD", "FUEL", "ONLINE_SERVICES"};
        return mcc[random.nextInt(mcc.length)];
    }

    private static String randomTerminalType() {
        String[] tt = {"POS", "ATM", "ONLINE", "MOBILE"};
        return tt[random.nextInt(tt.length)];
    }

    private static String randomIp() {
        return random.nextInt(256) + "." +
                random.nextInt(256) + "." +
                random.nextInt(256) + "." +
                random.nextInt(256);
    }

    private static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
