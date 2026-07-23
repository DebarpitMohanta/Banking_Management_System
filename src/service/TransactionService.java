package service;

import model.Transaction;
import model.TransactionType;
import repository.BankRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service responsible for creating and retrieving transaction records.
 * <p>
 * Demonstrates: Classes &amp; Objects, Encapsulation, Static Members (AtomicInteger counter),
 * Java Time API (used inside Transaction constructor), Collections (ArrayList via repository).
 * </p>
 */
public class TransactionService {

    // ------------------------------------------------------------------
    // Transaction ID counter — static so it persists across instances
    // ------------------------------------------------------------------

    /** Monotonically increasing counter for generating unique transaction IDs. */
    private static final AtomicInteger txnCounter = new AtomicInteger(1);

    /** Prefix for all generated transaction IDs. */
    private static final String TXN_PREFIX = "TXN";

    // ------------------------------------------------------------------
    // Dependencies
    // ------------------------------------------------------------------

    /** Shared repository that stores all transactions in memory. */
    private final BankRepository repository;

    // ------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------

    /**
     * Creates a TransactionService backed by the given repository.
     * Synchronises the internal ID counter with the number of transactions
     * already loaded from disk, so newly created IDs never conflict.
     *
     * @param repository the shared BankRepository instance
     */
    public TransactionService(BankRepository repository) {
        this.repository = repository;
        // Calibrate counter so loaded transactions don't cause ID collisions
        int loaded = repository.transactionCount();
        if (loaded >= txnCounter.get()) {
            txnCounter.set(loaded + 1);
        }
    }

    // ------------------------------------------------------------------
    // ID generation — Static Member + Method Overloading
    // ------------------------------------------------------------------

    /**
     * Generates the next unique transaction ID (e.g. "TXN00042").
     * Demonstrates: Static Members, String formatting.
     *
     * @return formatted transaction ID string
     */
    private String generateTransactionId() {
        return String.format("%s%05d", TXN_PREFIX, txnCounter.getAndIncrement());
    }

    // ------------------------------------------------------------------
    // Record operations — Method Overloading
    // ------------------------------------------------------------------

    /**
     * Records a deposit transaction.
     * Demonstrates: Method Overloading (same name, different parameters compared to transfer overloads).
     *
     * @param accountNumber the account that received the deposit
     * @param amount        the deposited amount
     * @return the recorded Transaction
     */
    public Transaction recordDeposit(String accountNumber, double amount) {
        Transaction txn = new Transaction(
                generateTransactionId(),
                accountNumber,
                TransactionType.DEPOSIT,
                amount,
                "Cash deposit");
        repository.addTransaction(txn);
        return txn;
    }

    /**
     * Records a withdrawal transaction.
     *
     * @param accountNumber the account that was debited
     * @param amount        the withdrawn amount
     * @return the recorded Transaction
     */
    public Transaction recordWithdrawal(String accountNumber, double amount) {
        Transaction txn = new Transaction(
                generateTransactionId(),
                accountNumber,
                TransactionType.WITHDRAWAL,
                amount,
                "Cash withdrawal");
        repository.addTransaction(txn);
        return txn;
    }

    /**
     * Records a deposit transaction with a custom description.
     * Demonstrates: Method Overloading.
     *
     * @param accountNumber the account that received the deposit
     * @param amount        the deposited amount
     * @param description   custom description
     * @return the recorded Transaction
     */
    public Transaction recordDeposit(String accountNumber, double amount, String description) {
        Transaction txn = new Transaction(
                generateTransactionId(),
                accountNumber,
                TransactionType.DEPOSIT,
                amount,
                description);
        repository.addTransaction(txn);
        return txn;
    }

    /**
     * Records a withdrawal transaction with a custom description.
     * Demonstrates: Method Overloading.
     *
     * @param accountNumber the account that was debited
     * @param amount        the withdrawn amount
     * @param description   custom description
     * @return the recorded Transaction
     */
    public Transaction recordWithdrawal(String accountNumber, double amount, String description) {
        Transaction txn = new Transaction(
                generateTransactionId(),
                accountNumber,
                TransactionType.WITHDRAWAL,
                amount,
                description);
        repository.addTransaction(txn);
        return txn;
    }

    /**
     * Records the outgoing leg of a fund transfer.
     *
     * @param fromAccountNumber source account number
     * @param toAccountNumber   destination account number
     * @param amount            transferred amount
     * @return the recorded outgoing Transaction
     */
    public Transaction recordTransferOut(String fromAccountNumber,
                                         String toAccountNumber,
                                         double amount) {
        Transaction txn = new Transaction(
                generateTransactionId(),
                fromAccountNumber,
                TransactionType.TRANSFER_OUT,
                amount,
                "Transfer to " + toAccountNumber);
        repository.addTransaction(txn);
        return txn;
    }

    /**
     * Records the incoming leg of a fund transfer.
     *
     * @param toAccountNumber   destination account number
     * @param fromAccountNumber source account number
     * @param amount            transferred amount
     * @return the recorded incoming Transaction
     */
    public Transaction recordTransferIn(String toAccountNumber,
                                        String fromAccountNumber,
                                        double amount) {
        Transaction txn = new Transaction(
                generateTransactionId(),
                toAccountNumber,
                TransactionType.TRANSFER_IN,
                amount,
                "Transfer from " + fromAccountNumber);
        repository.addTransaction(txn);
        return txn;
    }

    // ------------------------------------------------------------------
    // Query operations
    // ------------------------------------------------------------------

    /**
     * Returns the transaction history for a specific account.
     *
     * @param accountNumber the account whose history is requested
     * @return list of Transactions sorted chronologically
     */
    public List<Transaction> getTransactionHistory(String accountNumber) {
        return repository.findTransactionsByAccount(accountNumber);
    }

    /**
     * Returns every transaction in the system.
     *
     * @return all Transactions sorted chronologically
     */
    public List<Transaction> getAllTransactions() {
        return repository.findAllTransactions();
    }
}
