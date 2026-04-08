package baon.data;

import baon.model.ExpenseEntry;
import baon.model.IncomeEntry;
import baon.model.SavingEntry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class JsonStateStore {
    private static final Path DATA_PATH = Paths.get("data", "database.json");
    private static final Object FILE_LOCK = new Object();

    private JsonStateStore() {
    }

    static AppDatabase.DatabaseState loadState(String email) {
        synchronized (FILE_LOCK) {
            try {
                Map<String, Object> root = readRoot();
                String normalizedEmail = normalizeEmail(email);
                Map<String, Object> userState = readUserState(root, normalizedEmail);
                if (userState == null) {
                    userState = root;
                }
                return toDatabaseState(userState);
            } catch (IOException exception) {
                return new AppDatabase.DatabaseState();
            }
        }
    }

    static void saveState(String email, AppDatabase.DatabaseState state) {
        if (state == null) {
            return;
        }

        synchronized (FILE_LOCK) {
            try {
                Map<String, Object> root = readRoot();
                Map<String, Object> stateMap = fromDatabaseState(state);
                String normalizedEmail = normalizeEmail(email);

                for (Map.Entry<String, Object> entry : stateMap.entrySet()) {
                    root.put(entry.getKey(), entry.getValue());
                }

                if (!normalizedEmail.isEmpty()) {
                    Map<String, Object> users = asObject(root.get("users"));
                    if (users == null) {
                        users = new LinkedHashMap<String, Object>();
                    }
                    users.put(normalizedEmail, stateMap);
                    root.put("users", users);
                }

                writeRoot(root);
            } catch (IOException ignored) {
                // Keep UI responsive even if persistence fails.
            }
        }
    }

    static void clearState(String email) {
        synchronized (FILE_LOCK) {
            try {
                Map<String, Object> root = readRoot();
                Map<String, Object> defaults = defaultRoot();
                String normalizedEmail = normalizeEmail(email);

                root.put("budgetLimit", defaults.get("budgetLimit"));
                root.put("savingGoalTarget", defaults.get("savingGoalTarget"));
                root.put("incomeEntries", defaults.get("incomeEntries"));
                root.put("expenseEntries", defaults.get("expenseEntries"));
                root.put("savingEntries", defaults.get("savingEntries"));
                root.put("categoryBudgetLimits", defaults.get("categoryBudgetLimits"));

                if (!normalizedEmail.isEmpty()) {
                    Map<String, Object> users = asObject(root.get("users"));
                    if (users != null) {
                        users.remove(normalizedEmail);
                        if (users.isEmpty()) {
                            root.remove("users");
                        } else {
                            root.put("users", users);
                        }
                    }
                }

                writeRoot(root);
            } catch (IOException ignored) {
                // Keep UI responsive even if persistence fails.
            }
        }
    }

    private static Map<String, Object> readRoot() throws IOException {
        ensureFileExists();
        String json = new String(Files.readAllBytes(DATA_PATH), StandardCharsets.UTF_8).trim();
        if (json.isEmpty()) {
            return defaultRoot();
        }
        try {
            Object parsed = new JsonParser(json).parseValue();
            Map<String, Object> root = asObject(parsed);
            return root == null ? defaultRoot() : root;
        } catch (IllegalArgumentException exception) {
            return defaultRoot();
        }
    }

    private static void writeRoot(Map<String, Object> root) throws IOException {
        ensureFileExists();
        Files.write(DATA_PATH, (JsonWriter.write(root) + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
    }

    private static void ensureFileExists() throws IOException {
        Path parent = DATA_PATH.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        if (!Files.exists(DATA_PATH)) {
            Files.write(DATA_PATH, (JsonWriter.write(defaultRoot()) + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
        }
    }

    private static Map<String, Object> defaultRoot() {
        return fromDatabaseState(new AppDatabase.DatabaseState());
    }

    private static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private static Map<String, Object> readUserState(Map<String, Object> root, String normalizedEmail) {
        if (normalizedEmail.isEmpty()) {
            return null;
        }
        Map<String, Object> users = asObject(root.get("users"));
        if (users == null) {
            return null;
        }
        return asObject(users.get(normalizedEmail));
    }

    private static AppDatabase.DatabaseState toDatabaseState(Map<String, Object> rawState) {
        AppDatabase.DatabaseState state = new AppDatabase.DatabaseState();
        if (rawState == null) {
            return state;
        }

        state.budgetLimit = asDouble(rawState.get("budgetLimit"));
        state.savingGoalTarget = asDouble(rawState.get("savingGoalTarget"));

        List<Object> incomeEntries = asList(rawState.get("incomeEntries"));
        if (incomeEntries != null) {
            for (Object item : incomeEntries) {
                Map<String, Object> entry = asObject(item);
                if (entry == null) {
                    continue;
                }
                state.incomeEntries.add(new IncomeEntry(
                        asDouble(entry.get("amount")),
                        asString(entry.get("source")),
                        asString(entry.get("date"))));
            }
        }

        List<Object> expenseEntries = asList(rawState.get("expenseEntries"));
        if (expenseEntries != null) {
            for (Object item : expenseEntries) {
                Map<String, Object> entry = asObject(item);
                if (entry == null) {
                    continue;
                }
                state.expenseEntries.add(new ExpenseEntry(
                        asDouble(entry.get("amount")),
                        asString(entry.get("category")),
                        asString(entry.get("item")),
                        asString(entry.get("date"))));
            }
        }

        List<Object> savingEntries = asList(rawState.get("savingEntries"));
        if (savingEntries != null) {
            for (Object item : savingEntries) {
                Map<String, Object> entry = asObject(item);
                if (entry == null) {
                    continue;
                }
                state.savingEntries.add(new SavingEntry(
                        asDouble(entry.get("amount")),
                        asString(entry.get("category")),
                        asString(entry.get("date"))));
            }
        }

        Map<String, Object> categoryBudgetLimits = asObject(rawState.get("categoryBudgetLimits"));
        if (categoryBudgetLimits != null) {
            for (Map.Entry<String, Object> entry : categoryBudgetLimits.entrySet()) {
                state.categoryBudgetLimits.put(entry.getKey(), asDouble(entry.getValue()));
            }
        }

        return state;
    }

    private static Map<String, Object> fromDatabaseState(AppDatabase.DatabaseState state) {
        LinkedHashMap<String, Object> rawState = new LinkedHashMap<String, Object>();
        rawState.put("budgetLimit", state.budgetLimit);
        rawState.put("savingGoalTarget", state.savingGoalTarget);

        ArrayList<Object> incomeEntries = new ArrayList<Object>();
        for (IncomeEntry entry : state.incomeEntries) {
            LinkedHashMap<String, Object> rawEntry = new LinkedHashMap<String, Object>();
            rawEntry.put("amount", entry.amount);
            rawEntry.put("source", entry.source);
            rawEntry.put("date", entry.date);
            incomeEntries.add(rawEntry);
        }
        rawState.put("incomeEntries", incomeEntries);

        ArrayList<Object> expenseEntries = new ArrayList<Object>();
        for (ExpenseEntry entry : state.expenseEntries) {
            LinkedHashMap<String, Object> rawEntry = new LinkedHashMap<String, Object>();
            rawEntry.put("amount", entry.amount);
            rawEntry.put("category", entry.category);
            rawEntry.put("item", entry.item);
            rawEntry.put("date", entry.date);
            expenseEntries.add(rawEntry);
        }
        rawState.put("expenseEntries", expenseEntries);

        ArrayList<Object> savingEntries = new ArrayList<Object>();
        for (SavingEntry entry : state.savingEntries) {
            LinkedHashMap<String, Object> rawEntry = new LinkedHashMap<String, Object>();
            rawEntry.put("amount", entry.amount);
            rawEntry.put("category", entry.category);
            rawEntry.put("date", entry.date);
            savingEntries.add(rawEntry);
        }
        rawState.put("savingEntries", savingEntries);

        LinkedHashMap<String, Object> categoryBudgetLimits = new LinkedHashMap<String, Object>();
        for (Map.Entry<String, Double> entry : state.categoryBudgetLimits.entrySet()) {
            categoryBudgetLimits.put(entry.getKey(), entry.getValue());
        }
        rawState.put("categoryBudgetLimits", categoryBudgetLimits);
        return rawState;
    }

    private static Map<String, Object> asObject(Object value) {
        if (value instanceof Map<?, ?>) {
            LinkedHashMap<String, Object> mapped = new LinkedHashMap<String, Object>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                Object key = entry.getKey();
                if (key != null) {
                    mapped.put(String.valueOf(key), entry.getValue());
                }
            }
            return mapped;
        }
        return null;
    }

    private static List<Object> asList(Object value) {
        if (value instanceof List<?>) {
            return new ArrayList<Object>((List<?>) value);
        }
        return null;
    }

    private static String asString(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private static double asDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value instanceof String) {
            try {
                return Double.parseDouble(((String) value).trim());
            } catch (NumberFormatException ignored) {
                return 0.0;
            }
        }
        return 0.0;
    }

    private static final class JsonWriter {
        private static final String INDENT = "  ";

        private JsonWriter() {
        }

        static String write(Object value) {
            StringBuilder builder = new StringBuilder();
            appendValue(builder, value, 0);
            return builder.toString();
        }

        private static void appendValue(StringBuilder builder, Object value, int indentLevel) {
            if (value == null) {
                builder.append("null");
                return;
            }
            if (value instanceof String) {
                appendString(builder, (String) value);
                return;
            }
            if (value instanceof Number || value instanceof Boolean) {
                builder.append(String.valueOf(value));
                return;
            }
            if (value instanceof Map<?, ?>) {
                appendObject(builder, (Map<?, ?>) value, indentLevel);
                return;
            }
            if (value instanceof List<?>) {
                appendArray(builder, (List<?>) value, indentLevel);
                return;
            }
            appendString(builder, String.valueOf(value));
        }

        private static void appendObject(StringBuilder builder, Map<?, ?> map, int indentLevel) {
            builder.append('{');
            if (map.isEmpty()) {
                builder.append('}');
                return;
            }
            builder.append('\n');

            int index = 0;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                appendIndent(builder, indentLevel + 1);
                appendString(builder, String.valueOf(entry.getKey()));
                builder.append(": ");
                appendValue(builder, entry.getValue(), indentLevel + 1);
                if (index < map.size() - 1) {
                    builder.append(',');
                }
                builder.append('\n');
                index++;
            }

            appendIndent(builder, indentLevel);
            builder.append('}');
        }

        private static void appendArray(StringBuilder builder, List<?> list, int indentLevel) {
            builder.append('[');
            if (list.isEmpty()) {
                builder.append(']');
                return;
            }
            builder.append('\n');

            for (int index = 0; index < list.size(); index++) {
                appendIndent(builder, indentLevel + 1);
                appendValue(builder, list.get(index), indentLevel + 1);
                if (index < list.size() - 1) {
                    builder.append(',');
                }
                builder.append('\n');
            }

            appendIndent(builder, indentLevel);
            builder.append(']');
        }

        private static void appendIndent(StringBuilder builder, int indentLevel) {
            for (int index = 0; index < indentLevel; index++) {
                builder.append(INDENT);
            }
        }

        private static void appendString(StringBuilder builder, String value) {
            builder.append('"');
            for (int index = 0; index < value.length(); index++) {
                char current = value.charAt(index);
                switch (current) {
                    case '\\':
                        builder.append("\\\\");
                        break;
                    case '"':
                        builder.append("\\\"");
                        break;
                    case '\b':
                        builder.append("\\b");
                        break;
                    case '\f':
                        builder.append("\\f");
                        break;
                    case '\n':
                        builder.append("\\n");
                        break;
                    case '\r':
                        builder.append("\\r");
                        break;
                    case '\t':
                        builder.append("\\t");
                        break;
                    default:
                        if (current < 0x20) {
                            builder.append(String.format("\\u%04x", (int) current));
                        } else {
                            builder.append(current);
                        }
                        break;
                }
            }
            builder.append('"');
        }
    }

    private static final class JsonParser {
        private final String text;
        private int index;

        JsonParser(String text) {
            this.text = text == null ? "" : text;
        }

        Object parseValue() {
            skipWhitespace();
            if (index >= text.length()) {
                throw new IllegalArgumentException("Unexpected end of JSON.");
            }
            char current = text.charAt(index);
            if (current == '{') {
                return parseObject();
            }
            if (current == '[') {
                return parseArray();
            }
            if (current == '"') {
                return parseString();
            }
            if (current == 't' || current == 'f') {
                return parseBoolean();
            }
            if (current == 'n') {
                return parseNull();
            }
            return parseNumber();
        }

        private Map<String, Object> parseObject() {
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            expect('{');
            skipWhitespace();
            if (peek('}')) {
                expect('}');
                return map;
            }
            while (true) {
                skipWhitespace();
                String key = parseString();
                skipWhitespace();
                expect(':');
                Object value = parseValue();
                map.put(key, value);
                skipWhitespace();
                if (peek('}')) {
                    expect('}');
                    return map;
                }
                expect(',');
            }
        }

        private List<Object> parseArray() {
            ArrayList<Object> list = new ArrayList<Object>();
            expect('[');
            skipWhitespace();
            if (peek(']')) {
                expect(']');
                return list;
            }
            while (true) {
                list.add(parseValue());
                skipWhitespace();
                if (peek(']')) {
                    expect(']');
                    return list;
                }
                expect(',');
            }
        }

        private String parseString() {
            expect('"');
            StringBuilder builder = new StringBuilder();
            while (index < text.length()) {
                char current = text.charAt(index++);
                if (current == '"') {
                    return builder.toString();
                }
                if (current == '\\') {
                    if (index >= text.length()) {
                        throw new IllegalArgumentException("Invalid escape sequence.");
                    }
                    char escaped = text.charAt(index++);
                    switch (escaped) {
                        case '"':
                        case '\\':
                        case '/':
                            builder.append(escaped);
                            break;
                        case 'b':
                            builder.append('\b');
                            break;
                        case 'f':
                            builder.append('\f');
                            break;
                        case 'n':
                            builder.append('\n');
                            break;
                        case 'r':
                            builder.append('\r');
                            break;
                        case 't':
                            builder.append('\t');
                            break;
                        case 'u':
                            if (index + 4 > text.length()) {
                                throw new IllegalArgumentException("Invalid unicode escape.");
                            }
                            String hex = text.substring(index, index + 4);
                            builder.append((char) Integer.parseInt(hex, 16));
                            index += 4;
                            break;
                        default:
                            throw new IllegalArgumentException("Unsupported escape sequence.");
                    }
                    continue;
                }
                builder.append(current);
            }
            throw new IllegalArgumentException("Unterminated string.");
        }

        private Boolean parseBoolean() {
            if (text.startsWith("true", index)) {
                index += 4;
                return Boolean.TRUE;
            }
            if (text.startsWith("false", index)) {
                index += 5;
                return Boolean.FALSE;
            }
            throw new IllegalArgumentException("Invalid boolean.");
        }

        private Object parseNull() {
            if (!text.startsWith("null", index)) {
                throw new IllegalArgumentException("Invalid null literal.");
            }
            index += 4;
            return null;
        }

        private Number parseNumber() {
            int start = index;
            if (text.charAt(index) == '-') {
                index++;
            }
            while (index < text.length() && Character.isDigit(text.charAt(index))) {
                index++;
            }
            if (index < text.length() && text.charAt(index) == '.') {
                index++;
                while (index < text.length() && Character.isDigit(text.charAt(index))) {
                    index++;
                }
            }
            if (index < text.length() && (text.charAt(index) == 'e' || text.charAt(index) == 'E')) {
                index++;
                if (index < text.length() && (text.charAt(index) == '+' || text.charAt(index) == '-')) {
                    index++;
                }
                while (index < text.length() && Character.isDigit(text.charAt(index))) {
                    index++;
                }
            }
            String number = text.substring(start, index);
            try {
                return Double.valueOf(number);
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException("Invalid number.", exception);
            }
        }

        private void skipWhitespace() {
            while (index < text.length() && Character.isWhitespace(text.charAt(index))) {
                index++;
            }
        }

        private void expect(char expected) {
            skipWhitespace();
            if (index >= text.length() || text.charAt(index) != expected) {
                throw new IllegalArgumentException("Expected '" + expected + "'.");
            }
            index++;
        }

        private boolean peek(char expected) {
            skipWhitespace();
            return index < text.length() && text.charAt(index) == expected;
        }
    }
}
