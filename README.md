# 🏦 Banking Management System

> A feature-rich **Banking Management System** built using **Core Java** that demonstrates Object-Oriented Programming, Collections Framework, File Handling, Exception Handling, and Layered Architecture.

![Java](https://img.shields.io/badge/Java-Core-orange)
![OOP](https://img.shields.io/badge/OOP-Implemented-blue)
![Collections](https://img.shields.io/badge/Collections-Framework-green)
![File%20Handling](https://img.shields.io/badge/File-Handling-red)
![Status](https://img.shields.io/badge/Status-Completed-success)

---

## 📌 Project Overview

This project simulates a real-world banking system where users can:

- Create Savings and Current accounts
- Deposit and withdraw money
- Transfer funds securely
- View account details
- Check account balance
- Track transaction history
- Delete accounts
- Save and load data using text files

The application follows a layered architecture (`Model → Service → Repository → Utility`) and is designed as a portfolio project to demonstrate strong Core Java fundamentals.

---

# ✨ Features

- ✅ Create Savings & Current Accounts
- ✅ Deposit Money
- ✅ Withdraw Money
- ✅ Transfer Money
- ✅ Check Balance
- ✅ View Account Details
- ✅ View All Accounts
- ✅ Transaction History
- ✅ Delete Account
- ✅ Persistent Data Storage
- ✅ Automatic Data Saving on Exit
- ✅ Custom Exception Handling
- ✅ Input Validation

---

# 🛠️ Tech Stack

- Java 17+
- Core Java
- Object-Oriented Programming
- Collections Framework
- Exception Handling
- File Handling
- Java Time API

No external frameworks or libraries are used.

---

# 📂 Project Structure

```
BankingManagementSystem
│
├── src
│   ├── exception
│   ├── model
│   ├── repository
│   ├── service
│   ├── util
│   └── Main.java
│
├── data
│   ├── accounts.txt
│   └── transactions.txt
│
├── README.md
```

---

# 🚀 How to Run

## Clone Repository

```bash
git clone https://github.com/DebarpitMohanta/Banking_Management_System.git

cd Banking_Management_System
```

## Compile

### Windows PowerShell

```powershell
javac -d bin (Get-ChildItem src -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
```

### Linux / macOS

```bash
bash compile.sh
```

---

## Run

### Windows

```powershell
java -cp bin Main
```

### Linux / macOS

```bash
bash run.sh
```

---

# 💻 Sample Menu

```
==========================================
     BANKING MANAGEMENT SYSTEM
==========================================

1. Create Account
2. Deposit Money
3. Withdraw Money
4. Transfer Money
5. Check Balance
6. Account Details
7. View All Accounts
8. Transaction History
9. Delete Account
10. Save Data
11. Exit
```

---

# 📚 Core Java Concepts Used

- Object-Oriented Programming (OOP)
- Classes & Objects
- Encapsulation
- Inheritance
- Polymorphism
- Abstract Classes
- Interfaces
- Collections Framework (HashMap, ArrayList)
- Exception Handling
- Custom Exceptions
- File Handling
- Enums
- Java Time API
- Scanner
- Packages

---

# 🧠 Design Patterns

- Singleton Pattern
- Layered Architecture
- Service-Repository Pattern

---

# 📈 Future Improvements

- Database Integration (MySQL)
- JDBC Support
- Spring Boot REST API
- Authentication with PIN/Login
- PDF Account Statements
- Interest Calculator
- Loan Management Module
- Email Notifications

---

# 🎯 Learning Outcomes

This project helped reinforce:

- Designing scalable Java applications
- Applying OOP principles
- Organizing code into layers
- Managing persistent storage
- Creating reusable utility classes
- Handling exceptions cleanly
- Working with Java Collections

---

# ⭐ Resume Highlights

- Layered Architecture
- File-Based Data Persistence
- Custom Exception Handling
- Collections Framework
- Object-Oriented Design
- Console-Based Banking System

---

# 👨‍💻 Author

**Debarpit Mohanta**

🎓 B.Tech CSE, ITER, SOA University

- GitHub: https://github.com/DebarpitMohanta
- LinkedIn: https://www.linkedin.com/in/debarpit-mohanta-491a86211/

---

## ⭐ If you found this project useful, consider giving it a star!
