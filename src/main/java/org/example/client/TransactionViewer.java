package org.example.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TransactionViewer {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/transactions";

        try {
            System.out.println("Fetching all stored transactions...");
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("--- Stored Transactions (JSON) ---");
                System.out.println(response.getBody());
                System.out.println("----------------------------------");
            } else {
                System.out.println("Failed to fetch transactions. Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error connecting to the API. Make sure the application is running on port 8080.");
            e.printStackTrace();
        }
    }
}
