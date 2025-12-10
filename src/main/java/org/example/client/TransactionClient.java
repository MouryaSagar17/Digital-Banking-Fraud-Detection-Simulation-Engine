package org.example.client;

import org.example.dto.TransactionRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TransactionClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "http://localhost:8080/api/transactions";

    public void sendTransaction(TransactionRequest transactionRequest) {
        try {
            restTemplate.postForObject(API_URL, transactionRequest, String.class);
        } catch (Exception e) {
            System.err.println("Error sending transaction: " + e.getMessage());
        }
    }
}
