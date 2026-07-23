package exception;

/**
 * Custom exception thrown when an account does not have enough funds for a withdrawal or transfer.
 * Demonstrates: Custom Exception, Inheritance (extends Exception), Final fields.
 */
public class InsufficientBalanceException extends Exception {

    /** Current balance at the time of the failed operation. */
    private final double currentBalance;

    /** Amount that was requested. */
    private final double requestedAmount;

    /**
     * Constructs an InsufficientBalanceException with only a message.
     *
     * @param message descriptive error message
     */
    public InsufficientBalanceException(String message) {
        super(message);
        this.currentBalance = 0.0;
        this.requestedAmount = 0.0;
    }

    /**
     * Constructs an InsufficientBalanceException with full context.
     *
     * @param message         descriptive error message
     * @param currentBalance  the account balance when the operation failed
     * @param requestedAmount the amount that was requested
     */
    public InsufficientBalanceException(String message, double currentBalance, double requestedAmount) {
        super(message);
        this.currentBalance = currentBalance;
        this.requestedAmount = requestedAmount;
    }

    /**
     * Returns the balance at the time the exception was thrown.
     *
     * @return current account balance
     */
    public double getCurrentBalance() {
        return currentBalance;
    }

    /**
     * Returns the amount that was requested.
     *
     * @return requested amount
     */
    public double getRequestedAmount() {
        return requestedAmount;
    }

    /**
     * Returns the shortfall — how much more was needed.
     *
     * @return the difference between requested and current balance
     */
    public double getShortfall() {
        return requestedAmount - currentBalance;
    }
}
