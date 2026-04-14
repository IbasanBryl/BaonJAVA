package baon.data;

import baon.model.ExpenseEntry;
import baon.model.IncomeEntry;
import baon.model.SavingEntry;
import baon.security.SmtpClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public final class AppDatabase {
    private static final String DEFAULT_JDBC_URL = "jdbc:sqlite:data/baon.db";
    private static final String DEFAULT_APP_EMAIL = "student@email.com";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final boolean OTP_DEV_FALLBACK = true;
    private static volatile boolean initialized;

    private AppDatabase() {
    }

    // initialize
    public static synchronized void initialize() {
        if (initialized) {
            return;
        }

        try (Connection connection = openConnection();
                Statement statement = connection.createStatement()) {
            if (isSqlite()) {
                statement.execute("PRAGMA foreign_keys = ON");
            }

            statement.execute("CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "display_name TEXT NOT NULL,"
                    + "email TEXT NOT NULL UNIQUE,"
                    + "password_hash TEXT NOT NULL,"
                    + "created_at TEXT NOT NULL"
                    + ")");

            statement.execute("CREATE TABLE IF NOT EXISTS user_settings ("
                    + "user_id INTEGER PRIMARY KEY,"
                    + "budget_limit REAL NOT NULL DEFAULT 0,"
                    + "saving_goal_target REAL NOT NULL DEFAULT 0,"
                    + "saving_goal_category TEXT NOT NULL DEFAULT 'Other',"
                    + "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE"
                    + ")");

            statement.execute("CREATE TABLE IF NOT EXISTS category_budgets ("
                    + "user_id INTEGER NOT NULL,"
                    + "category TEXT NOT NULL,"
                    + "amount REAL NOT NULL,"
                    + "PRIMARY KEY(user_id, category),"
                    + "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE"
                    + ")");

            statement.execute("CREATE TABLE IF NOT EXISTS income_entries ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user_id INTEGER NOT NULL,"
                    + "amount REAL NOT NULL,"
                    + "source TEXT NOT NULL,"
                    + "entry_date TEXT NOT NULL,"
                    + "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE"
                    + ")");

            statement.execute("CREATE TABLE IF NOT EXISTS expense_entries ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user_id INTEGER NOT NULL,"
                    + "amount REAL NOT NULL,"
                    + "category TEXT NOT NULL,"
                    + "item TEXT NOT NULL DEFAULT '',"
                    + "entry_date TEXT NOT NULL,"
                    + "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE"
                    + ")");

            statement.execute("CREATE TABLE IF NOT EXISTS saving_entries ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user_id INTEGER NOT NULL,"
                    + "amount REAL NOT NULL,"
                    + "category TEXT NOT NULL DEFAULT 'Other',"
                    + "entry_date TEXT NOT NULL,"
                    + "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE"
                    + ")");

            statement.execute("CREATE TABLE IF NOT EXISTS otp_codes ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "email TEXT NOT NULL,"
                    + "code_hash TEXT NOT NULL,"
                    + "expires_at_epoch_ms INTEGER NOT NULL,"
                    + "used INTEGER NOT NULL DEFAULT 0,"
                    + "created_at_epoch_ms INTEGER NOT NULL"
                    + ")");

            statement.execute("CREATE INDEX IF NOT EXISTS idx_otp_codes_email ON otp_codes(email)");
            statement.execute("CREATE INDEX IF NOT EXISTS idx_income_entries_user ON income_entries(user_id)");
            statement.execute("CREATE INDEX IF NOT EXISTS idx_expense_entries_user ON expense_entries(user_id)");
            ensureSavingGoalCategoryColumn(connection);
            ensureExpenseEntryItemColumn(connection);
            ensureSavingEntryCategoryColumn(connection);
            statement.execute("CREATE INDEX IF NOT EXISTS idx_saving_entries_user ON saving_entries(user_id)");
            initialized = true;
        } catch (SQLException | IOException exception) {
            throw new IllegalStateException("Failed to initialize JDBC schema.", exception);
        }
    }

    // load
    public static DatabaseState load() {
        return loadForUser(defaultEmail());
    }

    // save
    public static void save(DatabaseState state) {
        saveForUser(defaultEmail(), state);
    }

    // loadForUser
    public static DatabaseState loadForUser(String email) {
        initialize();
        String normalizedEmail = normalizeEmail(email);
        DatabaseState jsonState = JsonStateStore.loadState(normalizedEmail);
        if (!jsonState.categoryBudgetLimits.isEmpty()) {
            jsonState.budgetLimit = calculateBudgetTotal(jsonState.categoryBudgetLimits);
        }
        if (hasStoredState(jsonState)) {
            saveSqlStateForUser(normalizedEmail, jsonState);
            return jsonState;
        }

        DatabaseState sqliteState = loadSqlStateForUser(normalizedEmail);
        if (!sqliteState.categoryBudgetLimits.isEmpty()) {
            sqliteState.budgetLimit = calculateBudgetTotal(sqliteState.categoryBudgetLimits);
        }
        if (hasStoredState(sqliteState)) {
            JsonStateStore.saveState(normalizedEmail, sqliteState);
        }
        return sqliteState;
    }

    // saveForUser
    public static void saveForUser(String email, DatabaseState state) {
        initialize();
        if (state == null) {
            return;
        }
        String normalizedEmail = normalizeEmail(email);
        if (!state.categoryBudgetLimits.isEmpty()) {
            state.budgetLimit = calculateBudgetTotal(state.categoryBudgetLimits);
        }
        JsonStateStore.saveState(normalizedEmail, state);
        saveSqlStateForUser(normalizedEmail, state);
    }

    // resetFinancialDataForUser
    public static void resetFinancialDataForUser(String email) {
        initialize();
        String normalizedEmail = normalizeEmail(email);
        JsonStateStore.clearState(normalizedEmail);
        saveSqlStateForUser(normalizedEmail, new DatabaseState());
    }

    // loadSqlStateForUser
    private static DatabaseState loadSqlStateForUser(String normalizedEmail) {
        DatabaseState state = new DatabaseState();
        if (normalizedEmail.isEmpty()) {
            return state;
        }

        try (Connection connection = openConnection()) {
            Integer userId = findUserId(connection, normalizedEmail);
            if (userId == null) {
                return state;
            }

            loadSettings(connection, userId.intValue(), state);
            loadIncomeEntries(connection, userId.intValue(), state);
            loadExpenseEntries(connection, userId.intValue(), state);
            loadSavingEntries(connection, userId.intValue(), state);
            loadCategoryBudgets(connection, userId.intValue(), state);
            if (!state.categoryBudgetLimits.isEmpty()) {
                state.budgetLimit = calculateBudgetTotal(state.categoryBudgetLimits);
            }
            return state;
        } catch (SQLException | IOException exception) {
            return new DatabaseState();
        }
    }

    // saveSqlStateForUser
    private static void saveSqlStateForUser(String normalizedEmail, DatabaseState state) {
        if (normalizedEmail.isEmpty() || state == null) {
            return;
        }

        try (Connection connection = openConnection()) {
            connection.setAutoCommit(false);
            Integer userId = findUserId(connection, normalizedEmail);
            if (userId == null) {
                String fallbackName = normalizedEmail.contains("@")
                        ? normalizedEmail.substring(0, normalizedEmail.indexOf('@'))
                        : "User";
                createUser(connection, fallbackName, normalizedEmail, hashPassword("password123"));
                userId = findUserId(connection, normalizedEmail);
            }

            if (userId == null) {
                connection.rollback();
                return;
            }

            overwriteSettings(connection, userId.intValue(), state);
            overwriteIncomeEntries(connection, userId.intValue(), state.incomeEntries);
            overwriteExpenseEntries(connection, userId.intValue(), state.expenseEntries);
            overwriteSavingEntries(connection, userId.intValue(), state.savingEntries);
            overwriteCategoryBudgets(connection, userId.intValue(), state.categoryBudgetLimits);
            connection.commit();
        } catch (SQLException | IOException exception) {
            // save data fallback logic
        }
    }

    // hasStoredState
    private static boolean hasStoredState(DatabaseState state) {
        return state != null
                && (Math.abs(state.budgetLimit) > 0.0001
                || Math.abs(state.savingGoalTarget) > 0.0001
                || !state.incomeEntries.isEmpty()
                || !state.expenseEntries.isEmpty()
                || !state.savingEntries.isEmpty()
                || !state.categoryBudgetLimits.isEmpty());
    }

    // userExists
    public static boolean userExists(String email) {
        initialize();
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail.isEmpty()) {
            return false;
        }
        try (Connection connection = openConnection()) {
            return findUserId(connection, normalizedEmail) != null;
        } catch (SQLException | IOException exception) {
            return false;
        }
    }

    // createUser
    public static boolean createUser(String displayName, String email, String plainPassword) {
        initialize();
        String normalizedEmail = normalizeEmail(email);
        String safeName = sanitizeDisplayName(displayName, normalizedEmail);
        if (normalizedEmail.isEmpty() || plainPassword == null || plainPassword.trim().isEmpty()) {
            return false;
        }

        String passwordHash = hashPassword(plainPassword.trim());
        try (Connection connection = openConnection()) {
            connection.setAutoCommit(false);
            if (findUserId(connection, normalizedEmail) != null) {
                connection.rollback();
                return false;
            }
            createUser(connection, safeName, normalizedEmail, passwordHash);
            connection.commit();
            return true;
        } catch (SQLException | IOException exception) {
            return false;
        }
    }

    // authenticateUser
    public static UserRecord authenticateUser(String email, String plainPassword) {
        initialize();
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail.isEmpty() || plainPassword == null || plainPassword.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT id, display_name, email, password_hash FROM users WHERE email = ?";
        try (Connection connection = openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, normalizedEmail);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                String storedHash = resultSet.getString("password_hash");
                if (!verifyPassword(plainPassword.trim(), storedHash)) {
                    return null;
                }
                return new UserRecord(
                        resultSet.getInt("id"),
                        resultSet.getString("display_name"),
                        resultSet.getString("email"));
            }
        } catch (SQLException | IOException exception) {
            return null;
        }
    }

    // updateDisplayName
    public static boolean updateDisplayName(String email, String displayName) {
        initialize();
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail.isEmpty()) {
            return false;
        }
        String safeName = sanitizeDisplayName(displayName, normalizedEmail);
        String sql = "UPDATE users SET display_name = ? WHERE email = ?";
        try (Connection connection = openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, safeName);
            statement.setString(2, normalizedEmail);
            return statement.executeUpdate() > 0;
        } catch (SQLException | IOException exception) {
            return false;
        }
    }

    // updatePassword
    public static boolean updatePassword(String email, String newPassword) {
        initialize();
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail.isEmpty() || newPassword == null || newPassword.trim().isEmpty()) {
            return false;
        }

        String sql = "UPDATE users SET password_hash = ? WHERE email = ?";
        try (Connection connection = openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hashPassword(newPassword.trim()));
            statement.setString(2, normalizedEmail);
            return statement.executeUpdate() > 0;
        } catch (SQLException | IOException exception) {
            return false;
        }
    }

    // sendOtp
    public static OtpDispatchResult sendOtp(String email) {
        initialize();
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail.isEmpty()) {
            return OtpDispatchResult.failure("Email is required.");
        }

        String otpCode = String.format("%06d", Integer.valueOf(RANDOM.nextInt(1000000)));
        long now = Instant.now().toEpochMilli();
        int expiryMinutes = OTP_EXPIRY_MINUTES;
        long expiresAt = now + (expiryMinutes * 60L * 1000L);

        try (Connection connection = openConnection()) {
            connection.setAutoCommit(false);
            markUnusedOtpsUsed(connection, normalizedEmail);
            String insertSql = "INSERT INTO otp_codes (email, code_hash, expires_at_epoch_ms, used, created_at_epoch_ms) "
                    + "VALUES (?, ?, ?, 0, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
                statement.setString(1, normalizedEmail);
                statement.setString(2, hashOtpCode(otpCode));
                statement.setLong(3, expiresAt);
                statement.setLong(4, now);
                statement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException | IOException exception) {
            return OtpDispatchResult.failure("Could not generate OTP. Please try again.");
        }

        String subject = "Your BaonBrain OTP Code";
        String body = "Your one-time code is " + otpCode + ". It expires in " + expiryMinutes + " minutes.";
        try {
            SmtpClient.sendEmail(normalizedEmail, subject, body);
            return OtpDispatchResult.success("OTP sent. Check your email inbox.");
        } catch (Throwable exception) {
            if (OTP_DEV_FALLBACK) {
                return OtpDispatchResult.success("SMTP unavailable. Dev OTP: " + otpCode);
            }
            invalidateAllOtps(normalizedEmail);
            return OtpDispatchResult.failure("Unable to send OTP email. Check SMTP settings in SmtpClient.");
        }
    }

    // verifyOtp
    public static boolean verifyOtp(String email, String otpCode) {
        initialize();
        String normalizedEmail = normalizeEmail(email);
        String trimmedCode = otpCode == null ? "" : otpCode.trim();
        if (normalizedEmail.isEmpty() || trimmedCode.isEmpty()) {
            return false;
        }

        String selectSql = "SELECT id, code_hash, expires_at_epoch_ms FROM otp_codes "
                + "WHERE email = ? AND used = 0 ORDER BY created_at_epoch_ms DESC LIMIT 1";
        try (Connection connection = openConnection();
                PreparedStatement select = connection.prepareStatement(selectSql)) {
            select.setString(1, normalizedEmail);
            try (ResultSet resultSet = select.executeQuery()) {
                if (!resultSet.next()) {
                    return false;
                }

                int otpId = resultSet.getInt("id");
                long expiresAt = resultSet.getLong("expires_at_epoch_ms");
                String codeHash = resultSet.getString("code_hash");

                if (Instant.now().toEpochMilli() > expiresAt) {
                    markOtpUsedById(connection, otpId);
                    return false;
                }

                if (!safeEquals(hashOtpCode(trimmedCode), codeHash)) {
                    return false;
                }

                markOtpUsedById(connection, otpId);
                return true;
            }
        } catch (SQLException | IOException exception) {
            return false;
        }
    }

    private static void ensureSavingGoalCategoryColumn(Connection connection) throws SQLException {
        boolean hasCategoryColumn = false;
        String sql = "PRAGMA table_info(user_settings)";
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                if ("saving_goal_category".equalsIgnoreCase(resultSet.getString("name"))) {
                    hasCategoryColumn = true;
                    break;
                }
            }
        }

        if (hasCategoryColumn) {
            return;
        }

        try (Statement alter = connection.createStatement()) {
            alter.execute("ALTER TABLE user_settings ADD COLUMN saving_goal_category TEXT NOT NULL DEFAULT 'Other'");
        }
    }

    private static void ensureExpenseEntryItemColumn(Connection connection) throws SQLException {
        boolean hasItemColumn = false;
        String sql = "PRAGMA table_info(expense_entries)";
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                if ("item".equalsIgnoreCase(resultSet.getString("name"))) {
                    hasItemColumn = true;
                    break;
                }
            }
        }

        if (hasItemColumn) {
            return;
        }

        try (Statement alter = connection.createStatement()) {
            alter.execute("ALTER TABLE expense_entries ADD COLUMN item TEXT NOT NULL DEFAULT ''");
        }
    }

    private static void ensureSavingEntryCategoryColumn(Connection connection) throws SQLException {
        boolean hasCategoryColumn = false;
        String sql = "PRAGMA table_info(saving_entries)";
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                if ("category".equalsIgnoreCase(resultSet.getString("name"))) {
                    hasCategoryColumn = true;
                    break;
                }
            }
        }

        if (hasCategoryColumn) {
            return;
        }

        try (Statement alter = connection.createStatement()) {
            alter.execute("ALTER TABLE saving_entries ADD COLUMN category TEXT NOT NULL DEFAULT 'Other'");
        }
    }

    private static void loadSettings(Connection connection, int userId, DatabaseState state) throws SQLException {
        String sql = "SELECT budget_limit, saving_goal_target, saving_goal_category FROM user_settings WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    state.budgetLimit = resultSet.getDouble("budget_limit");
                    state.savingGoalTarget = resultSet.getDouble("saving_goal_target");
                    state.savingGoalCategory = resultSet.getString("saving_goal_category");
                }
            }
        }
    }

    private static void loadIncomeEntries(Connection connection, int userId, DatabaseState state) throws SQLException {
        String sql = "SELECT amount, source, entry_date FROM income_entries WHERE user_id = ? ORDER BY id ASC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    state.incomeEntries.add(new IncomeEntry(
                            resultSet.getDouble("amount"),
                            resultSet.getString("source"),
                            resultSet.getString("entry_date")));
                }
            }
        }
    }

    private static void loadExpenseEntries(Connection connection, int userId, DatabaseState state) throws SQLException {
        String sql = "SELECT amount, category, item, entry_date FROM expense_entries WHERE user_id = ? ORDER BY id ASC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    state.expenseEntries.add(new ExpenseEntry(
                            resultSet.getDouble("amount"),
                            resultSet.getString("category"),
                            resultSet.getString("item"),
                            resultSet.getString("entry_date")));
                }
            }
        }
    }

    private static void loadSavingEntries(Connection connection, int userId, DatabaseState state) throws SQLException {
        String sql = "SELECT amount, category, entry_date FROM saving_entries WHERE user_id = ? ORDER BY id ASC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    state.savingEntries.add(new SavingEntry(
                            resultSet.getDouble("amount"),
                            resultSet.getString("category"),
                            resultSet.getString("entry_date")));
                }
            }
        }
    }

    private static void overwriteSettings(Connection connection, int userId, DatabaseState state) throws SQLException {
        String updateSql = "UPDATE user_settings SET budget_limit = ?, saving_goal_target = ?, saving_goal_category = ? WHERE user_id = ?";
        try (PreparedStatement update = connection.prepareStatement(updateSql)) {
            update.setDouble(1, state.budgetLimit);
            update.setDouble(2, state.savingGoalTarget);
            update.setString(3, state.savingGoalCategory == null || state.savingGoalCategory.trim().isEmpty() ? "Other" : state.savingGoalCategory.trim());
            update.setInt(4, userId);
            int affected = update.executeUpdate();
            if (affected > 0) {
                return;
            }
        }

        String insertSql = "INSERT INTO user_settings (user_id, budget_limit, saving_goal_target, saving_goal_category) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insert = connection.prepareStatement(insertSql)) {
            insert.setInt(1, userId);
            insert.setDouble(2, state.budgetLimit);
            insert.setDouble(3, state.savingGoalTarget);
            insert.setString(4, state.savingGoalCategory == null || state.savingGoalCategory.trim().isEmpty() ? "Other" : state.savingGoalCategory.trim());
            insert.executeUpdate();
        }
    }

    private static void overwriteIncomeEntries(Connection connection, int userId, ArrayList<IncomeEntry> entries)
            throws SQLException {
        try (PreparedStatement delete = connection.prepareStatement("DELETE FROM income_entries WHERE user_id = ?")) {
            delete.setInt(1, userId);
            delete.executeUpdate();
        }

        String insertSql = "INSERT INTO income_entries (user_id, amount, source, entry_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insert = connection.prepareStatement(insertSql)) {
            for (IncomeEntry entry : entries) {
                insert.setInt(1, userId);
                insert.setDouble(2, entry.amount);
                insert.setString(3, entry.source);
                insert.setString(4, entry.date);
                insert.addBatch();
            }
            insert.executeBatch();
        }
    }

    private static void overwriteExpenseEntries(Connection connection, int userId, ArrayList<ExpenseEntry> entries)
            throws SQLException {
        try (PreparedStatement delete = connection.prepareStatement("DELETE FROM expense_entries WHERE user_id = ?")) {
            delete.setInt(1, userId);
            delete.executeUpdate();
        }

        String insertSql = "INSERT INTO expense_entries (user_id, amount, category, item, entry_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement insert = connection.prepareStatement(insertSql)) {
            for (ExpenseEntry entry : entries) {
                insert.setInt(1, userId);
                insert.setDouble(2, entry.amount);
                insert.setString(3, entry.category);
                insert.setString(4, entry.item == null ? "" : entry.item);
                insert.setString(5, entry.date);
                insert.addBatch();
            }
            insert.executeBatch();
        }
    }

    private static void overwriteSavingEntries(Connection connection, int userId, ArrayList<SavingEntry> entries)
            throws SQLException {
        try (PreparedStatement delete = connection.prepareStatement("DELETE FROM saving_entries WHERE user_id = ?")) {
            delete.setInt(1, userId);
            delete.executeUpdate();
        }

        String insertSql = "INSERT INTO saving_entries (user_id, amount, category, entry_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insert = connection.prepareStatement(insertSql)) {
            for (SavingEntry entry : entries) {
                insert.setInt(1, userId);
                insert.setDouble(2, entry.amount);
                insert.setString(3, entry.category);
                insert.setString(4, entry.date);
                insert.addBatch();
            }
            insert.executeBatch();
        }
    }
    private static void loadCategoryBudgets(Connection connection, int userId, DatabaseState state) throws SQLException {
        String sql = "SELECT category, amount FROM category_budgets WHERE user_id = ? ORDER BY category ASC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    state.categoryBudgetLimits.put(
                            resultSet.getString("category"),
                            Double.valueOf(resultSet.getDouble("amount")));
                }
            }
        }
    }

    private static void overwriteCategoryBudgets(Connection connection, int userId, LinkedHashMap<String, Double> budgets)
            throws SQLException {
        try (PreparedStatement delete = connection.prepareStatement("DELETE FROM category_budgets WHERE user_id = ?")) {
            delete.setInt(1, userId);
            delete.executeUpdate();
        }

        if (budgets == null || budgets.isEmpty()) {
            return;
        }

        String insertSql = "INSERT INTO category_budgets (user_id, category, amount) VALUES (?, ?, ?)";
        try (PreparedStatement insert = connection.prepareStatement(insertSql)) {
            for (Map.Entry<String, Double> entry : budgets.entrySet()) {
                insert.setInt(1, userId);
                insert.setString(2, entry.getKey());
                insert.setDouble(3, entry.getValue().doubleValue());
                insert.addBatch();
            }
            insert.executeBatch();
        }
    }

    private static double calculateBudgetTotal(LinkedHashMap<String, Double> budgets) {
        double total = 0.0;
        if (budgets == null) {
            return total;
        }
        for (Double value : budgets.values()) {
            if (value != null) {
                total += value.doubleValue();
            }
        }
        return total;
    }

    private static void createUser(Connection connection, String displayName, String email, String passwordHash)
            throws SQLException {
        String insertSql = "INSERT INTO users (display_name, email, password_hash, created_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
            statement.setString(1, displayName);
            statement.setString(2, email);
            statement.setString(3, passwordHash);
            statement.setString(4, Instant.now().toString());
            statement.executeUpdate();
        }
    }

    private static Integer findUserId(Connection connection, String normalizedEmail) throws SQLException {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, normalizedEmail);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Integer.valueOf(resultSet.getInt("id"));
                }
                return null;
            }
        }
    }

    private static void markUnusedOtpsUsed(Connection connection, String email) throws SQLException {
        String sql = "UPDATE otp_codes SET used = 1 WHERE email = ? AND used = 0";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.executeUpdate();
        }
    }

    private static void invalidateAllOtps(String email) {
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail.isEmpty()) {
            return;
        }
        String sql = "UPDATE otp_codes SET used = 1 WHERE email = ? AND used = 0";
        try (Connection connection = openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, normalizedEmail);
            statement.executeUpdate();
        } catch (SQLException | IOException ignored) {
            // otp invalidation fallback logic
        }
    }

    private static void markOtpUsedById(Connection connection, int otpId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE otp_codes SET used = 1 WHERE id = ?")) {
            statement.setInt(1, otpId);
            statement.executeUpdate();
        }
    }

    private static Connection openConnection() throws SQLException, IOException {
        String jdbcUrl = jdbcUrl();
        ensureSqliteDirectory(jdbcUrl);
        return DriverManager.getConnection(jdbcUrl);
    }

    private static void ensureSqliteDirectory(String jdbcUrl) throws IOException {
        if (jdbcUrl == null || !jdbcUrl.startsWith("jdbc:sqlite:")) {
            return;
        }

        String target = jdbcUrl.substring("jdbc:sqlite:".length());
        int queryIndex = target.indexOf('?');
        if (queryIndex >= 0) {
            target = target.substring(0, queryIndex);
        }

        if (target.isEmpty() || ":memory:".equals(target) || target.startsWith("file:")) {
            return;
        }

        Path dbPath = Paths.get(target);
        Path parent = dbPath.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    private static boolean isSqlite() {
        return jdbcUrl().startsWith("jdbc:sqlite:");
    }

    private static String jdbcUrl() {
        return DEFAULT_JDBC_URL;
    }

    private static String defaultEmail() {
        return normalizeEmail(DEFAULT_APP_EMAIL);
    }

    private static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private static String sanitizeDisplayName(String displayName, String fallbackEmail) {
        String trimmed = displayName == null ? "" : displayName.trim();
        if (!trimmed.isEmpty()) {
            return trimmed;
        }
        if (fallbackEmail != null && fallbackEmail.contains("@")) {
            return fallbackEmail.substring(0, fallbackEmail.indexOf('@'));
        }
        return "User";
    }

    private static String hashPassword(String plainPassword) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        byte[] digest = sha256(concat(salt, plainPassword.getBytes(StandardCharsets.UTF_8)));
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(digest);
    }

    private static boolean verifyPassword(String plainPassword, String storedHash) {
        if (storedHash == null || !storedHash.contains(":")) {
            return false;
        }
        String[] parts = storedHash.split(":", 2);
        try {
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedDigest = Base64.getDecoder().decode(parts[1]);
            byte[] actualDigest = sha256(concat(salt, plainPassword.getBytes(StandardCharsets.UTF_8)));
            return MessageDigest.isEqual(expectedDigest, actualDigest);
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    private static String hashOtpCode(String otpCode) {
        byte[] digest = sha256(otpCode.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(digest);
    }

    private static boolean safeEquals(String left, String right) {
        if (left == null || right == null) {
            return false;
        }
        return MessageDigest.isEqual(
                left.getBytes(StandardCharsets.UTF_8),
                right.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] sha256(byte[] value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(value);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable.", exception);
        }
    }

    private static byte[] concat(byte[] first, byte[] second) {
        byte[] merged = new byte[first.length + second.length];
        System.arraycopy(first, 0, merged, 0, first.length);
        System.arraycopy(second, 0, merged, first.length, second.length);
        return merged;
    }


    public static final class DatabaseState {
        public final ArrayList<IncomeEntry> incomeEntries = new ArrayList<IncomeEntry>();
        public final ArrayList<ExpenseEntry> expenseEntries = new ArrayList<ExpenseEntry>();
        public final ArrayList<SavingEntry> savingEntries = new ArrayList<SavingEntry>();
        public final LinkedHashMap<String, Double> categoryBudgetLimits = new LinkedHashMap<String, Double>();
        public double budgetLimit;
        public double savingGoalTarget;
        public String savingGoalCategory = "Other";
    }

    public static final class UserRecord {
        public final int id;
        public final String displayName;
        public final String email;

        public UserRecord(int id, String displayName, String email) {
            this.id = id;
            this.displayName = displayName;
            this.email = email;
        }
    }

    public static final class OtpDispatchResult {
        public final boolean success;
        public final String message;

        private OtpDispatchResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public static OtpDispatchResult success(String message) {
            return new OtpDispatchResult(true, message);
        }

        public static OtpDispatchResult failure(String message) {
            return new OtpDispatchResult(false, message);
        }
    }
}
