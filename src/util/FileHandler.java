package util;

import model.Account;
import model.AccountType;
import model.CurrentAccount;
import model.SavingsAccount;
import model.Transaction;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class that handles all file I/O for persisting accounts and transactions.
 * <p>
 * Demonstrates: File Handling (FileWriter, FileReader, BufferedWriter, BufferedReader),
 * Exception Handling, Static Members, Loops, String Handling.
 * </p>
 *
 * <p>
 * Data files live inside a {@code data/} directory relative to the working directory:
 * <ul>
 *   <li>{@code data/accounts.txt}     — one account per line, pipe-delimited</li>
 *   <li>{@code data/transactions.txt} — one transaction per line, pipe-delimited</li>
 * </ul>
 * </p>
 */
public final class FileHandler {

    // ------------------------------------------------------------------
    // Constants — file paths
    // ------------------------------------------------------------------

    /** Directory under which all data files are stored. */
    public static final String DATA_DIR = "data";

    /** Path to the accounts data file. */
    public static final String ACCOUNTS_FILE = DATA_DIR + File.separator + "accounts.txt";

    /** Path to the transactions data file. */
    public static final String TRANSACTIONS_FILE = DATA_DIR + File.separator + "transactions.txt";

    // ------------------------------------------------------------------
    // Private constructor — utility class
    // ------------------------------------------------------------------

    private FileHandler() {
        throw new UnsupportedOperationException("Utility class — do not instantiate.");
    }

    // ------------------------------------------------------------------
    // Directory / file initialisation
    // ------------------------------------------------------------------

    /**
     * Ensures the {@code data/} directory and both data files exist.
     * Creates them if they are absent. Safe to call on every startup.
     * Demonstrates: File Handling.
     */
    public static void initialise() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        createIfAbsent(ACCOUNTS_FILE);
        createIfAbsent(TRANSACTIONS_FILE);
    }

    /**
     * Creates the file at the given path if it does not already exist.
     *
     * @param path file path to create
     */
    private static void createIfAbsent(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("[FileHandler] Could not create file: " + path + " — " + e.getMessage());
            }
        }
    }

    // ------------------------------------------------------------------
    // Save accounts
    // ------------------------------------------------------------------

    /**
     * Writes all accounts in the supplied map to {@code data/accounts.txt}.
     * Each account is serialised to one pipe-delimited line via its
     * {@link Account#toFileString()} method.
     * Demonstrates: BufferedWriter, FileWriter, Loops.
     *
     * @param accounts map of accountNumber → Account to persist
     */
    public static void saveAccounts(Map<String, Account> accounts) {
        initialise();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNTS_FILE, false))) {
            for (Account account : accounts.values()) {
                writer.write(account.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("[FileHandler] Error saving accounts: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Load accounts
    // ------------------------------------------------------------------

    /**
     * Reads all accounts from {@code data/accounts.txt} and returns them
     * as a HashMap keyed by account number.
     * Also recalibrates the {@link AccountNumberGenerator} counter so that
     * new accounts receive IDs beyond those already on disk.
     * Demonstrates: BufferedReader, FileReader, Loops, String handling.
     *
     * @return map of accountNumber → Account, possibly empty
     */
    public static Map<String, Account> loadAccounts() {
        initialise();
        Map<String, Account> accounts = new HashMap<>();
        int maxSequence = AccountNumberGenerator.STARTING_SEQUENCE - 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    Account account = parseAccount(line);
                    accounts.put(account.getAccountNumber(), account);
                    int seq = AccountNumberGenerator.extractSequence(account.getAccountNumber());
                    if (seq > maxSequence) {
                        maxSequence = seq;
                    }
                } catch (Exception e) {
                    System.err.println("[FileHandler] Skipping malformed account record: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[FileHandler] Error loading accounts: " + e.getMessage());
        }

        // Advance the counter so the next generated account number won't collide
        AccountNumberGenerator.setCounter(maxSequence + 1);
        return accounts;
    }

    /**
     * Parses a single pipe-delimited line into an Account subclass instance.
     * The 7th field (index 6) contains the account type and determines which
     * subclass to instantiate.
     *
     * @param line pipe-delimited record read from the file
     * @return the reconstructed Account object
     */
    private static Account parseAccount(String line) {
        // Format: accNum|name|age|phone|address|email|accountType|balance|dateCreated|specificField
        String[] parts = line.split("\\|", 10);
        if (parts.length < 10) {
            throw new IllegalArgumentException("Expected 10 fields, got " + parts.length);
        }
        String    accNum      = parts[0].trim();
        String    name        = parts[1].trim();
        int       age         = Integer.parseInt(parts[2].trim());
        String    phone       = parts[3].trim();
        String    address     = parts[4].trim();
        String    email       = parts[5].trim();
        AccountType type      = AccountType.fromString(parts[6].trim());
        double    balance     = Double.parseDouble(parts[7].trim());
        LocalDate dateCreated = LocalDate.parse(parts[8].trim(), Account.DATE_FORMATTER);
        String    specific    = parts[9].trim();

        return switch (type) {
            case SAVINGS -> new SavingsAccount(accNum, name, age, phone, address, email,
                                               balance, dateCreated,
                                               Double.parseDouble(specific));
            case CURRENT -> new CurrentAccount(accNum, name, age, phone, address, email,
                                               balance, dateCreated,
                                               Double.parseDouble(specific));
        };
    }

    // ------------------------------------------------------------------
    // Save transactions
    // ------------------------------------------------------------------

    /**
     * Appends all transactions in the supplied list to {@code data/transactions.txt}.
     * Overwrites the file from scratch to avoid duplication on repeated saves.
     * Demonstrates: BufferedWriter, FileWriter, Loops.
     *
     * @param transactions list of Transaction objects to persist
     */
    public static void saveTransactions(List<Transaction> transactions) {
        initialise();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE, false))) {
            for (Transaction txn : transactions) {
                writer.write(txn.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("[FileHandler] Error saving transactions: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Load transactions
    // ------------------------------------------------------------------

    /**
     * Reads all transactions from {@code data/transactions.txt} and returns them
     * as an ArrayList ordered as they appear in the file.
     * Demonstrates: BufferedReader, FileReader, ArrayList, Loops.
     *
     * @return list of Transaction objects, possibly empty
     */
    public static List<Transaction> loadTransactions() {
        initialise();
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    transactions.add(Transaction.fromFileString(line));
                } catch (Exception e) {
                    System.err.println("[FileHandler] Skipping malformed transaction record: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[FileHandler] Error loading transactions: " + e.getMessage());
        }

        return transactions;
    }
}
