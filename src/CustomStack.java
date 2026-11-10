import java.math.BigDecimal;

public class CustomStack {

    private BigDecimal[] stack;
    private int top;
    private int capacity;

    // Constructor
    public CustomStack(int capacity) {

        this.capacity = capacity;
        stack = new BigDecimal[capacity];

        top = -1;
    }

    // Push an adjustment onto the stack
    public void push(BigDecimal amount) {

        if (top == capacity - 1) {

            System.out.println("Stack Overflow!");
            return;
        }

        top++;

        stack[top] = amount;

        System.out.println("Adjustment added: Rs. " + amount);
    }

    // Pop the most recent adjustment
    public BigDecimal pop() {

        if (top == -1) {

            System.out.println("Stack Underflow!");
            return null;
        }

        BigDecimal removed = stack[top];

        stack[top] = null;

        top--;

        return removed;
    }

    // View the latest adjustment
    public BigDecimal peek() {

        if (top == -1) {

            System.out.println("Stack is empty!");
            return null;
        }

        return stack[top];
    }

    // Display all adjustments
    public void display() {

        if (top == -1) {

            System.out.println("No adjustments available.");
            return;
        }

        System.out.println("\nAdjustment History (Latest First):");

        for (int i = top; i >= 0; i--) {

            System.out.println("Rs. " + stack[i]);
        }
    }
}
