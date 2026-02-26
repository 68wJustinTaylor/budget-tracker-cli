import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.YearMonth;

public class BudgetTracker {

    private final File dataFile = new File(System.getProperty("user.dir"), "transactions.csv");

    private final ArrayList<Transaction> transactions = new ArrayList<>();
    private final DecimalFormat df = new DecimalFormat("###,##0.00");

    public BudgetTracker() {
        System.out.println("Data File: " + dataFile.getAbsolutePath());
        loadFromFile();
        System.out.println("Loaded transactions: " + transactions.size());
    }

    public void addTransaction(Scanner scanner) {
        double amount = InputHandling.readNonNegativeDouble(scanner, "Enter amount: ");

        String type = InputHandling.readType(scanner, "Enter type (income/expense): ");

        System.out.print("Enter category: ");
        String category = scanner.nextLine().trim();

        LocalDate date = InputHandling.readDate(scanner, "Enter date (YYYY-MM-DD): ");

        transactions.add(new Transaction(amount, type, category, date));
        System.out.println("Transaction added!");

        saveToFile();
    }

    public void printSummary() {
        double income = calculateIncome();
        double expenses = calculateExpenses();

        System.out.println("\nSummary:");
        System.out.println("Income: $" + df.format(income));
        System.out.println("Expenses: $" + df.format(expenses));
        System.out.println("Net: $" + df.format(income - expenses));
    }

    public void printCategoryTotals() {
        HashMap<String, Double> totals = new HashMap<>();

        for (Transaction t : transactions) {
            if (t.type.equals("expense")) {
                totals.put(t.category, totals.getOrDefault(t.category, 0.0) + t.amount);
            }
        }

        System.out.println("\nCategory Totals (Expenses):");
        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            System.out.println(entry.getKey() + ": $" + df.format(entry.getValue()));
        }
    }

    public void listTransactions() {
        System.out.println("\nTransactions:");

        ArrayList<Transaction> sorted = new ArrayList<>(transactions);

        sorted.sort(
                Comparator
                        .comparing((Transaction t) -> t.date).reversed()
                        .thenComparing(t -> t.category.toLowerCase())
                        .thenComparing((Transaction t) -> t.amount, Comparator.reverseOrder())
                        .thenComparing(t -> t.id.toString()));
        for (Transaction t : transactions) {
            System.out.println(
                    t.id + " | " + t.date + " | " + t.type + " | " + t.category + " | $" + df.format(t.amount));
        }
    }

    public void printMonthlySummary(Scanner scanner) {

        YearMonth yearMonth = InputHandling.readYearMonth(scanner, "Enter month (YYYY-MM): ");

        double income = 0.0;
        double expenses = 0.0;

        for (Transaction t : transactions) {
            if (YearMonth.from(t.date).equals(yearMonth)) {
                if (t.type.equals("income")) {
                    income += t.amount;
                } else if (t.type.equals("expense")) {
                    expenses += t.amount;
                }
            }
        }

        System.out.println("\nSummary for " + yearMonth + ":");
        System.out.println("Income: $" + df.format(income));
        System.out.println("Expenses: $" + df.format(expenses));
        System.out.println("Net: $" + df.format(income - expenses));
    }

    public void printMonthlyCategoryTotals(Scanner scanner) {

        YearMonth yearMonth = InputHandling.readYearMonth(scanner, "Enter month (YYYY-MM): ");

        HashMap<String, Double> totals = new HashMap<>();

        for (Transaction t : transactions) {
            if (t.type.equals("expense") && YearMonth.from(t.date).equals(yearMonth)) {
                totals.put(t.category, totals.getOrDefault(t.category, 0.0) + t.amount);
            }
        }

        System.out.println("\nCategory Totals (Expenses) for " + yearMonth + ":");

        if (totals.isEmpty()) {
            System.out.println("No expenses recorded");
            return;
        }

        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            System.out.println(entry.getKey() + ": $" + df.format(entry.getValue()));
        }
    }

    public void deleteTransactionById(Scanner scanner) {
        System.out.print("Enter transaction ID to delete: ");
        String idInput = scanner.nextLine().trim();

        UUID id;
        try {
            id = UUID.fromString(idInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid ID format");
            return;
        }

        boolean removed = transactions.removeIf(t -> t.id.equals(id));

        if (removed) {
            saveToFile(); // Option 3: persist immediately
            System.out.println("Transaction deleted.");
        } else {
            System.out.println("No transaction found with that ID");
        }
    }

    public void editTransactionById(Scanner scanner) {
        System.out.print("Enter transaction ID to edit: ");
        String idInput = scanner.nextLine().trim();

        UUID id;
        try {
            id = UUID.fromString(idInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid ID format.");
            return;
        }

        Transaction target = null;
        for (Transaction t : transactions) {
            if (t.id.equals(id)) {
                target = t;
                break;
            }
        }

        if (target == null) {
            System.out.println("No transaction found with that ID.");
            return;
        }

        System.out.println("\nEditing transaction:");
        System.out.println(target.id + " | " + target.date + " | " + target.type + " | " + target.category + " | $"
                + df.format(target.amount));

        // Date
        while (true) {
            System.out.print("New date (YYYY-MM-DD) [Enter to keep " + target.date + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
                break;
            try {
                target.date = LocalDate.parse(input);
                break;
            } catch (Exception e) {
                System.out.println("Invalid date. Example: 2026-03-01");
            }
        }

        // Type
        while (true) {
            System.out.print("New type (income/expense) [Enter to keep " + target.type + "]: ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.isEmpty())
                break;
            if (input.equals("income") || input.equals("expense")) {
                target.type = input;
                break;
            }
            System.out.println("Type must be 'income' or 'expense'.");
        }

        // Category
        System.out.print("New category [Enter to keep \"" + target.category + "\"]: ");
        String catInput = scanner.nextLine().trim();
        if (!catInput.isEmpty()) {
            target.category = catInput;
        }

        // Amount
        while (true) {
            System.out.print("New amount [Enter to keep " + df.format(target.amount) + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
                break;
            try {
                double newAmount = Double.parseDouble(input);
                if (newAmount < 0) {
                    System.out.println("Amount must be non-negative.");
                    continue;
                }
                target.amount = newAmount;
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Example: 501.17");
            }
        }

        saveToFile(); // Option 3: persist immediately
        System.out.println("Transaction updated.");
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            // header
            writer.write("id,date,type,category,amount");
            writer.newLine();

            for (Transaction t : transactions) {
                // date, type, category, amount
                writer.write(t.id + "," + t.date + "," + t.type + "," + escapeCsv(t.category) + "," + t.amount);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        if (!dataFile.exists()) {
            return; // nothing to load yet
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line = reader.readLine(); // header
            if (line == null)
                return;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                String[] parts = parseCsvLine(line);
                if (parts.length != 5)
                    continue;

                UUID id = UUID.fromString(parts[0].trim());
                LocalDate date = LocalDate.parse(parts[1].trim());
                String type = parts[2].trim().toLowerCase();
                String category = parts[3].trim();
                double amount = Double.parseDouble(parts[4].trim().replace(",", ""));

                transactions.add(new Transaction(id, amount, type, category, date));
            }
        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Data file is malformed: " + e.getMessage());
        }
    }

    /**
     * Escapes a csv field if it contains commas or quotes.
     */
    private String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }

    /**
     * Minimal csv parser supporting quoted fields.
     */
    private String[] parseCsvLine(String line) {
        ArrayList<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                // handle escaped quote
                if (inQuotes && i < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++; // skip the second quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());

        return fields.toArray(new String[0]);
    }

    private double calculateIncome() {
        double income = 0.0;
        for (Transaction t : transactions) {
            if (t.type.equals("income")) {
                income += t.amount;
            }
        }
        return income;
    }

    private double calculateExpenses() {
        double expenses = 0.0;
        for (Transaction t : transactions) {
            if (t.type.equals("expense")) {
                expenses += t.amount;
            }
        }
        return expenses;
    }
}