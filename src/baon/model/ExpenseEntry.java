package baon.model;

public class ExpenseEntry {
    public final double amount;
    public final String category;
    public final String item;
    public final String date;

    public ExpenseEntry(double amount, String category, String item, String date) {
        this.amount = amount;
        this.category = category;
        this.item = item;
        this.date = date;
    }
}
