package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TransactionSimulator transactionSimulator;

    public DataInitializer(TransactionSimulator transactionSimulator) {
        this.transactionSimulator = transactionSimulator;
    }

    @Override
    public void run(String... args) throws Exception {
        // Automatic transaction generation is now disabled.
        // You can trigger it manually by calling the /api/simulation/run endpoint.
        
        // // 1. Run the simulation to generate and store data
        // transactionSimulator.runSimulation(10); // Generate 10 transactions

        // // Add a small delay to ensure all transactions are processed and saved
        // System.out.println("\nWaiting for transactions to be processed...");
        // Thread.sleep(2000); 

        // // 2. Fetch and display all transactions from the database via the API
        // System.out.println("\n--- Fetching All Stored Transactions ---");
        // TransactionViewer.main(new String[]{}); // Call the viewer to print the data
        // System.out.println("--- End of Report ---");
    }
}
