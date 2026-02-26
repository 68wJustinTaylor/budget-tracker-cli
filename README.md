# Budget Tracker (Java CLI Application)

A fully-featured command-line budget tracking application built in Java.

This project demonstrates clean architecture, persistent storage, full CRUD operations, robust input validation, and monthly financial analytics.

---

## Features

- Add transactions (income or expense)
- Edit transactions by UUID
- Delete transaction by UUID
- List transactions (sorted: newest first)
- Monthly summary reporting
- Monthly category breakdown (expenses only)
- CSV persistence (auto-load on startup, auto-save on change)
- Robust input validation (no crashes on invalid input)
- UUID-based transaction identity

---
```
## Demo (Sample CLI Output)

==== Budget Tracker ====
1. Add Transaction
2. View Summary
3. View Category Totals
4. List Transactions
5. Monthly Summary
6. Monthly Category Totals
7. Delete Transaction
8. Edit Transaction
9. Exit

Choose an option: 1
Enter type (income/expense): income
Enter category: salary
Enter amount: 2417.83
Enter date (YYYY-MM-DD): 2026-02-27

Transaction added successfully.

### Monthly Summary Example

Enter month (YYYY-MM): 2026-02

Income:   $4,800.00
Expenses: $1,700.00
Net:      $3,100.00
```
---

## Architecture

The application follow seperation of concerns:

- 'Main.java' -> CLI interface / menu
- 'BudgetTracker.java' -> business logic + persistence
- 'Transaction.java' -> data model
- 'InputHandling.java' -> centralized input validation utilities

Key design principles:
- Single Responsibility Principle
- Encapsulation of business logic
- Separation of persistence and presentation
- Defensive programming (validated user input)

---

## Persistence

Transactions are stored in:
'transactions.csv'

The application:
- Loads transaction on startup
- Saves automatically after add/edit/delete
- Stores raw numeric values (no formatted output in storage)
- Uses UUID for stable record identity

---

## Reporting

Supports:

- Monthly summary (income, expenses, net)
- Monthly category totals (expenses only)
- Sorted transaction listing (by date, category, amount)

---

## Technologies Used

- Java
- UUID (java.util.UUID)
- LocalDate / YearMonth (java.time API)
- File I/O (BufferedReader / BufferedWriter)
- Comparator API (custom sorting)

---

## How to Run

1. Clone the repository
2. Open in VS Code
3. Run 'Main.java'
4. Follow CLI prompts

---

## What I Learned

- Designing layered CLI applications
- Implementing full CRUD functionality
- Building file persistence without external libraries
- Handling CSV edge cases (formatting vs storage separation)
- Implementing robust user input validation
- Debugging data corruption caused by formatted numeric logic

---

## Future Improvements

- Unit testing (JUnit)
- Search and filtering capabilities
- Budget goals and tracking
- Migration to REST API backend
- Web-based UI frontend
