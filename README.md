# Banking Management System — Core Java

> A professional, console-based Banking Management System demonstrating every important Core Java concept. Built without any frameworks — no Spring Boot, no Hibernate, no external libraries.

---

## Project Overview

This project simulates a real-world banking system where users can create accounts, deposit and withdraw money, transfer funds, view transaction history, and persist all data to plain text files. It is designed to be **interview-ready**, **GitHub-ready**, and **resume-worthy** for Java freshers and enthusiasts.

---

## Features

| # | Feature | Description |
|---|---------|-------------|
| 1 | Create Account | Create Savings or Current accounts with full customer details |
| 2 | Deposit Money | Credit any amount to an account |
| 3 | Withdraw Money | Debit money with balance and minimum-balance checks |
| 4 | Transfer Money | Move funds between two accounts atomically |
| 5 | Check Balance | Instantly view the current balance |
| 6 | Account Details | View all details including type-specific fields |
| 7 | View All Accounts | Tabular listing of every account, sorted by number |
| 8 | Transaction History | Per-account, chronologically sorted ledger |
| 9 | Delete Account | Safely delete a zero-balance account |
| 10 | Save Data | Manually persist all data to disk |
| 11 | Exit | Auto-save option on exit |

---

## Technologies Used

- **Language**: Java 17+ (Core Java only)
- **No frameworks, no Maven, no Gradle, no external libraries**
- All I/O via `Scanner`, `BufferedReader`, `BufferedWriter`, `FileReader`, `FileWriter`
- All data in plain `accounts.txt` and `transactions.txt`

---

## Folder Structure

```
BankingManagementSystem/
├── src/
│   ├── Main.java                          ← Entry point + menu loop
│   │
│   ├── model/
│   │   ├── AccountType.java               ← Enum: SAVINGS, CURRENT
│   │   ├── TransactionType.java           ← Enum: DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT
│   │   ├── InterestBearing.java           ← Interface for accounts that earn interest
│   │   ├── Overdraftable.java             ← Interface for accounts with overdraft facility
│   │   ├── Account.java                   ← Abstract base class for all accounts
│   │   ├── SavingsAccount.java            ← Concrete: interest rate, minimum balance
│   │   ├── CurrentAccount.java            ← Concrete: overdraft limit
│   │   └── Transaction.java               ← Immutable transaction record
│   │
│   ├── service/
│   │   ├── BankService.java               ← Core banking operations (create, deposit, etc.)
│   │   └── TransactionService.java        ← Records and retrieves transactions
│   │
│   ├── repository/
│   │   └── BankRepository.java            ← In-memory store (HashMap + ArrayList), Singleton
│   │
│   ├── util/
│   │   ├── Validation.java                ← Static validators (amount, age, phone, email)
│   │   ├── FileHandler.java               ← BufferedReader/Writer for file persistence
│   │   └── AccountNumberGenerator.java    ← Generates sequential account numbers
│   │
│   └── exception/
│       ├── InvalidAmountException.java
│       ├── AccountNotFoundException.java
│       ├── InsufficientBalanceException.java
│       ├── DuplicateAccountException.java
│       ├── InvalidPhoneException.java
│       ├── InvalidAgeException.java
│       ├── TransferToSameAccountException.java
│       └── InvalidAccountTypeException.java
│
├── data/
│   ├── accounts.txt                        ← Pipe-delimited account records
│   └── transactions.txt                    ← Pipe-delimited transaction records
│
├── bin/                                    ← Compiled .class files (created by compile.sh)
├── compile.sh                              ← Compiles all sources
├── run.sh                                  ← Runs the application
└── README.md
```

---

## Screenshots

> _Run the application and replace these placeholders with actual terminal screenshots._

| Menu | Create Account | Transaction History |
|------|----------------|---------------------|
| ![menu](screenshots/menu.png) | ![create](screenshots/create_account.png) | ![history](screenshots/txn_history.png) |

---

## How to Run

### Prerequisites

- Java 17 or later installed
- Terminal / Command Prompt

### Steps

```bash
# 1. Clone the repository
git clone https://github.com/<your-username>/BankingManagementSystem.git
cd BankingManagementSystem

# 2. Compile all sources
bash compile.sh

# 3. Run the application
bash run.sh
```

> **Windows users:** Use Git Bash, WSL, or replace `bash` with `javac`/`java` commands directly:
> ```
> javac -d bin -sourcepath src src/exception/*.java src/model/*.java src/util/*.java src/repository/*.java src/service/*.java src/Main.java
> java -cp bin Main
> ```

---

## Java Concepts Used

| Concept | Where Used |
|---------|-----------|
| **Classes & Objects** | Every class in the project |
| **Encapsulation** | Private fields + getters/setters in `Account`, `Transaction` |
| **Constructors** | All model classes; overloaded constructors in `SavingsAccount`, `CurrentAccount` |
| **Inheritance** | `SavingsAccount` and `CurrentAccount` extend `Account` |
| **Polymorphism** | `account.withdraw()` calls subclass override at runtime |
| **Method Overloading** | `createSavingsAccount()`, `recordDeposit()` (multiple signatures) |
| **Method Overriding** | `withdraw()`, `getAccountType()`, `toFileString()`, `toString()` |
| **Interfaces** | `InterestBearing`, `Overdraftable`, `Comparable` |
| **Abstract Classes** | `Account` — defines the contract for all account types |
| **Static Members** | `AccountNumberGenerator.counter`, `Validation` methods, `BankRepository.getInstance()` |
| **Final Keyword** | Immutable fields in `Transaction`; constants in `Account`, `SavingsAccount` |
| **Collections** | `HashMap<String, Account>` in `BankRepository`; `ArrayList<Transaction>` |
| **Exception Handling** | `try-catch` blocks throughout `Main`; custom exceptions propagated through service layer |
| **Custom Exceptions** | 8 dedicated exception classes in the `exception` package |
| **File Handling** | `BufferedReader`, `BufferedWriter`, `FileReader`, `FileWriter` in `FileHandler` |
| **Scanner** | Console input in `Main` |
| **Packages** | `model`, `service`, `repository`, `util`, `exception` |
| **Java Time API** | `LocalDate`, `LocalTime`, `DateTimeFormatter` in `Account` and `Transaction` |
| **String Handling** | `String.format`, `split`, `trim`, `equalsIgnoreCase`, `isBlank` throughout |
| **Loops** | `while` menu loop, `for-each` in repository, `while` in safe-read helpers |
| **Switch** | Menu dispatch using enhanced switch expressions in `main()` |
| **Enums** | `AccountType`, `TransactionType` with display names and `fromString()` |
| **Comparable** | `Account.compareTo()` (by account number), `Transaction.compareTo()` (by date/time) |
| **Singleton Pattern** | `BankRepository.getInstance()` |
| **Shutdown Hook** | Auto-save on JVM exit via `Runtime.getRuntime().addShutdownHook()` |

---

## Data File Format

### accounts.txt
```
ACC10001|Rahul Sharma|25|9876543210|123 MG Road Bangalore|rahul@email.com|SAVINGS|15000.00|01-07-2025|4.0
ACC10002|Priya Patel|30|8765432109|456 Park Street Mumbai|priya@email.com|CURRENT|50000.00|01-07-2025|10000.0
```

### transactions.txt
```
TXN00001|ACC10001|DEPOSIT|10000.0|01-07-2025|10:30:00|Cash deposit
TXN00002|ACC10001|WITHDRAWAL|2000.0|01-07-2025|11:00:00|Cash withdrawal
```

---

## Future Improvements

- [ ] Add loan management module
- [ ] Implement interest calculation scheduler
- [ ] Add multi-user login with PIN authentication
- [ ] Export statements as PDF (using iText library)
- [ ] Add currency conversion module
- [ ] Implement account linking (joint accounts)
- [ ] Add notification system (email alerts via JavaMail)
- [ ] Migrate to a proper RDBMS (JDBC + PostgreSQL)

---

## Learning Outcomes

After studying this project you will understand:

1. **OOP Design** — how abstract classes, interfaces, and inheritance create a flexible class hierarchy
2. **Layered Architecture** — separating model, service, repository, and utility concerns
3. **Exception Strategy** — when to create custom exceptions vs. using built-in ones
4. **File Persistence** — serialising and deserialising objects without any framework
5. **Collections** — choosing the right collection (`HashMap` for O(1) lookup, `ArrayList` for ordered appending)
6. **Static vs. Instance** — when something truly belongs to the class, not to an object
7. **Immutability** — why `Transaction` fields are `final` and why that matters
8. **Interface Contracts** — how `InterestBearing` and `Overdraftable` describe *capability*, not *identity*

---

## Interview Questions

1. Why did you choose an abstract class for `Account` instead of an interface?
2. What is the difference between method overloading and method overriding? Show an example from this project.
3. Why is `Transaction` immutable? What would break if you added setters?
4. How does the Singleton pattern in `BankRepository` prevent data duplication?
5. What is the purpose of the `Comparable` interface in `Account` and `Transaction`?
6. Why do you use `HashMap` for accounts but `ArrayList` for transactions?
7. How would you make this application thread-safe if multiple users logged in concurrently?
8. How would you add a new account type (e.g., `FixedDepositAccount`) without modifying existing code? (Open/Closed Principle)
9. What happens to data if the JVM crashes mid-transfer? How would you handle atomicity?
10. How would you replace file storage with a database without changing `BankService`?

---

## Author

Built as a Core Java portfolio project demonstrating professional coding standards without using any external frameworks.

_Good luck with your interviews!_ 🚀
