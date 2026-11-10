import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class TransactionDataGenerator {

    public static void main(String[] args) {

        final int TOTAL_TRANSACTIONS = 100000;

        Random random = new Random();

        ArrayList<Transaction> transactions = new ArrayList<>();

        String[] accounts = {
            "ACC101", "ACC102", "ACC103", "ACC104",
            "ACC105", "ACC106", "ACC107", "ACC108"
        };

        String[] merchants = {
            "Amazon", "Flipkart", "Swiggy",
            "Zomato", "Myntra", "BigBasket",
            "Uber", "BookMyShow"
        };

        // Generate 100,000 transactions
        for (int i = 0; i < TOTAL_TRANSACTIONS; i++) {

            long transactionId = 100001 + i;

            Transaction transaction;

            // Create an intentional duplicate every 1000 records
            if (i > 0 && i % 1000 == 0) {

                Transaction original = transactions.get(i - 1);

                LocalDateTime duplicateTime =
                    original.postingDate.plusSeconds(
                        random.nextInt(59) + 1
                    );

                transaction = new Transaction(
                    transactionId,
                    original.accountId,
                    original.amount,
                    original.merchant,
                    duplicateTime
                );

            } else {

                String accountId =
                    accounts[random.nextInt(accounts.length)];

                String merchant =
                    merchants[random.nextInt(merchants.length)];

                // Generate an amount between Rs. 1 and Rs. 5000
                BigDecimal amount = BigDecimal.valueOf(
                    random.nextInt(500000) + 100, 2
                );

                // Generate a random timestamp within 10 days
                LocalDateTime postingDate =
                    LocalDateTime.of(2026, 11, 1, 0, 0)
                    .plusSeconds(random.nextInt(864000));

                transaction = new Transaction(
                    transactionId,
                    accountId,
                    amount,
                    merchant,
                    postingDate
                );
            }

            transactions.add(transaction);
        }

        // Write transactions into CSV
        try {

            java.nio.file.Files.createDirectories(
                java.nio.file.Paths.get("data")
            );

            BufferedWriter writer = new BufferedWriter(
                new FileWriter("data/transactions.csv")
            );

            // CSV header
            writer.write(
                "transactionId,accountId,amount,merchant,postingDate"
            );

            writer.newLine();

            for (Transaction t : transactions) {

                writer.write(
                    t.transactionId + "," +
                    t.accountId + "," +
                    t.amount.toPlainString() + "," +
                    t.merchant + "," +
                    t.postingDate
                );

                writer.newLine();
            }

            writer.close();

            System.out.println("Transaction data generated successfully!");
            System.out.println("Total transactions: " + transactions.size());
            System.out.println("File location: data/transactions.csv");

        } catch (IOException e) {

            System.out.println("Error generating CSV file: " + e.getMessage());
        }
    }
}
