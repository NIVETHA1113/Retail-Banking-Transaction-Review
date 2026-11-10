import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class DuplicateDetector {

    // 1. Load transactions from CSV
    public static ArrayList<Transaction> loadTransactions() {

        ArrayList<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader =
                 new BufferedReader(new FileReader("data/transactions.csv"))) {

            String line;

            // Skip CSV header
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                long transactionId = Long.parseLong(data[0]);

                String accountId = data[1];

                BigDecimal amount = new BigDecimal(data[2]);

                String merchant = data[3];

                LocalDateTime postingDate =
                        LocalDateTime.parse(data[4]);

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


    // 2. Generate a unique grouping key
    public static String generateKey(Transaction t) {

        return t.accountId + "|" +
               t.amount.toPlainString() + "|" +
               t.merchant;
    }


    // 3. Detect potential duplicate transactions
    public static void detectDuplicates(
            ArrayList<Transaction> transactions) {

        // Sort transactions by posting date
        transactions.sort(
                Comparator.comparing(t -> t.postingDate)
        );

        // HashMap stores recent transactions for each key
        HashMap<String, ArrayDeque<Transaction>> transactionMap =
                new HashMap<>();

        int duplicateCount = 0;

        int comparisons = 0;

        System.out.println("\nPOTENTIAL DUPLICATE TRANSACTIONS");
        System.out.println("================================================");

        for (Transaction current : transactions) {

            String key = generateKey(current);

            // Get the queue for this transaction group
            ArrayDeque<Transaction> recentTransactions =
                    transactionMap.computeIfAbsent(
                            key,
                            k -> new ArrayDeque<>()
                    );

            // Remove transactions older than 60 seconds
            while (!recentTransactions.isEmpty()) {

                Transaction oldest = recentTransactions.peekFirst();

                long timeDifference = Duration.between(
                        oldest.postingDate,
                        current.postingDate
                ).getSeconds();

                if (timeDifference > 60) {

                    recentTransactions.pollFirst();

                } else {

                    break;
                }
            }

            // Compare current transaction with recent matching transactions
            for (Transaction previous : recentTransactions) {

                comparisons++;

                long timeDifference = Duration.between(
                        previous.postingDate,
                        current.postingDate
                ).getSeconds();

                if (timeDifference <= 60) {

                    duplicateCount++;

                    // Display only the first 20 duplicate pairs
                    if (duplicateCount <= 20) {

                        System.out.println("\nPotential Duplicate Pair:");

                        System.out.println(
                                "Original Transaction ID: " +
                                previous.transactionId
                        );

                        System.out.println(
                                "Duplicate Transaction ID: " +
                                current.transactionId
                        );

                        System.out.println(
                                "Account ID: " + current.accountId
                        );

                        System.out.println(
                                "Amount: Rs. " + current.amount
                        );

                        System.out.println(
                                "Merchant: " + current.merchant
                        );

                        System.out.println(
                                "Time Difference: " +
                                timeDifference + " seconds"
                        );

                        System.out.println("------------------------------");
                    }
                }
            }

            // Add current transaction to its group
            recentTransactions.addLast(current);
        }

        // Final summary
        System.out.println("\nDUPLICATE DETECTION SUMMARY");
        System.out.println("================================================");

        System.out.println(
                "Total Transactions: " + transactions.size()
        );

        System.out.println(
                "Potential Duplicate Pairs: " + duplicateCount
        );

        System.out.println(
                "Hash-Based Comparisons: " + comparisons
        );

        if (duplicateCount > 20) {

            System.out.println(
                    "Only the first 20 duplicate pairs were displayed."
            );
        }

        System.out.println("================================================");
    }


    // 4. Main Method
    public static void main(String[] args) {

        ArrayList<Transaction> transactions = loadTransactions();

        if (transactions.isEmpty()) {

            System.out.println("No transaction data found!");
            return;
        }

        System.out.println("TRANSACTION DUPLICATE DETECTION");

        System.out.println(
                "Total Transactions Loaded: " + transactions.size()
        );

        detectDuplicates(transactions);

        System.out.println("\nDuplicate detection completed successfully!");
    }
}
