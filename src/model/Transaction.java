package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Immutable value object representing a single financial transaction.
 * <p>
 * Demonstrates: Classes &amp; Objects, Encapsulation (private + getters),
 * Java Time API (LocalDate, LocalTime), Comparable, String handling,
 * Final keyword (immutable fields).
 * </p>
 */
public class Transaction implements Comparable<Transaction> {

    // ------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------

    /** Pipe delimiter used when serialising to/from the data file. */
    public static final String DELIMITER = "|";

    /** Date format used for display and file storage. */
    public static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /** Time format used for display and file storage. */
    public static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    // ------------------------------------------------------------------
    // Fields (all private — Encapsulation)
    // ------------------------------------------------------------------

    /** Unique transaction identifier (e.g. "TXN00001"). */
    private final String transactionId;

    /** Account number this transaction belongs to. */
    private final String accountNumber;

    /** Category of this transaction (DEPOSIT, WITHDRAWAL, etc.). */
    private final TransactionType transactionType;

    /** Monetary amount involved in this transaction. */
    private final double amount;

    /** Calendar date when the transaction occurred. */
    private final LocalDate date;

    /** Wall-clock time when the transaction occurred. */
    private final LocalTime time;

    /** Short description of the transaction purpose. */
    private final String description;

    // ------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------

    /**
     * Full constructor used when creating a brand-new transaction at runtime.
     * Date and time are captured automatically from the system clock.
     *
     * @param transactionId   unique transaction ID
     * @param accountNumber   owning account number
     * @param transactionType category of transaction
     * @param amount          monetary amount
     * @param description     brief description
     */
    public Transaction(String transactionId,
                       String accountNumber,
                       TransactionType transactionType,
                       double amount,
                       String description) {
        this.transactionId   = transactionId;
        this.accountNumber   = accountNumber;
        this.transactionType = transactionType;
        this.amount          = amount;
        this.description     = description;
        this.date            = LocalDate.now();
        this.time            = LocalTime.now();
    }

    /**
     * Reconstruction constructor used when loading a transaction from a file.
     * Accepts an explicit date and time instead of capturing the current moment.
     *
     * @param transactionId   unique transaction ID
     * @param accountNumber   owning account number
     * @param transactionType category of transaction
     * @param amount          monetary amount
     * @param description     brief description
     * @param date            original transaction date
     * @param time            original transaction time
     */
    public Transaction(String transactionId,
                       String accountNumber,
                       TransactionType transactionType,
                       double amount,
                       String description,
                       LocalDate date,
                       LocalTime time) {
        this.transactionId   = transactionId;
        this.accountNumber   = accountNumber;
        this.transactionType = transactionType;
        this.amount          = amount;
        this.description     = description;
        this.date            = date;
        this.time            = time;
    }

    // ------------------------------------------------------------------
    // Getters (no setters — this class is immutable)
    // ------------------------------------------------------------------

    /** @return unique transaction ID */
    public String getTransactionId()   { return transactionId;   }

    /** @return owning account number */
    public String getAccountNumber()   { return accountNumber;   }

    /** @return transaction category */
    public TransactionType getTransactionType() { return transactionType; }

    /** @return monetary amount */
    public double getAmount()          { return amount;          }

    /** @return date of transaction */
    public LocalDate getDate()         { return date;            }

    /** @return time of transaction */
    public LocalTime getTime()         { return time;            }

    /** @return short description */
    public String getDescription()     { return description;     }

    // ------------------------------------------------------------------
    // Comparable — enables sorting by date then time (newest last)
    // ------------------------------------------------------------------

    /**
     * Compares two transactions chronologically (older transactions sort before newer ones).
     * Demonstrates: Comparable interface and polymorphism.
     *
     * @param other the transaction to compare against
     * @return negative if this is earlier, zero if simultaneous, positive if later
     */
    @Override
    public int compareTo(Transaction other) {
        int dateCmp = this.date.compareTo(other.date);
        if (dateCmp != 0) {
            return dateCmp;
        }
        return this.time.compareTo(other.time);
    }

    // ------------------------------------------------------------------
    // Serialisation helpers
    // ------------------------------------------------------------------

    /**
     * Converts this transaction into a pipe-delimited string for file storage.
     * Format: transactionId|accountNumber|transactionType|amount|date|time|description
     *
     * @return pipe-delimited record string
     */
    public String toFileString() {
        return transactionId
                + DELIMITER + accountNumber
                + DELIMITER + transactionType.name()
                + DELIMITER + amount
                + DELIMITER + date.format(DATE_FORMATTER)
                + DELIMITER + time.format(TIME_FORMATTER)
                + DELIMITER + description;
    }

    /**
     * Reconstructs a Transaction from a pipe-delimited file record.
     *
     * @param line a single line read from transactions.txt
     * @return the reconstructed Transaction object
     * @throws IllegalArgumentException if the line is malformed
     */
    public static Transaction fromFileString(String line) {
        String[] parts = line.split("\\|", 7);
        if (parts.length < 7) {
            throw new IllegalArgumentException("Malformed transaction record: " + line);
        }
        String          txnId    = parts[0].trim();
        String          accNum   = parts[1].trim();
        TransactionType type     = TransactionType.fromString(parts[2].trim());
        double          amount   = Double.parseDouble(parts[3].trim());
        LocalDate       date     = LocalDate.parse(parts[4].trim(), DATE_FORMATTER);
        LocalTime       time     = LocalTime.parse(parts[5].trim(), TIME_FORMATTER);
        String          desc     = parts[6].trim();
        return new Transaction(txnId, accNum, type, amount, desc, date, time);
    }

    // ------------------------------------------------------------------
    // toString
    // ------------------------------------------------------------------

    /**
     * Returns a formatted, human-readable summary of this transaction.
     * Demonstrates: String handling and formatting.
     *
     * @return formatted transaction string
     */
    @Override
    public String toString() {
        return String.format(
                "  Transaction ID : %s%n" +
                "  Account No.    : %s%n" +
                "  Type           : %s%n" +
                "  Amount         : Rs. %.2f%n" +
                "  Date           : %s%n" +
                "  Time           : %s%n" +
                "  Description    : %s",
                transactionId,
                accountNumber,
                transactionType.getDisplayName(),
                amount,
                date.format(DATE_FORMATTER),
                time.format(TIME_FORMATTER),
                description);
    }
}
