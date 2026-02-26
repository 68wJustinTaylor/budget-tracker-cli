import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Scanner;


public class InputHandling {

    public static int readIntInRange(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    public static double readNonNegativeDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                double value = Double.parseDouble(input);
                if (value < 0) {
                    System.out.println("Amount must be non-negative");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Example: 501.17");
            }
        }
    }


    public static String readType(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.equals("income") || input.equals("expense")) {
                return input;
            }
            System.out.println("Type must be 'income' or 'expense'.");
        }
    }


    public static LocalDate readDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input); // YYYY-MM-DD
            } catch (Exception e) {
                System.out.println("Invalid date. Example: 2026-01-01");
            }
        }
    }


    public static YearMonth readYearMonth(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                // Expects YYYY-MM
                return YearMonth.parse(input);
            } catch (Exception e) {
                System.out.println("Invalid month. Example: 2026-01");
            }
        }
    }
}