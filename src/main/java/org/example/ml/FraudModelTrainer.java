package org.example.ml;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;

import java.util.Random;

public class FraudModelTrainer {

    public static void main(String[] args) {
        try {
            System.out.println("Loading dataset...");
            DataSource source = new DataSource("src/main/resources/Sample_Transaction_Dataset.csv");
            Instances data = source.getDataSet();

            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);

            // Convert class to nominal
            NumericToNominal convert = new NumericToNominal();
            convert.setAttributeIndices("last");
            convert.setInputFormat(data);
            Instances nominalData = Filter.useFilter(data, convert);

            // 1. Random Forest
            System.out.println("\n--- Training Random Forest ---");
            RandomForest rf = new RandomForest();
            rf.setNumIterations(100);
            rf.setSeed(42);
            evaluateModel(rf, nominalData, "Random Forest");

            // 2. J48 (Decision Tree)
            System.out.println("\n--- Training J48 (Decision Tree) ---");
            J48 j48 = new J48();
            evaluateModel(j48, nominalData, "J48");

            // 3. Naive Bayes
            System.out.println("\n--- Training Naive Bayes ---");
            NaiveBayes nb = new NaiveBayes();
            evaluateModel(nb, nominalData, "Naive Bayes");

            // Save the best model (Random Forest for now)
            System.out.println("\nSaving Random Forest model to fraud_rf.model...");
            rf.buildClassifier(nominalData);
            weka.core.SerializationHelper.write("fraud_rf.model", rf);
            System.out.println("Model saved successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void evaluateModel(Classifier model, Instances data, String modelName) throws Exception {
        model.buildClassifier(data);
        Evaluation eval = new Evaluation(data);
        eval.crossValidateModel(model, data, 10, new Random(1));

        System.out.println(modelName + " Accuracy: " + String.format("%.2f%%", eval.pctCorrect()));
        System.out.println("Precision: " + String.format("%.2f", eval.weightedPrecision()));
        System.out.println("Recall: " + String.format("%.2f", eval.weightedRecall()));
        System.out.println("F1-Score: " + String.format("%.2f", eval.weightedFMeasure()));
    }
}
