package org.example.client;

import org.example.dto.TransactionRequest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Random;

public class TransactionGenerator {

    public static void main(String[] args) {
        TransactionRequest req = generateSampleTransaction();

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/transactions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TransactionRequest> entity = new HttpEntity<>(req, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, entity, String.class);

        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Body:   " + response.getBody());
    }

    private static TransactionRequest generateSampleTransaction() {
        Random random = new Random();

        TransactionRequest req = new TransactionRequest();
        req.setCustomerId("CUST" + (1000 + random.nextInt(50)));
        req.setAccountId("ACC" + (2000 + random.nextInt(50)));

        req.setTransactionAmount(1500.75);
        req.setCurrency("INR");

        String[] channels = {"WEB", "MOBILE_APP", "UPI"};
        req.setChannel(channels[random.nextInt(channels.length)]);

        req.setCountry("IN");
        req.setMerchantId("M" + (3000 + random.nextInt(100)));
        req.setMerchantCategory("GROCERY");
        req.setIpAddress("192.168.1." + random.nextInt(255));

        return req;
    }
}
