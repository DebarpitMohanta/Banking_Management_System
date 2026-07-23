package exception;

/**
 * Custom exception thrown when an attempt is made to create an account
 * that already exists in the system.
 * Demonstrates: Custom Exception, Inheritance (extends Exception).
 */
public class DuplicateAccountException extends Exception {

    /** The account number that already exists. */
    private final String accountNumber;

    /**
     * Constructs a DuplicateAccountException with only a message.
     *
     * @param message descriptive error message
     */
    public DuplicateAccountException(String message) {
        super(message);
        this.accountNumber = "";
    }

    /**
     * Constructs a DuplicateAccountException with a message and the duplicate account number.
     *
     * @param message       descriptive error message
     * @param accountNumber the account number that is duplicated
     */
    public DuplicateAccountException(String message, String accountNumber) {
        super(message);
        this.accountNumber = accountNumber;
    }

    /**
     * Returns the duplicate account number.
     *
     * @return the account number that already exists
     */
    public String getAccountNumber() {
        return accountNumber;
    }
}
