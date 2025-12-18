package org.example.controller;

import org.example.TransactionSimulator;
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

    public SimulationController(TransactionSimulator transactionSimulator) {
        this.transactionSimulator = transactionSimulator;
    }

    @PostMapping("/run")
    public Map<String, String> runSimulation(@RequestParam(defaultValue = "10") int count) {
        try {
            transactionSimulator.runSimulation(count);
            Map<String, String> response = new HashMap<>();
            response.put("message", count + " transactions generated successfully.");
            return response;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Map<String, String> response = new HashMap<>();
            response.put("error", "Simulation was interrupted.");
            return response;
        }
    }
}
