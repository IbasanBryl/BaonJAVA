package baon.ui;

import baon.data.AppDatabase;
import baon.data.RememberedLoginStore;

import java.awt.BasicStroke;
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
import java.awt.Image;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
    private static final Color ACCENT_SOFT = new Color(ACTION.getRed(), ACTION.getGreen(), ACTION.getBlue(), 28);
    private static final int LOGIN_CARD_PREFERRED_WIDTH = 720;
    private static final int LOGIN_CARD_MAX_WIDTH = 760;
    private static final int LOGIN_CARD_MIN_WIDTH = 420;
    private static final int LOGIN_CARD_MIN_HEIGHT = 430;
    private static final int LOGIN_CONTENT_MIN_WIDTH = 360;
    private static final int LOGIN_CONTENT_MIN_HEIGHT = 560;
    private static final int FORM_WIDTH = 460;
    private static final String BRAND_LOGO_PATH = "D:\\BaonJava\\lib\\bb-logo.png";
    private static final int BRAND_LOGO_SIZE = 72;
    private static final int TAB_BUTTON_RADIUS = 28;
    private static final int PRIMARY_BUTTON_RADIUS = 32;

    private static final String TAB_LOGIN = "login";
    private static final String TAB_CREATE = "create";

    private final JTextField loginEmailField = new JTextField();
    private final JPasswordField loginPasswordField = new JPasswordField();
    private final JButton loginPasswordToggleButton = new JButton();
    private final JCheckBox rememberPasswordCheckBox = new JCheckBox("Remember password");

    private final JTextField createNameField = new JTextField();
    private final JTextField createEmailField = new JTextField();
    private final JPasswordField createPasswordField = new JPasswordField();
    private final JPasswordField createConfirmPasswordField = new JPasswordField();
    private final JButton createPasswordToggleButton = new JButton();
    private final JButton createConfirmPasswordToggleButton = new JButton();

    private final JLabel statusLabel = new JLabel(" ");
    private final CardLayout formLayout = new CardLayout();
    private final JPanel formContentPanel = new JPanel(formLayout);
    private final JLabel formTitleLabel = new JLabel();
    private final JLabel formDescriptionLabel = new JLabel();
    private JButton signInTabButton;
    private JButton createAccountTabButton;
    private JButton loginButton;
    private JButton createButton;
    private char loginPasswordEchoChar;
    private char createPasswordEchoChar;
    private char createConfirmPasswordEchoChar;
    private boolean loginPasswordVisible;
    private boolean createPasswordVisible;
    private boolean createConfirmPasswordVisible;

    // LoginFrame
    public LoginFrame() {
        super("BaonBrain Login");
        configureFrame();
    }

    // configureFrame
    private void configureFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(680, 540));
        setSize(1240, 760);
        setLocationRelativeTo(null);
        setContentPane(createRoot());
        applyRememberedLogin();
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

    // createRoot
    private JPanel createRoot() {
        GradientPanel root = new GradientPanel();
        root.setLayout(new GridLayout(1, 1));
        root.add(new ResponsiveCenterPanel(createLoginContent()));
        return root;
    }

    // createLoginContent
    private JPanel createLoginContent() {
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBorder(new EmptyBorder(18, 28, 18, 28));

        JPanel hero = new JPanel();
        hero.setOpaque(false);
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));
        hero.setAlignmentX(Component.CENTER_ALIGNMENT);
        hero.setMaximumSize(new Dimension(760, Integer.MAX_VALUE));

        JLabel eyebrow = new JLabel("STUDENT BUDGET COMPANION");
        eyebrow.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        eyebrow.setForeground(TEXT_SECONDARY);
        eyebrow.setAlignmentX(Component.CENTER_ALIGNMENT);
        eyebrow.setBorder(new EmptyBorder(0, 0, 6, 0));

        JLabel pageTitle = new JLabel("BaonBrain");
        pageTitle.setFont(new Font(FONT_FAMILY, Font.BOLD, 68));
        pageTitle.setForeground(TEXT_PRIMARY);
        pageTitle.setIcon(loadBrandLogoIcon());
        pageTitle.setIconTextGap(14);
        pageTitle.setHorizontalTextPosition(SwingConstants.RIGHT);
        pageTitle.setVerticalTextPosition(SwingConstants.CENTER);
        pageTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel pageSubtitle = new JLabel(
                "<html><div style='text-align:center; width:520px'>Track your allowance, stay ahead of spending, and keep your daily baon decisions clear and stress-free.</div></html>");
        pageSubtitle.setFont(new Font(FONT_FAMILY, Font.PLAIN, 18));
        pageSubtitle.setForeground(TEXT_SECONDARY);
        pageSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel heroPills = createHeroPills();

        JPanel cardWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        cardWrap.setOpaque(false);
        cardWrap.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardWrap.add(createFormCard());

        hero.add(eyebrow);
        hero.add(pageTitle);
        hero.add(Box.createVerticalStrut(8));
        hero.add(pageSubtitle);
        hero.add(Box.createVerticalStrut(14));
        hero.add(heroPills);
        hero.setMaximumSize(new Dimension(760, hero.getPreferredSize().height));
        wrap.add(Box.createVerticalStrut(14));
        wrap.add(hero);
        wrap.add(Box.createVerticalStrut(12));
        wrap.add(cardWrap);
        wrap.add(Box.createVerticalStrut(8));
        return wrap;
    }

    // loadBrandLogoIcon
    private Icon loadBrandLogoIcon() {
        ImageIcon icon = new ImageIcon(BRAND_LOGO_PATH);
        if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            return null;
        }
        Image scaled = icon.getImage().getScaledInstance(BRAND_LOGO_SIZE, BRAND_LOGO_SIZE, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private JPanel createHeroPills() {
        JPanel pills = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pills.setOpaque(false);
        pills.setAlignmentX(Component.CENTER_ALIGNMENT);
        pills.add(createHeroPill("Track Expenses"));
        pills.add(createHeroPill("Set Budgets"));
        pills.add(createHeroPill("Forecast Ahead"));
        return pills;
    }

    private JLabel createHeroPill(String text) {
        JLabel pill = new JLabel(text);
        pill.setOpaque(true);
        pill.setBackground(new Color(255, 255, 255, 120));
        pill.setForeground(TEXT_SECONDARY);
        pill.setFont(new Font(FONT_FAMILY, Font.BOLD, 12));
        pill.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 204, 164), 1, true),
                new EmptyBorder(5, 10, 5, 10)));
        return pill;
    }

    // createFormCard
    private JPanel createFormCard() {
        ResponsiveFormCardPanel card = new ResponsiveFormCardPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 20, 16, 20));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel tabs = new JPanel(new GridLayout(1, 2, 6, 0));
        tabs.setOpaque(true);
        tabs.setBackground(FIELD_BACKGROUND);
        tabs.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(FIELD_BORDER, 1, true),
                new EmptyBorder(4, 4, 4, 4)));

        // sign in button logic for tab
        signInTabButton = createTab("Sign In", true);
        signInTabButton.addActionListener(event -> switchTab(TAB_LOGIN));
        // register button logic for tab
        createAccountTabButton = createTab("Register", false);
        createAccountTabButton.addActionListener(event -> switchTab(TAB_CREATE));

        tabs.add(signInTabButton);
        tabs.add(createAccountTabButton);

        formTitleLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 28));
        formTitleLabel.setForeground(TEXT_PRIMARY);
        formTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formTitleLabel.setHorizontalAlignment(JLabel.CENTER);

        formDescriptionLabel.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
        formDescriptionLabel.setForeground(TEXT_SECONDARY);
        formDescriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formDescriptionLabel.setHorizontalAlignment(JLabel.CENTER);

        formContentPanel.setOpaque(false);
        formContentPanel.setBorder(new EmptyBorder(6, 0, 0, 0));
        formContentPanel.add(createLoginPanel(), TAB_LOGIN);
        formContentPanel.add(createCreateAccountPanel(), TAB_CREATE);

        statusLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        statusLabel.setForeground(TEXT_SECONDARY);
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        statusLabel.setBorder(new EmptyBorder(8, 0, 0, 0));

        JPanel statusWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        statusWrap.setOpaque(false);

        JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        statusRow.setOpaque(false);
        statusRow.setMaximumSize(new Dimension(FORM_WIDTH, 24));
        statusRow.setPreferredSize(new Dimension(FORM_WIDTH, 24));
        statusRow.add(statusLabel);
        statusWrap.add(statusRow);

        content.add(tabs);
        content.add(Box.createVerticalStrut(10));
        content.add(formTitleLabel);
        content.add(Box.createVerticalStrut(6));
        content.add(formDescriptionLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(formContentPanel);

        card.add(content, BorderLayout.CENTER);
        card.add(statusWrap, BorderLayout.SOUTH);

        switchTab(TAB_LOGIN);
        return card;
    }

    // createLoginPanel
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel columnWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        columnWrap.setOpaque(false);

        JPanel column = new JPanel();
        column.setOpaque(false);
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));

        JLabel emailLabel = createFieldLabel("Email");
        styleTextField(loginEmailField, "Enter your email");
        loginEmailField.setMaximumSize(new Dimension(FORM_WIDTH, 48));
        loginEmailField.setPreferredSize(new Dimension(FORM_WIDTH, 48));

        JLabel passwordLabel = createFieldLabel("Password");
        JPanel passwordFieldRow = createLoginPasswordFieldRow();

        JPanel rememberAndForgotRow = new JPanel(new BorderLayout());
        rememberAndForgotRow.setOpaque(false);
        rememberAndForgotRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        rememberAndForgotRow.setMaximumSize(new Dimension(FORM_WIDTH, 28));
        rememberAndForgotRow.setPreferredSize(new Dimension(FORM_WIDTH, 28));

        styleRememberPasswordCheckBox();
        rememberAndForgotRow.add(rememberPasswordCheckBox, BorderLayout.WEST);

        JPanel forgotRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        forgotRow.setOpaque(false);
        forgotRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        forgotRow.setMaximumSize(new Dimension(FORM_WIDTH, 24));
        forgotRow.setPreferredSize(new Dimension(FORM_WIDTH, 24));
        // forgot password button logic for reset
        JButton forgotPasswordButton = createLinkButton("Forgot password?");
        forgotPasswordButton.addActionListener(event -> handleForgotPassword());
        forgotRow.add(forgotPasswordButton);
        rememberAndForgotRow.add(forgotRow, BorderLayout.EAST);

        // sign in button logic for login
        loginButton = createPrimaryButton("Sign In to Dashboard");
        loginButton.addActionListener(event -> handleLogin());
        loginButton.setMaximumSize(new Dimension(FORM_WIDTH, 48));
        loginButton.setPreferredSize(new Dimension(FORM_WIDTH, 48));

        column.add(emailLabel);
        column.add(Box.createVerticalStrut(6));
        column.add(loginEmailField);
        column.add(Box.createVerticalStrut(12));
        column.add(passwordLabel);
        column.add(Box.createVerticalStrut(6));
        column.add(passwordFieldRow);
        column.add(Box.createVerticalStrut(8));
        column.add(rememberAndForgotRow);
        column.add(Box.createVerticalStrut(12));
        column.add(loginButton);
        column.add(Box.createVerticalStrut(10));

        JPanel signupRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        signupRow.setOpaque(false);
        signupRow.setMaximumSize(new Dimension(FORM_WIDTH, 22));
        signupRow.setPreferredSize(new Dimension(FORM_WIDTH, 22));
        signupRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel signupHint = new JLabel("Don't have an account?");
        signupHint.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
        signupHint.setForeground(TEXT_SECONDARY);
        // sign up button logic for register
        JButton signupLink = createLinkButton("Sign up");
        signupLink.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        signupLink.addActionListener(event -> switchTab(TAB_CREATE));
        signupRow.add(signupHint);
        signupRow.add(signupLink);
        column.add(signupRow);

        columnWrap.add(column);
        panel.add(columnWrap, BorderLayout.CENTER);
        return panel;
    }

    // createCreateAccountPanel
    private JPanel createCreateAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel columnWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        columnWrap.setOpaque(false);

        JPanel column = new JPanel();
        column.setOpaque(false);
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));

        JLabel nameLabel = createFieldLabel("Full Name");
        styleTextField(createNameField, "Juan Dela Cruz");
        createNameField.setMaximumSize(new Dimension(FORM_WIDTH, 48));
        createNameField.setPreferredSize(new Dimension(FORM_WIDTH, 48));

        JLabel emailLabel = createFieldLabel("Email");
        styleTextField(createEmailField, "you@email.com");
        createEmailField.setMaximumSize(new Dimension(FORM_WIDTH, 48));
        createEmailField.setPreferredSize(new Dimension(FORM_WIDTH, 48));

        JLabel passwordLabel = createFieldLabel("Password");
        JPanel createPasswordFieldRow = createCreatePasswordFieldRow(
                createPasswordField,
                createPasswordToggleButton,
                "Create a password",
                true);

        JLabel confirmLabel = createFieldLabel("Confirm Password");
        JPanel createConfirmPasswordFieldRow = createCreatePasswordFieldRow(
                createConfirmPasswordField,
                createConfirmPasswordToggleButton,
                "Repeat your password",
                false);

        // create account button logic for register
        createButton = createPrimaryButton("Create Account");
        createButton.addActionListener(event -> handleCreateAccount());
        createButton.setMaximumSize(new Dimension(FORM_WIDTH, 48));
        createButton.setPreferredSize(new Dimension(FORM_WIDTH, 48));

        column.add(nameLabel);
        column.add(Box.createVerticalStrut(6));
        column.add(createNameField);
        column.add(Box.createVerticalStrut(12));
        column.add(emailLabel);
        column.add(Box.createVerticalStrut(6));
        column.add(createEmailField);
        column.add(Box.createVerticalStrut(12));
        column.add(passwordLabel);
        column.add(Box.createVerticalStrut(6));
        column.add(createPasswordFieldRow);
        column.add(Box.createVerticalStrut(12));
        column.add(confirmLabel);
        column.add(Box.createVerticalStrut(6));
        column.add(createConfirmPasswordFieldRow);
        column.add(Box.createVerticalStrut(12));
        column.add(createButton);

        columnWrap.add(column);
        panel.add(columnWrap, BorderLayout.CENTER);
        return panel;
    }

    // switchTab
    private void switchTab(String tabKey) {
        formLayout.show(formContentPanel, tabKey);
        boolean loginActive = TAB_LOGIN.equals(tabKey);
        styleTabButton(signInTabButton, loginActive);
        styleTabButton(createAccountTabButton, !loginActive);
        formTitleLabel.setText(loginActive ? "Welcome back" : "Create your account");
        formDescriptionLabel.setText(loginActive
                ? "<html><div style='text-align:center; width: 500px'>Sign in to view your budget, recent spending, and allowance forecast.</div></html>"
                : "<html><div style='text-align:center; width: 500px'>Start tracking your baon with a fresh account and secure email verification.</div></html>");
        if (loginActive && loginButton != null) {
            getRootPane().setDefaultButton(loginButton);
        } else if (!loginActive && createButton != null) {
            getRootPane().setDefaultButton(createButton);
        }
        setLoginPasswordVisible(false);
        setCreatePasswordVisible(false);
        setCreateConfirmPasswordVisible(false);
        statusLabel.setText(" ");
    }

    private JPanel createLoginPasswordFieldRow() {
        stylePasswordField(loginPasswordField, "Enter your password");
        loginPasswordField.setMaximumSize(new Dimension(FORM_WIDTH, 48));
        loginPasswordField.setPreferredSize(new Dimension(FORM_WIDTH, 48));
        if (loginPasswordEchoChar == 0) {
            loginPasswordEchoChar = loginPasswordField.getEchoChar();
        }
        loginPasswordField.setOpaque(false);
        loginPasswordField.setBorder(new EmptyBorder(11, 15, 11, 6));

        configureLoginPasswordToggleButton();
        setLoginPasswordVisible(false);

        JPanel passwordFieldRow = new JPanel(new BorderLayout());
        passwordFieldRow.setOpaque(true);
        passwordFieldRow.setBackground(FIELD_BACKGROUND);
        passwordFieldRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordFieldRow.setMaximumSize(new Dimension(FORM_WIDTH, 48));
        passwordFieldRow.setPreferredSize(new Dimension(FORM_WIDTH, 48));
        passwordFieldRow.setBorder(new LineBorder(FIELD_BORDER, 1, true));
        passwordFieldRow.add(loginPasswordField, BorderLayout.CENTER);
        passwordFieldRow.add(loginPasswordToggleButton, BorderLayout.EAST);
        return passwordFieldRow;
    }

    private JPanel createCreatePasswordFieldRow(
            JPasswordField field,
            JButton toggleButton,
            String placeholder,
            boolean primaryPasswordField) {
        stylePasswordField(field, placeholder);
        field.setMaximumSize(new Dimension(FORM_WIDTH, 48));
        field.setPreferredSize(new Dimension(FORM_WIDTH, 48));
        if (primaryPasswordField) {
            if (createPasswordEchoChar == 0) {
                createPasswordEchoChar = field.getEchoChar();
            }
        } else if (createConfirmPasswordEchoChar == 0) {
            createConfirmPasswordEchoChar = field.getEchoChar();
        }
        field.setOpaque(false);
        field.setBorder(new EmptyBorder(11, 15, 11, 6));

        configureCreatePasswordToggleButton(toggleButton, primaryPasswordField);
        if (primaryPasswordField) {
            setCreatePasswordVisible(false);
        } else {
            setCreateConfirmPasswordVisible(false);
        }

        JPanel passwordFieldRow = new JPanel(new BorderLayout());
        passwordFieldRow.setOpaque(true);
        passwordFieldRow.setBackground(FIELD_BACKGROUND);
        passwordFieldRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordFieldRow.setMaximumSize(new Dimension(FORM_WIDTH, 48));
        passwordFieldRow.setPreferredSize(new Dimension(FORM_WIDTH, 48));
        passwordFieldRow.setBorder(new LineBorder(FIELD_BORDER, 1, true));
        passwordFieldRow.add(field, BorderLayout.CENTER);
        passwordFieldRow.add(toggleButton, BorderLayout.EAST);
        return passwordFieldRow;
    }

    // applyRememberedLogin
    private void applyRememberedLogin() {
        RememberedLoginStore.RememberedLogin rememberedLogin = RememberedLoginStore.load();
        if (!rememberedLogin.remembered) {
            return;
        }

        loginEmailField.setText(rememberedLogin.email);
        loginPasswordField.setText(rememberedLogin.password);
        rememberPasswordCheckBox.setSelected(true);
    }

    private void configureLoginPasswordToggleButton() {
        for (java.awt.event.ActionListener listener : loginPasswordToggleButton.getActionListeners()) {
            loginPasswordToggleButton.removeActionListener(listener);
        }
        loginPasswordToggleButton.setFocusable(false);
        loginPasswordToggleButton.setOpaque(false);
        loginPasswordToggleButton.setContentAreaFilled(false);
        loginPasswordToggleButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 12));
        loginPasswordToggleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginPasswordToggleButton.setPreferredSize(new Dimension(44, 46));
        // password button logic for login
        loginPasswordToggleButton.addActionListener(event -> setLoginPasswordVisible(!loginPasswordVisible));
    }

    private void configureCreatePasswordToggleButton(JButton toggleButton, boolean primaryPasswordField) {
        for (java.awt.event.ActionListener listener : toggleButton.getActionListeners()) {
            toggleButton.removeActionListener(listener);
        }
        toggleButton.setFocusable(false);
        toggleButton.setOpaque(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 12));
        toggleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleButton.setPreferredSize(new Dimension(44, 46));
        if (primaryPasswordField) {
            // password button logic for create account
            toggleButton.addActionListener(event -> setCreatePasswordVisible(!createPasswordVisible));
        } else {
            // confirm password button logic for create account
            toggleButton.addActionListener(event -> setCreateConfirmPasswordVisible(!createConfirmPasswordVisible));
        }
    }

    private void setLoginPasswordVisible(boolean visible) {
        loginPasswordVisible = visible;
        loginPasswordField.setEchoChar(visible ? (char) 0 : loginPasswordEchoChar);
        loginPasswordToggleButton.setIcon(new EyeIcon(18, 12, !visible));
        loginPasswordToggleButton.setToolTipText(visible ? "Hide password" : "Show password");
    }

    private void setCreatePasswordVisible(boolean visible) {
        createPasswordVisible = visible;
        createPasswordField.setEchoChar(visible ? (char) 0 : createPasswordEchoChar);
        createPasswordToggleButton.setIcon(new EyeIcon(18, 12, !visible));
        createPasswordToggleButton.setToolTipText(visible ? "Hide password" : "Show password");
    }

    private void setCreateConfirmPasswordVisible(boolean visible) {
        createConfirmPasswordVisible = visible;
        createConfirmPasswordField.setEchoChar(visible ? (char) 0 : createConfirmPasswordEchoChar);
        createConfirmPasswordToggleButton.setIcon(new EyeIcon(18, 12, !visible));
        createConfirmPasswordToggleButton.setToolTipText(visible ? "Hide password" : "Show password");
    }

    private JButton createTab(String text, boolean active) {
        JButton tab = new RoundedButton(text, TAB_BUTTON_RADIUS);
        tab.setFocusable(false);
        tab.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tab.setFont(new Font(FONT_FAMILY, Font.BOLD, 15));
        tab.setFocusPainted(false);
        tab.setOpaque(false);
        tab.setContentAreaFilled(false);
        styleTabButton(tab, active);
        return tab;
    }

    private void styleTabButton(JButton button, boolean active) {
        button.setForeground(active ? TEXT_PRIMARY : TEXT_SECONDARY);
        button.setBackground(active ? TAB_ACTIVE : TAB_INACTIVE);
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, active ? 17 : 16));
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(active ? TAB_ACTIVE_BORDER : TAB_INACTIVE_BORDER, TAB_BUTTON_RADIUS, 1),
                new EmptyBorder(12, 18, 12, 18)));
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new RoundedButton(text, PRIMARY_BUTTON_RADIUS);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        button.setPreferredSize(new Dimension(0, 48));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBackground(ACTION);
        button.setForeground(Color.WHITE);
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(ACTION_BORDER, PRIMARY_BUTTON_RADIUS, 1),
                new EmptyBorder(12, 18, 12, 18)));
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
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 15));
        label.setForeground(TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleTextField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        field.setPreferredSize(new Dimension(0, 48));
        field.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        field.setBackground(FIELD_BACKGROUND);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACTION);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(FIELD_BORDER, 1, true),
                new EmptyBorder(11, 15, 11, 15)));
        field.setToolTipText(placeholder);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void stylePasswordField(JPasswordField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        field.setPreferredSize(new Dimension(0, 48));
        field.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        field.setBackground(FIELD_BACKGROUND);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACTION);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(FIELD_BORDER, 1, true),
                new EmptyBorder(11, 15, 11, 15)));
        field.setToolTipText(placeholder);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleRememberPasswordCheckBox() {
        rememberPasswordCheckBox.setOpaque(false);
        rememberPasswordCheckBox.setFocusPainted(false);
        rememberPasswordCheckBox.setForeground(TEXT_SECONDARY);
        rememberPasswordCheckBox.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        rememberPasswordCheckBox.setBorder(new EmptyBorder(0, 0, 0, 0));
        rememberPasswordCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    // handleLogin
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

        if (rememberPasswordCheckBox.isSelected()) {
            RememberedLoginStore.save(email, password);
        } else {
            RememberedLoginStore.clear();
        }

        showStatus("Login successful. Opening dashboard...", SUCCESS);
        dispose();
        new MainFrame(user.displayName, user.email).setVisible(true);
    }

    // handleCreateAccount
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
            JOptionPane.showMessageDialog(this, "Create account failed. Check SMTP settings and rebuild.", "Error", JOptionPane.ERROR_MESSAGE);
            showStatus("Create account failed. Check SMTP settings and rebuild.", ERROR);
            return;
        }

        loginEmailField.setText(email);
        loginPasswordField.setText("");
        showStatus("Account created for " + name + ". You can now sign in.", SUCCESS);
        switchTab(TAB_LOGIN);
    }

    // handleForgotPassword
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
            loginPasswordField.setText(newPassword);
            RememberedLoginStore.updatePasswordIfRemembered(email, newPassword);
            showStatus("Password reset successful. You can now sign in.", SUCCESS);
        } catch (Throwable exception) {
            JOptionPane.showMessageDialog(this, "Password reset failed. Check SMTP settings and rebuild.", "Error", JOptionPane.ERROR_MESSAGE);
            showStatus("Password reset failed. Check SMTP settings and rebuild.", ERROR);
        }
    }

    // promptForOtp
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
    // showOtpDispatchDialog
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
    // showStatus
    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    private static class EyeIcon implements Icon {
        private final int width;
        private final int height;
        private final boolean slashed;

        EyeIcon(int width, int height, boolean slashed) {
            this.width = width;
            this.height = height;
            this.slashed = slashed;
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(TEXT_SECONDARY);
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int left = x + 1;
            int top = y + 1;
            int iconWidth = width - 2;
            int iconHeight = height - 2;
            int centerX = left + (iconWidth / 2);
            int centerY = top + (iconHeight / 2);

            g2.drawArc(left, top + 1, iconWidth, iconHeight, 0, 180);
            g2.drawArc(left, top - 1, iconWidth, iconHeight, 180, 180);
            g2.fillOval(centerX - 2, centerY - 2, 4, 4);

            if (slashed) {
                g2.drawLine(left + 1, top + iconHeight, left + iconWidth - 1, top);
            }
            g2.dispose();
        }
    }

    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, PAGE_TOP, 0, getHeight(), PAGE_BOTTOM));
            g2.fillRect(0, 0, getWidth(), getHeight());

            float[] fractions = new float[] { 0.0f, 0.45f, 1.0f };
            Color[] colors = new Color[] {
                    new Color(255, 248, 223, 64),
                    new Color(255, 248, 223, 26),
                    new Color(255, 248, 223, 0)
            };
            float radius = Math.max(getWidth(), getHeight()) * 0.62f;
            RadialGradientPaint glow = new RadialGradientPaint(getWidth() / 2.0f, getHeight() / 2.4f, radius, fractions, colors);
            g2.setPaint(glow);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(new Color(255, 255, 255, 0));
            for (int x = -30; x < getWidth() + 70; x += 140) {
                g2.fillRoundRect(x, 0, 58, getHeight(), 24, 24);
            }

            g2.setPaint(new GradientPaint(0, 0, new Color(244, 231, 190, 20), getWidth() / 2.0f, 0, new Color(244, 231, 190, 0)));
            g2.fillRect(0, 0, getWidth() / 2, getHeight());
            g2.setPaint(new GradientPaint(getWidth(), 0, new Color(244, 231, 190, 20), getWidth() / 2.0f, 0, new Color(244, 231, 190, 0)));
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
            int radius = 28;

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

    private static class ResponsiveCenterPanel extends JPanel {
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
            int contentHeight = content.getPreferredSize().height + 24;
            int height = Math.max(LOGIN_CONTENT_MIN_HEIGHT, Math.max(contentHeight, parentHeight));
            return new Dimension(width, height);
        }
    }
}

