package org.example.ml;

import org.example.model.Transaction;
import org.springframework.stereotype.Component;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class FraudPredictor {

    private final FraudModelLoader modelLoader;
    private Instances dataStructure;

    public FraudPredictor(FraudModelLoader modelLoader) {
        this.modelLoader = modelLoader;
        initializeDataStructure();
    }

    private void initializeDataStructure() {
        // Define attributes exactly as they were in the training data
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("transactionAmount"));
        attributes.add(new Attribute("transactionFrequency"));
        attributes.add(new Attribute("countryRiskScore"));
        attributes.add(new Attribute("ipRiskScore"));
        attributes.add(new Attribute("deviceTrustScore"));
        attributes.add(new Attribute("transactionTime"));
        attributes.add(new Attribute("ruleRiskScore"));
        
        ArrayList<String> classValues = new ArrayList<>();
        classValues.add("non-fraud");
        classValues.add("fraud");
        attributes.add(new Attribute("class", classValues));

        dataStructure = new Instances("FraudPrediction", attributes, 0);
        dataStructure.setClassIndex(dataStructure.numAttributes() - 1);
    }

    public String predict(Transaction transaction) {
        RandomForest model = modelLoader.getModel();
        if (model == null) {
            return "UNKNOWN (Model not loaded)";
        }

        try {
            Instance instance = new DenseInstance(8);
            instance.setDataset(dataStructure);
            
            // Map transaction fields to ML features
            // Note: You might need to calculate some of these if they aren't directly in Transaction
            instance.setValue(0, transaction.getTransactionAmount());
            instance.setValue(1, transaction.getDailyTransactionCount()); // Mapping frequency
            instance.setValue(2, transaction.getUserAccountCountry().equals(transaction.getCountry()) ? 0 : 50); // Simple logic for country risk
            instance.setValue(3, transaction.getIpRiskScore());
            instance.setValue(4, transaction.isNewDevice() ? 20 : 90); // Simple logic for device trust
            instance.setValue(5, transaction.getTxnTimestamp().getHour());
            instance.setValue(6, transaction.getRuleRiskScore());
            // Class is missing, which is what we want to predict

            double result = model.classifyInstance(instance);
            return dataStructure.classAttribute().value((int) result);

        } catch (Exception e) {
            System.err.println("Error during prediction: " + e.getMessage());
            return "ERROR";
        }
    }
    
    public double getFraudProbability(Transaction transaction) {
         RandomForest model = modelLoader.getModel();
        if (model == null) {
            return -1.0;
        }

        try {
            Instance instance = new DenseInstance(8);
            instance.setDataset(dataStructure);
            
            instance.setValue(0, transaction.getTransactionAmount());
            instance.setValue(1, transaction.getDailyTransactionCount());
            instance.setValue(2, transaction.getUserAccountCountry().equals(transaction.getCountry()) ? 0 : 50);
            instance.setValue(3, transaction.getIpRiskScore());
            instance.setValue(4, transaction.isNewDevice() ? 20 : 90);
            instance.setValue(5, transaction.getTxnTimestamp().getHour());
            instance.setValue(6, transaction.getRuleRiskScore());

            double[] probabilities = model.distributionForInstance(instance);
            // Assuming index 1 is "fraud" (check your ARFF/CSV header order)
            // In our CSV: non-fraud is 0, fraud is 1 usually if sorted alphabetically? 
            // Actually Weka uses the order in the header. 
            // In initializeDataStructure: non-fraud is 0, fraud is 1.
            return probabilities[1]; 

        } catch (Exception e) {
            System.err.println("Error during probability calculation: " + e.getMessage());
            return -1.0;
        }
    }
}
