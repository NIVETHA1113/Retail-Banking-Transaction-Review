public class CustomQueue {

    private long[] queue;

    private int front;
    private int rear;
    private int size;
    private int capacity;

    // Constructor
    public CustomQueue(int capacity) {

        this.capacity = capacity;

        queue = new long[capacity];

        front = 0;
        rear = -1;
        size = 0;
    }

    // Add a transaction to the queue
    public void enqueue(long transactionId) {

        if (size == capacity) {

            System.out.println("Queue is Full!");
            return;
        }

        rear = (rear + 1) % capacity;

        queue[rear] = transactionId;

        size++;

        System.out.println(
                "Transaction added for review: " + transactionId
        );
    }

    // Remove the oldest transaction
    public long dequeue() {

        if (size == 0) {

            System.out.println("Queue is Empty!");
            return -1;
        }

        long removed = queue[front];

        front = (front + 1) % capacity;

        size--;

        return removed;
    }

    // View the next transaction
    public long peek() {

        if (size == 0) {

            System.out.println("Queue is Empty!");
            return -1;
        }

        return queue[front];
    }

    // Display pending transactions
    public void display() {

        if (size == 0) {

            System.out.println("No pending transactions.");
            return;
        }

        System.out.println("\nTransactions Waiting for Review:");

        for (int i = 0; i < size; i++) {

            int index = (front + i) % capacity;

            System.out.println(queue[index]);
        }
    }
}

