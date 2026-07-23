import exception.*;
import model.Account;
import model.AccountType;
import model.Transaction;
import repository.BankRepository;
import service.BankService;

import java.util.List;
import java.util.Scanner;

/**
 * Application entry point — contains the main menu loop.
 * <p>
 * Demonstrates: Scanner (console input), Switch statement, Loops, Exception Handling
 * (catch blocks for every custom exception), Static Members (constants for menu items),
 * String Handling, Polymorphism (prints any Account subclass via toString).
 * </p>
 */
public class Main {

    // ------------------------------------------------------------------
    // Constants — menu item numbers
    // ------------------------------------------------------------------

    private static final int MENU_CREATE_ACCOUNT    = 1;
    private static final int MENU_DEPOSIT           = 2;
    private static final int MENU_WITHDRAW          = 3;
    private static final int MENU_TRANSFER          = 4;
    private static final int MENU_CHECK_BALANCE     = 5;
    private static final int MENU_ACCOUNT_DETAILS   = 6;
    private static final int MENU_VIEW_ALL_ACCOUNTS = 7;
    private static final int MENU_TXN_HISTORY       = 8;
    private static final int MENU_DELETE_ACCOUNT    = 9;
    private static final int MENU_SAVE_DATA         = 10;
    private static final int MENU_EXIT              = 11;

    // ------------------------------------------------------------------
    // Shared resources
    // ------------------------------------------------------------------

    /** Single Scanner instance for all console input throughout the session. */
    private static final Scanner scanner = new Scanner(System.in);

    /** Core service that handles all business logic. */
    private static BankService bankService;

    // ------------------------------------------------------------------
    // Entry point
    // ------------------------------------------------------------------

    /**
     * Application entry point.
     * Initialises services, displays the main menu, and handles user input.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        printBanner();

        // Initialise repository (loads data from file automatically)
        BankRepository repository = BankRepository.getInstance();
        bankService = new BankService(repository);

        System.out.println("  Data loaded successfully. " +
                           repository.accountCount() + " account(s) found.\n");

        // Register a shutdown hook to auto-save on unexpected JVM exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            bankService.saveData();
            System.out.println("\n[System] Data auto-saved on exit.");
        }));

        // Main menu loop
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readIntSafe("  Enter Choice : ", 1, MENU_EXIT);

            switch (choice) {
                case MENU_CREATE_ACCOUNT    -> handleCreateAccount();
                case MENU_DEPOSIT           -> handleDeposit();
                case MENU_WITHDRAW          -> handleWithdraw();
                case MENU_TRANSFER          -> handleTransfer();
                case MENU_CHECK_BALANCE     -> handleCheckBalance();
                case MENU_ACCOUNT_DETAILS   -> handleAccountDetails();
                case MENU_VIEW_ALL_ACCOUNTS -> handleViewAllAccounts();
                case MENU_TXN_HISTORY       -> handleTransactionHistory();
                case MENU_DELETE_ACCOUNT    -> handleDeleteAccount();
                case MENU_SAVE_DATA         -> handleSaveData();
                case MENU_EXIT              -> running = handleExit();
                default                     -> System.out.println("  [!] Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    // ------------------------------------------------------------------
    // Menu display
    // ------------------------------------------------------------------

    private static void printBanner() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║      BANKING MANAGEMENT SYSTEM           ║");
        System.out.println("  ║      Core Java — Console Application     ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.println();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("  ==========================================");
        System.out.println("         BANKING MANAGEMENT SYSTEM          ");
        System.out.println("  ==========================================");
        System.out.printf("  %-3s %-25s%n",  "1.", "Create Account");
        System.out.printf("  %-3s %-25s%n",  "2.", "Deposit Money");
        System.out.printf("  %-3s %-25s%n",  "3.", "Withdraw Money");
        System.out.printf("  %-3s %-25s%n",  "4.", "Transfer Money");
        System.out.printf("  %-3s %-25s%n",  "5.", "Check Balance");
        System.out.printf("  %-3s %-25s%n",  "6.", "Account Details");
        System.out.printf("  %-3s %-25s%n",  "7.", "View All Accounts");
        System.out.printf("  %-3s %-25s%n",  "8.", "Transaction History");
        System.out.printf("  %-3s %-25s%n",  "9.", "Delete Account");
        System.out.printf("  %-3s %-25s%n", "10.", "Save Data");
        System.out.printf("  %-3s %-25s%n", "11.", "Exit");
        System.out.println("  ==========================================");
    }

    // ------------------------------------------------------------------
    // Handler — 1. Create Account
    // ------------------------------------------------------------------

    private static void handleCreateAccount() {
        printSectionHeader("CREATE ACCOUNT");
        System.out.println("  Account Types:");
        System.out.println("    1. Savings Account");
        System.out.println("    2. Current Account");

        int typeChoice = readIntSafe("  Select Account Type (1/2) : ", 1, 2);
        AccountType type = (typeChoice == 1) ? AccountType.SAVINGS : AccountType.CURRENT;

        System.out.print("  Customer Name  : ");
        String name = scanner.nextLine().trim();

        int age = readIntSafe("  Age            : ", Account.MIN_AGE, Account.MAX_AGE);

        System.out.print("  Phone Number   : ");
        String phone = scanner.nextLine().trim();

        System.out.print("  Address        : ");
        String address = scanner.nextLine().trim();

        System.out.print("  Email Address  : ");
        String email = scanner.nextLine().trim();

        try {
            Account account;
            if (type == AccountType.SAVINGS) {
                account = bankService.createSavingsAccount(name, age, phone, address, email);
            } else {
                account = bankService.createCurrentAccount(name, age, phone, address, email);
            }
            printSuccess("Account created successfully!");
            System.out.println();
            System.out.println(account);
        } catch (InvalidPhoneException e) {
            printError("Invalid phone number: " + e.getMessage());
        } catch (InvalidAgeException e) {
            printError("Invalid age: " + e.getMessage());
        } catch (DuplicateAccountException e) {
            printError("Account already exists: " + e.getMessage());
        } catch (Exception e) {
            printError("Error creating account: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Handler — 2. Deposit
    // ------------------------------------------------------------------

    private static void handleDeposit() {
        printSectionHeader("DEPOSIT MONEY");
        System.out.print("  Account Number : ");
        String accNum = scanner.nextLine().trim();

        double amount = readDoubleSafe("  Deposit Amount : Rs. ");

        try {
            Transaction txn = bankService.deposit(accNum, amount);
            printSuccess(String.format("Rs. %.2f deposited successfully!", amount));
            System.out.println();
            System.out.println(txn);
            Account account = bankService.getAccountDetails(accNum);
            System.out.printf("%n  Updated Balance : Rs. %.2f%n", account.getBalance());
        } catch (AccountNotFoundException e) {
            printError("Account not found: " + e.getMessage());
        } catch (InvalidAmountException e) {
            printError("Invalid amount: " + e.getMessage());
        } catch (Exception e) {
            printError("Deposit failed: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Handler — 3. Withdraw
    // ------------------------------------------------------------------

    private static void handleWithdraw() {
        printSectionHeader("WITHDRAW MONEY");
        System.out.print("  Account Number : ");
        String accNum = scanner.nextLine().trim();

        double amount = readDoubleSafe("  Withdraw Amount: Rs. ");

        try {
            Transaction txn = bankService.withdraw(accNum, amount);
            printSuccess(String.format("Rs. %.2f withdrawn successfully!", amount));
            System.out.println();
            System.out.println(txn);
            Account account = bankService.getAccountDetails(accNum);
            System.out.printf("%n  Updated Balance : Rs. %.2f%n", account.getBalance());
        } catch (AccountNotFoundException e) {
            printError("Account not found: " + e.getMessage());
        } catch (InvalidAmountException e) {
            printError("Invalid amount: " + e.getMessage());
        } catch (InsufficientBalanceException e) {
            printError("Insufficient balance: " + e.getMessage());
        } catch (Exception e) {
            printError("Withdrawal failed: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Handler — 4. Transfer
    // ------------------------------------------------------------------

    private static void handleTransfer() {
        printSectionHeader("TRANSFER MONEY");
        System.out.print("  From Account   : ");
        String fromAccNum = scanner.nextLine().trim();

        System.out.print("  To Account     : ");
        String toAccNum = scanner.nextLine().trim();

        double amount = readDoubleSafe("  Transfer Amount: Rs. ");

        try {
            bankService.transfer(fromAccNum, toAccNum, amount);
            printSuccess(String.format("Rs. %.2f transferred from %s to %s successfully!",
                                       amount, fromAccNum, toAccNum));
            Account from = bankService.getAccountDetails(fromAccNum);
            System.out.printf("  %s New Balance: Rs. %.2f%n", fromAccNum, from.getBalance());
        } catch (TransferToSameAccountException e) {
            printError("Cannot transfer to the same account: " + e.getMessage());
        } catch (AccountNotFoundException e) {
            printError("Account not found: " + e.getMessage());
        } catch (InvalidAmountException e) {
            printError("Invalid amount: " + e.getMessage());
        } catch (InsufficientBalanceException e) {
            printError("Insufficient balance: " + e.getMessage());
        } catch (Exception e) {
            printError("Transfer failed: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Handler — 5. Check Balance
    // ------------------------------------------------------------------

    private static void handleCheckBalance() {
        printSectionHeader("CHECK BALANCE");
        System.out.print("  Account Number : ");
        String accNum = scanner.nextLine().trim();

        try {
            double balance = bankService.checkBalance(accNum);
            Account account = bankService.getAccountDetails(accNum);
            System.out.println();
            System.out.printf("  Account        : %s%n", accNum);
            System.out.printf("  Holder         : %s%n", account.getCustomerName());
            System.out.printf("  Account Type   : %s%n", account.getAccountType().getDisplayName());
            System.out.printf("  Balance        : Rs. %.2f%n", balance);
        } catch (AccountNotFoundException e) {
            printError("Account not found: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Handler — 6. Account Details
    // ------------------------------------------------------------------

    private static void handleAccountDetails() {
        printSectionHeader("ACCOUNT DETAILS");
        System.out.print("  Account Number : ");
        String accNum = scanner.nextLine().trim();

        try {
            Account account = bankService.getAccountDetails(accNum);
            System.out.println();
            System.out.println(account);
        } catch (AccountNotFoundException e) {
            printError("Account not found: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Handler — 7. View All Accounts
    // ------------------------------------------------------------------

    private static void handleViewAllAccounts() {
        printSectionHeader("ALL ACCOUNTS");
        List<Account> accounts = bankService.getAllAccounts();

        if (accounts.isEmpty()) {
            System.out.println("  No accounts found in the system.");
            return;
        }

        System.out.printf("  Total accounts : %d%n%n", accounts.size());
        System.out.printf("  %-12s %-20s %-10s %-15s %-15s%n",
                          "Account No.", "Name", "Type", "Balance", "Date Created");
        System.out.println("  " + "-".repeat(75));

        for (Account account : accounts) {
            System.out.printf("  %-12s %-20s %-10s %-15s %-15s%n",
                              account.getAccountNumber(),
                              account.getCustomerName(),
                              account.getAccountType().name(),
                              String.format("Rs. %.2f", account.getBalance()),
                              account.getDateCreated().format(model.Account.DATE_FORMATTER));
        }
    }

    // ------------------------------------------------------------------
    // Handler — 8. Transaction History
    // ------------------------------------------------------------------

    private static void handleTransactionHistory() {
        printSectionHeader("TRANSACTION HISTORY");
        System.out.print("  Account Number : ");
        String accNum = scanner.nextLine().trim();

        try {
            List<Transaction> history = bankService.getTransactionHistory(accNum);
            if (history.isEmpty()) {
                System.out.println("  No transactions found for account: " + accNum);
                return;
            }
            System.out.printf("%n  Transactions for account %s (%d record(s)):%n%n",
                              accNum, history.size());
            for (Transaction txn : history) {
                System.out.println("  " + "-".repeat(50));
                System.out.println(txn);
            }
            System.out.println("  " + "-".repeat(50));
        } catch (AccountNotFoundException e) {
            printError("Account not found: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Handler — 9. Delete Account
    // ------------------------------------------------------------------

    private static void handleDeleteAccount() {
        printSectionHeader("DELETE ACCOUNT");
        System.out.print("  Account Number : ");
        String accNum = scanner.nextLine().trim();

        try {
            Account account = bankService.getAccountDetails(accNum);
            System.out.println();
            System.out.printf("  Account : %s | Holder : %s | Balance : Rs. %.2f%n",
                              account.getAccountNumber(),
                              account.getCustomerName(),
                              account.getBalance());
            System.out.print("  Are you sure you want to delete this account? (yes/no) : ");
            String confirm = scanner.nextLine().trim();
            if (!confirm.equalsIgnoreCase("yes")) {
                System.out.println("  [i] Account deletion cancelled.");
                return;
            }
            bankService.deleteAccount(accNum);
            printSuccess("Account " + accNum + " deleted successfully.");
        } catch (AccountNotFoundException e) {
            printError("Account not found: " + e.getMessage());
        } catch (IllegalStateException e) {
            printError(e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Handler — 10. Save Data
    // ------------------------------------------------------------------

    private static void handleSaveData() {
        printSectionHeader("SAVE DATA");
        bankService.saveData();
        printSuccess("All data saved successfully to disk.");
    }

    // ------------------------------------------------------------------
    // Handler — 11. Exit
    // ------------------------------------------------------------------

    private static boolean handleExit() {
        System.out.print("  Save data before exiting? (yes/no) : ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("yes")) {
            bankService.saveData();
            System.out.println("  [✓] Data saved.");
        }
        System.out.println();
        System.out.println("  ==========================================");
        System.out.println("   Thank you for using Banking Management   ");
        System.out.println("   System. Goodbye!                         ");
        System.out.println("  ==========================================");
        System.out.println();
        return false;  // signals the main loop to stop
    }

    // ------------------------------------------------------------------
    // UI helper methods
    // ------------------------------------------------------------------

    /**
     * Prints a formatted section header.
     *
     * @param title section title to display
     */
    private static void printSectionHeader(String title) {
        System.out.println();
        System.out.println("  ------------------------------------------");
        System.out.println("   " + title);
        System.out.println("  ------------------------------------------");
    }

    /**
     * Prints a success message.
     *
     * @param message the success text
     */
    private static void printSuccess(String message) {
        System.out.println("  [✓] " + message);
    }

    /**
     * Prints an error message.
     *
     * @param message the error text
     */
    private static void printError(String message) {
        System.out.println("  [✗] ERROR: " + message);
    }

    /**
     * Reads an integer from the console, re-prompting on invalid input.
     * Demonstrates: Loops, Exception Handling, Scanner.
     *
     * @param prompt the prompt to display
     * @param min    minimum acceptable value (inclusive)
     * @param max    maximum acceptable value (inclusive)
     * @return valid integer in [min, max]
     */
    private static int readIntSafe(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("  [!] Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid input. Please enter a whole number.");
            }
        }
    }

    /**
     * Reads a positive double from the console, re-prompting on invalid input.
     * Demonstrates: Loops, Exception Handling, Scanner.
     *
     * @param prompt the prompt to display
     * @return valid positive double
     */
    private static double readDoubleSafe(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (value > 0) {
                    return value;
                }
                System.out.println("  [!] Amount must be greater than zero.");
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid input. Please enter a numeric amount.");
            }
        }
    }
}
