package exception;

/**
 * Custom exception thrown when an unrecognised account type is provided.
 * Demonstrates: Custom Exception, Inheritance (extends Exception).
 */
public class InvalidAccountTypeException extends Exception {

    /** The invalid account type string provided by the user. */
    private final String accountType;

    /**
     * Constructs an InvalidAccountTypeException with only a message.
     *
     * @param message descriptive error message
     */
    public InvalidAccountTypeException(String message) {
        super(message);
        this.accountType = "";
    }

    /**
     * Constructs an InvalidAccountTypeException with a message and the bad type string.
     *
     * @param message     descriptive error message
     * @param accountType the account type string that could not be recognised
     */
    public InvalidAccountTypeException(String message, String accountType) {
        super(message);
        this.accountType = accountType;
    }

    /**
     * Returns the unrecognised account type.
     *
     * @return the invalid account type value
     */
    public String getAccountType() {
        return accountType;
    }
}
