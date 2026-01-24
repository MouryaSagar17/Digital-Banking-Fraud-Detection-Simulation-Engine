package org.example;

import org.example.client.TransactionClient;
import org.example.dto.TransactionRequest;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
public class TransactionSimulator {

    private final TransactionClient transactionClient;
    private static final Random random = new Random();
    private static final String[] CURRENCIES = {"USD", "EUR", "GBP", "INR"};
    private static final String[] CHANNELS = {"WEB", "MOBILE", "POS"};
    private static final String[] COUNTRIES = {"USA", "GBR", "IND", "CAN"};
    private static final String[] MERCHANT_CATEGORIES = {"electronics", "groceries", "apparel", "entertainment"};

    public TransactionSimulator(TransactionClient transactionClient) {
        this.transactionClient = transactionClient;
    }

    public void runSimulation(int transactionCount) throws InterruptedException {
        System.out.println("Starting deterministic transaction simulation...");
        for (int i = 0; i < transactionCount; i++) {
            int type = i % 10; // Cycle 0-9
            TransactionRequest request;

            if (type < 5) { // 5 Success
                request = createSuccessTransaction();
            } else if (type < 7) { // 2 Pending
                request = createPendingTransaction();
            } else if (type < 9) { // 2 Fraud
                request = createFraudTransaction();
            } else { // 1 Failed
                request = createFailedTransaction();
            }

            transactionClient.sendTransaction(request);
            Thread.sleep(100);
        }
        System.out.println("Transaction simulation finished.");
    }

    private TransactionRequest createSuccessTransaction() {
        TransactionRequest request = new TransactionRequest();
        request.setCustomerId("CUST_NORMAL_" + random.nextInt(100));
        request.setAccountId(generateAccountId());
        request.setTransactionAmount(10 + (200 - 10) * random.nextDouble()); // Low amount
        request.setCountry("USA");
        request.setChannel("WEB");
        request.setIpAddress("192.168.1." + random.nextInt(255)); // Safe local IP
        request.setMerchantCategory("groceries");
        request.setCurrency("USD");
        request.setMerchantId(generateAccountId()); // Use generateAccountId for receiver
        return request;
    }

    private TransactionRequest createPendingTransaction() {
        TransactionRequest request = new TransactionRequest();
        request.setCustomerId("CUST_PENDING_" + random.nextInt(100));
        request.setAccountId(generateAccountId());
        
        // Trigger 1 rule: Country Mismatch (User US, Txn BRA)
        request.setTransactionAmount(2500.00); 
        request.setCountry("BRA"); 
        request.setChannel("MOBILE");
        request.setIpAddress("192.168.1.50"); // Safe IP to keep ML score low
        request.setMerchantCategory("travel");
        request.setCurrency("BRL");
        request.setMerchantId(generateAccountId()); // Use generateAccountId for receiver
        return request;
    }

    private TransactionRequest createFraudTransaction() {
        TransactionRequest request = new TransactionRequest();
        request.setCustomerId("CUST_FRAUD_" + random.nextInt(100));
        request.setAccountId(generateAccountId());
        request.setTransactionAmount(60000 + (100000 - 60000) * random.nextDouble()); // High amount
        request.setCountry(getRandom(COUNTRIES));
        request.setChannel(getRandom(CHANNELS));
        request.setIpAddress(generateRandomIpAddress());
        request.setMerchantCategory("luxury");
        request.setCurrency("EUR");
        request.setMerchantId(generateAccountId()); // Use generateAccountId for receiver
        return request;
    }

    private TransactionRequest createFailedTransaction() {
        TransactionRequest request = new TransactionRequest();
        request.setCustomerId("CUST_BLOCKED");
        request.setAccountId("ACC_BLOCKED_USER"); // Fixed ID for blocking logic
        request.setTransactionAmount(100.0);
        request.setCountry("USA");
        request.setChannel("WEB");
        request.setIpAddress("10.0.0.1");
        request.setMerchantCategory("retail");
        request.setCurrency("USD");
        request.setMerchantId(generateAccountId()); // Use generateAccountId for receiver
        return request;
    }

    private String generateAccountId() {
        // Format: ACC + 12 digits
        StringBuilder sb = new StringBuilder("ACC");
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private static String getRandom(String[] array) {
        return array[random.nextInt(array.length)];
    }

    private static String generateRandomIpAddress() {
        return random.nextInt(256) + "." +
               random.nextInt(256) + "." +
               random.nextInt(256) + "." +
               random.nextInt(256);
    }
}
