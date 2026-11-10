import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TransactionSearch {

    // Comparison counters
    static int linearComparisons = 0;
    static int binaryComparisons = 0;


    // 1. Load transaction IDs from CSV
    public static long[] loadTransactionIds() {

        ArrayList<Long> ids = new ArrayList<>();

        try (BufferedReader reader =
                 new BufferedReader(new FileReader("data/transactions.csv"))) {

            String line;

            // Skip CSV header
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                long transactionId = Long.parseLong(data[0]);

                ids.add(transactionId);
            }

        } catch (IOException e) {

            System.out.println("Error reading CSV: " + e.getMessage());
        }

        // Convert ArrayList into primitive long array
        long[] transactionIds = new long[ids.size()];

        for (int i = 0; i < ids.size(); i++) {
            transactionIds[i] = ids.get(i);
        }

        return transactionIds;
    }


    // 2. Linear Search
    public static int linearSearch(long[] ids, long target) {

        linearComparisons = 0;

        for (int i = 0; i < ids.length; i++) {

            linearComparisons++;

            if (ids[i] == target) {

                return i;
            }
        }

        return -1;
    }


    // 3. Binary Search
    public static int binarySearch(long[] ids, long target) {

        binaryComparisons = 0;

        int low = 0;
        int high = ids.length - 1;

        while (low <= high) {

            int mid = low + (high - low) / 2;

            binaryComparisons++;

            if (ids[mid] == target) {

                return mid;
            }

            else if (ids[mid] < target) {

                low = mid + 1;
            }

            else {

                high = mid - 1;
            }
        }

        return -1;
    }


    // 4. Main Method
    public static void main(String[] args) {

        long[] transactionIds = loadTransactionIds();

        if (transactionIds.length == 0) {

            System.out.println("No transaction data found!");
            return;
        }

        long[] targets = {
            100001,
            150000,
            200000,
            200001
        };

        String[] testCases = {
            "Best Case",
            "Middle Case",
            "Worst Case",
            "Not Found"
        };

        System.out.println("\nTRANSACTION SEARCH PERFORMANCE");
        System.out.println("==========================================================================");

        System.out.printf("%-15s %-12s %-12s %-15s %-15s%n",
                "Test Case",
                "Algorithm",
                "Comparisons",
                "Index",
                "Time (ns)");

        System.out.println("--------------------------------------------------------------------------");


        for (int i = 0; i < targets.length; i++) {

            long target = targets[i];


            // Linear Search
            long linearStart = System.nanoTime();

            int linearResult = linearSearch(transactionIds, target);

            long linearEnd = System.nanoTime();

            long linearTime = linearEnd - linearStart;


            System.out.printf("%-15s %-12s %-12d %-15d %-15d%n",
                    testCases[i],
                    "Linear",
                    linearComparisons,
                    linearResult,
                    linearTime);


            // Binary Search
            long binaryStart = System.nanoTime();

            int binaryResult = binarySearch(transactionIds, target);

            long binaryEnd = System.nanoTime();

            long binaryTime = binaryEnd - binaryStart;


            System.out.printf("%-15s %-12s %-12d %-15d %-15d%n",
                    testCases[i],
                    "Binary",
                    binaryComparisons,
                    binaryResult,
                    binaryTime);
        }

        System.out.println("==========================================================================");
    }
}
