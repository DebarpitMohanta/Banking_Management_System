package repository;

import model.Account;
import model.Transaction;
import util.FileHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * In-memory data store for accounts and transactions.
 * Acts as the single source of truth for all runtime data.
 * <p>
 * Demonstrates: Collections (HashMap, ArrayList), Encapsulation,
 * Static Members (singleton pattern), Loops, Comparable (sorting).
 * </p>
 *
 * <p>
 * This class follows a lightweight <em>Repository pattern</em>:
 * all business logic lives in the service layer; this class
 * only stores, retrieves, and persists data.
 * </p>
 */
public class BankRepository {

    // ------------------------------------------------------------------
    // Singleton — one shared repository for the whole application
    // ------------------------------------------------------------------

    /** The single instance of this repository. Demonstrates: Static Members. */
    private static BankRepository instance;

    /**
     * Returns the singleton instance, creating it on first call.
     * Demonstrates: Static method, lazy initialisation.
     *
     * @return the shared BankRepository instance
     */
    public static BankRepository getInstance() {
        if (instance == null) {
            instance = new BankRepository();
        }
        return instance;
    }

    // ------------------------------------------------------------------
    // In-memory storage
    // ------------------------------------------------------------------

    /**
     * Map from account number to Account object.
     * HashMap gives O(1) lookup — ideal for find-by-ID operations.
     * Demonstrates: Collections (HashMap).
     */
    private final Map<String, Account> accounts;

    /**
     * Ordered list of all transactions recorded during this session plus loaded from file.
     * ArrayList gives O(1) appends — ideal for an ever-growing ledger.
     * Demonstrates: Collections (ArrayList).
     */
    private final List<Transaction> transactions;

    // ------------------------------------------------------------------
    // Constructor — private (singleton)
    // ------------------------------------------------------------------

    /**
     * Private constructor loads existing data from file on first instantiation.
     * Demonstrates: Encapsulation (private constructor), File Handling delegation.
     */
    private BankRepository() {
        accounts     = new HashMap<>(FileHandler.loadAccounts());
        transactions = new ArrayList<>(FileHandler.loadTransactions());
    }

    // ------------------------------------------------------------------
    // Account CRUD operations
    // ------------------------------------------------------------------

    /**
     * Adds an account to the in-memory store.
     *
     * @param account the Account to add
     * @throws exception.DuplicateAccountException if an account with the same number already exists
     */
    public void addAccount(Account account) throws exception.DuplicateAccountException {
        if (accounts.containsKey(account.getAccountNumber())) {
            throw new exception.DuplicateAccountException(
                    "Account already exists: " + account.getAccountNumber(),
                    account.getAccountNumber());
        }
        accounts.put(account.getAccountNumber(), account);
    }

    /**
     * Retrieves an account by its account number.
     *
     * @param accountNumber the account number to look up
     * @return the matching Account
     * @throws exception.AccountNotFoundException if no account exists with that number
     */
    public Account findById(String accountNumber) throws exception.AccountNotFoundException {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new exception.AccountNotFoundException(
                    "Account not found: " + accountNumber, accountNumber);
        }
        return account;
    }

    /**
     * Returns all accounts as an unmodifiable sorted list (sorted by account number).
     * Demonstrates: Collections, Comparable, Loops.
     *
     * @return sorted, unmodifiable list of all accounts
     */
    public List<Account> findAll() {
        List<Account> list = new ArrayList<>(accounts.values());
        Collections.sort(list);          // uses Account.compareTo()
        return Collections.unmodifiableList(list);
    }

    /**
     * Removes an account from the in-memory store.
     *
     * @param accountNumber the account number to remove
     * @throws exception.AccountNotFoundException if no account exists with that number
     */
    public void deleteAccount(String accountNumber) throws exception.AccountNotFoundException {
        if (!accounts.containsKey(accountNumber)) {
            throw new exception.AccountNotFoundException(
                    "Cannot delete — account not found: " + accountNumber, accountNumber);
        }
        accounts.remove(accountNumber);
    }

    /**
     * Returns {@code true} if an account with the given number exists.
     *
     * @param accountNumber the account number to check
     * @return true if the account is present
     */
    public boolean exists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    /**
     * Returns the total number of accounts currently stored.
     *
     * @return account count
     */
    public int accountCount() {
        return accounts.size();
    }

    // ------------------------------------------------------------------
    // Transaction operations
    // ------------------------------------------------------------------

    /**
     * Records a transaction by appending it to the in-memory list.
     *
     * @param transaction the Transaction to record
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Returns all transactions for a specific account, sorted chronologically.
     * Demonstrates: Loops (using Comparable via sort), Collections.
     *
     * @param accountNumber the account whose transaction history is requested
     * @return sorted list of matching transactions (may be empty)
     */
    public List<Transaction> findTransactionsByAccount(String accountNumber) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction txn : transactions) {
            if (txn.getAccountNumber().equals(accountNumber)) {
                result.add(txn);
            }
        }
        Collections.sort(result);        // uses Transaction.compareTo()
        return result;
    }

    /**
     * Returns all transactions in the system, sorted chronologically.
     *
     * @return sorted, unmodifiable list of all transactions
     */
    public List<Transaction> findAllTransactions() {
        List<Transaction> sorted = new ArrayList<>(transactions);
        Collections.sort(sorted);
        return Collections.unmodifiableList(sorted);
    }

    /**
     * Returns the total number of transactions recorded.
     *
     * @return transaction count
     */
    public int transactionCount() {
        return transactions.size();
    }

    // ------------------------------------------------------------------
    // Persistence delegation
    // ------------------------------------------------------------------

    /**
     * Persists all accounts and transactions to disk via {@link FileHandler}.
     * Called explicitly by the menu ("Save Data") and automatically on exit.
     */
    public void saveAll() {
        FileHandler.saveAccounts(accounts);
        FileHandler.saveTransactions(transactions);
    }
}
