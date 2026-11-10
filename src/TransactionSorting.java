import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TransactionSorting {

    static long insertionComparisons = 0;
    static long mergeComparisons = 0;

    // 1. Load transactions from CSV
    public static ArrayList<Transaction> loadTransactions() {

        ArrayList<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader =
                 new BufferedReader(new FileReader("data/transactions.csv"))) {

            String line;

            // Skip header
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


    // 2. Insertion Sort
    public static void insertionSort(Transaction[] arr) {

        insertionComparisons = 0;

        for (int i = 1; i < arr.length; i++) {

            Transaction key = arr[i];

            int j = i - 1;

            while (j >= 0) {

                insertionComparisons++;

                if (arr[j].postingDate.compareTo(key.postingDate) > 0) {

                    arr[j + 1] = arr[j];

                    j--;
                }

                else {

                    break;
                }
            }

            arr[j + 1] = key;
        }
    }


    // 3. Merge Sort
    public static void mergeSort(Transaction[] arr,
                                 Transaction[] temp,
                                 int left,
                                 int right) {

        if (left < right) {

            int mid = left + (right - left) / 2;

            mergeSort(arr, temp, left, mid);

            mergeSort(arr, temp, mid + 1, right);

            merge(arr, temp, left, mid, right);
        }
    }


    // 4. Merge two sorted portions
    public static void merge(Transaction[] arr,
                             Transaction[] temp,
                             int left,
                             int mid,
                             int right) {

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {

            mergeComparisons++;

            if (arr[i].postingDate.compareTo(arr[j].postingDate) <= 0) {

                temp[k] = arr[i];
                i++;
            }

            else {

                temp[k] = arr[j];
                j++;
            }

            k++;
        }

        // Copy remaining left-side elements
        while (i <= mid) {

            temp[k] = arr[i];

            i++;
            k++;
        }

        // Copy remaining right-side elements
        while (j <= right) {

            temp[k] = arr[j];

            j++;
            k++;
        }

        // Copy merged elements back into original array
        for (int index = left; index <= right; index++) {

            arr[index] = temp[index];
        }
    }


    // 5. Display first five transactions
    public static void displayTransactions(Transaction[] arr) {

        for (int i = 0; i < Math.min(5, arr.length); i++) {

            System.out.println(
                    arr[i].transactionId + " | " +
                    arr[i].postingDate + " | " +
                    arr[i].merchant
            );
        }
    }


    // 6. Main Method
    public static void main(String[] args) {

        ArrayList<Transaction> transactions = loadTransactions();

        if (transactions.isEmpty()) {

            System.out.println("No transaction data found!");
            return;
        }

        System.out.println("TOTAL TRANSACTIONS LOADED: "
                + transactions.size());

        // Use the same 5000 records for a fair comparison
        int sampleSize = Math.min(5000, transactions.size());

        Transaction[] insertionData =
                transactions.subList(0, sampleSize)
                        .toArray(new Transaction[0]);

        Transaction[] mergeData = insertionData.clone();

        Transaction[] temp = new Transaction[sampleSize];


        // Insertion Sort timing
        long insertionStart = System.nanoTime();

        insertionSort(insertionData);

        long insertionEnd = System.nanoTime();

        long insertionTime = insertionEnd - insertionStart;


        // Merge Sort timing
        mergeComparisons = 0;

        long mergeStart = System.nanoTime();

        mergeSort(mergeData, temp, 0, mergeData.length - 1);

        long mergeEnd = System.nanoTime();

        long mergeTime = mergeEnd - mergeStart;


        // Display comparison results
        System.out.println("\nSORTING PERFORMANCE COMPARISON");
        System.out.println("================================================");

        System.out.printf("%-20s %-15s %-15s%n",
                "Algorithm", "Comparisons", "Time (ns)");

        System.out.println("------------------------------------------------");

        System.out.printf("%-20s %-15d %-15d%n",
                "Insertion Sort",
                insertionComparisons,
                insertionTime);

        System.out.printf("%-20s %-15d %-15d%n",
                "Merge Sort",
                mergeComparisons,
                mergeTime);

        System.out.println("================================================");


        // Display sorted transactions
        System.out.println("\nINSERTION SORT - FIRST 5 RECORDS");

        displayTransactions(insertionData);

        System.out.println("\nMERGE SORT - FIRST 5 RECORDS");

        displayTransactions(mergeData);


        System.out.println("\nSORTING COMPLETED SUCCESSFULLY!");
        System.out.println("Records sorted: " + sampleSize);
    }
}

