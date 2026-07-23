package model;

import java.time.LocalDate;

/**
 * Concrete current (cheque) account that supports an overdraft facility.
 * <p>
 * Demonstrates: Inheritance (extends Account), Polymorphism (overrides withdraw,
 * getAccountType, getTypeSpecificInfo, toFileString), Interface implementation
 * (Overdraftable), Method Overriding, Encapsulation, Constructors.
 * </p>
 */
public class CurrentAccount extends Account implements Overdraftable {

    // ------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------

    /** Default overdraft limit applied when none is explicitly specified. */
    public static final double DEFAULT_OVERDRAFT_LIMIT = 10000.0;

    // ------------------------------------------------------------------
    // Current-specific field
    // ------------------------------------------------------------------

    /** Maximum amount the account holder may borrow beyond a zero balance. */
    private double overdraftLimit;

    // ------------------------------------------------------------------
    // Constructors — Method Overloading (two constructors)
    // ------------------------------------------------------------------

    /**
     * Creates a new CurrentAccount with the default overdraft limit.
     * Demonstrates: Constructor Overloading and calling the superclass constructor.
     *
     * @param accountNumber unique account identifier
     * @param customerName  full name of the account holder
     * @param age           age of the account holder
     * @param phone         10-digit mobile number
     * @param address       residential address
     * @param email         e-mail address
     */
    public CurrentAccount(String accountNumber,
                          String customerName,
                          int age,
                          String phone,
                          String address,
                          String email) {
        super(accountNumber, customerName, age, phone, address, email);
        this.overdraftLimit = DEFAULT_OVERDRAFT_LIMIT;
    }

    /**
     * Creates a new CurrentAccount with a custom overdraft limit.
     * Demonstrates: Constructor Overloading.
     *
     * @param accountNumber  unique account identifier
     * @param customerName   full name of the account holder
     * @param age            age of the account holder
     * @param phone          10-digit mobile number
     * @param address        residential address
     * @param email          e-mail address
     * @param overdraftLimit maximum overdraft in local currency
     */
    public CurrentAccount(String accountNumber,
                          String customerName,
                          int age,
                          String phone,
                          String address,
                          String email,
                          double overdraftLimit) {
        super(accountNumber, customerName, age, phone, address, email);
        this.overdraftLimit = overdraftLimit;
    }

    /**
     * Reconstruction constructor used when loading from a file.
     *
     * @param accountNumber  unique account identifier
     * @param customerName   full name of the account holder
     * @param age            age of the account holder
     * @param phone          10-digit mobile number
     * @param address        residential address
     * @param email          e-mail address
     * @param balance        existing balance to restore
     * @param dateCreated    original creation date
     * @param overdraftLimit maximum overdraft in local currency
     */
    public CurrentAccount(String accountNumber,
                          String customerName,
                          int age,
                          String phone,
                          String address,
                          String email,
                          double balance,
                          LocalDate dateCreated,
                          double overdraftLimit) {
        super(accountNumber, customerName, age, phone, address, email, balance, dateCreated);
        this.overdraftLimit = overdraftLimit;
    }

    // ------------------------------------------------------------------
    // Overridden withdraw — allows overdraft
    // ------------------------------------------------------------------

    /**
     * Withdraws the given amount, allowing the balance to go negative up to
     * the configured overdraft limit.
     * Demonstrates: Method Overriding — specialises base class behaviour.
     *
     * @param amount amount to withdraw
     * @throws exception.InvalidAmountException       if amount is zero or negative
     * @throws exception.InsufficientBalanceException if the withdrawal would exceed the overdraft limit
     */
    @Override
    public void withdraw(double amount)
            throws exception.InvalidAmountException,
                   exception.InsufficientBalanceException {
        if (amount <= 0) {
            throw new exception.InvalidAmountException(
                    "Withdrawal amount must be greater than zero. Provided: " + amount, amount);
        }
        double availableCredit = getBalance() + overdraftLimit;
        if (amount > availableCredit) {
            throw new exception.InsufficientBalanceException(
                    String.format("Withdrawal denied. Available credit (balance + overdraft): Rs. %.2f, Requested: Rs. %.2f",
                                  availableCredit, amount),
                    getBalance(), amount);
        }
        // Use the parent's balance field directly via setBalance since we're going negative
        setBalance(getBalance() - amount);
    }

    // ------------------------------------------------------------------
    // Overdraftable interface implementation
    // ------------------------------------------------------------------

    /**
     * Returns the maximum overdraft limit for this account.
     * Demonstrates: Interface method implementation.
     *
     * @return overdraft limit in local currency
     */
    @Override
    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    /**
     * Returns {@code true} when the current balance is below zero.
     *
     * @return true if overdrawn
     */
    @Override
    public boolean isOverdrawn() {
        return getBalance() < 0;
    }

    /**
     * Returns how much can still be withdrawn (balance + remaining overdraft capacity).
     *
     * @return available credit
     */
    @Override
    public double getAvailableCredit() {
        return getBalance() + overdraftLimit;
    }

    // ------------------------------------------------------------------
    // Setter
    // ------------------------------------------------------------------

    /**
     * Updates the overdraft limit for this account.
     *
     * @param overdraftLimit new overdraft limit
     */
    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    // ------------------------------------------------------------------
    // Abstract method implementations
    // ------------------------------------------------------------------

    /**
     * Returns CURRENT as the account type for this class.
     * Demonstrates: Method Overriding (polymorphism).
     *
     * @return AccountType.CURRENT
     */
    @Override
    public AccountType getAccountType() {
        return AccountType.CURRENT;
    }

    /**
     * Returns a one-line description of the current-account-specific fields.
     *
     * @return overdraft info string
     */
    @Override
    public String getTypeSpecificInfo() {
        return String.format("Overdraft Limit: Rs. %.2f | Available Credit: Rs. %.2f | %s",
                             overdraftLimit, getAvailableCredit(),
                             isOverdrawn() ? "Status: OVERDRAWN" : "Status: Normal");
    }

    /**
     * Serialises this CurrentAccount to a pipe-delimited string.
     * Format: [base fields]|overdraftLimit
     * Demonstrates: Method Overriding, String handling.
     *
     * @return full pipe-delimited record string
     */
    @Override
    public String toFileString() {
        return toFileStringBase() + DELIMITER + overdraftLimit;
    }
}
