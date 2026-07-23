package exception;

/**
 * Custom exception thrown when a transfer is attempted between the same account.
 * Demonstrates: Custom Exception, Inheritance (extends Exception).
 */
public class TransferToSameAccountException extends Exception {

    /** The account number involved in the invalid self-transfer. */
    private final String accountNumber;

    /**
     * Constructs a TransferToSameAccountException with only a message.
     *
     * @param message descriptive error message
     */
    public TransferToSameAccountException(String message) {
        super(message);
        this.accountNumber = "";
    }

    /**
     * Constructs a TransferToSameAccountException with a message and the account number.
     *
     * @param message       descriptive error message
     * @param accountNumber the account number that was used as both source and destination
     */
    public TransferToSameAccountException(String message, String accountNumber) {
        super(message);
        this.accountNumber = accountNumber;
    }

    /**
     * Returns the account number involved in the invalid self-transfer.
     *
     * @return the source/destination account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }
}
