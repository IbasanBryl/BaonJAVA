package baon.ui;

import baon.data.AppDatabase;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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
import javax.swing.border.LineBorder;

public class LoginFrame extends JFrame {
    private static final String FONT_FAMILY = AppTheme.text("--font-family", "Segoe UI");
    private static final Color PAGE_TOP = AppTheme.color("--login-page-top", "#E7E9EE");
    private static final Color PAGE_BOTTOM = AppTheme.color("--login-page-bottom", "#DBDEE6");
    private static final Color CARD_BACKGROUND = AppTheme.color("--login-card-background", "#F3F4F8");
    private static final Color CARD_BORDER = AppTheme.color("--login-card-border", "#CFD3DC");
    private static final Color CARD_SHADOW = AppTheme.color("--login-card-shadow", "rgba(43, 52, 76, 30)");
    private static final Color TEXT_PRIMARY = AppTheme.color("--login-text-primary", "#1F253B");
    private static final Color TEXT_SECONDARY = AppTheme.color("--login-text-secondary", "#5C667E");
    private static final Color FIELD_BACKGROUND = AppTheme.color("--login-field-background", "#ECEFF5");
    private static final Color FIELD_BORDER = AppTheme.color("--login-field-border", "#CDD3DF");
    private static final Color TAB_ACTIVE = AppTheme.color("--login-tab-active", "#FFFFFF");
    private static final Color TAB_ACTIVE_BORDER = AppTheme.color("--login-tab-active-border", "#CCD3E0");
    private static final Color TAB_INACTIVE = AppTheme.color("--login-tab-inactive", "#DEE2EA");
    private static final Color TAB_INACTIVE_BORDER = AppTheme.color("--login-tab-inactive-border", "#CDD3DF");
    private static final Color ACTION = AppTheme.color("--login-action", "#3369D8");
    private static final Color ACTION_HOVER = AppTheme.color("--login-action-hover", "#2859BD");
    private static final Color ACTION_BORDER = AppTheme.color("--login-action-border", "#2859BD");
    private static final Color LINK = AppTheme.color("--login-link", "#3A61BD");
    private static final Color ERROR = AppTheme.color("--login-error", "#B83D3D");
    private static final Color SUCCESS = AppTheme.color("--login-success", "#2F8D55");
    private static final int LOGIN_CARD_PREFERRED_WIDTH = 600;
    private static final int LOGIN_CARD_MAX_WIDTH = 620;
    private static final int LOGIN_CARD_MIN_WIDTH = 420;
    private static final int LOGIN_CARD_MIN_HEIGHT = 430;
    private static final int LOGIN_CONTENT_MIN_WIDTH = 360;
    private static final int LOGIN_CONTENT_MIN_HEIGHT = 520;

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
    private JButton loginButton;
    private JButton createButton;

    public LoginFrame() {
        super("BaonBrain Login");
        configureFrame();
    }

    private void configureFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(680, 540));
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
        wrap.setBorder(new EmptyBorder(20, 28, 20, 28));

        JLabel pageTitle = new JLabel("BaonBrain");
        pageTitle.setFont(new Font(FONT_FAMILY, Font.BOLD, 52));
        pageTitle.setForeground(TEXT_PRIMARY);
        pageTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel pageSubtitle = new JLabel("Sign in to your account or create a new one.");
        pageSubtitle.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        pageSubtitle.setForeground(TEXT_SECONDARY);
        pageSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel cardWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        cardWrap.setOpaque(false);
        cardWrap.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardWrap.add(createFormCard());

        wrap.add(Box.createVerticalGlue());
        wrap.add(pageTitle);
        wrap.add(Box.createVerticalStrut(10));
        wrap.add(pageSubtitle);
        wrap.add(Box.createVerticalStrut(18));
        wrap.add(cardWrap);
        wrap.add(Box.createVerticalGlue());
        return wrap;
    }

    private JPanel createFormCard() {
        ResponsiveFormCardPanel card = new ResponsiveFormCardPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 16, 12, 16));

        JPanel tabs = new JPanel(new GridLayout(1, 2, 8, 0));
        tabs.setOpaque(false);
        tabs.setBorder(new EmptyBorder(0, 0, 8, 0));

        signInTabButton = createTab("Sign In", true);
        signInTabButton.addActionListener(event -> switchTab(TAB_LOGIN));
        createAccountTabButton = createTab("Register", false);
        createAccountTabButton.addActionListener(event -> switchTab(TAB_CREATE));

        tabs.add(signInTabButton);
        tabs.add(createAccountTabButton);

        formContentPanel.setOpaque(false);
        formContentPanel.setBorder(new EmptyBorder(8, 0, 0, 0));
        formContentPanel.add(createLoginPanel(), TAB_LOGIN);
        formContentPanel.add(createCreateAccountPanel(), TAB_CREATE);

        statusLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        statusLabel.setForeground(TEXT_SECONDARY);
        statusLabel.setBorder(new EmptyBorder(6, 2, 0, 2));

        card.add(tabs, BorderLayout.NORTH);
        card.add(formContentPanel, BorderLayout.CENTER);
        card.add(statusLabel, BorderLayout.SOUTH);

        switchTab(TAB_LOGIN);
        return card;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel columnWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        columnWrap.setOpaque(false);

        JPanel column = new JPanel();
        column.setOpaque(false);
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));

        int formWidth = 320;

        JLabel emailLabel = createFieldLabel("Email");
        styleTextField(loginEmailField, "Enter your email");
        loginEmailField.setMaximumSize(new Dimension(formWidth, 40));
        loginEmailField.setPreferredSize(new Dimension(formWidth, 40));

        JLabel passwordLabel = createFieldLabel("Password");
        stylePasswordField(loginPasswordField, "Enter your password");
        loginPasswordField.setMaximumSize(new Dimension(formWidth, 40));
        loginPasswordField.setPreferredSize(new Dimension(formWidth, 40));

        JPanel forgotRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        forgotRow.setOpaque(false);
        forgotRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        forgotRow.setMaximumSize(new Dimension(formWidth, 24));
        forgotRow.setPreferredSize(new Dimension(formWidth, 24));
        JButton forgotPasswordButton = createLinkButton("Forgot password?");
        forgotPasswordButton.addActionListener(event -> handleForgotPassword());
        forgotRow.add(forgotPasswordButton);

        loginButton = createPrimaryButton("Sign In  ->");
        loginButton.addActionListener(event -> handleLogin());
        loginButton.setMaximumSize(new Dimension(formWidth, 42));
        loginButton.setPreferredSize(new Dimension(formWidth, 42));

        column.add(emailLabel);
        column.add(Box.createVerticalStrut(7));
        column.add(loginEmailField);
        column.add(Box.createVerticalStrut(12));
        column.add(passwordLabel);
        column.add(Box.createVerticalStrut(7));
        column.add(loginPasswordField);
        column.add(Box.createVerticalStrut(8));
        column.add(forgotRow);
        column.add(Box.createVerticalStrut(12));
        column.add(loginButton);
        column.add(Box.createVerticalStrut(10));

        JPanel signupRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        signupRow.setOpaque(false);
        signupRow.setMaximumSize(new Dimension(formWidth, 22));
        signupRow.setPreferredSize(new Dimension(formWidth, 22));
        signupRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel signupHint = new JLabel("Don't have an account?");
        signupHint.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        signupHint.setForeground(TEXT_SECONDARY);
        JButton signupLink = createLinkButton("Sign up");
        signupLink.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        signupLink.addActionListener(event -> switchTab(TAB_CREATE));
        signupRow.add(signupHint);
        signupRow.add(signupLink);
        column.add(signupRow);

        columnWrap.add(column);
        panel.add(columnWrap, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCreateAccountPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel nameLabel = createFieldLabel("Full Name");
        styleTextField(createNameField, "Juan Dela Cruz");

        JLabel emailLabel = createFieldLabel("Email");
        styleTextField(createEmailField, "you@email.com");

        JLabel passwordLabel = createFieldLabel("Password");
        stylePasswordField(createPasswordField, "Create a password");

        JLabel confirmLabel = createFieldLabel("Confirm Password");
        stylePasswordField(createConfirmPasswordField, "Repeat your password");

        createButton = createPrimaryButton("Create Account  ->");
        createButton.addActionListener(event -> handleCreateAccount());

        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(7));
        panel.add(createNameField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(emailLabel);
        panel.add(Box.createVerticalStrut(7));
        panel.add(createEmailField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(passwordLabel);
        panel.add(Box.createVerticalStrut(7));
        panel.add(createPasswordField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(confirmLabel);
        panel.add(Box.createVerticalStrut(7));
        panel.add(createConfirmPasswordField);
        panel.add(Box.createVerticalStrut(14));
        panel.add(createButton);
        return panel;
    }

    private void switchTab(String tabKey) {
        formLayout.show(formContentPanel, tabKey);
        boolean loginActive = TAB_LOGIN.equals(tabKey);
        styleTabButton(signInTabButton, loginActive);
        styleTabButton(createAccountTabButton, !loginActive);
        if (loginActive && loginButton != null) {
            getRootPane().setDefaultButton(loginButton);
        } else if (!loginActive && createButton != null) {
            getRootPane().setDefaultButton(createButton);
        }
        statusLabel.setText(" ");
    }

    private JButton createTab(String text, boolean active) {
        JButton tab = new JButton(text);
        tab.setFocusable(false);
        tab.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tab.setFont(new Font(FONT_FAMILY, Font.BOLD, 15));
        tab.setFocusPainted(false);
        tab.setOpaque(true);
        tab.setContentAreaFilled(true);
        styleTabButton(tab, active);
        return tab;
    }

    private void styleTabButton(JButton button, boolean active) {
        button.setForeground(active ? TEXT_PRIMARY : TEXT_SECONDARY);
        button.setBackground(active ? TAB_ACTIVE : TAB_INACTIVE);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(active ? TAB_ACTIVE_BORDER : TAB_INACTIVE_BORDER, 1, true),
                new EmptyBorder(10, 16, 10, 16)));
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setPreferredSize(new Dimension(0, 42));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBackground(ACTION);
        button.setForeground(Color.WHITE);
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ACTION_BORDER, 1, true),
                new EmptyBorder(10, 16, 10, 16)));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                button.setBackground(ACTION_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent event) {
                button.setBackground(ACTION);
            }
        });
        return button;
    }

    private JButton createLinkButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
        button.setForeground(LINK);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        label.setForeground(TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleTextField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setPreferredSize(new Dimension(0, 40));
        field.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
        field.setBackground(FIELD_BACKGROUND);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACTION);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(FIELD_BORDER, 1, true),
                new EmptyBorder(8, 14, 8, 14)));
        field.setToolTipText(placeholder);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void stylePasswordField(JPasswordField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setPreferredSize(new Dimension(0, 40));
        field.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
        field.setBackground(FIELD_BACKGROUND);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACTION);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(FIELD_BORDER, 1, true),
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
            showOtpDispatchDialog("OTP Information", dispatchResult.message);
            String otpCode = promptForOtp(email, "Email Verification");
            if (otpCode == null) {
                showStatus("OTP verification cancelled.", ERROR);
                return;
            }

            if (!AppDatabase.verifyOtp(email, otpCode)) {
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
            showOtpDispatchDialog("Password Reset OTP", dispatchResult.message);

            String otpCode = promptForOtp(email, "Verify OTP");
            if (otpCode == null) {
                showStatus("Password reset cancelled.", ERROR);
                return;
            }

            if (!AppDatabase.verifyOtp(email, otpCode)) {
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

    private String promptForOtp(String email, String title) {
        JTextField otpField = new JTextField();
        otpField.setColumns(8);
        otpField.setPreferredSize(new Dimension(240, 42));
        otpField.setMaximumSize(new Dimension(240, 42));
        otpField.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        otpField.setHorizontalAlignment(JTextField.CENTER);
        otpField.setBackground(FIELD_BACKGROUND);
        otpField.setForeground(TEXT_PRIMARY);
        otpField.setCaretColor(ACTION);
        otpField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(FIELD_BORDER, 1, true),
                new EmptyBorder(10, 14, 10, 14)));

        JPanel dialogPanel = new JPanel();
        dialogPanel.setOpaque(false);
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));

        JLabel instruction = new JLabel("Enter the 6-digit code sent to:");
        instruction.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        instruction.setForeground(TEXT_SECONDARY);
        instruction.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel emailLabel = new JLabel(email);
        emailLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        emailLabel.setForeground(TEXT_PRIMARY);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        otpField.setAlignmentX(Component.LEFT_ALIGNMENT);

        dialogPanel.add(instruction);
        dialogPanel.add(Box.createVerticalStrut(4));
        dialogPanel.add(emailLabel);
        dialogPanel.add(Box.createVerticalStrut(12));
        dialogPanel.add(otpField);

        while (true) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    dialogPanel,
                    title,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result != JOptionPane.OK_OPTION) {
                return null;
            }

            String otpCode = otpField.getText().trim().replaceAll("\\D", "");
            if (otpCode.length() == 6) {
                return otpCode;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter exactly 6 digits.",
                    "Invalid OTP",
                    JOptionPane.WARNING_MESSAGE);
            otpField.requestFocusInWindow();
            otpField.selectAll();
        }
    }
    private void showOtpDispatchDialog(String title, String message) {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel badge = new JLabel("i", JLabel.CENTER);
        badge.setOpaque(true);
        badge.setBackground(ACTION);
        badge.setForeground(Color.WHITE);
        badge.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        badge.setPreferredSize(new Dimension(30, 30));
        badge.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));

        JLabel text = new JLabel("<html><div style='font-size: 12px;'>" + message + "</div></html>");
        text.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        text.setForeground(TEXT_PRIMARY);

        panel.add(badge, BorderLayout.WEST);
        panel.add(text, BorderLayout.CENTER);
        panel.setBorder(new EmptyBorder(8, 6, 4, 6));

        JOptionPane.showMessageDialog(
                this,
                panel,
                title,
                JOptionPane.PLAIN_MESSAGE);
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

            float[] fractions = new float[] { 0.0f, 0.45f, 1.0f };
            Color[] colors = new Color[] {
                    new Color(250, 231, 186, 120),
                    new Color(250, 231, 186, 55),
                    new Color(250, 231, 186, 0)
            };
            float radius = Math.max(getWidth(), getHeight()) * 0.48f;
            RadialGradientPaint glow = new RadialGradientPaint(getWidth() / 2.0f, getHeight() / 2.4f, radius, fractions, colors);
            g2.setPaint(glow);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setPaint(new GradientPaint(0, 0, new Color(148, 146, 144, 46), getWidth() / 2.0f, 0, new Color(148, 146, 144, 0)));
            g2.fillRect(0, 0, getWidth() / 2, getHeight());
            g2.setPaint(new GradientPaint(getWidth(), 0, new Color(148, 146, 144, 44), getWidth() / 2.0f, 0, new Color(148, 146, 144, 0)));
            g2.fillRect(getWidth() / 2, 0, getWidth() / 2, getHeight());
            g2.dispose();
        }
    }

    private static class ShadowCardPanel extends JPanel {
        ShadowCardPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth() - 1;
            int height = getHeight() - 10;
            int radius = 24;

            Color shadowSoft = new Color(CARD_SHADOW.getRed(), CARD_SHADOW.getGreen(), CARD_SHADOW.getBlue(), 18);
            Color shadowStrong = new Color(CARD_SHADOW.getRed(), CARD_SHADOW.getGreen(), CARD_SHADOW.getBlue(), 36);
            g2.setColor(shadowSoft);
            g2.fillRoundRect(2, 8, width, height + 2, radius + 4, radius + 4);
            g2.setColor(shadowStrong);
            g2.fillRoundRect(7, 12, width - 10, height - 2, radius, radius);

            g2.setColor(CARD_BACKGROUND);
            g2.fillRoundRect(0, 0, width, height, radius, radius);

            g2.setColor(CARD_BORDER);
            g2.drawRoundRect(0, 0, width, height, radius, radius);
            g2.dispose();
        }
    }

    private static class ResponsiveFormCardPanel extends ShadowCardPanel {
        private static final int HORIZONTAL_MARGIN = 12;

        @Override
        public Dimension getPreferredSize() {
            Dimension preferred = super.getPreferredSize();
            int width = LOGIN_CARD_PREFERRED_WIDTH;
            Container parent = getParent();

            if (parent != null && parent.getWidth() > 0) {
                int availableWidth = parent.getWidth() - (HORIZONTAL_MARGIN * 2);
                width = Math.min(LOGIN_CARD_MAX_WIDTH, Math.max(LOGIN_CARD_MIN_WIDTH, availableWidth));
            }

            int height = Math.max(LOGIN_CARD_MIN_HEIGHT, preferred.height);
            return new Dimension(width, height);
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(LOGIN_CARD_MIN_WIDTH, LOGIN_CARD_MIN_HEIGHT);
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(LOGIN_CARD_MAX_WIDTH, Integer.MAX_VALUE);
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
            int parentWidth = getParent() == null ? 1240 : getParent().getWidth();
            int parentHeight = getParent() == null ? 0 : getParent().getHeight();

            int width = Math.max(LOGIN_CONTENT_MIN_WIDTH, parentWidth);
            int contentHeight = content.getPreferredSize().height + 80;
            int height = Math.max(LOGIN_CONTENT_MIN_HEIGHT, Math.max(contentHeight, parentHeight));
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







