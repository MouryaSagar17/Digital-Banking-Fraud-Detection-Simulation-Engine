package org.example.ml;

import weka.classifiers.trees.RandomForest;
import weka.core.SerializationHelper;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FraudModelLoader {

    private static final String MODEL_PATH = "fraud_rf.model";
    private RandomForest model;

    public FraudModelLoader() {
        try {
            File modelFile = new File(MODEL_PATH);
            if (modelFile.exists()) {
                System.out.println("Loading saved fraud detection model...");
                model = (RandomForest) SerializationHelper.read(MODEL_PATH);
                System.out.println("Model loaded successfully.");
            } else {
                System.err.println("Model file not found at " + MODEL_PATH + ". Please run FraudModelTrainer first.");
            }
        } catch (Exception e) {
            System.err.println("Error loading model: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public RandomForest getModel() {
        return model;
    }
}
