package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RandomTransactionGenerator {

    private static final Random random = new Random();
    private static final String[] CURRENCIES = {"USD", "EUR", "GBP", "INR"};
    private static final String[] CHANNELS = {"online", "in-store", "mobile"};
    private static final String[] COUNTRIES = {"USA", "GBR", "IND", "CAN"};
    private static final String[] MERCHANT_CATEGORIES = {"electronics", "groceries", "apparel", "entertainment"};

    public static List<Transaction> generateTransactions(int count) {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            transactions.add(createRandomTransaction());
        }
        return transactions;
    }

    private static Transaction createRandomTransaction() {
        return new Transaction(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                1 + (1000 - 1) * random.nextDouble(),
                getRandom(CURRENCIES),
                LocalDateTime.now(),
                getRandom(CHANNELS),
                getRandom(COUNTRIES),
                UUID.randomUUID().toString(),
                getRandom(MERCHANT_CATEGORIES),
                generateRandomIpAddress(),
                random.nextInt(101),
                random.nextInt(101),
                random.nextBoolean()
        );
    }

    private static String getRandom(String[] array) {
        return array[random.nextInt(array.length)];
    }

    private static String generateRandomIpAddress() {
        return random.nextInt(256) + "." +
               random.nextInt(256) + "." +
               random.nextInt(256) + "." +
               random.nextInt(256);
    }

    public static void main(String[] args) {
        List<Transaction> transactions = generateTransactions(10);
        for (Transaction transaction : transactions) {
            System.out.println("Generated transaction: " + transaction.getCustomerId());
        }
    }
}
