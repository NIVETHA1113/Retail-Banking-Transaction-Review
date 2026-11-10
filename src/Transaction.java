import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    // Transaction details
    long transactionId;
    String accountId;
    BigDecimal amount;
    String merchant;
    LocalDateTime postingDate;

    // Constructor
    public Transaction(long transactionId,
                       String accountId,
                       BigDecimal amount,
                       String merchant,
                       LocalDateTime postingDate) {

        this.transactionId = transactionId;
        this.accountId = accountId;
        this.amount = amount;
        this.merchant = merchant;
        this.postingDate = postingDate;
    }

    // Display transaction details
    @Override
    public String toString() {

        return "Transaction ID: " + transactionId +
               "\nAccount ID: " + accountId +
               "\nAmount: Rs. " + amount +
               "\nMerchant: " + merchant +
               "\nPosting Date: " + postingDate;
    }
}
