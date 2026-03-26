package baon.ui;

import baon.data.AppDatabase;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {
    private static final String FONT_FAMILY = AppTheme.text("--font-family", "Segoe UI");
    private static final Color PAGE_TOP = AppTheme.color("--login-page-top", "#E8E3D5");
    private static final Color PAGE_BOTTOM = AppTheme.color("--login-page-bottom", "#F6EED7");
    private static final Color CARD_BACKGROUND = AppTheme.color("--login-card-background", "#F8F7F3");
    private static final Color CARD_BORDER = AppTheme.color("--login-card-border", "#D9CDB4");
    private static final Color CARD_SHADOW = AppTheme.color("--login-card-shadow", "rgba(86, 68, 43, 28)");
    private static final Color TEXT_PRIMARY = AppTheme.color("--login-text-primary", "#0F1B2D");
    private static final Color TEXT_SECONDARY = AppTheme.color("--login-text-secondary", "#715F43");
    private static final Color FIELD_BACKGROUND = AppTheme.color("--login-field-background", "#FBFBFA");
    private static final Color FIELD_BORDER = AppTheme.color("--login-field-border", "#CDBEA1");
    private static final Color TAB_ACTIVE = AppTheme.color("--login-tab-active", "#2C7781");
    private static final Color TAB_ACTIVE_BORDER = AppTheme.color("--login-tab-active-border", "#2A6A73");
    private static final Color TAB_INACTIVE = AppTheme.color("--login-tab-inactive", "#FFFFFF");
    private static final Color TAB_INACTIVE_BORDER = AppTheme.color("--login-tab-inactive-border", "#CDBEA1");
    private static final Color ACTION = AppTheme.color("--login-action", "#C77754");
    private static final Color ACTION_BORDER = AppTheme.color("--login-action-border", "#B46A4B");
    private static final Color ERROR = AppTheme.color("--login-error", "#B83D3D");
    private static final Color SUCCESS = AppTheme.color("--login-success", "#2F8D55");

    private static final String TAB_LOGIN = "login";
    private static final String TAB_CREATE = "create";

    private final JTextField loginEmailField = new JTextField();
    private final JPasswordField loginPasswordField = new JPasswordField();

    private final JTextField createNameField = new JTextField();
    private final JTextField createEmailField = new JTextField();
    private final JPasswordField createPasswordField = new JPasswordField();
    private final JPasswordField createConfirmPasswordField = new JPasswordField();

    private final JLabel statusLabel = new JLabel(" ");
    private final CardLayout formLayout = new CardLayout();
    private final JPanel formContentPanel = new JPanel(formLayout);
    private JButton signInTabButton;
    private JButton createAccountTabButton;

    public LoginFrame() {
        super("BaonBrain Login");
        configureFrame();
    }

    private void configureFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 620));
        setSize(1240, 760);
        setLocationRelativeTo(null);
        setContentPane(createRoot());
        try {
            AppDatabase.initialize();
        } catch (RuntimeException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database initialization failed. Check JDBC_URL and JDBC driver setup.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createRoot() {
        GradientPanel root = new GradientPanel();
        root.setLayout(new BorderLayout());

        ResponsiveCenterPanel center = new ResponsiveCenterPanel(createLoginContent());
        JScrollPane scrollPane = new JScrollPane(center);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        root.add(scrollPane, BorderLayout.CENTER);
        return root;
    }

    private JPanel createLoginContent() {
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBorder(new EmptyBorder(24, 20, 24, 20));

        JLabel pageTitle = new JLabel("Login / Registration");
        pageTitle.setFont(new Font(FONT_FAMILY, Font.BOLD, 56));
        pageTitle.setForeground(TEXT_PRIMARY);
        pageTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cardWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        cardWrap.setOpaque(false);
        cardWrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardWrap.add(createFormCard());

        wrap.add(pageTitle);
        wrap.add(Box.createVerticalStrut(20));
        wrap.add(cardWrap);
        wrap.add(Box.createVerticalGlue());
        return wrap;
    }

    private JPanel createFormCard() {
        ShadowCardPanel card = new ShadowCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(28, 30, 24, 30));
        card.setPreferredSize(new Dimension(960, 560));
        card.setMaximumSize(new Dimension(980, Integer.MAX_VALUE));

        JPanel tabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        tabs.setOpaque(false);
        tabs.setAlignmentX(Component.LEFT_ALIGNMENT);

        signInTabButton = createTab("Sign In", true);
        signInTabButton.addActionListener(event -> switchTab(TAB_LOGIN));
        createAccountTabButton = createTab("Create Account", false);
        createAccountTabButton.addActionListener(event -> switchTab(TAB_CREATE));

        tabs.add(signInTabButton);
        tabs.add(createAccountTabButton);

        formContentPanel.setOpaque(false);
        formContentPanel.add(createLoginPanel(), TAB_LOGIN);
        formContentPanel.add(createCreateAccountPanel(), TAB_CREATE);

        statusLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        statusLabel.setForeground(TEXT_SECONDARY);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(tabs);
        card.add(Box.createVerticalStrut(20));
        card.add(formContentPanel);
        card.add(Box.createVerticalStrut(10));
        card.add(statusLabel);

        getRootPane().setDefaultButton(null);
        switchTab(TAB_LOGIN);
        return card;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Sign In");
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 46));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Access your BaonBrain account");
        subtitle.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel emailLabel = createFieldLabel("Email Address");
        styleTextField(loginEmailField, "student@email.com");

        JLabel passwordLabel = createFieldLabel("Password");
        stylePasswordField(loginPasswordField, "Enter your password");

        JPanel optionsRow = new JPanel(new BorderLayout());
        optionsRow.setOpaque(false);
        optionsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox rememberMe = new JCheckBox("Remember me");
        rememberMe.setOpaque(false);
        rememberMe.setFocusable(false);
        rememberMe.setFont(new Font(FONT_FAMILY, Font.PLAIN, 18));
        rememberMe.setForeground(TEXT_SECONDARY);
        rememberMe.setBorder(new EmptyBorder(0, 0, 0, 0));

        JButton forgotPasswordButton = new JButton("Forgot password?");
        forgotPasswordButton.setFont(new Font(FONT_FAMILY, Font.PLAIN, 18));
        forgotPasswordButton.setForeground(TAB_ACTIVE_BORDER);
        forgotPasswordButton.setOpaque(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setFocusPainted(false);
        forgotPasswordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPasswordButton.addActionListener(event -> handleForgotPassword());

        optionsRow.add(rememberMe, BorderLayout.WEST);
        optionsRow.add(forgotPasswordButton, BorderLayout.EAST);

        JButton loginButton = createPrimaryButton("[ Sign In ]");
        loginButton.addActionListener(event -> handleLogin());

        panel.add(title);
        panel.add(Box.createVerticalStrut(6));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(18));
        panel.add(emailLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(loginEmailField);
        panel.add(Box.createVerticalStrut(14));
        panel.add(passwordLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(loginPasswordField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(optionsRow);
        panel.add(Box.createVerticalStrut(16));
        panel.add(loginButton);
        return panel;
    }

    private JPanel createCreateAccountPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Create Account");
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 42));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Create credentials for your BaonBrain account");
        subtitle.setFont(new Font(FONT_FAMILY, Font.PLAIN, 16));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLabel = createFieldLabel("Full Name");
        styleTextField(createNameField, "Juan Dela Cruz");

        JLabel emailLabel = createFieldLabel("Email Address");
        styleTextField(createEmailField, "you@email.com");

        JLabel passwordLabel = createFieldLabel("Password");
        stylePasswordField(createPasswordField, "Create a password");

        JLabel confirmLabel = createFieldLabel("Confirm Password");
        stylePasswordField(createConfirmPasswordField, "Repeat your password");

        JButton createButton = createPrimaryButton("[ Create Account ]");
        createButton.addActionListener(event -> handleCreateAccount());

        panel.add(title);
        panel.add(Box.createVerticalStrut(6));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(16));
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(createNameField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(emailLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(createEmailField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(passwordLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(createPasswordField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(confirmLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(createConfirmPasswordField);
        panel.add(Box.createVerticalStrut(16));
        panel.add(createButton);
        return panel;
    }

    private void switchTab(String tabKey) {
        formLayout.show(formContentPanel, tabKey);
        boolean loginActive = TAB_LOGIN.equals(tabKey);
        styleTabButton(signInTabButton, loginActive);
        styleTabButton(createAccountTabButton, !loginActive);
        statusLabel.setText(" ");
    }

    private JButton createTab(String text, boolean active) {
        JButton tab = new JButton(text);
        tab.setFocusable(false);
        tab.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tab.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        styleTabButton(tab, active);
        tab.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(active ? TAB_ACTIVE_BORDER : TAB_INACTIVE_BORDER),
                new EmptyBorder(8, 16, 8, 16)));
        return tab;
    }

    private void styleTabButton(JButton button, boolean active) {
        button.setForeground(active ? Color.WHITE : TEXT_SECONDARY);
        button.setBackground(active ? TAB_ACTIVE : TAB_INACTIVE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(active ? TAB_ACTIVE_BORDER : TAB_INACTIVE_BORDER),
                new EmptyBorder(8, 16, 8, 16)));
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setPreferredSize(new Dimension(0, 40));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBackground(ACTION);
        button.setForeground(Color.WHITE);
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACTION_BORDER),
                new EmptyBorder(8, 16, 8, 16)));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        return button;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        label.setForeground(TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleTextField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        field.setPreferredSize(new Dimension(0, 36));
        field.setFont(new Font(FONT_FAMILY, Font.PLAIN, 12));
        field.setBackground(new Color(227, 234, 246));
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TAB_ACTIVE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER),
                new EmptyBorder(8, 14, 8, 14)));
        field.setToolTipText(placeholder);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void stylePasswordField(JPasswordField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        field.setPreferredSize(new Dimension(0, 36));
        field.setFont(new Font(FONT_FAMILY, Font.PLAIN, 12));
        field.setBackground(new Color(227, 234, 246));
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TAB_ACTIVE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER),
                new EmptyBorder(8, 14, 8, 14)));
        field.setToolTipText(placeholder);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

        private void handleLogin() {
        String email = loginEmailField.getText().trim().toLowerCase();
        String password = new String(loginPasswordField.getPassword()).trim();

        if (email.isEmpty() && password.isEmpty()) {
            showStatus("Email and password are required.", ERROR);
            return;
        }

        if (email.isEmpty()) {
            showStatus("Please enter your email.", ERROR);
            return;
        }

        if (password.isEmpty()) {
            showStatus("Please enter your password.", ERROR);
            return;
        }

        AppDatabase.UserRecord user;
        try {
            user = AppDatabase.authenticateUser(email, password);
        } catch (RuntimeException exception) {
            showStatus("Database is unavailable. Check JDBC settings.", ERROR);
            return;
        }
        if (user == null) {
            showStatus("Invalid email or password.", ERROR);
            return;
        }

        showStatus("Login successful. Opening dashboard...", SUCCESS);
        dispose();
        new MainFrame(user.displayName, user.email).setVisible(true);
    }

    private void handleCreateAccount() {
        String name = createNameField.getText().trim();
        String email = createEmailField.getText().trim().toLowerCase();
        String password = new String(createPasswordField.getPassword()).trim();
        String confirmPassword = new String(createConfirmPasswordField.getPassword()).trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showStatus("All create-account fields are required.", ERROR);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showStatus("Please enter a valid email address.", ERROR);
            return;
        }

        if (password.length() < 6) {
            showStatus("Password must be at least 6 characters.", ERROR);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showStatus("Password and confirm password do not match.", ERROR);
            return;
        }

        try {
            if (AppDatabase.userExists(email)) {
                showStatus("An account with this email already exists.", ERROR);
                return;
            }

            AppDatabase.OtpDispatchResult dispatchResult = AppDatabase.sendOtp(email);
            if (!dispatchResult.success) {
                showStatus(dispatchResult.message, ERROR);
                return;
            }

            showStatus(dispatchResult.message, SUCCESS);
            JOptionPane.showMessageDialog(
                    this,
                    dispatchResult.message,
                    "OTP Information",
                    JOptionPane.INFORMATION_MESSAGE);
            String otpCode = JOptionPane.showInputDialog(
                    this,
                    "Enter the 6-digit OTP sent to " + email + ":",
                    "Email Verification",
                    JOptionPane.PLAIN_MESSAGE);

            if (otpCode == null) {
                showStatus("OTP verification cancelled.", ERROR);
                return;
            }

            if (!AppDatabase.verifyOtp(email, otpCode.trim())) {
                showStatus("Invalid or expired OTP. Please try again.", ERROR);
                return;
            }

            if (!AppDatabase.createUser(name, email, password)) {
                showStatus("Could not create account. Try again.", ERROR);
                return;
            }
        } catch (Throwable exception) {
            JOptionPane.showMessageDialog(this, "Create account failed. Check .env and rebuild.", "Error", JOptionPane.ERROR_MESSAGE);
            showStatus("Create account failed. Check SMTP settings and rebuild.", ERROR);
            return;
        }

        loginEmailField.setText(email);
        loginPasswordField.setText("");
        showStatus("Account created for " + name + ". You can now sign in.", SUCCESS);
        switchTab(TAB_LOGIN);
    }

    private void handleForgotPassword() {
        String email = loginEmailField.getText().trim().toLowerCase();
        if (email.isEmpty()) {
            String enteredEmail = JOptionPane.showInputDialog(
                    this,
                    "Enter your account email:",
                    "Forgot Password",
                    JOptionPane.PLAIN_MESSAGE);
            if (enteredEmail == null) {
                showStatus("Password reset cancelled.", ERROR);
                return;
            }
            email = enteredEmail.trim().toLowerCase();
        }

        if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
            showStatus("Please enter a valid email address.", ERROR);
            return;
        }

        try {
            if (!AppDatabase.userExists(email)) {
                showStatus("No account found for that email.", ERROR);
                return;
            }

            AppDatabase.OtpDispatchResult dispatchResult = AppDatabase.sendOtp(email);
            if (!dispatchResult.success) {
                showStatus(dispatchResult.message, ERROR);
                return;
            }

            showStatus(dispatchResult.message, SUCCESS);
            JOptionPane.showMessageDialog(
                    this,
                    dispatchResult.message,
                    "Password Reset OTP",
                    JOptionPane.INFORMATION_MESSAGE);

            String otpCode = JOptionPane.showInputDialog(
                    this,
                    "Enter the 6-digit OTP sent to " + email + ":",
                    "Verify OTP",
                    JOptionPane.PLAIN_MESSAGE);
            if (otpCode == null) {
                showStatus("Password reset cancelled.", ERROR);
                return;
            }

            if (!AppDatabase.verifyOtp(email, otpCode.trim())) {
                showStatus("Invalid or expired OTP. Please try again.", ERROR);
                return;
            }

            JPasswordField newPasswordField = new JPasswordField();
            JPasswordField confirmPasswordField = new JPasswordField();
            JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
            panel.add(new JLabel("New Password"));
            panel.add(newPasswordField);
            panel.add(new JLabel("Confirm Password"));
            panel.add(confirmPasswordField);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    panel,
                    "Set New Password",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
            if (result != JOptionPane.OK_OPTION) {
                showStatus("Password reset cancelled.", ERROR);
                return;
            }

            String newPassword = new String(newPasswordField.getPassword()).trim();
            String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
            if (newPassword.length() < 6) {
                showStatus("Password must be at least 6 characters.", ERROR);
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                showStatus("Password and confirm password do not match.", ERROR);
                return;
            }

            if (!AppDatabase.updatePassword(email, newPassword)) {
                showStatus("Could not reset password. Try again.", ERROR);
                return;
            }

            loginEmailField.setText(email);
            loginPasswordField.setText("");
            showStatus("Password reset successful. You can now sign in.", SUCCESS);
        } catch (Throwable exception) {
            JOptionPane.showMessageDialog(this, "Password reset failed. Check .env and rebuild.", "Error", JOptionPane.ERROR_MESSAGE);
            showStatus("Password reset failed. Check SMTP settings and rebuild.", ERROR);
        }
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

        private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, PAGE_TOP, getWidth(), getHeight(), PAGE_BOTTOM));
            g2.fillRect(0, 0, getWidth(), getHeight());

            float[] fractions = new float[] { 0.0f, 0.38f, 1.0f };
            Color[] colors = new Color[] {
                    new Color(255, 247, 227, 170),
                    new Color(255, 243, 214, 95),
                    new Color(255, 241, 210, 0)
            };
            float radius = Math.max(getWidth(), getHeight()) * 0.48f;
            RadialGradientPaint glow = new RadialGradientPaint(getWidth() / 2.0f, 110.0f, radius, fractions, colors);
            g2.setPaint(glow);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }
        private static class ShadowCardPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth() - 1;
            int height = getHeight() - 10;

            g2.setColor(new Color(121, 94, 58, 38));
            g2.fillRoundRect(8, 10, width - 16, height, 24, 24);

            g2.setColor(CARD_BACKGROUND);
            g2.fillRoundRect(0, 0, width, height, 24, 24);

            g2.setColor(CARD_BORDER);
            g2.drawRoundRect(0, 0, width, height, 24, 24);
            g2.dispose();
        }
    }
    private static class ResponsiveCenterPanel extends JPanel implements Scrollable {
        private final JPanel content;

        ResponsiveCenterPanel(JPanel content) {
            this.content = content;
            setOpaque(false);
            setLayout(new GridLayout(1, 1));
            add(content);
        }

        @Override
        public Dimension getPreferredSize() {
            int width = getParent() == null ? 1240 : Math.max(980, getParent().getWidth());
            int height = Math.max(640, content.getPreferredSize().height + 80);
            return new Dimension(width, height);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 24;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return Math.max(visibleRect.height - 48, 48);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return getParent() != null && getPreferredSize().height <= getParent().getHeight();
        }
    }
}
















