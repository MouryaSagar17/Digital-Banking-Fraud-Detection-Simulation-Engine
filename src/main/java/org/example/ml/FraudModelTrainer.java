package org.example.ml;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.SerializationHelper;

import java.io.File;
import java.util.Random;

public class FraudModelTrainer {

    private static final String DATASET_PATH = "src/main/resources/fraud_dataset.csv";
    private static final String MODEL_PATH = "fraud_rf.model";

    public static void main(String[] args) {
        try {
            System.out.println("Loading dataset...");
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File(DATASET_PATH));
            Instances data = loader.getDataSet();

            // Set class index to the last attribute (fraud/non-fraud)
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);

            System.out.println("Training Random Forest Model...");
            RandomForest rf = new RandomForest();
            rf.setNumIterations(100); // Number of trees
            rf.setMaxDepth(0); // Unlimited depth
            rf.setSeed(42); // Random seed for reproducibility
            rf.buildClassifier(data);

            System.out.println("Evaluating Model (10-Fold Cross-Validation)...");
            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(rf, data, 10, new Random(1));

            System.out.println(eval.toSummaryString("\nResults\n======\n", false));
            System.out.println("Precision: " + eval.precision(1));
            System.out.println("Recall: " + eval.recall(1));
            System.out.println("F1-Score: " + eval.fMeasure(1));
            System.out.println(eval.toMatrixString("\nConfusion Matrix\n================\n"));

            System.out.println("Feature Importance (Information Gain):");
            // Use InfoGain to rank features as a proxy for importance
            AttributeSelection attsel = new AttributeSelection();
            InfoGainAttributeEval evalAttr = new InfoGainAttributeEval();
            Ranker search = new Ranker();
            attsel.setEvaluator(evalAttr);
            attsel.setSearch(search);
            attsel.SelectAttributes(data);
            
            double[][] rankedAttributes = search.rankedAttributes();
            for (double[] rankedAttribute : rankedAttributes) {
                int attributeIndex = (int) rankedAttribute[0];
                double score = rankedAttribute[1];
                System.out.println(data.attribute(attributeIndex).name() + ": " + score);
            }

            System.out.println("Saving model to " + MODEL_PATH);
            SerializationHelper.write(MODEL_PATH, rf);
            System.out.println("Model saved successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
