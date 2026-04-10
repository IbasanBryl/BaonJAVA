package baon.data;

import java.util.prefs.Preferences;

public final class RememberedLoginStore {
    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(RememberedLoginStore.class);
    private static final String KEY_EMAIL = "rememberedLogin.email";
    private static final String KEY_PASSWORD = "rememberedLogin.password";

    private RememberedLoginStore() {
    }

    public static RememberedLogin load() {
        String email = normalizeEmail(PREFERENCES.get(KEY_EMAIL, ""));
        String password = PREFERENCES.get(KEY_PASSWORD, "");
        if (email.isEmpty() || password.isEmpty()) {
            return RememberedLogin.empty();
        }
        return new RememberedLogin(email, password, true);
    }

    public static void save(String email, String password) {
        String normalizedEmail = normalizeEmail(email);
        String safePassword = password == null ? "" : password;
        if (normalizedEmail.isEmpty() || safePassword.isEmpty()) {
            clear();
            return;
        }

        PREFERENCES.put(KEY_EMAIL, normalizedEmail);
        PREFERENCES.put(KEY_PASSWORD, safePassword);
    }

    public static void clear() {
        PREFERENCES.remove(KEY_EMAIL);
        PREFERENCES.remove(KEY_PASSWORD);
    }

    public static void updatePasswordIfRemembered(String email, String newPassword) {
        String normalizedEmail = normalizeEmail(email);
        if (!normalizedEmail.equals(normalizeEmail(PREFERENCES.get(KEY_EMAIL, "")))) {
            return;
        }

        String safePassword = newPassword == null ? "" : newPassword.trim();
        if (safePassword.isEmpty()) {
            clear();
            return;
        }
        PREFERENCES.put(KEY_PASSWORD, safePassword);
    }

    private static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    public static final class RememberedLogin {
        public final String email;
        public final String password;
        public final boolean remembered;

        private RememberedLogin(String email, String password, boolean remembered) {
            this.email = email;
            this.password = password;
            this.remembered = remembered;
        }

        private static RememberedLogin empty() {
            return new RememberedLogin("", "", false);
        }
    }
}
