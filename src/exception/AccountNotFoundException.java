package exception;

/**
 * Custom exception thrown when an account cannot be found in the system.
 * Demonstrates: Custom Exception, Inheritance (extends Exception).
 */
public class AccountNotFoundException extends Exception {

    /** The account number that was not found. */
    private final String accountNumber;

    /**
     * Constructs an AccountNotFoundException with only a message.
     *
     * @param message descriptive error message
     */
    public AccountNotFoundException(String message) {
        super(message);
        this.accountNumber = "";
    }

    /**
     * Constructs an AccountNotFoundException with a message and the missing account number.
     *
     * @param message       descriptive error message
     * @param accountNumber the account number that could not be located
     */
    public AccountNotFoundException(String message, String accountNumber) {
        super(message);
        this.accountNumber = accountNumber;
    }

    /**
     * Returns the account number that was not found.
     *
     * @return the missing account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }
}
