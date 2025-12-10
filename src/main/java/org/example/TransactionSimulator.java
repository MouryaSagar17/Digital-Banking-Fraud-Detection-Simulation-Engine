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
    private static final String[] CHANNELS = {"online", "in-store", "mobile"};
    private static final String[] COUNTRIES = {"USA", "GBR", "IND", "CAN"};
    private static final String[] MERCHANT_CATEGORIES = {"electronics", "groceries", "apparel", "entertainment"};

    public TransactionSimulator(TransactionClient transactionClient) {
        this.transactionClient = transactionClient;
    }

    public void runSimulation(int transactionCount) throws InterruptedException {
        System.out.println("Starting transaction simulation...");
        for (int i = 0; i < transactionCount; i++) {
            transactionClient.sendTransaction(createRandomTransactionRequest());
            Thread.sleep(200); // Short delay between transactions
        }
        System.out.println("Transaction simulation finished.");
    }

    private TransactionRequest createRandomTransactionRequest() {
        TransactionRequest request = new TransactionRequest();
        request.setCustomerId(UUID.randomUUID().toString());
        request.setAccountId(UUID.randomUUID().toString());
        request.setTransactionAmount(1 + (15000 - 1) * random.nextDouble());
        request.setCurrency(getRandom(CURRENCIES));
        request.setChannel(getRandom(CHANNELS));
        request.setCountry(getRandom(COUNTRIES));
        request.setMerchantId(UUID.randomUUID().toString());
        request.setMerchantCategory(getRandom(MERCHANT_CATEGORIES));
        request.setIpAddress(generateRandomIpAddress());
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
