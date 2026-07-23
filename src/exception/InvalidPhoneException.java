package exception;

/**
 * Custom exception thrown when an invalid phone number is provided.
 * Demonstrates: Custom Exception, Inheritance (extends Exception).
 */
public class InvalidPhoneException extends Exception {

    /** The invalid phone number that triggered this exception. */
    private final String phone;

    /**
     * Constructs an InvalidPhoneException with only a message.
     *
     * @param message descriptive error message
     */
    public InvalidPhoneException(String message) {
        super(message);
        this.phone = "";
    }

    /**
     * Constructs an InvalidPhoneException with a message and the invalid phone string.
     *
     * @param message descriptive error message
     * @param phone   the phone number that failed validation
     */
    public InvalidPhoneException(String message, String phone) {
        super(message);
        this.phone = phone;
    }

    /**
     * Returns the invalid phone number.
     *
     * @return the phone number that caused validation to fail
     */
    public String getPhone() {
        return phone;
    }
}
