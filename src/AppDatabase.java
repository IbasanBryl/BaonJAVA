import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AppDatabase {
    private static final Path DATA_PATH = Paths.get("data", "database.json");

    private AppDatabase() {
    }

    public static DatabaseState load() {
        try {
            ensureFileExists();
            String json = new String(Files.readAllBytes(DATA_PATH), StandardCharsets.UTF_8);
            DatabaseState state = new DatabaseState();
            state.budgetLimit = readDouble(json, "budgetLimit", 0.0);
            state.savingGoalTarget = readDouble(json, "savingGoalTarget", 0.0);
            state.incomeEntries.addAll(readIncomeEntries(readArray(json, "incomeEntries")));
            state.expenseEntries.addAll(readExpenseEntries(readArray(json, "expenseEntries")));
            state.savingEntries.addAll(readSavingEntries(readArray(json, "savingEntries")));
            return state;
        } catch (IOException exception) {
            return new DatabaseState();
        }
    }

    public static void save(DatabaseState state) {
        try {
            ensureFileExists();
            Files.write(DATA_PATH, toJson(state).getBytes(StandardCharsets.UTF_8));
        } catch (IOException ignored) {
            // Keep the UI responsive even if persistence fails.
        }
    }

    private static void ensureFileExists() throws IOException {
        Path parent = DATA_PATH.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        if (!Files.exists(DATA_PATH)) {
            Files.write(DATA_PATH, defaultJson().getBytes(StandardCharsets.UTF_8));
        }
    }

    private static String toJson(DatabaseState state) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("  \"budgetLimit\": ").append(formatDouble(state.budgetLimit)).append(",\n");
        builder.append("  \"savingGoalTarget\": ").append(formatDouble(state.savingGoalTarget)).append(",\n");
        builder.append("  \"incomeEntries\": [\n");
        appendIncomeEntries(builder, state.incomeEntries);
        builder.append("  ],\n");
        builder.append("  \"expenseEntries\": [\n");
        appendExpenseEntries(builder, state.expenseEntries);
        builder.append("  ],\n");
        builder.append("  \"savingEntries\": [\n");
        appendSavingEntries(builder, state.savingEntries);
        builder.append("  ]\n");
        builder.append("}\n");
        return builder.toString();
    }

    private static void appendIncomeEntries(StringBuilder builder, ArrayList<IncomeEntry> entries) {
        for (int index = 0; index < entries.size(); index++) {
            IncomeEntry entry = entries.get(index);
            builder.append("    {\"amount\": ").append(formatDouble(entry.amount))
                    .append(", \"source\": \"").append(escape(entry.source))
                    .append("\", \"date\": \"").append(escape(entry.date)).append("\"}");
            if (index < entries.size() - 1) {
                builder.append(',');
            }
            builder.append("\n");
        }
    }

    private static void appendExpenseEntries(StringBuilder builder, ArrayList<ExpenseEntry> entries) {
        for (int index = 0; index < entries.size(); index++) {
            ExpenseEntry entry = entries.get(index);
            builder.append("    {\"amount\": ").append(formatDouble(entry.amount))
                    .append(", \"category\": \"").append(escape(entry.category))
                    .append("\", \"date\": \"").append(escape(entry.date)).append("\"}");
            if (index < entries.size() - 1) {
                builder.append(',');
            }
            builder.append("\n");
        }
    }

    private static void appendSavingEntries(StringBuilder builder, ArrayList<SavingEntry> entries) {
        for (int index = 0; index < entries.size(); index++) {
            SavingEntry entry = entries.get(index);
            builder.append("    {\"amount\": ").append(formatDouble(entry.amount))
                    .append(", \"date\": \"").append(escape(entry.date)).append("\"}");
            if (index < entries.size() - 1) {
                builder.append(',');
            }
            builder.append("\n");
        }
    }

    private static ArrayList<IncomeEntry> readIncomeEntries(String arrayJson) {
        ArrayList<IncomeEntry> entries = new ArrayList<IncomeEntry>();
        for (String objectJson : splitObjects(arrayJson)) {
            entries.add(new IncomeEntry(
                    readDouble(objectJson, "amount", 0.0),
                    readString(objectJson, "source", ""),
                    readString(objectJson, "date", "")));
        }
        return entries;
    }

    private static ArrayList<ExpenseEntry> readExpenseEntries(String arrayJson) {
        ArrayList<ExpenseEntry> entries = new ArrayList<ExpenseEntry>();
        for (String objectJson : splitObjects(arrayJson)) {
            entries.add(new ExpenseEntry(
                    readDouble(objectJson, "amount", 0.0),
                    readString(objectJson, "category", ""),
                    readString(objectJson, "date", "")));
        }
        return entries;
    }

    private static ArrayList<SavingEntry> readSavingEntries(String arrayJson) {
        ArrayList<SavingEntry> entries = new ArrayList<SavingEntry>();
        for (String objectJson : splitObjects(arrayJson)) {
            entries.add(new SavingEntry(
                    readDouble(objectJson, "amount", 0.0),
                    readString(objectJson, "date", "")));
        }
        return entries;
    }

    private static ArrayList<String> splitObjects(String arrayJson) {
        ArrayList<String> objects = new ArrayList<String>();
        Matcher matcher = Pattern.compile("\\{[^\\{\\}]*\\}", Pattern.DOTALL).matcher(arrayJson);
        while (matcher.find()) {
            objects.add(matcher.group());
        }
        return objects;
    }

    private static String readArray(String json, String key) {
        Matcher matcher = Pattern.compile("\\\"" + Pattern.quote(key) + "\\\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL)
                .matcher(json);
        return matcher.find() ? matcher.group(1) : "";
    }

    private static double readDouble(String json, String key, double fallback) {
        Matcher matcher = Pattern.compile("\\\"" + Pattern.quote(key) + "\\\"\\s*:\\s*(-?[0-9]+(?:\\.[0-9]+)?)")
                .matcher(json);
        if (!matcher.find()) {
            return fallback;
        }
        try {
            return Double.parseDouble(matcher.group(1));
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    private static String readString(String json, String key, String fallback) {
        Matcher matcher = Pattern.compile("\\\"" + Pattern.quote(key) + "\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"])*)\\\"")
                .matcher(json);
        return matcher.find() ? unescape(matcher.group(1)) : fallback;
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String unescape(String value) {
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private static String formatDouble(double value) {
        if (value == (long) value) {
            return String.format("%d", Long.valueOf((long) value));
        }
        return String.valueOf(value);
    }

    private static String defaultJson() {
        return "{\n"
                + "  \"budgetLimit\": 0.0,\n"
                + "  \"savingGoalTarget\": 0.0,\n"
                + "  \"incomeEntries\": [],\n"
                + "  \"expenseEntries\": [],\n"
                + "  \"savingEntries\": []\n"
                + "}\n";
    }

    public static final class DatabaseState {
        public final ArrayList<IncomeEntry> incomeEntries = new ArrayList<IncomeEntry>();
        public final ArrayList<ExpenseEntry> expenseEntries = new ArrayList<ExpenseEntry>();
        public final ArrayList<SavingEntry> savingEntries = new ArrayList<SavingEntry>();
        public double budgetLimit;
        public double savingGoalTarget;
    }
}
