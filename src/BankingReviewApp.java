import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

public class BankingReviewApp {

    static Scanner sc = new Scanner(System.in);

    public static void displayMenu() {

        System.out.println("\n==============================================");
        System.out.println("       RETAIL BANKING TRANSACTION REVIEW");
        System.out.println("==============================================");
        System.out.println("1. Transaction Search Analysis");
        System.out.println("2. Transaction Sorting Analysis");
        System.out.println("3. Duplicate Transaction Detection");
        System.out.println("4. Stack and Queue Operations");
        System.out.println("5. Minimum and Maximum Transaction Analysis");
        System.out.println("6. Exit");
        System.out.println("==============================================");
        System.out.print("Enter your choice: ");
    }

    // Task 1: Search
    public static void runSearch() {

        long[] ids = TransactionSearch.loadTransactionIds();

        if (ids.length == 0) {
            System.out.println("No transaction data found!");
            return;
        }

        System.out.print("Enter Transaction ID to search: ");
        long target = sc.nextLong();

        long start = System.nanoTime();
        int linearResult = TransactionSearch.linearSearch(ids, target);
        long linearTime = System.nanoTime() - start;

        int linearCount = TransactionSearch.linearComparisons;

        start = System.nanoTime();
        int binaryResult = TransactionSearch.binarySearch(ids, target);
        long binaryTime = System.nanoTime() - start;

        int binaryCount = TransactionSearch.binaryComparisons;

        System.out.println("\nSEARCH RESULTS");
        System.out.println("----------------------------------------------");

        System.out.printf("%-15s %-15s %-15s%n",
                "Algorithm", "Comparisons", "Time (ns)");

        System.out.printf("%-15s %-15d %-15d%n",
                "Linear Search", linearCount, linearTime);

        System.out.printf("%-15s %-15d %-15d%n",
                "Binary Search", binaryCount, binaryTime);

        if (linearResult != -1) {
            System.out.println("\nTransaction found at index: " + linearResult);
        } else {
            System.out.println("\nTransaction not found!");
        }

        System.out.println("Binary Search result index: " + binaryResult);
    }

    // Task 2: Sorting
    public static void runSorting() {

        ArrayList<Transaction> transactions =
                TransactionSorting.loadTransactions();

        if (transactions.isEmpty()) {
            System.out.println("No transaction data found!");
            return;
        }

        int sampleSize = Math.min(5000, transactions.size());

        Transaction[] insertionData =
                transactions.subList(0, sampleSize)
                        .toArray(new Transaction[0]);

        Transaction[] mergeData = insertionData.clone();

        Transaction[] temp = new Transaction[sampleSize];

        long start = System.nanoTime();
        TransactionSorting.insertionSort(insertionData);
        long insertionTime = System.nanoTime() - start;

        start = System.nanoTime();
        TransactionSorting.mergeSort(
                mergeData, temp, 0, mergeData.length - 1);
        long mergeTime = System.nanoTime() - start;

        System.out.println("\nSORTING RESULTS");
        System.out.println("----------------------------------------------");

        System.out.printf("%-20s %-15s %-15s%n",
                "Algorithm", "Comparisons", "Time (ns)");

        System.out.printf("%-20s %-15d %-15d%n",
                "Insertion Sort",
                TransactionSorting.insertionComparisons,
                insertionTime);

        System.out.printf("%-20s %-15d %-15d%n",
                "Merge Sort",
                TransactionSorting.mergeComparisons,
                mergeTime);

        System.out.println("\nFirst 5 sorted transactions:");
        TransactionSorting.displayTransactions(mergeData);
    }

    // Task 3: Duplicate Detection
    public static void runDuplicateDetection() {

        ArrayList<Transaction> transactions =
                DuplicateDetector.loadTransactions();

        if (transactions.isEmpty()) {
            System.out.println("No transaction data found!");
            return;
        }

        DuplicateDetector.detectDuplicates(transactions);
    }

    // Task 4: Stack and Queue
    public static void runStackQueue() {

        System.out.println("\nBANKING ADJUSTMENT STACK");

        CustomStack stack = new CustomStack(5);

        stack.push(new BigDecimal("500.00"));
        stack.push(new BigDecimal("1000.00"));
        stack.push(new BigDecimal("750.00"));

        stack.display();

        System.out.println("\nUndoing latest adjustment...");

        BigDecimal undone = stack.pop();
        System.out.println("Reversed adjustment: Rs. " + undone);

        stack.display();

        System.out.println("\nMANUAL REVIEW QUEUE");

        CustomQueue queue = new CustomQueue(5);

        queue.enqueue(100101);
        queue.enqueue(100102);
        queue.enqueue(100103);
        queue.enqueue(100104);

        queue.display();

        System.out.println("\nProcessing next transaction...");

        System.out.println("Processed transaction: " + queue.dequeue());

        queue.display();
    }

    // Task 5: Min-Max Analysis
    public static void runMinMax() {

        ArrayList<Transaction> transactions =
                MinMaxFinder.loadTransactions();

        if (transactions.isEmpty()) {
            System.out.println("No transaction data found!");
            return;
        }

        Transaction[] data =
                transactions.toArray(new Transaction[0]);

        long start = System.nanoTime();
        MinMaxFinder.MinMaxResult bruteResult =
                MinMaxFinder.bruteForce(data);
        long bruteTime = System.nanoTime() - start;

        start = System.nanoTime();
        MinMaxFinder.MinMaxResult divideResult =
                MinMaxFinder.divideAndConquer(
                        data, 0, data.length - 1);
        long divideTime = System.nanoTime() - start;

        System.out.println("\nMIN-MAX ANALYSIS");
        System.out.println("----------------------------------------------");

        System.out.printf("%-22s %-15s %-15s%n",
                "Algorithm", "Comparisons", "Time (ns)");

        System.out.printf("%-22s %-15d %-15d%n",
                "Brute Force",
                MinMaxFinder.bruteForceComparisons,
                bruteTime);

        System.out.printf("%-22s %-15d %-15d%n",
                "Divide and Conquer",
                MinMaxFinder.divideConquerComparisons,
                divideTime);

        System.out.println("\nMinimum Amount: Rs. " +
                bruteResult.min.amount);

        System.out.println("Minimum Transaction ID: " +
                bruteResult.min.transactionId);

        System.out.println("\nMaximum Amount: Rs. " +
                bruteResult.max.amount);

        System.out.println("Maximum Transaction ID: " +
                bruteResult.max.transactionId);

        boolean verified =
                bruteResult.min.amount.compareTo(
                        divideResult.min.amount) == 0
                &&
                bruteResult.max.amount.compareTo(
                        divideResult.max.amount) == 0;

        System.out.println("\nVerification: " +
                (verified ? "SUCCESS" : "FAILED"));
    }

    public static void main(String[] args) {

        int choice;

        do {

            displayMenu();

            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    runSearch();
                    break;

                case 2:
                    runSorting();
                    break;

                case 3:
                    runDuplicateDetection();
                    break;

                case 4:
                    runStackQueue();
                    break;

                case 5:
                    runMinMax();
                    break;

                case 6:
                    System.out.println(
                            "\nThank you for using Retail Banking Transaction Review!"
                    );
                    break;

                default:
                    System.out.println(
                            "\nInvalid choice! Please enter a number from 1 to 6."
                    );
            }

        } while (choice != 6);

        sc.close();
    }
}
