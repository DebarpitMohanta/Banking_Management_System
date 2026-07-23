package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Abstract base class for all bank account types in the system.
 * <p>
 * Demonstrates: Abstract Classes, Encapsulation (private fields + getters/setters),
 * Constructors, Inheritance (extended by SavingsAccount and CurrentAccount),
 * Polymorphism (abstract methods overridden in subclasses), Final keyword,
 * Java Time API, String handling, Comparable.
 * </p>
 *
 * <p>
 * Every concrete account type MUST override {@link #getAccountType()} and
 * {@link #getTypeSpecificInfo()} to describe itself, and must override
 * {@link #toFileString()} to serialise its extra fields.
 * </p>
 */
public abstract class Account implements Comparable<Account> {

    // ------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------

    /** Minimum allowed account holder age. */
    public static final int MIN_AGE = 18;

    /** Maximum allowed account holder age. */
    public static final int MAX_AGE = 100;

    /** Pipe delimiter used when serialising to/from the data file. */
    public static final String DELIMITER = "|";

    /** Date format used for display and file storage. */
    public static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // ------------------------------------------------------------------
    // Private fields — Encapsulation
    // ------------------------------------------------------------------

    /** Unique account identifier (e.g. "ACC10001"). */
    private final String accountNumber;

    /** Full name of the account holder. */
    private String customerName;

    /** Age of the account holder in years. */
    private int age;

    /** 10-digit mobile phone number. */
    private String phone;

    /** Residential / mailing address. */
    private String address;

    /** E-mail address of the account holder. */
    private String email;

    /** Current account balance in the local currency. */
    private double balance;

    /** The date on which this account was created. */
    private final LocalDate dateCreated;

    // ------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------

    /**
     * Creates a new Account with the supplied details and a zero balance.
     * The creation date is captured automatically.
     *
     * @param accountNumber unique account identifier
     * @param customerName  full name of the account holder
     * @param age           age of the account holder (must be 18–100)
     * @param phone         10-digit mobile number
     * @param address       residential address
     * @param email         e-mail address
     */
    protected Account(String accountNumber,
                      String customerName,
                      int age,
                      String phone,
                      String address,
                      String email) {
        this.accountNumber = accountNumber;
        this.customerName  = customerName;
        this.age           = age;
        this.phone         = phone;
        this.address       = address;
        this.email         = email;
        this.balance       = 0.0;
        this.dateCreated   = LocalDate.now();
    }

    /**
     * Reconstruction constructor used when loading an account from a file.
     * Accepts an explicit balance and creation date instead of defaults.
     *
     * @param accountNumber unique account identifier
     * @param customerName  full name of the account holder
     * @param age           age of the account holder
     * @param phone         10-digit mobile number
     * @param address       residential address
     * @param email         e-mail address
     * @param balance       existing balance to restore
     * @param dateCreated   original account creation date
     */
    protected Account(String accountNumber,
                      String customerName,
                      int age,
                      String phone,
                      String address,
                      String email,
                      double balance,
                      LocalDate dateCreated) {
        this.accountNumber = accountNumber;
        this.customerName  = customerName;
        this.age           = age;
        this.phone         = phone;
        this.address       = address;
        this.email         = email;
        this.balance       = balance;
        this.dateCreated   = dateCreated;
    }

    // ------------------------------------------------------------------
    // Abstract methods — Polymorphism
    // ------------------------------------------------------------------

    /**
     * Returns the account type constant for this account.
     * Subclasses MUST implement this method.
     *
     * @return the AccountType of this account
     */
    public abstract AccountType getAccountType();

    /**
     * Returns a one-line string describing the subclass-specific field
     * (e.g. interest rate for Savings, overdraft limit for Current).
     *
     * @return type-specific info string
     */
    public abstract String getTypeSpecificInfo();

    /**
     * Serialises this account to a pipe-delimited string for file storage.
     * Subclasses append their own extra fields after calling the common prefix.
     *
     * @return pipe-delimited record string
     */
    public abstract String toFileString();

    // ------------------------------------------------------------------
    // Concrete banking operations
    // ------------------------------------------------------------------

    /**
     * Deposits the given amount into this account.
     * Demonstrates: Method definition, validation delegation.
     *
     * @param amount the amount to deposit (must be positive)
     * @throws exception.InvalidAmountException if amount is zero or negative
     */
    public void deposit(double amount) throws exception.InvalidAmountException {
        if (amount <= 0) {
            throw new exception.InvalidAmountException(
                    "Deposit amount must be greater than zero. Provided: " + amount, amount);
        }
        this.balance += amount;
    }

    /**
     * Withdraws the given amount from this account.
     * Subclasses may override this to allow overdraft behaviour.
     * Demonstrates: Polymorphism (CurrentAccount overrides this).
     *
     * @param amount the amount to withdraw (must be positive and ≤ balance)
     * @throws exception.InvalidAmountException       if amount is zero or negative
     * @throws exception.InsufficientBalanceException if the balance is too low
     */
    public void withdraw(double amount)
            throws exception.InvalidAmountException,
                   exception.InsufficientBalanceException {
        if (amount <= 0) {
            throw new exception.InvalidAmountException(
                    "Withdrawal amount must be greater than zero. Provided: " + amount, amount);
        }
        if (amount > this.balance) {
            throw new exception.InsufficientBalanceException(
                    "Insufficient balance. Available: " + this.balance + ", Requested: " + amount,
                    this.balance, amount);
        }
        this.balance -= amount;
    }

    // ------------------------------------------------------------------
    // Getters (read-only where appropriate)
    // ------------------------------------------------------------------

    /** @return unique account number */
    public String getAccountNumber() { return accountNumber; }

    /** @return full name of the account holder */
    public String getCustomerName()  { return customerName;  }

    /** @return age of the account holder */
    public int getAge()              { return age;           }

    /** @return phone number */
    public String getPhone()         { return phone;         }

    /** @return residential address */
    public String getAddress()       { return address;       }

    /** @return e-mail address */
    public String getEmail()         { return email;         }

    /** @return current balance */
    public double getBalance()       { return balance;       }

    /** @return account creation date */
    public LocalDate getDateCreated() { return dateCreated;  }

    // ------------------------------------------------------------------
    // Setters (mutable fields only — accountNumber and dateCreated are final)
    // ------------------------------------------------------------------

    /** @param customerName updated name */
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    /** @param age updated age */
    public void setAge(int age)        { this.age   = age;   }

    /** @param phone updated phone */
    public void setPhone(String phone) { this.phone = phone; }

    /** @param address updated address */
    public void setAddress(String address) { this.address = address; }

    /** @param email updated e-mail */
    public void setEmail(String email) { this.email = email; }

    /**
     * Directly sets the balance — used ONLY by the file-loading mechanism.
     * All runtime changes should use {@link #deposit} or {@link #withdraw}.
     *
     * @param balance balance to restore
     */
    protected void setBalance(double balance) { this.balance = balance; }

    // ------------------------------------------------------------------
    // Common serialisation helper
    // ------------------------------------------------------------------

    /**
     * Returns the shared pipe-delimited prefix used by every account subclass.
     * Format: accountNumber|customerName|age|phone|address|email|accountType|balance|dateCreated
     *
     * @return common fields as a pipe-delimited string
     */
    protected String toFileStringBase() {
        return accountNumber
                + DELIMITER + customerName
                + DELIMITER + age
                + DELIMITER + phone
                + DELIMITER + address
                + DELIMITER + email
                + DELIMITER + getAccountType().name()
                + DELIMITER + balance
                + DELIMITER + dateCreated.format(DATE_FORMATTER);
    }

    // ------------------------------------------------------------------
    // Comparable — alphabetical order by account number
    // ------------------------------------------------------------------

    /**
     * Compares accounts by account number alphabetically.
     * Demonstrates: Comparable interface implementation.
     *
     * @param other the account to compare to
     * @return negative, zero, or positive as per String.compareTo
     */
    @Override
    public int compareTo(Account other) {
        return this.accountNumber.compareTo(other.accountNumber);
    }

    // ------------------------------------------------------------------
    // toString — detailed display
    // ------------------------------------------------------------------

    /**
     * Returns a formatted, human-readable summary of common account details.
     * Demonstrates: String handling and formatting.
     *
     * @return formatted account summary string
     */
    @Override
    public String toString() {
        return String.format(
                "  Account Number : %s%n" +
                "  Customer Name  : %s%n" +
                "  Age            : %d%n" +
                "  Phone          : %s%n" +
                "  Address        : %s%n" +
                "  Email          : %s%n" +
                "  Account Type   : %s%n" +
                "  Balance        : Rs. %.2f%n" +
                "  Date Created   : %s%n" +
                "  %s",
                accountNumber,
                customerName,
                age,
                phone,
                address,
                email,
                getAccountType().getDisplayName(),
                balance,
                dateCreated.format(DATE_FORMATTER),
                getTypeSpecificInfo());
    }
}
