package util;

/**
 * Utility class that generates unique, sequential account numbers.
 * <p>
 * Demonstrates: Static Members (static field + static methods),
 * Final keyword (constants), Encapsulation, Synchronization (thread safety).
 * </p>
 *
 * <p>
 * Account numbers follow the format "ACC" + 5-digit zero-padded sequence,
 * starting from 10001. Example: ACC10001, ACC10002, etc.
 * </p>
 */
public final class AccountNumberGenerator {

    // ------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------

    /** Prefix prepended to every generated account number. */
    public static final String PREFIX = "ACC";

    /** The sequence counter starts from this value. */
    public static final int STARTING_SEQUENCE = 10001;

    // ------------------------------------------------------------------
    // Static state — shared across all calls
    // ------------------------------------------------------------------

    /**
     * Monotonically increasing counter used to guarantee uniqueness.
     * Declared static so it persists across all method calls within a JVM session.
     * Demonstrates: Static members.
     */
    private static int counter = STARTING_SEQUENCE;

    // ------------------------------------------------------------------
    // Private constructor — prevents instantiation (utility class)
    // ------------------------------------------------------------------

    /**
     * Private constructor prevents creating instances of this utility class.
     * All public members are static, so there is no need to instantiate it.
     * Demonstrates: Encapsulation (hiding construction).
     */
    private AccountNumberGenerator() {
        throw new UnsupportedOperationException("Utility class — do not instantiate.");
    }

    // ------------------------------------------------------------------
    // Static methods
    // ------------------------------------------------------------------

    /**
     * Generates and returns the next unique account number.
     * The counter is incremented atomically to guard against concurrent calls.
     * Demonstrates: Static method, String formatting, synchronization.
     *
     * @return a unique account number (e.g. "ACC10001")
     */
    public static synchronized String generate() {
        return PREFIX + counter++;
    }

    /**
     * Peeks at the next account number without advancing the counter.
     * Useful for display or preview purposes.
     *
     * @return the account number that will be assigned on the next call to {@link #generate()}
     */
    public static synchronized String peek() {
        return PREFIX + counter;
    }

    /**
     * Returns the current value of the internal counter.
     * Used by {@link util.FileHandler} when restoring counter state after a data load.
     *
     * @return current counter value
     */
    public static synchronized int getCounter() {
        return counter;
    }

    /**
     * Sets the counter to a specific value.
     * Called by {@link util.FileHandler} after loading accounts from file,
     * so newly created accounts do not reuse existing account numbers.
     *
     * @param value the counter value to set (must be ≥ STARTING_SEQUENCE)
     */
    public static synchronized void setCounter(int value) {
        if (value >= STARTING_SEQUENCE) {
            counter = value;
        }
    }

    /**
     * Extracts the numeric part of an account number and returns it as an int.
     * Used to calibrate the counter when loading existing accounts from file.
     *
     * @param accountNumber a formatted account number (e.g. "ACC10042")
     * @return the numeric sequence portion, or -1 if parsing fails
     */
    public static int extractSequence(String accountNumber) {
        try {
            return Integer.parseInt(accountNumber.replace(PREFIX, "").trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
