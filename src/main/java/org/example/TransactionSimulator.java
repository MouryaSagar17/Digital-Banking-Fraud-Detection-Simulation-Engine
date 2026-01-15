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
    private static final String[] CHANNELS = {"WEB", "MOBILE", "POS"}; // Use uppercase to match rules
    private static final String[] COUNTRIES = {"USA", "GBR", "IND", "CAN"};
    private static final String[] MERCHANT_CATEGORIES = {"electronics", "groceries", "apparel", "entertainment"};

    public TransactionSimulator(TransactionClient transactionClient) {
        this.transactionClient = transactionClient;
    }

    public void runSimulation(int transactionCount) throws InterruptedException {
        System.out.println("Starting transaction simulation...");
        for (int i = 0; i < transactionCount; i++) {
            transactionClient.sendTransaction(createMixedTransactionRequest());
            Thread.sleep(100); 
        }
        System.out.println("Transaction simulation finished.");
    }

    private TransactionRequest createMixedTransactionRequest() {
        TransactionRequest request = new TransactionRequest();
        int type = random.nextInt(10); // 0-9

        if (type < 6) { 
            // 60% Normal (Success)
            // We must ensure NO rules are triggered
            request.setCustomerId("CUST_NORMAL_" + random.nextInt(100));
            request.setAccountId("ACC_NORMAL_" + random.nextInt(100));
            request.setTransactionAmount(10 + (200 - 10) * random.nextDouble()); // Low amount
            request.setCountry("USA"); // Safe country
            request.setChannel("WEB"); // Safe channel
            request.setIpAddress("192.168.1." + random.nextInt(255)); // Safe local IP
            request.setMerchantCategory("groceries"); // Safe category
        } else if (type < 8) { 
            // 20% Fraud (High Amount)
            request.setCustomerId("CUST_FRAUD_" + random.nextInt(100));
            request.setAccountId("ACC_FRAUD_" + random.nextInt(100));
            request.setTransactionAmount(50000 + (100000 - 50000) * random.nextDouble());
            request.setCountry(getRandom(COUNTRIES));
            request.setChannel(getRandom(CHANNELS));
            request.setIpAddress(generateRandomIpAddress());
            request.setMerchantCategory(getRandom(MERCHANT_CATEGORIES));
        } else if (type < 9) {
            // 10% Blocked
            request.setCustomerId("CUST_BLOCKED");
            request.setAccountId("ACC_BLOCKED_USER"); 
            request.setTransactionAmount(100.0);
            request.setCountry("USA");
            request.setChannel("WEB");
            request.setIpAddress("10.0.0.1");
            request.setMerchantCategory("retail");
        } else {
            // 10% Pending
            request.setCustomerId("CUST_PENDING");
            request.setAccountId("ACC_PENDING_USER");
            request.setTransactionAmount(200.0);
            request.setCountry("USA");
            request.setChannel("MOBILE");
            request.setIpAddress("10.0.0.2");
            request.setMerchantCategory("dining");
        }

        request.setCurrency("USD"); // Keep currency simple for now
        request.setMerchantId(UUID.randomUUID().toString());

        return request;
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
