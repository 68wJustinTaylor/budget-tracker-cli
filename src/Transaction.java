import java.time.LocalDate;
import java.util.UUID;

public class Transaction {
    UUID id;
    double amount;
    String type; // income or expense
    String category;
    LocalDate date;

    // Constructor for NEW transactions
    public Transaction(double amount, String type, String category, LocalDate date) {
        this.id = UUID.randomUUID();
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
    }

    // Constructor for LOADED transactions
    public Transaction(UUID id, double amount, String type, String category, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
    }
}