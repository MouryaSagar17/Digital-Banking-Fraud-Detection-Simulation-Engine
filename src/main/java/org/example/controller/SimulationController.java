package org.example.controller;

import org.example.TransactionSimulator;
import org.example.client.NormalTransactionGenerator;
import org.example.client.TransactionClient;
import org.example.dto.TransactionRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

    private final TransactionSimulator transactionSimulator;
    private final NormalTransactionGenerator normalTransactionGenerator;
    private final TransactionClient transactionClient;

    public SimulationController(TransactionSimulator transactionSimulator, 
                                NormalTransactionGenerator normalTransactionGenerator,
                                TransactionClient transactionClient) {
        this.transactionSimulator = transactionSimulator;
        this.normalTransactionGenerator = normalTransactionGenerator;
        this.transactionClient = transactionClient;
    }

    @PostMapping("/run")
    public Map<String, String> runSimulation(@RequestParam(defaultValue = "10") int count) {
        try {
            transactionSimulator.runSimulation(count);
            Map<String, String> response = new HashMap<>();
            response.put("message", count + " mixed transactions generated successfully.");
            return response;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Map<String, String> response = new HashMap<>();
            response.put("error", "Simulation was interrupted.");
            return response;
        }
    }

    @PostMapping("/run-normal")
    public Map<String, String> runNormalSimulation(@RequestParam(defaultValue = "10") int count) {
        try {
            for (int i = 0; i < count; i++) {
                TransactionRequest request = normalTransactionGenerator.generate();
                transactionClient.sendTransaction(request);
                Thread.sleep(100); // Slight delay to prevent overwhelming the DB
            }
            Map<String, String> response = new HashMap<>();
            response.put("message", count + " NORMAL transactions generated successfully.");
            return response;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Map<String, String> response = new HashMap<>();
            response.put("error", "Simulation was interrupted.");
            return response;
        }
    }
}
