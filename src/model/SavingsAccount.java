package model;

import java.time.LocalDate;

/**
 * Concrete savings account that earns annual interest.
 * <p>
 * Demonstrates: Inheritance (extends Account), Polymorphism (overrides getAccountType,
 * getTypeSpecificInfo, toFileString), Interface implementation (InterestBearing),
 * Method Overriding, Encapsulation, Final keyword, Constructors.
 * </p>
 */
public class SavingsAccount extends Account implements InterestBearing {

    // ------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------

    /** Default annual interest rate applied when none is specified. */
    public static final double DEFAULT_INTEREST_RATE = 4.0;

    /** Minimum balance that must be maintained in a savings account. */
    public static final double MINIMUM_BALANCE = 500.0;

    // ------------------------------------------------------------------
    // Savings-specific field
    // ------------------------------------------------------------------

    /** Annual interest rate as a percentage (e.g. 4.0 = 4 %). */
    private double interestRate;

    // ------------------------------------------------------------------
    // Constructors — Method Overloading (two constructors)
    // ------------------------------------------------------------------

    /**
     * Creates a new SavingsAccount with the default interest rate.
     * Demonstrates: Constructor Overloading and calling the superclass constructor.
     *
     * @param accountNumber unique account identifier
     * @param customerName  full name of the account holder
     * @param age           age of the account holder
     * @param phone         10-digit mobile number
     * @param address       residential address
     * @param email         e-mail address
     */
    public SavingsAccount(String accountNumber,
                          String customerName,
                          int age,
                          String phone,
                          String address,
                          String email) {
        super(accountNumber, customerName, age, phone, address, email);
        this.interestRate = DEFAULT_INTEREST_RATE;
    }

    /**
     * Creates a new SavingsAccount with a custom interest rate.
     * Demonstrates: Constructor Overloading.
     *
     * @param accountNumber unique account identifier
     * @param customerName  full name of the account holder
     * @param age           age of the account holder
     * @param phone         10-digit mobile number
     * @param address       residential address
     * @param email         e-mail address
     * @param interestRate  annual interest rate (%)
     */
    public SavingsAccount(String accountNumber,
                          String customerName,
                          int age,
                          String phone,
                          String address,
                          String email,
                          double interestRate) {
        super(accountNumber, customerName, age, phone, address, email);
        this.interestRate = interestRate;
    }

    /**
     * Reconstruction constructor used when loading from a file.
     *
     * @param accountNumber unique account identifier
     * @param customerName  full name of the account holder
     * @param age           age of the account holder
     * @param phone         10-digit mobile number
     * @param address       residential address
     * @param email         e-mail address
     * @param balance       existing balance to restore
     * @param dateCreated   original creation date
     * @param interestRate  annual interest rate (%)
     */
    public SavingsAccount(String accountNumber,
                          String customerName,
                          int age,
                          String phone,
                          String address,
                          String email,
                          double balance,
                          LocalDate dateCreated,
                          double interestRate) {
        super(accountNumber, customerName, age, phone, address, email, balance, dateCreated);
        this.interestRate = interestRate;
    }

    // ------------------------------------------------------------------
    // Overridden withdraw — enforce minimum balance
    // ------------------------------------------------------------------

    /**
     * Withdraws the given amount while enforcing the minimum balance rule.
     * Demonstrates: Method Overriding — specialises the base class withdrawal.
     *
     * @param amount amount to withdraw
     * @throws exception.InvalidAmountException       if amount is zero or negative
     * @throws exception.InsufficientBalanceException if withdrawal would breach minimum balance
     */
    @Override
    public void withdraw(double amount)
            throws exception.InvalidAmountException,
                   exception.InsufficientBalanceException {
        if (amount <= 0) {
            throw new exception.InvalidAmountException(
                    "Withdrawal amount must be greater than zero. Provided: " + amount, amount);
        }
        double remaining = getBalance() - amount;
        if (remaining < MINIMUM_BALANCE) {
            throw new exception.InsufficientBalanceException(
                    String.format("Withdrawal denied. A minimum balance of Rs. %.2f must be maintained. " +
                                  "Available for withdrawal: Rs. %.2f",
                                  MINIMUM_BALANCE, getBalance() - MINIMUM_BALANCE),
                    getBalance(), amount);
        }
        super.withdraw(amount);
    }

    // ------------------------------------------------------------------
    // InterestBearing interface implementation
    // ------------------------------------------------------------------

    /**
     * Returns the annual interest rate for this savings account.
     * Demonstrates: Interface method implementation.
     *
     * @return annual interest rate as a percentage
     */
    @Override
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * Calculates and applies simple annual interest to the current balance,
     * then returns the interest amount credited.
     * Formula: interest = balance × rate / 100
     * Demonstrates: Interface method implementation, arithmetic.
     *
     * @return the interest amount added to the balance
     */
    @Override
    public double applyInterest() {
        double interest = getBalance() * (interestRate / 100.0);
        setBalance(getBalance() + interest);
        return interest;
    }

    // ------------------------------------------------------------------
    // Setter for interest rate
    // ------------------------------------------------------------------

    /**
     * Updates the interest rate for this account.
     *
     * @param interestRate new annual interest rate (%)
     */
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    // ------------------------------------------------------------------
    // Abstract method implementations
    // ------------------------------------------------------------------

    /**
     * Returns SAVINGS as the account type for this class.
     * Demonstrates: Method Overriding (polymorphism).
     *
     * @return AccountType.SAVINGS
     */
    @Override
    public AccountType getAccountType() {
        return AccountType.SAVINGS;
    }

    /**
     * Returns a one-line description of the savings-specific field.
     * Used by Account.toString() to produce a complete display.
     *
     * @return interest rate info string
     */
    @Override
    public String getTypeSpecificInfo() {
        return String.format("Interest Rate  : %.2f %% p.a. | Min Balance: Rs. %.2f",
                             interestRate, MINIMUM_BALANCE);
    }

    /**
     * Serialises this SavingsAccount to a pipe-delimited string.
     * Format: [base fields]|interestRate
     * Demonstrates: Method Overriding, String handling.
     *
     * @return full pipe-delimited record string
     */
    @Override
    public String toFileString() {
        return toFileStringBase() + DELIMITER + interestRate;
    }
}
