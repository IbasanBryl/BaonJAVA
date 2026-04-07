package baon.model;

public class SavingEntry {
    public final double amount;
    public final String category;
    public final String date;

    public SavingEntry(double amount, String date) {
        this(amount, "Other", date);
    }

    public SavingEntry(double amount, String category, String date) {
        this.amount = amount;
        this.category = category == null || category.trim().isEmpty() ? "Other" : category.trim();
        this.date = date;
    }
}
