package model;

/**
 * Enum representing every type of financial transaction the system records.
 * <p>
 * Demonstrates: Enums, Static members (implicit), String handling.
 * </p>
 */
public enum TransactionType {

    /** Money added to an account. */
    DEPOSIT("Deposit"),

    /** Money removed from an account. */
    WITHDRAWAL("Withdrawal"),

    /** Money received via a transfer from another account. */
    TRANSFER_IN("Transfer In"),

    /** Money sent via a transfer to another account. */
    TRANSFER_OUT("Transfer Out");

    // ------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------

    /** Human-readable label shown in transaction history. */
    private final String displayName;

    // ------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------

    /**
     * Enum constructor that binds a display label to each constant.
     *
     * @param displayName the user-friendly transaction type label
     */
    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    // ------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------

    /**
     * Returns the human-readable name of this transaction type.
     *
     * @return display name string
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Parses a string to the matching TransactionType (case-insensitive).
     *
     * @param value raw string value
     * @return the matching TransactionType constant
     * @throws IllegalArgumentException if the value cannot be matched
     */
    public static TransactionType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Transaction type cannot be blank.");
        }
        for (TransactionType type : TransactionType.values()) {
            if (type.name().equalsIgnoreCase(value.trim())
                    || type.displayName.equalsIgnoreCase(value.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown transaction type: " + value);
    }

    /**
     * Returns the display name as the string representation.
     *
     * @return display name
     */
    @Override
    public String toString() {
        return displayName;
    }
}
