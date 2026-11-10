
import java.math.BigDecimal;

public class StackQueueDemo {

    public static void main(String[] args) {

        // CUSTOM STACK DEMONSTRATION

        System.out.println("==================================");
        System.out.println("   BANKING ADJUSTMENT STACK");
        System.out.println("==================================");

        CustomStack adjustmentStack = new CustomStack(5);

        // Add adjustments
        adjustmentStack.push(new BigDecimal("500.00"));
        adjustmentStack.push(new BigDecimal("1000.00"));
        adjustmentStack.push(new BigDecimal("750.00"));

        // Display adjustment history
        adjustmentStack.display();

        // Undo latest adjustment
        System.out.println("\nUndoing Latest Adjustment...");

        BigDecimal undone = adjustmentStack.pop();

        System.out.println("Reversed Adjustment: Rs. " + undone);

        // Display remaining adjustments
        adjustmentStack.display();


        // CUSTOM QUEUE DEMONSTRATION

        System.out.println("\n==================================");
        System.out.println("   MANUAL REVIEW QUEUE");
        System.out.println("==================================");

        CustomQueue reviewQueue = new CustomQueue(5);

        // Add transactions for review
        reviewQueue.enqueue(100101);
        reviewQueue.enqueue(100102);
        reviewQueue.enqueue(100103);
        reviewQueue.enqueue(100104);

        // Display pending transactions
        reviewQueue.display();

        // Process the oldest transaction
        System.out.println("\nProcessing Next Transaction...");

        long processed = reviewQueue.dequeue();

        System.out.println("Processed Transaction: " + processed);

        // Display remaining queue
        reviewQueue.display();

        // View next transaction
        System.out.println("\nNext Transaction: " + reviewQueue.peek());
    }
}

