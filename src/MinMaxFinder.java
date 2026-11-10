import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MinMaxFinder {

    static long bruteForceComparisons = 0;
    static long divideConquerComparisons = 0;

    // Stores the minimum and maximum transactions
    static class MinMaxResult {

        Transaction min;
        Transaction max;

        MinMaxResult(Transaction min, Transaction max) {
            this.min = min;
            this.max = max;
        }
    }

    // Load transactions from CSV
    public static ArrayList<Transaction> loadTransactions() {

        ArrayList<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader =
                 new BufferedReader(new FileReader("data/transactions.csv"))) {

            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                long transactionId = Long.parseLong(data[0]);
                String accountId = data[1];
                BigDecimal amount = new BigDecimal(data[2]);
                String merchant = data[3];
                LocalDateTime postingDate = LocalDateTime.parse(data[4]);

                Transaction transaction = new Transaction(
                        transactionId,
                        accountId,
                        amount,
                        merchant,
                        postingDate
                );

                transactions.add(transaction);
            }

        } catch (IOException e) {
            System.out.println("Error reading CSV: " + e.getMessage());
        }

        return transactions;
    }

    // Approach 1: Brute Force
    public static MinMaxResult bruteForce(Transaction[] arr) {

        bruteForceComparisons = 0;

        Transaction min = arr[0];
        Transaction max = arr[0];

        for (int i = 1; i < arr.length; i++) {

            bruteForceComparisons++;

            if (arr[i].amount.compareTo(min.amount) < 0) {
                min = arr[i];
            }

            bruteForceComparisons++;

            if (arr[i].amount.compareTo(max.amount) > 0) {
                max = arr[i];
            }
        }

        return new MinMaxResult(min, max);
    }

    // Approach 2: Divide and Conquer
    public static MinMaxResult divideAndConquer(
            Transaction[] arr, int left, int right) {

        // Base case: Only one transaction
        if (left == right) {
            return new MinMaxResult(arr[left], arr[left]);
        }

        // Base case: Exactly two transactions
        if (right == left + 1) {

            divideConquerComparisons++;

            if (arr[left].amount.compareTo(arr[right].amount) < 0) {
                return new MinMaxResult(arr[left], arr[right]);
            } else {
                return new MinMaxResult(arr[right], arr[left]);
            }
        }

        // Divide the array into two halves
        int mid = left + (right - left) / 2;

        MinMaxResult firstHalf =
                divideAndConquer(arr, left, mid);

        MinMaxResult secondHalf =
                divideAndConquer(arr, mid + 1, right);

        // Combine the results
        Transaction min;
        Transaction max;

        divideConquerComparisons++;

        if (firstHalf.min.amount.compareTo(secondHalf.min.amount) < 0) {
            min = firstHalf.min;
        } else {
            min = secondHalf.min;
        }

        divideConquerComparisons++;

        if (firstHalf.max.amount.compareTo(secondHalf.max.amount) > 0) {
            max = firstHalf.max;
        } else {
            max = secondHalf.max;
        }

        return new MinMaxResult(min, max);
    }

    public static void main(String[] args) {

        ArrayList<Transaction> transactions = loadTransactions();

        if (transactions.isEmpty()) {
            System.out.println("No transaction data found!");
            return;
        }

        Transaction[] data =
                transactions.toArray(new Transaction[0]);

        System.out.println("TOTAL TRANSACTIONS: " + data.length);

        // Brute Force execution
        long bruteStart = System.nanoTime();

        MinMaxResult bruteResult = bruteForce(data);

        long bruteEnd = System.nanoTime();

        long bruteTime = bruteEnd - bruteStart;

        // Divide and Conquer execution
        long divideStart = System.nanoTime();

        MinMaxResult divideResult =
                divideAndConquer(data, 0, data.length - 1);

        long divideEnd = System.nanoTime();

        long divideTime = divideEnd - divideStart;

        // Display comparison
        System.out.println("\nMINIMUM AND MAXIMUM TRANSACTION ANALYSIS");
        System.out.println("================================================");

        System.out.printf("%-22s %-18s %-15s%n",
                "Algorithm", "Comparisons", "Time (ns)");

        System.out.println("------------------------------------------------");

        System.out.printf("%-22s %-18d %-15d%n",
                "Brute Force",
                bruteForceComparisons,
                bruteTime);

        System.out.printf("%-22s %-18d %-15d%n",
                "Divide and Conquer",
                divideConquerComparisons,
                divideTime);

        System.out.println("================================================");

        // Display minimum and maximum transactions
        System.out.println("\nMINIMUM TRANSACTION");
        System.out.println("------------------------------");
        System.out.println("Transaction ID: " +
                bruteResult.min.transactionId);
        System.out.println("Amount: Rs. " +
                bruteResult.min.amount);
        System.out.println("Merchant: " +
                bruteResult.min.merchant);

        System.out.println("\nMAXIMUM TRANSACTION");
        System.out.println("------------------------------");
        System.out.println("Transaction ID: " +
                bruteResult.max.transactionId);
        System.out.println("Amount: Rs. " +
                bruteResult.max.amount);
        System.out.println("Merchant: " +
                bruteResult.max.merchant);

        // Verify both approaches produce the same result
        boolean sameMinimum =
                bruteResult.min.amount.compareTo(
                        divideResult.min.amount) == 0;

        boolean sameMaximum =
                bruteResult.max.amount.compareTo(
                        divideResult.max.amount) == 0;

        System.out.println("\nRESULT VERIFICATION");

        if (sameMinimum && sameMaximum) {
            System.out.println(
                    "Both algorithms produced the same minimum and maximum amounts!"
            );
        } else {
            System.out.println("Result mismatch detected!");
        }

        System.out.println("\nMin-Max analysis completed successfully!");
    }
}
