package exception;

/**
 * Custom exception thrown when a monetary amount is invalid.
 * Demonstrates: Custom Exception, Inheritance (extends Exception), Final fields.
 */
public class InvalidAmountException extends Exception {

    /** The invalid amount that caused this exception. */
    private final double amount;

    /**
     * Constructs an InvalidAmountException with only a message.
     *
     * @param message descriptive error message
     */
    public InvalidAmountException(String message) {
        super(message);
        this.amount = 0.0;
    }

    /**
     * Constructs an InvalidAmountException with a message and the offending amount.
     *
     * @param message descriptive error message
     * @param amount  the invalid amount value
     */
    public InvalidAmountException(String message, double amount) {
        super(message);
        this.amount = amount;
    }

    /**
     * Returns the invalid amount that triggered this exception.
     *
     * @return the invalid amount
     */
    public double getAmount() {
        return amount;
    }
}
