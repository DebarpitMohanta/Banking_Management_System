package service;

import exception.*;
import model.*;
import repository.BankRepository;
import util.AccountNumberGenerator;
import util.Validation;

import java.util.List;

/**
 * Core service that orchestrates all banking operations.
 * This is the primary entry point for the application's business logic.
 * <p>
 * Demonstrates: Classes &amp; Objects, Encapsulation, Method Overloading
 * (multiple createAccount signatures), Exception Handling (propagates and catches
 * custom exceptions), Polymorphism (works with Account references, not concrete types),
 * Static Members (delegated to AccountNumberGenerator).
 * </p>
 */
public class BankService {

    // ------------------------------------------------------------------
    // Dependencies
    // ------------------------------------------------------------------

    /** The single shared repository — source of truth for all data. */
    private final BankRepository repository;

    /** Service responsible for recording all financial transactions. */
    private final TransactionService transactionService;

    // ------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------

    /**
     * Creates a BankService with the given repository and a fresh TransactionService.
     *
     * @param repository the shared BankRepository instance
     */
    public BankService(BankRepository repository) {
        this.repository         = repository;
        this.transactionService = new TransactionService(repository);
    }

    // ------------------------------------------------------------------
    // 1. Create Account — Method Overloading
    // ------------------------------------------------------------------

    /**
     * Creates a Savings Account with the default interest rate.
     * Demonstrates: Method Overloading, Polymorphism, Custom Exception.
     *
     * @param customerName customer's full name
     * @param age          customer's age
     * @param phone        10-digit mobile number
     * @param address      residential address
     * @param email        e-mail address
     * @return the newly created SavingsAccount
     * @throws InvalidPhoneException      if phone format is invalid
     * @throws InvalidAgeException        if age is out of range
     * @throws DuplicateAccountException  (unlikely but guarded) if ID already exists
     */
    public Account createSavingsAccount(String customerName,
                                        int age,
                                        String phone,
                                        String address,
                                        String email)
            throws InvalidPhoneException, InvalidAgeException, DuplicateAccountException {
        return createSavingsAccount(customerName, age, phone, address, email,
                                    SavingsAccount.DEFAULT_INTEREST_RATE);
    }

    /**
     * Creates a Savings Account with a custom interest rate.
     * Demonstrates: Method Overloading.
     *
     * @param customerName customer's full name
     * @param age          customer's age
     * @param phone        10-digit mobile number
     * @param address      residential address
     * @param email        e-mail address
     * @param interestRate custom annual interest rate (%)
     * @return the newly created SavingsAccount
     * @throws InvalidPhoneException     if phone format is invalid
     * @throws InvalidAgeException       if age is out of range
     * @throws DuplicateAccountException if ID already exists
     */
    public Account createSavingsAccount(String customerName,
                                        int age,
                                        String phone,
                                        String address,
                                        String email,
                                        double interestRate)
            throws InvalidPhoneException, InvalidAgeException, DuplicateAccountException {
        validateCommonFields(customerName, age, phone, email, address);
        String accountNumber = AccountNumberGenerator.generate();
        SavingsAccount account = new SavingsAccount(accountNumber, customerName.trim(),
                                                    age, phone.trim(), address.trim(),
                                                    email.trim(), interestRate);
        repository.addAccount(account);
        return account;
    }

    /**
     * Creates a Current Account with the default overdraft limit.
     * Demonstrates: Method Overloading.
     *
     * @param customerName customer's full name
     * @param age          customer's age
     * @param phone        10-digit mobile number
     * @param address      residential address
     * @param email        e-mail address
     * @return the newly created CurrentAccount
     * @throws InvalidPhoneException     if phone format is invalid
     * @throws InvalidAgeException       if age is out of range
     * @throws DuplicateAccountException if ID already exists
     */
    public Account createCurrentAccount(String customerName,
                                         int age,
                                         String phone,
                                         String address,
                                         String email)
            throws InvalidPhoneException, InvalidAgeException, DuplicateAccountException {
        return createCurrentAccount(customerName, age, phone, address, email,
                                    CurrentAccount.DEFAULT_OVERDRAFT_LIMIT);
    }

    /**
     * Creates a Current Account with a custom overdraft limit.
     * Demonstrates: Method Overloading.
     *
     * @param customerName   customer's full name
     * @param age            customer's age
     * @param phone          10-digit mobile number
     * @param address        residential address
     * @param email          e-mail address
     * @param overdraftLimit custom overdraft ceiling
     * @return the newly created CurrentAccount
     * @throws InvalidPhoneException     if phone format is invalid
     * @throws InvalidAgeException       if age is out of range
     * @throws DuplicateAccountException if ID already exists
     */
    public Account createCurrentAccount(String customerName,
                                         int age,
                                         String phone,
                                         String address,
                                         String email,
                                         double overdraftLimit)
            throws InvalidPhoneException, InvalidAgeException, DuplicateAccountException {
        validateCommonFields(customerName, age, phone, email, address);
        String accountNumber = AccountNumberGenerator.generate();
        CurrentAccount account = new CurrentAccount(accountNumber, customerName.trim(),
                                                    age, phone.trim(), address.trim(),
                                                    email.trim(), overdraftLimit);
        repository.addAccount(account);
        return account;
    }

    // ------------------------------------------------------------------
    // 2. Deposit
    // ------------------------------------------------------------------

    /**
     * Deposits money into the specified account and records the transaction.
     *
     * @param accountNumber the target account number
     * @param amount        the amount to deposit
     * @return the recorded Transaction
     * @throws AccountNotFoundException if the account does not exist
     * @throws InvalidAmountException   if the amount is invalid
     */
    public Transaction deposit(String accountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException {
        Validation.validateAmount(amount);
        Account account = repository.findById(accountNumber);
        account.deposit(amount);
        return transactionService.recordDeposit(accountNumber, amount);
    }

    // ------------------------------------------------------------------
    // 3. Withdraw
    // ------------------------------------------------------------------

    /**
     * Withdraws money from the specified account and records the transaction.
     *
     * @param accountNumber the source account number
     * @param amount        the amount to withdraw
     * @return the recorded Transaction
     * @throws AccountNotFoundException       if the account does not exist
     * @throws InvalidAmountException         if the amount is invalid
     * @throws InsufficientBalanceException   if there are insufficient funds
     */
    public Transaction withdraw(String accountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException, InsufficientBalanceException {
        Validation.validateAmount(amount);
        Account account = repository.findById(accountNumber);
        account.withdraw(amount);          // Polymorphism — calls subclass override if present
        return transactionService.recordWithdrawal(accountNumber, amount);
    }

    // ------------------------------------------------------------------
    // 4. Transfer
    // ------------------------------------------------------------------

    /**
     * Transfers money from one account to another and records both legs as transactions.
     *
     * @param fromAccountNumber source account number
     * @param toAccountNumber   destination account number
     * @param amount            the amount to transfer
     * @throws AccountNotFoundException        if either account does not exist
     * @throws InvalidAmountException          if the amount is invalid
     * @throws InsufficientBalanceException    if the source account lacks sufficient funds
     * @throws TransferToSameAccountException  if source and destination are the same
     */
    public void transfer(String fromAccountNumber, String toAccountNumber, double amount)
            throws AccountNotFoundException,
                   InvalidAmountException,
                   InsufficientBalanceException,
                   TransferToSameAccountException {
        if (fromAccountNumber.equalsIgnoreCase(toAccountNumber)) {
            throw new TransferToSameAccountException(
                    "Source and destination accounts cannot be the same.", fromAccountNumber);
        }
        Validation.validateAmount(amount);
        Account fromAccount = repository.findById(fromAccountNumber);
        Account toAccount   = repository.findById(toAccountNumber);

        // Debit source — may throw InsufficientBalanceException
        fromAccount.withdraw(amount);
        // Credit destination
        toAccount.deposit(amount);

        // Record both legs
        transactionService.recordTransferOut(fromAccountNumber, toAccountNumber, amount);
        transactionService.recordTransferIn(toAccountNumber, fromAccountNumber, amount);
    }

    // ------------------------------------------------------------------
    // 5. Check Balance
    // ------------------------------------------------------------------

    /**
     * Returns the current balance of the specified account.
     *
     * @param accountNumber the account number to query
     * @return the current balance
     * @throws AccountNotFoundException if the account does not exist
     */
    public double checkBalance(String accountNumber) throws AccountNotFoundException {
        return repository.findById(accountNumber).getBalance();
    }

    // ------------------------------------------------------------------
    // 6. Account Details
    // ------------------------------------------------------------------

    /**
     * Returns the Account object for detailed display.
     *
     * @param accountNumber the account number to look up
     * @return the Account
     * @throws AccountNotFoundException if the account does not exist
     */
    public Account getAccountDetails(String accountNumber) throws AccountNotFoundException {
        return repository.findById(accountNumber);
    }

    // ------------------------------------------------------------------
    // 7. View All Accounts
    // ------------------------------------------------------------------

    /**
     * Returns all accounts sorted by account number.
     *
     * @return sorted list of all Account objects
     */
    public List<Account> getAllAccounts() {
        return repository.findAll();
    }

    // ------------------------------------------------------------------
    // 8. Transaction History
    // ------------------------------------------------------------------

    /**
     * Returns the transaction history for a specific account.
     *
     * @param accountNumber the account number
     * @return list of Transactions sorted chronologically
     * @throws AccountNotFoundException if the account does not exist
     */
    public List<Transaction> getTransactionHistory(String accountNumber)
            throws AccountNotFoundException {
        repository.findById(accountNumber);    // Validates account exists
        return transactionService.getTransactionHistory(accountNumber);
    }

    // ------------------------------------------------------------------
    // 9. Delete Account
    // ------------------------------------------------------------------

    /**
     * Deletes the specified account from the system.
     * Requires the balance to be zero before deletion is permitted.
     *
     * @param accountNumber the account number to delete
     * @throws AccountNotFoundException       if the account does not exist
     * @throws IllegalStateException          if the account balance is non-zero
     */
    public void deleteAccount(String accountNumber) throws AccountNotFoundException {
        Account account = repository.findById(accountNumber);
        if (account.getBalance() != 0.0) {
            throw new IllegalStateException(
                    String.format("Cannot delete account %s — balance must be zero first. " +
                                  "Current balance: Rs. %.2f", accountNumber, account.getBalance()));
        }
        repository.deleteAccount(accountNumber);
    }

    // ------------------------------------------------------------------
    // 10. Save Data
    // ------------------------------------------------------------------

    /**
     * Persists all in-memory data to disk.
     */
    public void saveData() {
        repository.saveAll();
    }

    // ------------------------------------------------------------------
    // Helper / convenience methods
    // ------------------------------------------------------------------

    /**
     * Returns the total number of accounts in the system.
     *
     * @return account count
     */
    public int getTotalAccounts() {
        return repository.accountCount();
    }

    // ------------------------------------------------------------------
    // Private validation helper
    // ------------------------------------------------------------------

    /**
     * Validates all fields common to both account types.
     * Extracts shared validation into one place to avoid duplication.
     * Demonstrates: DRY principle, Custom Exception propagation.
     *
     * @param name    customer name
     * @param age     customer age
     * @param phone   mobile number
     * @param email   e-mail address
     * @param address residential address
     * @throws InvalidPhoneException if phone is invalid
     * @throws InvalidAgeException   if age is out of range
     */
    private void validateCommonFields(String name, int age, String phone,
                                      String email, String address)
            throws InvalidPhoneException, InvalidAgeException {
        Validation.validateName(name, "Customer name");
        Validation.validateAge(age);
        Validation.validatePhone(phone);
        Validation.validateEmail(email);
        Validation.validateNotBlank(address, "Address");
    }
}
