import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        BudgetTracker tracker = new BudgetTracker();
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {

            System.out.println("\n==== Budget Tracker ====");
            System.out.println("1. Add Transaction");
            System.out.println("2. View Summary");
            System.out.println("3. View Category Totals");
            System.out.println("4. List Transactions");
            System.out.println("5. Monthly Summary");
            System.out.println("6. Monthly Category Totals");
            System.out.println("7. Delete Transaction");
            System.out.println("8. Edit Transaction");
            System.out.println("9. Exit");
            System.out.println("Choose an option: ");

            int choice = InputHandling.readIntInRange(scanner, "Choose an option:", 1, 9);

            switch (choice) {

                case 1:
                    tracker.addTransaction(scanner);
                    break;

                case 2:
                    tracker.printSummary();
                    break;

                case 3:
                    tracker.printCategoryTotals();
                    break;

                case 4:
                    tracker.listTransactions();
                    break;

                case 5:
                    tracker.printMonthlySummary(scanner);
                    break;

                case 6:
                    tracker.printMonthlyCategoryTotals(scanner);
                    break;

                case 7:
                    tracker.deleteTransactionById(scanner);
                    break;

                case 8:
                    tracker.editTransactionById(scanner);
                    break;

                case 9:
                    running = false;
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        scanner.close();

    }
}
