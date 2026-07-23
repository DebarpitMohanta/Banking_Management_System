package model;

/**
 * Enum representing the types of bank accounts available in the system.
 * <p>
 * Demonstrates: Enums, String handling, Static members (implicit in enums).
 * </p>
 */
public enum AccountType {

    /** Standard savings account with an interest rate. */
    SAVINGS("Savings Account"),

    /** Current / cheque account with an overdraft facility. */
    CURRENT("Current Account");

    // ------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------

    /** Human-readable display name shown in the console UI. */
    private final String displayName;

    // ------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------

    /**
     * Enum constructor — associates a display name with each constant.
     *
     * @param displayName the user-friendly label
     */
    AccountType(String displayName) {
        this.displayName = displayName;
    }

    // ------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------

    /**
     * Returns the human-readable name of this account type.
     *
     * @return display name string
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Parses a string to the matching AccountType constant (case-insensitive).
     *
     * @param value the raw string (e.g. "savings", "CURRENT")
     * @return the matching AccountType
     * @throws IllegalArgumentException if the value is not recognised
     */
    public static AccountType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Account type cannot be blank.");
        }
        for (AccountType type : AccountType.values()) {
            if (type.name().equalsIgnoreCase(value.trim())
                    || type.displayName.equalsIgnoreCase(value.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown account type: " + value);
    }

    /**
     * Overrides toString to return the display name instead of the constant name.
     *
     * @return display name
     */
    @Override
    public String toString() {
        return displayName;
    }
}
