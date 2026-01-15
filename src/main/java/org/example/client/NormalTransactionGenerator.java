package org.example.client;

import org.example.dto.TransactionRequest;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
public class NormalTransactionGenerator {

    private static final Random random = new Random();
    private static final String[] CURRENCIES = {"USD", "EUR", "GBP"};
    private static final String[] CHANNELS = {"WEB", "MOBILE", "POS"};
    private static final String[] SAFE_COUNTRIES = {"USA", "GBR", "CAN", "FRA", "DEU"};
    private static final String[] SAFE_CATEGORIES = {"groceries", "utilities", "dining", "transport", "retail"};

    public TransactionRequest generate() {
        TransactionRequest request = new TransactionRequest();
        
        // Generate consistent IDs to simulate returning customers
        String customerId = "CUST_NORMAL_" + (1000 + random.nextInt(50)); 
        String accountId = "ACC_NORMAL_" + (1000 + random.nextInt(50));

        request.setCustomerId(customerId);
        request.setAccountId(accountId);
        
        // Safe, low amounts (e.g., $5.00 to $150.00)
        double amount = 5.0 + (145.0 * random.nextDouble());
        request.setTransactionAmount(Math.round(amount * 100.0) / 100.0);
        
        request.setCurrency(getRandom(CURRENCIES));
        request.setChannel(getRandom(CHANNELS));
        
        // Always pick a safe country
        request.setCountry(getRandom(SAFE_COUNTRIES));
        
        request.setMerchantId("MERCH_SAFE_" + random.nextInt(100));
        request.setMerchantCategory(getRandom(SAFE_CATEGORIES));
        
        // Generate a safe-looking IP (e.g., 192.168.x.x or 10.0.x.x)
        request.setIpAddress("192.168.1." + random.nextInt(255));

        return request;
    }

    private String getRandom(String[] array) {
        return array[random.nextInt(array.length)];
    }
}
