package baon.ui;

import baon.data.AppDatabase;
import baon.model.ExpenseEntry;
import baon.model.IncomeEntry;
import baon.model.SavingEntry;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.IsoFields;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {
    private static final String PAGE_DASHBOARD = "dashboard";
    private static final String PAGE_INCOME = "income";
    private static final String PAGE_EXPENSES = "expenses";
    private static final String PAGE_BUDGET = "budget";
    private static final String PAGE_SAVING_GOAL = "saving_goal";
    private static final String PAGE_FORECAST = "forecast";
    private static final int SAVINGS_HISTORY_MIN_PAGE_SIZE = 8;

    private static final String FONT_FAMILY = AppTheme.text("--font-family", "Segoe UI");
    private static final Color PAGE_BACKGROUND = AppTheme.color("--main-page-background", "#F5EED9");
    private static final Color PAGE_BACKGROUND_SOFT = AppTheme.color("--main-page-background-soft", "#FBF7EC");
    private static final Color SURFACE = AppTheme.color("--main-surface", "#FFFDF8");
    private static final Color SURFACE_TINT = AppTheme.color("--main-surface-tint", "#EFF8F7");
    private static final Color SURFACE_BLUE = AppTheme.color("--main-surface-blue", "#EEF7FB");
    private static final Color SURFACE_BORDER = AppTheme.color("--main-surface-border", "#E5D1A8");
    private static final Color TEXT_PRIMARY = AppTheme.color("--main-text-primary", "#14213D");
    private static final Color TEXT_SECONDARY = AppTheme.color("--main-text-secondary", "#7B5E34");
    private static final Color SIDEBAR_BACKGROUND = AppTheme.color("--main-sidebar-background", "#255D60");
    private static final Color SIDEBAR_BUTTON = AppTheme.color("--main-sidebar-button", "#2D676A");
    private static final Color SIDEBAR_BUTTON_ACTIVE = AppTheme.color("--main-sidebar-button-active", "#3E7779");
    private static final Color SIDEBAR_BORDER = AppTheme.color("--main-sidebar-border", "#4A8082");
    private static final Color SIDEBAR_TEXT = AppTheme.color("--main-sidebar-text", "#FFFFFF");
    private static final Color SIDEBAR_MUTED = AppTheme.color("--main-sidebar-muted", "#D5E7E7");
    private static final Color TEAL = AppTheme.color("--main-teal", "#4A8B8D");
    private static final Color TEAL_DARK = AppTheme.color("--main-teal-dark", "#2C6A6C");
    private static final Color GOLD = AppTheme.color("--main-gold", "#F3B24F");
    private static final Color ORANGE = AppTheme.color("--main-orange", "#E79A51");
    private static final Color RED = AppTheme.color("--main-red", "#D85A5A");
    private static final Color GREEN = AppTheme.color("--main-green", "#7CBF8E");
    private static final Color SHADOW = AppTheme.color("--main-shadow", "rgba(110, 76, 23, 28)");
    private static final Color ROOT_STRIPE = AppTheme.color("--main-root-stripe", "rgba(255, 255, 255, 90)");
    private static final Color ROOT_ORB = AppTheme.color("--main-root-orb", "rgba(217, 201, 164, 40)");
    private static final Color SIDEBAR_CHIP_BACKGROUND = AppTheme.color("--main-sidebar-chip-background", "#376F72");
    private static final Color MENU_BADGE_BACKGROUND = AppTheme.color("--main-menu-badge-background", "#3A7376");
    private static final Color MENU_BADGE_BORDER = AppTheme.color("--main-menu-badge-border", "#588B8E");
    private static final Color CARD_BLUE_BORDER = AppTheme.color("--main-card-blue-border", "#C7E5E8");
    private static final Color CARD_GOLD_BORDER = AppTheme.color("--main-card-gold-border", "#EDD1A3");
    private static final Color CARD_DIVIDER = AppTheme.color("--main-card-divider", "#CFE0EA");
    private static final Color CARD_TINT_BORDER = AppTheme.color("--main-card-tint-border", "#C6E6DE");
    private static final Color CARD_SKY_BORDER = AppTheme.color("--main-card-sky-border", "#D2E6E8");
    private static final Color CARD_CREAM_BORDER = AppTheme.color("--main-card-cream-border", "#E6D6B6");
    private static final Color CATEGORY_DEFAULT_BORDER = AppTheme.color("--main-category-default-border", "#CFE7D9");
    private static final Color PROGRESS_BACKGROUND = AppTheme.color("--main-progress-background", "#D5E8E2");
    private static final Color EMPTY_TABLE_BORDER = AppTheme.color("--main-empty-table-border", "#CDE6DE");
    private static final Color EMPTY_TABLE_HEADER_BACKGROUND = AppTheme.color("--main-empty-table-header-background", "rgb(230, 244, 240)");
    private static final Color TABLE_GRID = AppTheme.color("--main-table-grid", "#D7E6DD");
    private static final Color TABLE_SELECTION = AppTheme.color("--main-table-selection", "#DDEEE8");
    private static final Color DONUT_OUTER = AppTheme.color("--main-donut-outer", "rgb(239, 229, 210)");
    private static final Color DONUT_RING = AppTheme.color("--main-donut-ring", "rgb(222, 209, 180)");
    private static final Color SIDEBAR_ACTIVE_BORDER = AppTheme.color("--main-sidebar-active-border", "#7FA6A7");
    private static final Color FILLED_BADGE_BACKGROUND = AppTheme.color("--main-filled-badge-background", "rgba(255, 255, 255, 30)");
    private static final Color FILLED_BODY_TEXT = AppTheme.color("--main-filled-body-text", "rgb(245, 248, 248)");

    private final ArrayList<IncomeEntry> incomeEntries = new ArrayList<IncomeEntry>();
    private final ArrayList<ExpenseEntry> expenseEntries = new ArrayList<ExpenseEntry>();
    private final ArrayList<SavingEntry> savingEntries = new ArrayList<SavingEntry>();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    private final CardLayout pageLayout = new CardLayout();
    private final JPanel pagePanel = new ResponsivePagePanel(pageLayout);

    private JButton dashboardNavButton;
    private JButton incomeNavButton;
    private JButton expensesNavButton;
    private JButton budgetNavButton;
    private JButton savingGoalNavButton;
    private JButton forecastNavButton;

    private JLabel sidebarUsernameLabel;
    private String accountDisplayName;
    private final String accountEmail;

    private final JLabel dashboardIncomeValueLabel = new JLabel();
    private final JLabel dashboardIncomeBodyLabel = new JLabel();
    private final JLabel dashboardExpenseValueLabel = new JLabel();
    private final JLabel dashboardExpenseBodyLabel = new JLabel();
    private final JLabel dashboardRemainingValueLabel = new JLabel();
    private final JLabel dashboardRemainingBodyLabel = new JLabel();
    private final JLabel dashboardForecastValueLabel = new JLabel();
    private final JLabel dashboardForecastBodyLabel = new JLabel();
    private final JLabel weeklySpendingBadgeLabel = new JLabel();
    private final JPanel weeklySpendingContentPanel = new JPanel(new BorderLayout());
    private final JPanel categoryOverviewContentPanel = new JPanel(new BorderLayout());
    private final JLabel dashboardBudgetNoticeLabel = new JLabel();
    private final JLabel dashboardInsightNoticeLabel = new JLabel();

    private final JLabel incomeRecordBadgeLabel = new JLabel();
    private final JPanel incomeRecordsContentPanel = new JPanel(new BorderLayout());
    private final JLabel incomeMonthValueLabel = new JLabel();
    private final JLabel incomeWeekValueLabel = new JLabel();
    private final JLabel incomeTopSourceValueLabel = new JLabel();
    private final JLabel incomeTrackedCountValueLabel = new JLabel();

    private final JLabel expenseRecordBadgeLabel = new JLabel();
    private final JPanel expenseRecordsContentPanel = new JPanel(new BorderLayout());
    private final JLabel expenseTotalSpentValueLabel = new JLabel();
    private final JLabel expenseTopCategoryValueLabel = new JLabel();
    private final JLabel expenseTrackedCountValueLabel = new JLabel();
    private final JLabel expenseSummaryNoteLabel = new JLabel();

    private final JLabel budgetCategoryBadgeLabel = new JLabel();
    private final JPanel budgetProgressContentPanel = new JPanel();
    private final JLabel budgetTotalBudgetedValueLabel = new JLabel();
    private final JLabel budgetTotalSpentValueLabel = new JLabel();
    private final JLabel budgetRemainingValueLabel = new JLabel();
    private final JLabel budgetOverallUsedValueLabel = new JLabel();
    private final JLabel budgetClosestValueLabel = new JLabel();

    private final JLabel savingsStatusBadgeLabel = new JLabel();
    private final JLabel savingsPercentBadgeLabel = new JLabel();
    private final JLabel savingsGoalTitleLabel = new JLabel();
    private final JLabel savingsGoalBodyLabel = new JLabel();
    private final JProgressBar savingsGoalProgressBar = new JProgressBar(0, 100);
    private final JLabel savingsTargetValueLabel = new JLabel();
    private final JLabel savingsSavedValueLabel = new JLabel();
    private final JLabel savingsRemainingValueLabel = new JLabel();
    private final JLabel savingsEntriesValueLabel = new JLabel();
    private final JLabel savingsHistoryBadgeLabel = new JLabel();
    private final JPanel savingsHistoryContentPanel = new JPanel(new BorderLayout());
    private final JPanel savingsHistoryPaginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

    private final JLabel forecastRiskBadgeLabel = new JLabel();
    private final JLabel forecastEstimatedRemainingValueLabel = new JLabel();
    private final JLabel forecastHighRiskCategoryValueLabel = new JLabel();
    private final JLabel forecastProjectedDailyValueLabel = new JLabel();
    private final JLabel forecastOverspendingPercentLabel = new JLabel();
    private final JProgressBar forecastRiskProgressBar = new JProgressBar(0, 100);
    private final JPanel forecastBreakdownContentPanel = new JPanel(new BorderLayout());

    private final DefaultTableModel incomeTableModel = createTableModel(new String[] { "Source", "Date", "Amount" });
    private final DefaultTableModel expenseTableModel = createTableModel(new String[] { "Category", "Date", "Amount" });
    private final DefaultTableModel savingsTableModel = createTableModel(new String[] { "Date", "Amount" });
    private final JTable incomeTable = new JTable(incomeTableModel);
    private final JTable expenseTable = new JTable(expenseTableModel);
    private final JTable savingsTable = new JTable(savingsTableModel);
    private final JScrollPane incomeTableScrollPane = createTableScrollPane(incomeTable);
    private final JScrollPane expenseTableScrollPane = createTableScrollPane(expenseTable);
    private final JScrollPane savingsTableScrollPane = createTableScrollPane(savingsTable);

    private double budgetLimit = 0.0;
    private double savingGoalTarget = 0.0;
    private int savingsHistoryCurrentPage = 1;
    private int savingsHistoryLastPageSize = SAVINGS_HISTORY_MIN_PAGE_SIZE;
    private String currentPage = PAGE_DASHBOARD;

    public MainFrame() {
        this("User", "");
    }

    public MainFrame(String accountDisplayName, String accountEmail) {
        super("BaonBrain Financial Overview");
        this.accountEmail = accountEmail == null ? "" : accountEmail.trim().toLowerCase();
        String sanitizedDisplayName = accountDisplayName == null ? "" : accountDisplayName.trim();
        if (sanitizedDisplayName.isEmpty()) {
            if (this.accountEmail.contains("@")) {
                sanitizedDisplayName = this.accountEmail.substring(0, this.accountEmail.indexOf('@'));
            } else {
                sanitizedDisplayName = "User";
            }
        }
        this.accountDisplayName = sanitizedDisplayName;

        configureFrame();
        configureSavingsHistoryAutoPagination();
        loadStoredData();
        refreshAllSections();
        showPage(PAGE_DASHBOARD);
    }

    private void configureFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 720));
        setSize(1360, 820);
        setResizable(true);
        setLocationRelativeTo(null);

        BackgroundPanel root = new BackgroundPanel(PAGE_BACKGROUND, ROOT_STRIPE, ROOT_ORB);
        root.setLayout(new BorderLayout(24, 0));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        root.add(createSidebar(), BorderLayout.WEST);
        root.add(createPageScrollPane(), BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel(new BorderLayout(0, 22));
        panel.setPreferredSize(new Dimension(220, 0));
        panel.setBackground(SIDEBAR_BACKGROUND);
        panel.setBorder(new EmptyBorder(14, 12, 14, 12));

        JPanel brandBlock = new JPanel();
        brandBlock.setOpaque(false);
        brandBlock.setLayout(new BoxLayout(brandBlock, BoxLayout.Y_AXIS));

        JLabel chip = createBadgeLabel("STUDENT BUDGET HUB", SIDEBAR_CHIP_BACKGROUND, SIDEBAR_MUTED);
        chip.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel brand = new JLabel("BaonBrain");
        brand.setFont(new Font(FONT_FAMILY, Font.BOLD, 30));
        brand.setForeground(SIDEBAR_TEXT);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel body = new JLabel("<html>Track spending, plan smarter, and keep your weekly baon under control.</html>");
        body.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        body.setForeground(SIDEBAR_MUTED);
        body.setAlignmentX(Component.LEFT_ALIGNMENT);

        brandBlock.add(chip);
        brandBlock.add(Box.createVerticalStrut(16));
        brandBlock.add(brand);
        brandBlock.add(Box.createVerticalStrut(10));
        brandBlock.add(body);

        dashboardNavButton = createSidebarButton("Dashboard", PAGE_DASHBOARD);
        incomeNavButton = createSidebarButton("Income", PAGE_INCOME);
        expensesNavButton = createSidebarButton("Expenses", PAGE_EXPENSES);
        budgetNavButton = createSidebarButton("Budget", PAGE_BUDGET);
        savingGoalNavButton = createSidebarButton("Saving Goal", PAGE_SAVING_GOAL);
        forecastNavButton = createSidebarButton("Forecast", PAGE_FORECAST);

        JPanel navList = new JPanel();
        navList.setOpaque(false);
        navList.setLayout(new BoxLayout(navList, BoxLayout.Y_AXIS));
        navList.add(dashboardNavButton);
        navList.add(Box.createVerticalStrut(10));
        navList.add(incomeNavButton);
        navList.add(Box.createVerticalStrut(10));
        navList.add(expensesNavButton);
        navList.add(Box.createVerticalStrut(10));
        navList.add(budgetNavButton);
        navList.add(Box.createVerticalStrut(10));
        navList.add(savingGoalNavButton);
        navList.add(Box.createVerticalStrut(10));
        navList.add(forecastNavButton);
        navList.add(Box.createVerticalGlue());

        panel.add(brandBlock, BorderLayout.NORTH);
        panel.add(navList, BorderLayout.CENTER);
        panel.add(createSidebarFooter(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createSidebarFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        footer.setOpaque(false);
        JLabel menuBadge = new JLabel("=");
        menuBadge.setOpaque(true);
        menuBadge.setBackground(MENU_BADGE_BACKGROUND);
        menuBadge.setForeground(SIDEBAR_TEXT);
        menuBadge.setFont(new Font(FONT_FAMILY, Font.BOLD, 22));
        menuBadge.setHorizontalAlignment(SwingConstants.CENTER);
        menuBadge.setPreferredSize(new Dimension(38, 38));
        menuBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MENU_BADGE_BORDER),
                new EmptyBorder(2, 0, 4, 0)));
        menuBadge.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JPopupMenu accountMenu = createAccountMenu();
        menuBadge.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (event.isPopupTrigger()) {
                    showAccountMenu(accountMenu, menuBadge);
                }
            }
            @Override
            public void mouseReleased(MouseEvent event) {
                if (event.isPopupTrigger()) {
                    showAccountMenu(accountMenu, menuBadge);
                }
            }
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    showAccountMenu(accountMenu, menuBadge);
                }
            }
        });
        sidebarUsernameLabel = new JLabel("  " + accountDisplayName);
        sidebarUsernameLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        sidebarUsernameLabel.setForeground(SIDEBAR_TEXT);
        footer.add(menuBadge);
        footer.add(Box.createHorizontalStrut(10));
        footer.add(sidebarUsernameLabel);
        return footer;
    }
    private void showAccountMenu(JPopupMenu menu, Component anchor) {
        int margin = 8;
        int menuHeight = menu.getPreferredSize().height;
        Point anchorPoint = SwingUtilities.convertPoint(anchor, 0, 0, getContentPane());
        int availableAbove = anchorPoint.y - margin;
        int availableBelow = getContentPane().getHeight() - (anchorPoint.y + anchor.getHeight()) - margin;
        int x = 0;
        int y = anchor.getHeight() + 6;
        // Footer menu feels more natural above the profile row.
        if (availableAbove >= menuHeight || availableAbove > availableBelow) {
            y = -menuHeight - 6;
        }
        menu.show(anchor, x, y);
    }
    private JPopupMenu createAccountMenu() {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(SIDEBAR_BACKGROUND);
        menu.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(SIDEBAR_BORDER, 12, 1),
                new EmptyBorder(6, 6, 6, 6)));
        menu.setLayout(new GridLayout(0, 1, 0, 6));
        JMenuItem manageAccountItem = createAccountMenuItem("Manage account");
        manageAccountItem.addActionListener(event -> showManageAccountDialog());
        JMenuItem resetItem = createAccountMenuItem("Reset");
        resetItem.addActionListener(event -> resetFinancialData());
        JMenuItem logOutItem = createAccountMenuItem("Log out");
        logOutItem.addActionListener(event -> logOut());
        menu.add(manageAccountItem);
        menu.add(resetItem);
        menu.add(logOutItem);
        return menu;
    }
    private JMenuItem createAccountMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        item.setForeground(SIDEBAR_TEXT);
        item.setBackground(SIDEBAR_BUTTON);
        item.setOpaque(true);
        item.setPreferredSize(new Dimension(168, 36));
        item.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(SIDEBAR_BORDER, 9, 1),
                new EmptyBorder(6, 12, 6, 12)));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.setHorizontalAlignment(SwingConstants.LEFT);
        item.setFocusPainted(false);
        item.getModel().addChangeListener(event -> {
            boolean highlighted = item.isArmed() || item.isSelected();
            item.setBackground(highlighted ? SIDEBAR_BUTTON_ACTIVE : SIDEBAR_BUTTON);
        });
        return item;
    }
    private void showManageAccountDialog() {
        final Color dialogTop = new Color(12, 27, 53);
        final Color dialogBottom = new Color(33, 52, 82);
        final Color cardBackground = new Color(18, 35, 63);
        final Color cardBorder = new Color(56, 86, 130);
        final Color fieldBackground = new Color(7, 23, 44);
        final Color fieldBorder = new Color(76, 120, 181);
        final Color textPrimary = new Color(231, 238, 250);
        final Color textSecondary = new Color(194, 209, 236);
        JDialog dialog = new JDialog(this, "Manage Account", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(860, 760);
        dialog.setMinimumSize(new Dimension(600, 560));
        dialog.setLocationRelativeTo(this);
        JPanel root = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new java.awt.GradientPaint(0, 0, dialogTop, getWidth(), getHeight(), dialogBottom));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setLayout(new BorderLayout());
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(8, 0, 8, 0));
        JLabel chip = new JLabel("PROFILE SETTINGS", SwingConstants.CENTER);
        chip.setOpaque(true);
        chip.setBackground(new Color(35, 54, 87));
        chip.setForeground(new Color(175, 197, 236));
        chip.setFont(new Font(FONT_FAMILY, Font.BOLD, 12));
        chip.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(new Color(72, 103, 154), 20, 1),
                new EmptyBorder(5, 14, 5, 14)));
        chip.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel title = new JLabel("Manage Account");
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 30));
        title.setForeground(textPrimary);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel subtitle = new JLabel("<html>Update how your account appears in the dashboard and keep your sign-in details organized.</html>");
        subtitle.setFont(new Font(FONT_FAMILY, Font.PLAIN, 16));
        subtitle.setForeground(textSecondary);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField emailField = new JTextField(accountEmail);
        styleManageTextField(emailField, false, fieldBackground, fieldBorder, textSecondary);
        JPanel emailCard = createManageFieldCard("Email Address", emailField,
                "This is your sign-in email and cannot be changed here.",
                cardBackground, cardBorder, textPrimary, textSecondary);
        JTextField displayNameField = new JTextField(accountDisplayName);
        styleManageTextField(displayNameField, true, fieldBackground, fieldBorder, textPrimary);
        JPanel displayNameCard = createManageFieldCard("Display Name", displayNameField,
                "This name appears in your dashboard header and sidebar.",
                cardBackground, cardBorder, textPrimary, textSecondary);
        JPanel securityCard = new JPanel(new BorderLayout(16, 0));
        securityCard.setOpaque(true);
        securityCard.setBackground(cardBackground);
        securityCard.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(cardBorder, 18, 1),
                new EmptyBorder(14, 16, 14, 16)));
        securityCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel securityText = new JPanel();
        securityText.setOpaque(false);
        securityText.setLayout(new BoxLayout(securityText, BoxLayout.Y_AXIS));
        JLabel securityTitle = new JLabel("SECURITY");
        securityTitle.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        securityTitle.setForeground(new Color(188, 210, 246));
        securityTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel securityBody = new JLabel("<html>Change your password anytime to keep your account protected.</html>");
        securityBody.setFont(new Font(FONT_FAMILY, Font.PLAIN, 16));
        securityBody.setForeground(textPrimary);
        securityBody.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton changePasswordButton = createManageActionButton("Change Password", new Color(53, 84, 132),
                new Color(97, 139, 203), textPrimary);
        changePasswordButton.setPreferredSize(new Dimension(168, 46));
        changePasswordButton.addActionListener(event -> showChangePasswordDialog(dialog));
        securityText.add(securityTitle);
        securityText.add(Box.createVerticalStrut(10));
        securityText.add(securityBody);
        securityCard.add(securityText, BorderLayout.CENTER);
        securityCard.add(changePasswordButton, BorderLayout.EAST);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        actions.setOpaque(false);
        JButton cancelButton = createManageActionButton("Cancel", new Color(39, 90, 156),
                new Color(66, 124, 200), textPrimary);
        cancelButton.addActionListener(event -> dialog.dispose());
        JButton saveButton = createManageActionButton("Save Changes", new Color(123, 169, 233),
                new Color(145, 191, 255), new Color(21, 43, 76));
        saveButton.addActionListener(event -> {
            String displayName = displayNameField.getText().trim();
            if (displayName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Display name cannot be empty.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!AppDatabase.updateDisplayName(accountEmail, displayName)) {
                JOptionPane.showMessageDialog(dialog, "Could not update display name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            accountDisplayName = displayName;
            if (sidebarUsernameLabel != null) {
                sidebarUsernameLabel.setText("  " + accountDisplayName);
            }
            dialog.dispose();
        });
        actions.add(cancelButton);
        actions.add(saveButton);
        content.add(chip);
        content.add(Box.createVerticalStrut(20));
        content.add(title);
        content.add(Box.createVerticalStrut(14));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(24));
        content.add(emailCard);
        content.add(Box.createVerticalStrut(14));
        content.add(displayNameCard);
        content.add(Box.createVerticalStrut(14));
        content.add(securityCard);
        content.add(Box.createVerticalStrut(10));
        content.add(actions);
        JPanel centeredContent = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centeredContent.setOpaque(false);
        centeredContent.add(content);
        JScrollPane scrollPane = new JScrollPane(centeredContent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                int availableWidth = Math.max(320, scrollPane.getViewport().getWidth() - 14);
                int targetWidth = Math.min(760, availableWidth);
                int targetHeight = content.getPreferredSize().height;
                content.setPreferredSize(new Dimension(targetWidth, targetHeight));
                content.revalidate();
            }
        });
        root.add(scrollPane, BorderLayout.CENTER);
        dialog.setContentPane(root);
        SwingUtilities.invokeLater(() -> {
            int availableWidth = Math.max(320, scrollPane.getViewport().getWidth() - 14);
            int targetWidth = Math.min(760, availableWidth);
            content.setPreferredSize(new Dimension(targetWidth, content.getPreferredSize().height));
            content.revalidate();
        });
        dialog.setVisible(true);
    }
    private JPanel createManageFieldCard(String titleText, JTextField field, String noteText,
            Color cardBackground, Color cardBorder, Color titleColor, Color noteColor) {
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(cardBackground);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(cardBorder, 18, 1),
                new EmptyBorder(12, 14, 12, 14)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel title = new JLabel(titleText);
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        title.setForeground(titleColor);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel note = new JLabel(noteText);
        note.setFont(new Font(FONT_FAMILY, Font.PLAIN, 12));
        note.setForeground(noteColor);
        note.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(field);
        card.add(Box.createVerticalStrut(8));
        card.add(note);
        return card;
    }
    private void styleManageTextField(JTextField field, boolean editable, Color fillColor, Color borderColor, Color textColor) {
        field.setEditable(editable);
        field.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
        field.setBackground(fillColor);
        field.setForeground(textColor);
        field.setCaretColor(SIDEBAR_TEXT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setPreferredSize(new Dimension(0, 42));
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(borderColor, 22, 1),
                new EmptyBorder(9, 14, 9, 14)));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
    private JButton createManageActionButton(String text, Color fillColor, Color borderColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        button.setForeground(textColor);
        button.setBackground(fillColor);
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(borderColor, 28, 1),
                new EmptyBorder(9, 14, 9, 14)));
        button.setPreferredSize(new Dimension(152, 40));
        return button;
    }
    private void showChangePasswordDialog(JDialog owner) {
        final Color dialogTop = new Color(12, 27, 53);
        final Color dialogBottom = new Color(33, 52, 82);
        final Color cardBackground = new Color(18, 35, 63);
        final Color cardBorder = new Color(56, 86, 130);
        final Color fieldBackground = new Color(7, 23, 44);
        final Color fieldBorder = new Color(76, 120, 181);
        final Color textPrimary = new Color(231, 238, 250);
        final Color textSecondary = new Color(194, 209, 236);

        final boolean[] passwordUpdated = new boolean[] { false };

        JDialog dialog = new JDialog(owner, "Change Password", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);

        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        styleManageTextField(newPasswordField, true, fieldBackground, fieldBorder, textPrimary);
        styleManageTextField(confirmPasswordField, true, fieldBackground, fieldBorder, textPrimary);

        JPanel root = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new java.awt.GradientPaint(0, 0, dialogTop, getWidth(), getHeight(), dialogBottom));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setLayout(new BorderLayout());
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel content = new JPanel();
        content.setOpaque(true);
        content.setBackground(cardBackground);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(cardBorder, 18, 1),
                new EmptyBorder(14, 16, 14, 16)));

        JLabel chip = new JLabel("SECURITY", SwingConstants.LEFT);
        chip.setOpaque(true);
        chip.setBackground(new Color(35, 54, 87));
        chip.setForeground(new Color(175, 197, 236));
        chip.setFont(new Font(FONT_FAMILY, Font.BOLD, 12));
        chip.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(new Color(72, 103, 154), 20, 1),
                new EmptyBorder(5, 12, 5, 12)));
        chip.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Change Password");
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 22));
        title.setForeground(textPrimary);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("<html>Update your password to keep your account protected.</html>");
        subtitle.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
        subtitle.setForeground(textSecondary);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel newPasswordCard = createManageFieldCard(
                "New Password",
                newPasswordField,
                "Use a password only you can remember.",
                cardBackground,
                cardBorder,
                textPrimary,
                textSecondary);

        JPanel confirmPasswordCard = createManageFieldCard(
                "Confirm Password",
                confirmPasswordField,
                "Re-enter the same password to confirm.",
                cardBackground,
                cardBorder,
                textPrimary,
                textSecondary);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);

        JButton cancelButton = createManageActionButton("Cancel", new Color(39, 90, 156),
                new Color(66, 124, 200), textPrimary);
        cancelButton.setPreferredSize(new Dimension(124, 38));
        cancelButton.addActionListener(event -> dialog.dispose());

        JButton updateButton = createManageActionButton("Update Password", new Color(123, 169, 233),
                new Color(145, 191, 255), new Color(21, 43, 76));
        updateButton.setPreferredSize(new Dimension(168, 38));
        updateButton.addActionListener(event -> {
            String password = new String(newPasswordField.getPassword()).trim();
            String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Both password fields are required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(dialog, "Passwords do not match.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!AppDatabase.updatePassword(accountEmail, password)) {
                JOptionPane.showMessageDialog(dialog, "Could not update password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            passwordUpdated[0] = true;
            dialog.dispose();
        });

        actions.add(cancelButton);
        actions.add(updateButton);

        content.add(chip);
        content.add(Box.createVerticalStrut(10));
        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(10));
        content.add(newPasswordCard);
        content.add(Box.createVerticalStrut(10));
        content.add(confirmPasswordCard);
        content.add(Box.createVerticalStrut(10));
        content.add(actions);

        root.add(content, BorderLayout.CENTER);
        dialog.setContentPane(root);
        dialog.getRootPane().setDefaultButton(updateButton);
        dialog.getRootPane().registerKeyboardAction(
                event -> dialog.dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        dialog.pack();
        Dimension preferredSize = dialog.getPreferredSize();
        dialog.setSize(Math.max(440, preferredSize.width), preferredSize.height);
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);

        if (passwordUpdated[0]) {
            JOptionPane.showMessageDialog(owner, "Password updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void resetFinancialData() {
        boolean shouldReset = showActionConfirmationDialog(
                "Reset Data",
                "Reset all financial records?",
                "This will clear income, expenses, savings, budget, and goal entries. This action cannot be undone.",
                "Reset Data",
                new Color(191, 87, 74),
                new Color(157, 64, 53),
                Color.WHITE,
                new Color(252, 236, 218),
                new Color(228, 186, 130),
                "OptionPane.warningIcon");
        if (!shouldReset) {
            return;
        }
        incomeEntries.clear();
        expenseEntries.clear();
        savingEntries.clear();
        budgetLimit = 0.0;
        savingGoalTarget = 0.0;
        handleFinancialDataChanged();
        showPage(PAGE_DASHBOARD);
    }
    private void logOut() {
        boolean shouldLogOut = showActionConfirmationDialog(
                "Log out",
                "Leave your current session?",
                "You will return to the login screen and can sign back in at any time.",
                "Log out",
                TEAL,
                TEAL_DARK,
                Color.WHITE,
                SURFACE_BLUE,
                CARD_BLUE_BORDER,
                "OptionPane.questionIcon");
        if (!shouldLogOut) {
            return;
        }
        dispose();
        new LoginFrame().setVisible(true);
    }

    private boolean showActionConfirmationDialog(String title, String headingText, String bodyText,
            String confirmLabel, Color confirmFill, Color confirmBorder, Color confirmText,
            Color iconFill, Color iconBorder, String iconKey) {
        final boolean[] confirmed = new boolean[] { false };

        JDialog dialog = new JDialog(this, title, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BACKGROUND_SOFT);
        root.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(SURFACE_BORDER, 18, 1),
                new EmptyBorder(12, 12, 10, 12)));

        JPanel contentRow = new JPanel(new BorderLayout(10, 0));
        contentRow.setOpaque(false);

        JPanel iconShell = new JPanel(new BorderLayout());
        iconShell.setOpaque(true);
        iconShell.setBackground(iconFill);
        iconShell.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(iconBorder, 26, 1),
                new EmptyBorder(7, 7, 7, 7)));
        iconShell.setPreferredSize(new Dimension(44, 44));

        JLabel iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        Icon icon = UIManager.getIcon(iconKey);
        if (icon != null) {
            iconLabel.setIcon(icon);
        } else {
            iconLabel.setText("!");
            iconLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 24));
            iconLabel.setForeground(TEXT_PRIMARY);
        }
        iconShell.add(iconLabel, BorderLayout.CENTER);

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel(headingText);
        heading.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        heading.setForeground(TEXT_PRIMARY);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel body = new JLabel("<html><body style='width: 260px'>" + escapeHtml(bodyText) + "</body></html>");
        body.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        body.setForeground(TEXT_SECONDARY);
        body.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(heading);
        textPanel.add(Box.createVerticalStrut(6));
        textPanel.add(body);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);

        JButton cancelButton = createManageActionButton("Cancel", PAGE_BACKGROUND_SOFT, SURFACE_BORDER, TEXT_PRIMARY);
        cancelButton.setPreferredSize(new Dimension(108, 38));
        cancelButton.addActionListener(event -> dialog.dispose());

        JButton confirmButton = createManageActionButton(confirmLabel, confirmFill, confirmBorder, confirmText);
        confirmButton.setPreferredSize(new Dimension(124, 38));
        confirmButton.addActionListener(event -> {
            confirmed[0] = true;
            dialog.dispose();
        });

        actions.add(cancelButton);
        actions.add(confirmButton);

        contentRow.add(iconShell, BorderLayout.WEST);
        contentRow.add(textPanel, BorderLayout.CENTER);

        card.add(contentRow, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);
        root.add(card, BorderLayout.CENTER);

        dialog.setContentPane(root);
        dialog.getRootPane().setDefaultButton(cancelButton);
        dialog.getRootPane().registerKeyboardAction(
                event -> dialog.dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        dialog.pack();
        Dimension preferredSize = dialog.getPreferredSize();
        dialog.setSize(Math.max(440, preferredSize.width), preferredSize.height);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        return confirmed[0];
    }

    private static String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
    private JButton createSidebarButton(String text, String pageKey) {
        JButton button = new JButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        button.setPreferredSize(new Dimension(0, 54));
        button.setMargin(new Insets(10, 18, 10, 18));
        button.setFocusable(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(event -> showPage(pageKey));
        applySidebarButtonStyle(button, false);
        return button;
    }

    private JScrollPane createPageScrollPane() {
        pagePanel.setOpaque(false);
        pagePanel.add(createDashboardPage(), PAGE_DASHBOARD);
        pagePanel.add(createIncomePage(), PAGE_INCOME);
        pagePanel.add(createExpensesPage(), PAGE_EXPENSES);
        pagePanel.add(createBudgetPage(), PAGE_BUDGET);
        pagePanel.add(createSavingGoalPage(), PAGE_SAVING_GOAL);
        pagePanel.add(createForecastPage(), PAGE_FORECAST);

        JScrollPane scrollPane = new JScrollPane(pagePanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                pagePanel.revalidate();
                pagePanel.repaint();
            }
        });
        return scrollPane;
    }

    private JPanel createDashboardPage() {
        JPanel page = new JPanel(new BorderLayout(18, 18));
        page.setOpaque(false);
        page.add(createPageHeader(null, null), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0, 18));
        body.setOpaque(false);
        body.add(createDashboardMetricRow(), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 18));
        center.setOpaque(false);
        center.add(createPrimarySecondaryRow(createWeeklySpendingCard(), createCategoryOverviewCard(), 320), BorderLayout.CENTER);
        center.add(createDashboardNoticeStack(), BorderLayout.SOUTH);

        body.add(center, BorderLayout.CENTER);
        page.add(body, BorderLayout.CENTER);
        return page;
    }

    private JPanel createDashboardMetricRow() {
        ResponsiveGridPanel panel = new ResponsiveGridPanel(220, 16);
        panel.add(createDashboardMetricCard("INCOME TRACKED", "Total Allowance", dashboardIncomeValueLabel,
                dashboardIncomeBodyLabel, SURFACE, SURFACE_BORDER, false));
        panel.add(createDashboardMetricCard("OUTGOING CASH", "Total Spent", dashboardExpenseValueLabel,
                dashboardExpenseBodyLabel, SURFACE, SURFACE_BORDER, false));
        panel.add(createDashboardMetricCard("AVAILABLE NOW", "Remaining", dashboardRemainingValueLabel,
                dashboardRemainingBodyLabel, SURFACE, SURFACE_BORDER, false));
        panel.add(createDashboardMetricCard("SMART ESTIMATE", "AI Forecast", dashboardForecastValueLabel,
                dashboardForecastBodyLabel, TEAL, TEAL_DARK, true));
        return panel;
    }

    private JPanel createIncomePage() {
        JButton addIncomeButton = createActionButton("Add Income");
        addIncomeButton.addActionListener(event -> showIncomeDialog());

        JPanel page = new JPanel(new BorderLayout(18, 18));
        page.setOpaque(false);
        page.add(createPageHeader("Income",
                "Track every allowance, side hustle, or money added to your weekly budget.", addIncomeButton),
                BorderLayout.NORTH);
        page.add(createPrimarySecondaryRow(createIncomeRecordsCard(), createIncomeSummaryCard(), 360), BorderLayout.CENTER);
        return page;
    }

    private JPanel createExpensesPage() {
        JButton addExpenseButton = createActionButton("Add Expense");
        addExpenseButton.addActionListener(event -> showExpenseDialog());

        JPanel page = new JPanel(new BorderLayout(18, 18));
        page.setOpaque(false);
        page.add(createPageHeader("Expenses",
                "Keep an eye on where your baon goes and spot which categories are eating the most of your budget.",
                addExpenseButton), BorderLayout.NORTH);
        page.add(createPrimarySecondaryRow(createExpenseRecordsCard(), createExpenseSummaryCard(), 360), BorderLayout.CENTER);
        return page;
    }

    private JPanel createBudgetPage() {
        JButton addBudgetButton = createActionButton("Add Budget");
        addBudgetButton.addActionListener(event -> showBudgetDialog());

        JPanel page = new JPanel(new BorderLayout(18, 18));
        page.setOpaque(false);
        page.add(createPageHeader("Budget",
                "Set category limits and watch how close each one is to being fully used.", addBudgetButton),
                BorderLayout.NORTH);
        page.add(createPrimarySecondaryRow(createBudgetProgressCard(), createBudgetSnapshotCard(), 360), BorderLayout.CENTER);
        return page;
    }

    private JPanel createSavingGoalPage() {
        JButton addDailySavingsButton = createActionButton("Add Daily Savings");
        addDailySavingsButton.addActionListener(event -> showDailySavingsDialog());

        JButton setSavingGoalButton = createActionButton("Set Saving Goal");
        setSavingGoalButton.addActionListener(event -> showSavingGoalDialog());

        JPanel page = new JPanel(new BorderLayout(18, 18));
        page.setOpaque(false);
        page.add(createPageHeader("Saving Goal", "Track your target and how much you have already saved.",
                addDailySavingsButton, setSavingGoalButton), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 18));
        center.setOpaque(false);
        center.add(createPrimarySecondaryRow(createSavingGoalProgressCard(), createSavingSnapshotCard(), 360),
                BorderLayout.NORTH);
        center.add(createSavingHistoryCard(), BorderLayout.CENTER);

        page.add(center, BorderLayout.CENTER);
        return page;
    }

    private JPanel createForecastPage() {
        JButton runForecastButton = createActionButton("Run Forecast");
        runForecastButton.addActionListener(event -> refreshAllSections());

        JPanel page = new JPanel(new BorderLayout(18, 18));
        page.setOpaque(false);
        page.add(createPageHeader("Forecast",
                "Use your current income and spending pace to estimate how the rest of the month may look.",
                runForecastButton), BorderLayout.NORTH);
        page.add(createPrimarySecondaryRow(createForecastPredictionCard(), createForecastBreakdownCard(), 360),
                BorderLayout.CENTER);
        return page;
    }

    private JPanel createPageHeader(String sectionTitle, String description, JButton... actionButtons) {
        JPanel panel = new JPanel(new BorderLayout(18, 12));
        panel.setOpaque(false);

        JPanel textBlock = new JPanel();
        textBlock.setOpaque(false);
        textBlock.setLayout(new BoxLayout(textBlock, BoxLayout.Y_AXIS));

        JLabel mainTitle = new JLabel("Financial Overview");
        mainTitle.setFont(new Font(FONT_FAMILY, Font.BOLD, 46));
        mainTitle.setForeground(TEXT_PRIMARY);
        mainTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        textBlock.add(mainTitle);

        if (sectionTitle != null && description != null) {
            JLabel section = new JLabel(sectionTitle);
            section.setFont(new Font(FONT_FAMILY, Font.BOLD, 34));
            section.setForeground(TEXT_PRIMARY);
            section.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel body = new JLabel("<html>" + description + "</html>");
            body.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
            body.setForeground(TEXT_SECONDARY);
            body.setAlignmentX(Component.LEFT_ALIGNMENT);

            textBlock.add(Box.createVerticalStrut(24));
            textBlock.add(section);
            textBlock.add(Box.createVerticalStrut(8));
            textBlock.add(body);
        }

        panel.add(textBlock, BorderLayout.CENTER);

        if (actionButtons != null && actionButtons.length > 0) {
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            actions.setOpaque(false);
            actions.setBorder(new EmptyBorder(18, 0, 0, 0));
            for (JButton button : actionButtons) {
                actions.add(button);
            }
            panel.add(actions, BorderLayout.SOUTH);
        }

        return panel;
    }

    private JPanel createPrimarySecondaryRow(JPanel primaryPanel, JPanel secondaryPanel, int secondaryWidth) {
        return new ResponsiveSplitPanel(primaryPanel, secondaryPanel, secondaryWidth);
    }

    private JPanel createDashboardMetricCard(String tagText, String titleText, JLabel valueLabel, JLabel bodyLabel,
            Color fillColor, Color borderColor, boolean filled) {
        SurfacePanel panel = createSurface(new BorderLayout(0, 14), fillColor, borderColor, 24);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel tag = createBadgeLabel(tagText, filled ? FILLED_BADGE_BACKGROUND : PAGE_BACKGROUND_SOFT,
                filled ? Color.WHITE : TEXT_SECONDARY);
        tag.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel(titleText);
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        title.setForeground(filled ? Color.WHITE : TEXT_PRIMARY);

        valueLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 46));
        valueLabel.setForeground(filled ? Color.WHITE : TEXT_PRIMARY);

        bodyLabel.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        bodyLabel.setForeground(filled ? FILLED_BODY_TEXT : TEXT_SECONDARY);

        JPanel stack = new JPanel();
        stack.setOpaque(false);
        stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));
        stack.add(tag);
        stack.add(Box.createVerticalStrut(14));
        stack.add(title);
        stack.add(Box.createVerticalStrut(10));
        stack.add(valueLabel);
        stack.add(Box.createVerticalStrut(12));
        stack.add(bodyLabel);
        stack.add(Box.createVerticalGlue());

        panel.add(stack, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createWeeklySpendingCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = createCardHeader("Weekly Spending", "See how your spending moves through the week.",
                weeklySpendingBadgeLabel);
        weeklySpendingContentPanel.setOpaque(false);

        panel.add(header, BorderLayout.NORTH);
        panel.add(weeklySpendingContentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCategoryOverviewCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = createCardHeader("By Category", "Your biggest spending groups at a glance.", null);
        categoryOverviewContentPanel.setOpaque(false);

        panel.add(header, BorderLayout.NORTH);
        panel.add(categoryOverviewContentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDashboardNoticeStack() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 14));
        panel.setOpaque(false);
        panel.add(createNoticeCard(dashboardBudgetNoticeLabel));
        panel.add(createNoticeCard(dashboardInsightNoticeLabel));
        return panel;
    }

    private JPanel createNoticeCard(JLabel label) {
        SurfacePanel panel = createSurface(new BorderLayout(), SURFACE, SURFACE_BORDER, 18);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        label.setForeground(TEXT_PRIMARY);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createIncomeRecordsCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.add(createCardHeader("Income Records",
                "Your income history appears here once you start adding entries.", incomeRecordBadgeLabel),
                BorderLayout.NORTH);
        incomeRecordsContentPanel.setOpaque(false);
        panel.add(incomeRecordsContentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createIncomeSummaryCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16), SURFACE_BLUE, CARD_BLUE_BORDER, 24);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel stack = new JPanel();
        stack.setOpaque(false);
        stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));

        stack.add(createCardTitle("Income Summary", "A quick look at how much money has come in this month."));
        stack.add(Box.createVerticalStrut(18));
        stack.add(createValueRow("This month", incomeMonthValueLabel, false, null));
        stack.add(Box.createVerticalStrut(12));
        stack.add(createValueRow("Average per week", incomeWeekValueLabel, false, null));
        stack.add(Box.createVerticalStrut(12));
        stack.add(createValueRow("Top source", incomeTopSourceValueLabel, false, null));
        stack.add(Box.createVerticalStrut(14));
        stack.add(createValueRow("Records tracked", incomeTrackedCountValueLabel, true, TEAL));
        stack.add(Box.createVerticalStrut(16));

        JLabel footnote = new JLabel("Add your first income entry to start building a clearer picture.");
        footnote.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        footnote.setForeground(TEXT_SECONDARY);
        stack.add(footnote);
        stack.add(Box.createVerticalGlue());

        panel.add(stack, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createExpenseRecordsCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.add(createCardHeader("Recent Expenses",
                "Your most recent expense entries will appear here.", expenseRecordBadgeLabel), BorderLayout.NORTH);
        expenseRecordsContentPanel.setOpaque(false);
        panel.add(expenseRecordsContentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createExpenseSummaryCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16), SURFACE, CARD_GOLD_BORDER, 24);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel stack = new JPanel();
        stack.setOpaque(false);
        stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));

        stack.add(createCardTitle("Category Totals",
                "See which spending groups are taking the biggest share."));
        stack.add(Box.createVerticalStrut(18));
        stack.add(createValueRow("Total spent", expenseTotalSpentValueLabel, true, GOLD));
        stack.add(Box.createVerticalStrut(12));
        stack.add(createValueRow("Top category", expenseTopCategoryValueLabel, false, null));
        stack.add(Box.createVerticalStrut(12));
        stack.add(createValueRow("Expenses tracked", expenseTrackedCountValueLabel, false, null));
        stack.add(Box.createVerticalStrut(22));

        expenseSummaryNoteLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 17));
        expenseSummaryNoteLabel.setForeground(TEXT_PRIMARY);
        stack.add(expenseSummaryNoteLabel);
        stack.add(Box.createVerticalStrut(10));

        JLabel body = new JLabel("Once you add expenses, each category total will appear here.");
        body.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        body.setForeground(TEXT_SECONDARY);
        stack.add(body);
        stack.add(Box.createVerticalStrut(18));

        JLabel extra = new JLabel("Add an expense to see your category totals build up here.");
        extra.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        extra.setForeground(TEXT_SECONDARY);
        stack.add(extra);
        stack.add(Box.createVerticalGlue());

        panel.add(stack, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBudgetProgressCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.add(createCardHeader("Monthly Budget Progress",
                "Each category will show how much you have spent against its limit.", budgetCategoryBadgeLabel),
                BorderLayout.NORTH);

        budgetProgressContentPanel.setOpaque(false);
        budgetProgressContentPanel.setLayout(new BoxLayout(budgetProgressContentPanel, BoxLayout.Y_AXIS));
        panel.add(budgetProgressContentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBudgetSnapshotCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16), SURFACE_BLUE, CARD_DIVIDER, 24);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel stack = new JPanel();
        stack.setOpaque(false);
        stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));

        stack.add(createCardTitle("Budget Snapshot",
                "A quick read on how your category budgets compare with your overall monthly budget."));
        stack.add(Box.createVerticalStrut(18));
        stack.add(createValueRow("Total budgeted", budgetTotalBudgetedValueLabel, true, TEAL));
        stack.add(Box.createVerticalStrut(12));
        stack.add(createValueRow("Total spent", budgetTotalSpentValueLabel, false, null));
        stack.add(Box.createVerticalStrut(12));
        stack.add(createValueRow("Remaining budget", budgetRemainingValueLabel, false, null));
        stack.add(Box.createVerticalStrut(12));
        stack.add(createValueRow("Overall used", budgetOverallUsedValueLabel, false, null));
        stack.add(Box.createVerticalStrut(12));
        stack.add(createValueRow("Closest to limit", budgetClosestValueLabel, false, null));
        stack.add(Box.createVerticalStrut(18));

        JLabel note = new JLabel("Add your first category budget to start tracking spending progress.");
        note.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        note.setForeground(TEXT_SECONDARY);
        stack.add(note);
        stack.add(Box.createVerticalStrut(14));

        JPanel divider = new JPanel();
        divider.setOpaque(true);
        divider.setBackground(CARD_DIVIDER);
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setPreferredSize(new Dimension(0, 1));
        stack.add(divider);
        stack.add(Box.createVerticalStrut(14));

        JLabel extra = new JLabel("Budget progress and snapshot will compare here once you add categories.");
        extra.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        extra.setForeground(TEXT_SECONDARY);
        stack.add(extra);
        stack.add(Box.createVerticalGlue());

        panel.add(stack, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSavingGoalProgressCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16), SURFACE_TINT, CARD_TINT_BORDER, 24);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new BorderLayout(12, 0));
        top.setOpaque(false);
        top.add(createCardTitle("Goal Progress", "Set a target amount to start tracking progress."), BorderLayout.WEST);
        top.add(savingsStatusBadgeLabel, BorderLayout.EAST);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        savingsGoalTitleLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        savingsGoalTitleLabel.setForeground(TEXT_PRIMARY);
        savingsGoalBodyLabel.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        savingsGoalBodyLabel.setForeground(TEXT_SECONDARY);

        styleProgressBar(savingsGoalProgressBar);

        JPanel percentWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        percentWrap.setOpaque(false);
        percentWrap.add(savingsPercentBadgeLabel);

        content.add(savingsGoalTitleLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(savingsGoalBodyLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(percentWrap);
        content.add(Box.createVerticalStrut(10));
        content.add(savingsGoalProgressBar);
        content.add(Box.createVerticalStrut(10));

        JLabel footnote = new JLabel("Set a target amount to start tracking progress.");
        footnote.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        footnote.setForeground(TEXT_PRIMARY);
        content.add(footnote);
        content.add(Box.createVerticalGlue());

        panel.add(top, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSavingSnapshotCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16), SURFACE, CARD_SKY_BORDER, 24);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel stack = new JPanel();
        stack.setOpaque(false);
        stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));

        stack.add(createCardTitle("Saving Snapshot", "A quick check on how close you are to your target."));
        stack.add(Box.createVerticalStrut(18));
        stack.add(createValueRow("Target", savingsTargetValueLabel, true, TEAL));
        stack.add(Box.createVerticalStrut(12));
        stack.add(createValueRow("Saved", savingsSavedValueLabel, false, null));
        stack.add(Box.createVerticalStrut(12));
        stack.add(createValueRow("Remaining", savingsRemainingValueLabel, false, null));
        stack.add(Box.createVerticalStrut(12));
        stack.add(createValueRow("Entries", savingsEntriesValueLabel, false, null));
        stack.add(Box.createVerticalStrut(18));

        JLabel note = new JLabel("Set your goal to unlock a clearer savings snapshot.");
        note.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        note.setForeground(TEXT_SECONDARY);
        stack.add(note);
        stack.add(Box.createVerticalGlue());

        panel.add(stack, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSavingHistoryCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.add(createCardHeader("Saving History", "Every savings entry you add will be listed here.",
                savingsHistoryBadgeLabel), BorderLayout.NORTH);
        savingsHistoryContentPanel.setOpaque(false);
        savingsHistoryPaginationPanel.setOpaque(false);
        panel.add(savingsHistoryContentPanel, BorderLayout.CENTER);
        panel.add(savingsHistoryPaginationPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createForecastPredictionCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16), SURFACE, CARD_CREAM_BORDER, 24);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);
        header.add(createCardTitle("End-of-Month Prediction",
                "Run the forecast to refresh your latest month-end estimate."), BorderLayout.WEST);
        header.add(forecastRiskBadgeLabel, BorderLayout.EAST);

        ResponsiveGridPanel stats = new ResponsiveGridPanel(180, 10);
        stats.add(createMiniStatTile("Estimated Remaining", forecastEstimatedRemainingValueLabel));
        stats.add(createMiniStatTile("High-Risk Category", forecastHighRiskCategoryValueLabel));
        stats.add(createMiniStatTile("Projected Daily Spend", forecastProjectedDailyValueLabel));

        styleProgressBar(forecastRiskProgressBar);

        SurfacePanel riskPanel = createSurface(new BorderLayout(0, 12), SURFACE_TINT, CARD_TINT_BORDER, 18);
        riskPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel riskHeader = new JPanel(new BorderLayout());
        riskHeader.setOpaque(false);
        JLabel riskTitle = new JLabel("Chance of Overspending");
        riskTitle.setFont(new Font(FONT_FAMILY, Font.BOLD, 15));
        riskTitle.setForeground(TEXT_PRIMARY);
        forecastOverspendingPercentLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        forecastOverspendingPercentLabel.setForeground(TEXT_PRIMARY);
        riskHeader.add(riskTitle, BorderLayout.WEST);
        riskHeader.add(forecastOverspendingPercentLabel, BorderLayout.EAST);

        riskPanel.add(riskHeader, BorderLayout.NORTH);
        riskPanel.add(forecastRiskProgressBar, BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);
        center.add(stats, BorderLayout.NORTH);
        center.add(riskPanel, BorderLayout.SOUTH);

        panel.add(header, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createForecastBreakdownCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.add(createCardHeader("Forecast Breakdown",
                "See which categories are likely to shape your month-end spending.", null), BorderLayout.NORTH);
        forecastBreakdownContentPanel.setOpaque(false);
        panel.add(forecastBreakdownContentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCardHeader(String titleText, String bodyText, JLabel badgeLabel) {
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);

        header.add(createCardTitle(titleText, bodyText), BorderLayout.WEST);
        if (badgeLabel != null) {
            header.add(badgeLabel, BorderLayout.EAST);
        }
        return header;
    }

    private JPanel createCardTitle(String titleText, String bodyText) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 22));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel body = new JLabel("<html>" + bodyText + "</html>");
        body.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        body.setForeground(TEXT_SECONDARY);
        body.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        panel.add(body);
        return panel;
    }

    private JPanel createValueRow(String titleText, JLabel valueLabel, boolean highlighted, Color highlightColor) {
        Color fillColor = highlighted ? highlightColor : SURFACE;
        Color borderColor = highlighted ? highlightColor.darker() : CATEGORY_DEFAULT_BORDER;
        Color textColor = highlighted ? Color.WHITE : TEAL_DARK;

        SurfacePanel row = createSurface(new BorderLayout(), fillColor, borderColor, 16);
        row.setBorder(new EmptyBorder(12, 14, 12, 14));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        title.setForeground(textColor);

        valueLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 17));
        valueLabel.setForeground(highlighted ? Color.WHITE : TEXT_PRIMARY);
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(title, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);
        return row;
    }

    private JPanel createMiniStatTile(String titleText, JLabel valueLabel) {
        SurfacePanel panel = createSurface(new BorderLayout(0, 10), SURFACE, CARD_CREAM_BORDER, 16);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font(FONT_FAMILY, Font.PLAIN, 12));
        title.setForeground(TEXT_SECONDARY);

        valueLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 20));
        valueLabel.setForeground(TEXT_PRIMARY);

        panel.add(title, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(TEAL);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TEAL_DARK),
                new EmptyBorder(10, 18, 10, 18)));
        return button;
    }

    private JLabel createBadgeLabel(String text, Color fillColor, Color textColor) {
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setBackground(fillColor);
        label.setForeground(textColor);
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 12));
        label.setBorder(new EmptyBorder(7, 12, 7, 12));
        return label;
    }

    private void styleBadgeLabel(JLabel label, String text, Color fillColor, Color textColor) {
        label.setText(text);
        label.setOpaque(true);
        label.setBackground(fillColor);
        label.setForeground(textColor);
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 12));
        label.setBorder(new EmptyBorder(7, 12, 7, 12));
    }

    private void styleProgressBar(JProgressBar progressBar) {
        progressBar.setOpaque(false);
        progressBar.setStringPainted(false);
        progressBar.setForeground(TEAL);
        progressBar.setBackground(PROGRESS_BACKGROUND);
        progressBar.setBorder(BorderFactory.createEmptyBorder());
        progressBar.setPreferredSize(new Dimension(0, 12));
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 12));
    }

    private void showIncomeDialog() {
        JTextField amountField = new JTextField();
        JTextField sourceField = new JTextField();
        JTextField dateField = new JTextField(LocalDate.now().toString());

        JPanel form = createDialogForm(new String[] { "Amount", "Source", "Date" },
                new Component[] { amountField, sourceField, dateField });
        int result = JOptionPane.showConfirmDialog(this, form, "Add Income", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Double amount = parseAmount(amountField.getText());
            String source = sourceField.getText().trim();
            String date = dateField.getText().trim();

            if (amount == null || amount.doubleValue() <= 0) {
                showValidationMessage("Enter a valid income amount greater than 0.");
                return;
            }
            if (source.isEmpty()) {
                showValidationMessage("Enter an income source before saving.");
                return;
            }
            if (!isValidDate(date)) {
                showValidationMessage("Use date format YYYY-MM-DD.");
                return;
            }

            addIncomeEntry(new IncomeEntry(amount.doubleValue(), source, date));
            showPage(PAGE_INCOME);
        }
    }

    private void showExpenseDialog() {
        JTextField amountField = new JTextField();
        JComboBox<String> categoryBox = new JComboBox<String>(new String[] { "Food", "Transport", "Leisure", "School" });
        JTextField dateField = new JTextField(LocalDate.now().toString());

        JPanel form = createDialogForm(new String[] { "Amount", "Category", "Date" },
                new Component[] { amountField, categoryBox, dateField });
        int result = JOptionPane.showConfirmDialog(this, form, "Add Expense", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Double amount = parseAmount(amountField.getText());
            String category = String.valueOf(categoryBox.getSelectedItem());
            String date = dateField.getText().trim();

            if (amount == null || amount.doubleValue() <= 0) {
                showValidationMessage("Enter a valid expense amount greater than 0.");
                return;
            }
            if (!isValidDate(date)) {
                showValidationMessage("Use date format YYYY-MM-DD.");
                return;
            }

            addExpenseEntry(new ExpenseEntry(amount.doubleValue(), category, date));
            showPage(PAGE_EXPENSES);
        }
    }

    private void showBudgetDialog() {
        JTextField amountField = new JTextField(budgetLimit > 0 ? String.valueOf(budgetLimit) : "");
        JPanel form = createDialogForm(new String[] { "Monthly Budget" }, new Component[] { amountField });
        int result = JOptionPane.showConfirmDialog(this, form, "Set Budget", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Double amount = parseAmount(amountField.getText());
            if (amount == null || amount.doubleValue() <= 0) {
                showValidationMessage("Enter a valid budget amount greater than 0.");
                return;
            }

            budgetLimit = amount.doubleValue();
            handleFinancialDataChanged();
            showPage(PAGE_BUDGET);
        }
    }

    private void showSavingGoalDialog() {
        JTextField amountField = new JTextField(savingGoalTarget > 0 ? String.valueOf(savingGoalTarget) : "");
        JPanel form = createDialogForm(new String[] { "Target Amount" }, new Component[] { amountField });
        int result = JOptionPane.showConfirmDialog(this, form, "Set Saving Goal", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Double amount = parseAmount(amountField.getText());
            if (amount == null || amount.doubleValue() <= 0) {
                showValidationMessage("Enter a valid goal amount greater than 0.");
                return;
            }

            savingGoalTarget = amount.doubleValue();
            handleFinancialDataChanged();
            showPage(PAGE_SAVING_GOAL);
        }
    }

    private void showDailySavingsDialog() {
        JTextField amountField = new JTextField();
        JTextField dateField = new JTextField(LocalDate.now().toString());
        JPanel form = createDialogForm(new String[] { "Amount", "Date" }, new Component[] { amountField, dateField });
        int result = JOptionPane.showConfirmDialog(this, form, "Add Daily Savings", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Double amount = parseAmount(amountField.getText());
            String date = dateField.getText().trim();

            if (amount == null || amount.doubleValue() <= 0) {
                showValidationMessage("Enter a valid savings amount greater than 0.");
                return;
            }
            if (!isValidDate(date)) {
                showValidationMessage("Use date format YYYY-MM-DD.");
                return;
            }

            savingEntries.add(new SavingEntry(amount.doubleValue(), date));
            handleFinancialDataChanged();
            showPage(PAGE_SAVING_GOAL);
        }
    }

    private JPanel createDialogForm(String[] labels, Component[] components) {
        JPanel panel = new JPanel(new GridLayout(labels.length, 2, 10, 10));
        for (int index = 0; index < labels.length; index++) {
            JLabel label = new JLabel(labels[index]);
            label.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
            panel.add(label);
            panel.add(components[index]);
        }
        return panel;
    }

    private void showValidationMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation", JOptionPane.WARNING_MESSAGE);
    }

    private void addIncomeEntry(IncomeEntry entry) {
        incomeEntries.add(entry);
        handleFinancialDataChanged();
    }

    private void addExpenseEntry(ExpenseEntry entry) {
        expenseEntries.add(entry);
        handleFinancialDataChanged();
    }

    private void loadStoredData() {
        AppDatabase.DatabaseState state = AppDatabase.loadForUser(accountEmail);
        incomeEntries.clear();
        incomeEntries.addAll(state.incomeEntries);
        expenseEntries.clear();
        expenseEntries.addAll(state.expenseEntries);
        savingEntries.clear();
        savingEntries.addAll(state.savingEntries);
        budgetLimit = state.budgetLimit;
        savingGoalTarget = state.savingGoalTarget;
    }

    private void persistData() {
        AppDatabase.DatabaseState state = new AppDatabase.DatabaseState();
        state.incomeEntries.addAll(incomeEntries);
        state.expenseEntries.addAll(expenseEntries);
        state.savingEntries.addAll(savingEntries);
        state.budgetLimit = budgetLimit;
        state.savingGoalTarget = savingGoalTarget;
        AppDatabase.saveForUser(accountEmail, state);
    }

    private void handleFinancialDataChanged() {
        persistData();
        refreshAllSections();
    }

    private void refreshAllSections() {
        refreshDashboardSection();
        refreshIncomeSection();
        refreshExpenseSection();
        refreshBudgetSection();
        refreshSavingSection();
        refreshForecastSection();
    }

    private void refreshDashboardSection() {
        double totalIncome = calculateTotalIncome();
        double totalExpenses = calculateTotalExpenses();
        double remainingBalance = totalIncome - totalExpenses;
        double projectedRemaining = remainingBalance - predictFutureExpenses(3);

        dashboardIncomeValueLabel.setText(currencyFormat.format(totalIncome));
        dashboardIncomeBodyLabel.setText("Everything you have added as allowance or income.");
        dashboardExpenseValueLabel.setText(currencyFormat.format(totalExpenses));
        dashboardExpenseBodyLabel.setText("All recorded expenses across your categories.");
        dashboardRemainingValueLabel.setText(currencyFormat.format(remainingBalance));
        dashboardRemainingBodyLabel.setText("What is left after your recorded spending.");
        dashboardForecastValueLabel.setText(currencyFormat.format(projectedRemaining));
        dashboardForecastBodyLabel.setText("Projected balance based on your current pace.");

        if (expenseEntries.isEmpty()) {
            styleBadgeLabel(weeklySpendingBadgeLabel, "Waiting for data", SURFACE_TINT, TEAL_DARK);
            weeklySpendingContentPanel.removeAll();
            weeklySpendingContentPanel.add(createHintStrip(
                    "Add a few expenses and your weekly pattern will appear here."), BorderLayout.NORTH);
        } else {
            styleBadgeLabel(weeklySpendingBadgeLabel, "Tracking weekly spend", SURFACE_TINT, TEAL_DARK);
            weeklySpendingContentPanel.removeAll();
            weeklySpendingContentPanel.add(createWeeklySummaryPanel(), BorderLayout.NORTH);
        }

        categoryOverviewContentPanel.removeAll();
        if (expenseEntries.isEmpty()) {
            categoryOverviewContentPanel.add(createDonutEmptyPanel("No category data yet"), BorderLayout.CENTER);
        } else {
            categoryOverviewContentPanel.add(createCategoryListPanel(buildExpenseGroups()), BorderLayout.CENTER);
        }

        dashboardBudgetNoticeLabel.setText(budgetLimit > 0
                ? "Your current budget is " + currencyFormat.format(budgetLimit) + " with "
                        + currencyFormat.format(Math.max(0.0, budgetLimit - totalExpenses)) + " left to use."
                : "Add a budget to get category alerts and overspending warnings.");
        dashboardInsightNoticeLabel.setText(incomeEntries.isEmpty() && expenseEntries.isEmpty()
                ? "Add income and expenses to unlock smarter overview insights."
                : "Your overview cards are now reflecting the latest income, expenses, and forecast pace.");

        weeklySpendingContentPanel.revalidate();
        weeklySpendingContentPanel.repaint();
        categoryOverviewContentPanel.revalidate();
        categoryOverviewContentPanel.repaint();
    }

    private void refreshIncomeSection() {
        double totalIncome = calculateTotalIncome();
        double thisMonthIncome = calculateIncomeForMonth(YearMonth.now());

        styleBadgeLabel(incomeRecordBadgeLabel, incomeEntries.size() + " records", SURFACE_TINT, TEAL_DARK);
        incomeMonthValueLabel.setText(currencyFormat.format(thisMonthIncome));
        incomeWeekValueLabel.setText(currencyFormat.format(calculateAveragePerWeek(totalIncome, incomeEntries)));
        incomeTopSourceValueLabel.setText(findTopIncomeSource());
        incomeTrackedCountValueLabel.setText(String.valueOf(incomeEntries.size()));

        incomeTableModel.setRowCount(0);
        for (IncomeEntry entry : incomeEntries) {
            incomeTableModel.addRow(new Object[] { entry.source, entry.date, currencyFormat.format(entry.amount) });
        }

        incomeRecordsContentPanel.removeAll();
        if (incomeEntries.isEmpty()) {
            incomeRecordsContentPanel.add(createEmptyTableState(new String[] { "Source", "Date", "Amount" },
                    "No income yet", "Add allowance, salary, or side income to start tracking your balance."),
                    BorderLayout.CENTER);
        } else {
            incomeRecordsContentPanel.add(incomeTableScrollPane, BorderLayout.CENTER);
        }
        incomeRecordsContentPanel.revalidate();
        incomeRecordsContentPanel.repaint();
    }

    private void refreshExpenseSection() {
        LinkedHashMap<String, ArrayList<ExpenseEntry>> groups = buildExpenseGroups();
        double totalExpenses = calculateTotalExpenses();

        styleBadgeLabel(expenseRecordBadgeLabel, expenseEntries.size() + " records", SURFACE_TINT, TEAL_DARK);
        expenseTotalSpentValueLabel.setText(currencyFormat.format(totalExpenses));
        expenseTopCategoryValueLabel.setText(findTopExpenseCategory());
        expenseTrackedCountValueLabel.setText(String.valueOf(expenseEntries.size()));
        expenseSummaryNoteLabel.setText(expenseEntries.isEmpty() ? "No category totals yet" : "Category totals are active");

        expenseTableModel.setRowCount(0);
        for (ExpenseEntry entry : expenseEntries) {
            expenseTableModel.addRow(new Object[] { entry.category, entry.date, currencyFormat.format(entry.amount) });
        }

        expenseRecordsContentPanel.removeAll();
        if (expenseEntries.isEmpty()) {
            expenseRecordsContentPanel.add(createEmptyTableState(new String[] { "Category", "Date", "Amount" },
                    "No expenses yet", "Add food, transport, school, or other spending to see where your money goes."),
                    BorderLayout.CENTER);
        } else {
            expenseRecordsContentPanel.add(expenseTableScrollPane, BorderLayout.CENTER);
        }
        expenseRecordsContentPanel.revalidate();
        expenseRecordsContentPanel.repaint();
    }

    private void refreshBudgetSection() {
        double totalExpenses = calculateTotalExpenses();
        double remainingBudget = budgetLimit - totalExpenses;
        int trackedCategories = buildExpenseGroups().size();
        int budgetCategories = budgetLimit > 0 ? Math.max(1, trackedCategories) : 0;
        double usedPercent = budgetLimit > 0 ? Math.min(100.0, (totalExpenses / budgetLimit) * 100.0) : 0.0;

        styleBadgeLabel(budgetCategoryBadgeLabel, budgetCategories + " categories", SURFACE_TINT, TEAL_DARK);
        budgetTotalBudgetedValueLabel.setText(currencyFormat.format(budgetLimit));
        budgetTotalSpentValueLabel.setText(currencyFormat.format(totalExpenses));
        budgetRemainingValueLabel.setText(currencyFormat.format(Math.max(remainingBudget, 0.0)));
        budgetOverallUsedValueLabel.setText(String.format("%.0f%%", usedPercent));
        budgetClosestValueLabel.setText(budgetLimit > 0 ? findTopExpenseCategory() : "-");

        budgetProgressContentPanel.removeAll();
        if (budgetLimit <= 0) {
            budgetProgressContentPanel.add(createLargeEmptyState("No budget categories yet",
                    "Add a budget category to start tracking your limits."));
        } else {
            budgetProgressContentPanel.add(createBudgetProgressRow("Overall budget", totalExpenses, budgetLimit));
            if (!expenseEntries.isEmpty()) {
                budgetProgressContentPanel.add(Box.createVerticalStrut(12));
                budgetProgressContentPanel.add(createMutedNote(
                        "Current progress is based on your overall monthly budget against total recorded spending."));
            }
        }
        budgetProgressContentPanel.revalidate();
        budgetProgressContentPanel.repaint();
    }

    private void refreshSavingSection() {
        double totalSaved = calculateTotalSavings();
        double remaining = Math.max(0.0, savingGoalTarget - totalSaved);
        int progress = savingGoalTarget > 0 ? (int) Math.min(100, Math.round((totalSaved / savingGoalTarget) * 100.0)) : 0;

        styleBadgeLabel(savingsStatusBadgeLabel, savingGoalTarget > 0 ? "Set" : "Not set", SURFACE_TINT, TEAL_DARK);
        styleBadgeLabel(savingsPercentBadgeLabel, progress + "%", SURFACE_TINT, TEAL_DARK);
        savingsGoalTitleLabel.setText(savingGoalTarget > 0 ? "Saving goal in progress" : "No saving goal yet");
        savingsGoalBodyLabel.setText(savingGoalTarget > 0
                ? "You have saved " + currencyFormat.format(totalSaved) + " out of your target."
                : "Set a target amount to start tracking progress.");
        savingsGoalProgressBar.setValue(progress);

        savingsTargetValueLabel.setText(currencyFormat.format(savingGoalTarget));
        savingsSavedValueLabel.setText(currencyFormat.format(totalSaved));
        savingsRemainingValueLabel.setText(currencyFormat.format(remaining));
        savingsEntriesValueLabel.setText(String.valueOf(savingEntries.size()));

        savingsHistoryContentPanel.removeAll();
        savingsHistoryPaginationPanel.removeAll();
        if (savingEntries.isEmpty()) {
            savingsHistoryContentPanel.add(createEmptyTableState(new String[] { "Date", "Amount" },
                    "No savings history yet",
                    "Add daily savings after setting a goal to build your history here."), BorderLayout.CENTER);
            savingsHistoryCurrentPage = 1;
            savingsHistoryLastPageSize = SAVINGS_HISTORY_MIN_PAGE_SIZE;
            styleBadgeLabel(savingsHistoryBadgeLabel, "0 entries", SURFACE_TINT, TEAL_DARK);
        } else {
            int pageSize = resolveSavingsHistoryPageSize();
            savingsHistoryLastPageSize = pageSize;
            int totalPages = (int) Math.ceil((double) savingEntries.size() / (double) pageSize);
            savingsHistoryCurrentPage = Math.max(1, Math.min(savingsHistoryCurrentPage, totalPages));

            int startIndex = (savingsHistoryCurrentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, savingEntries.size());

            savingsTableModel.setRowCount(0);
            for (int i = startIndex; i < endIndex; i++) {
                SavingEntry entry = savingEntries.get(i);
                savingsTableModel.addRow(new Object[] { entry.date, currencyFormat.format(entry.amount) });
            }

            savingsHistoryContentPanel.add(savingsTableScrollPane, BorderLayout.CENTER);
            buildSavingsHistoryPagination(totalPages);
            styleBadgeLabel(savingsHistoryBadgeLabel, "Showing " + (startIndex + 1) + "-" + endIndex + " of " + savingEntries.size(), SURFACE_TINT, TEAL_DARK);
        }
        savingsHistoryContentPanel.revalidate();
        savingsHistoryContentPanel.repaint();
        savingsHistoryPaginationPanel.revalidate();
        savingsHistoryPaginationPanel.repaint();
    }

    private int resolveSavingsHistoryPageSize() {
        int rowHeight = Math.max(1, savingsTable.getRowHeight());
        int viewportHeight = savingsTableScrollPane.getViewport().getHeight();
        int headerHeight = savingsTable.getTableHeader() != null ? savingsTable.getTableHeader().getHeight() : 0;

        if (viewportHeight <= 0) {
            int panelHeight = savingsHistoryContentPanel.getHeight();
            int usablePanelHeight = Math.max(0, panelHeight - headerHeight);
            int fitFromPanel = usablePanelHeight / rowHeight;
            if (fitFromPanel > 0) {
                return Math.max(SAVINGS_HISTORY_MIN_PAGE_SIZE, fitFromPanel);
            }

            // Layout is not ready yet: use a stable minimum so pagination is correct on first render.
            return SAVINGS_HISTORY_MIN_PAGE_SIZE;
        }

        int usableHeight = Math.max(0, viewportHeight - headerHeight);
        int fitRows = usableHeight / rowHeight;
        return Math.max(SAVINGS_HISTORY_MIN_PAGE_SIZE, fitRows);
    }


    private void configureSavingsHistoryAutoPagination() {
        savingsTableScrollPane.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                if (savingEntries.isEmpty()) {
                    return;
                }

                int resolvedPageSize = resolveSavingsHistoryPageSize();
                if (resolvedPageSize != savingsHistoryLastPageSize) {
                    refreshSavingSection();
                }
            }
        });
    }
    private void buildSavingsHistoryPagination(int totalPages) {
        JButton prevButton = createSavingsPaginationButton("Prev", false, savingsHistoryCurrentPage > 1);
        prevButton.addActionListener(event -> {
            if (savingsHistoryCurrentPage > 1) {
                savingsHistoryCurrentPage--;
                refreshSavingSection();
            }
        });
        savingsHistoryPaginationPanel.add(prevButton);

        for (Integer pageNumber : buildSavingsHistoryPageNumbers(totalPages)) {
            boolean active = pageNumber.intValue() == savingsHistoryCurrentPage;
            JButton pageButton = createSavingsPaginationButton(String.valueOf(pageNumber.intValue()), active, true);
            pageButton.addActionListener(event -> {
                savingsHistoryCurrentPage = pageNumber.intValue();
                refreshSavingSection();
            });
            savingsHistoryPaginationPanel.add(pageButton);
        }

        JButton nextButton = createSavingsPaginationButton("Next", true, savingsHistoryCurrentPage < totalPages);
        nextButton.addActionListener(event -> {
            if (savingsHistoryCurrentPage < totalPages) {
                savingsHistoryCurrentPage++;
                refreshSavingSection();
            }
        });
        savingsHistoryPaginationPanel.add(nextButton);
    }
    private ArrayList<Integer> buildSavingsHistoryPageNumbers(int totalPages) {
        ArrayList<Integer> pages = new ArrayList<Integer>();
        if (totalPages <= 6) {
            for (int page = 1; page <= totalPages; page++) {
                pages.add(Integer.valueOf(page));
            }
            return pages;
        }

        if (savingsHistoryCurrentPage <= 4) {
            for (int page = 1; page <= 5; page++) {
                pages.add(Integer.valueOf(page));
            }
        } else if (savingsHistoryCurrentPage >= totalPages - 3) {
            pages.add(Integer.valueOf(1));
            for (int page = totalPages - 4; page <= totalPages; page++) {
                pages.add(Integer.valueOf(page));
            }
            return pages;
        } else {
            pages.add(Integer.valueOf(1));
            for (int page = savingsHistoryCurrentPage - 1; page <= savingsHistoryCurrentPage + 2; page++) {
                pages.add(Integer.valueOf(page));
            }
        }

        pages.add(Integer.valueOf(totalPages));
        return pages;
    }

    private JButton createSavingsPaginationButton(String text, boolean emphasize, boolean enabled) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setEnabled(enabled);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, 12));
        button.setForeground(emphasize ? Color.BLACK : new Color(245, 245, 245));
        button.setBackground(emphasize ? GOLD : new Color(12, 14, 19));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(emphasize ? GOLD : new Color(28, 33, 42), 14, 1),
                new EmptyBorder(6, 12, 6, 12)));

        int minWidth = text.length() <= 2 ? 40 : 72;
        int computedWidth = text.length() * 8 + 22;
        button.setPreferredSize(new Dimension(Math.max(minWidth, computedWidth), 32));

        if (!enabled) {
            button.setForeground(new Color(85, 90, 100));
            button.setBackground(new Color(16, 18, 24));
        }
        return button;
    }

    private void refreshForecastSection() {
        double totalIncome = calculateTotalIncome();
        double totalExpenses = calculateTotalExpenses();
        double averageSpending = calculateAverageSpending();
        double projectedDailySpend = expenseEntries.isEmpty() ? 0.0 : totalExpenses / Math.max(1, LocalDate.now().getDayOfMonth());
        double estimatedRemaining = (totalIncome - totalExpenses) - predictFutureExpenses(3);
        double overspendingChance = calculateOverspendingChance(totalIncome, totalExpenses, averageSpending);

        styleBadgeLabel(forecastRiskBadgeLabel, describeRisk(overspendingChance), SURFACE_TINT, TEAL_DARK);
        forecastEstimatedRemainingValueLabel.setText(currencyFormat.format(estimatedRemaining));
        forecastHighRiskCategoryValueLabel.setText(findTopExpenseCategory());
        forecastProjectedDailyValueLabel.setText(currencyFormat.format(projectedDailySpend));
        forecastOverspendingPercentLabel.setText(String.format("%.0f%%", overspendingChance));
        forecastRiskProgressBar.setValue((int) Math.round(overspendingChance));

        forecastBreakdownContentPanel.removeAll();
        if (expenseEntries.isEmpty()) {
            forecastBreakdownContentPanel.add(createDonutEmptyPanel("No category data yet"), BorderLayout.CENTER);
        } else {
            forecastBreakdownContentPanel.add(createCategoryListPanel(buildExpenseGroups()), BorderLayout.CENTER);
        }
        forecastBreakdownContentPanel.revalidate();
        forecastBreakdownContentPanel.repaint();
    }

    private JPanel createWeeklySummaryPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Latest weekly pace: " + currencyFormat.format(calculateLatestWeekSpending()));
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel body = new JLabel("Average spending per expense is " + currencyFormat.format(calculateAverageSpending()) + ".");
        body.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        body.setForeground(TEXT_SECONDARY);
        body.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(12));
        panel.add(createHintStrip("Your weekly trend is based on the dates of your recent expense entries."));
        panel.add(Box.createVerticalStrut(12));
        panel.add(body);
        return panel;
    }

    private JPanel createCategoryListPanel(LinkedHashMap<String, ArrayList<ExpenseEntry>> groupedExpenses) {
        JPanel panel = new JPanel(new BorderLayout(16, 0));
        panel.setOpaque(false);
        panel.add(new DonutPlaceholderPanel(groupedExpenses.isEmpty() ? "No data\nyet" : "Top\nspend", SURFACE, DONUT_OUTER, DONUT_RING, TEXT_SECONDARY), BorderLayout.WEST);

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        for (Map.Entry<String, ArrayList<ExpenseEntry>> entry : groupedExpenses.entrySet()) {
            list.add(createCategoryRow(entry.getKey(), calculateExpenseGroupTotal(entry.getValue())));
            list.add(Box.createVerticalStrut(8));
        }

        panel.add(list, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCategoryRow(String titleText, double amount) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);

        JLabel title = new JLabel(titleText);
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        title.setForeground(TEXT_PRIMARY);

        JLabel value = new JLabel(currencyFormat.format(amount), SwingConstants.RIGHT);
        value.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        value.setForeground(TEXT_PRIMARY);

        row.add(title, BorderLayout.WEST);
        row.add(value, BorderLayout.EAST);
        return row;
    }

    private JPanel createHintStrip(String text) {
        SurfacePanel panel = createSurface(new BorderLayout(), PAGE_BACKGROUND_SOFT, SURFACE_BORDER, 16);
        panel.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        label.setForeground(TEXT_SECONDARY);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDonutEmptyPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);
        panel.add(new DonutPlaceholderPanel("No data\nyet", SURFACE, DONUT_OUTER, DONUT_RING, TEXT_SECONDARY), BorderLayout.NORTH);

        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        label.setForeground(TEXT_SECONDARY);
        panel.add(label, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createLargeEmptyState(String titleText, String bodyText) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 20));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel body = new JLabel(bodyText);
        body.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        body.setForeground(TEXT_SECONDARY);
        body.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(Box.createVerticalStrut(24));
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        panel.add(body);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createEmptyTableState(String[] headers, String titleText, String bodyText) {
        SurfacePanel panel = createSurface(new BorderLayout(0, 0), SURFACE_BLUE, EMPTY_TABLE_BORDER, 18);
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel headerRow = new JPanel(new GridLayout(1, headers.length, 0, 0));
        headerRow.setOpaque(true);
        headerRow.setBackground(EMPTY_TABLE_HEADER_BACKGROUND);
        headerRow.setBorder(new EmptyBorder(12, 12, 12, 12));
        for (String header : headers) {
            JLabel label = new JLabel(header);
            label.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
            label.setForeground(TEXT_SECONDARY);
            headerRow.add(label);
        }

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(32, 18, 32, 18));
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel text = new JLabel(bodyText);
        text.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        text.setForeground(TEXT_SECONDARY);
        text.setAlignmentX(Component.LEFT_ALIGNMENT);

        body.add(title);
        body.add(Box.createVerticalStrut(10));
        body.add(text);
        body.add(Box.createVerticalGlue());

        panel.add(headerRow, BorderLayout.NORTH);
        panel.add(body, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBudgetProgressRow(String titleText, double spent, double limit) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel title = new JLabel(titleText);
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 15));
        title.setForeground(TEXT_PRIMARY);

        JLabel value = new JLabel(currencyFormat.format(spent) + " / " + currencyFormat.format(limit), SwingConstants.RIGHT);
        value.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        value.setForeground(TEXT_SECONDARY);

        JProgressBar progressBar = new JProgressBar(0, 100);
        styleProgressBar(progressBar);
        progressBar.setValue((int) Math.min(100, Math.round((spent / Math.max(limit, 1.0)) * 100.0)));

        panel.add(top);
        top.add(title, BorderLayout.WEST);
        top.add(value, BorderLayout.EAST);
        panel.add(Box.createVerticalStrut(8));
        panel.add(progressBar);
        return panel;
    }

    private JPanel createMutedNote(String text) {
        JLabel label = new JLabel("<html>" + text + "</html>");
        label.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        label.setForeground(TEXT_SECONDARY);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private void applySidebarButtonStyle(JButton button, boolean active) {
        button.setBackground(active ? SIDEBAR_BUTTON_ACTIVE : SIDEBAR_BUTTON);
        button.setForeground(SIDEBAR_TEXT);
        button.setFont(new Font(FONT_FAMILY, active ? Font.BOLD : Font.PLAIN, 16));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(active ? SIDEBAR_ACTIVE_BORDER : SIDEBAR_BORDER),
                new EmptyBorder(9, 14, 9, 14)));
    }

    private void showPage(String pageKey) {
        currentPage = pageKey;
        pageLayout.show(pagePanel, pageKey);
        applySidebarButtonStyle(dashboardNavButton, PAGE_DASHBOARD.equals(pageKey));
        applySidebarButtonStyle(incomeNavButton, PAGE_INCOME.equals(pageKey));
        applySidebarButtonStyle(expensesNavButton, PAGE_EXPENSES.equals(pageKey));
        applySidebarButtonStyle(budgetNavButton, PAGE_BUDGET.equals(pageKey));
        applySidebarButtonStyle(savingGoalNavButton, PAGE_SAVING_GOAL.equals(pageKey));
        applySidebarButtonStyle(forecastNavButton, PAGE_FORECAST.equals(pageKey));
    }

    private DefaultTableModel createTableModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JScrollPane createTableScrollPane(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(28);
        table.setFont(new Font(FONT_FAMILY, Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, 12));
        table.getTableHeader().setBackground(EMPTY_TABLE_HEADER_BACKGROUND);
        table.getTableHeader().setForeground(TEXT_SECONDARY);
        table.setGridColor(TABLE_GRID);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(TABLE_SELECTION);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setBorder(BorderFactory.createEmptyBorder());
        table.setBackground(SURFACE_BLUE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(EMPTY_TABLE_BORDER));
        scrollPane.getViewport().setBackground(SURFACE_BLUE);
        return scrollPane;
    }

    private Double parseAmount(String value) {
        try {
            return Double.valueOf(value.trim());
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private boolean isValidDate(String value) {
        try {
            LocalDate.parse(value);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private double calculateTotalIncome() {
        double total = 0.0;
        for (IncomeEntry entry : incomeEntries) {
            total += entry.amount;
        }
        return total;
    }

    private double calculateTotalExpenses() {
        double total = 0.0;
        for (ExpenseEntry entry : expenseEntries) {
            total += entry.amount;
        }
        return total;
    }

    private double calculateTotalSavings() {
        double total = 0.0;
        for (SavingEntry entry : savingEntries) {
            total += entry.amount;
        }
        return total;
    }

    private double calculateIncomeForMonth(YearMonth month) {
        double total = 0.0;
        for (IncomeEntry entry : incomeEntries) {
            if (YearMonth.from(LocalDate.parse(entry.date)).equals(month)) {
                total += entry.amount;
            }
        }
        return total;
    }

    private double calculateAveragePerWeek(double totalAmount, ArrayList<?> entries) {
        return entries.isEmpty() ? 0.0 : totalAmount / Math.max(1, countDistinctWeeks(entries));
    }

    private int countDistinctWeeks(ArrayList<?> entries) {
        ArrayList<String> weeks = new ArrayList<String>();
        for (Object value : entries) {
            String date = value instanceof IncomeEntry ? ((IncomeEntry) value).date : ((ExpenseEntry) value).date;
            LocalDate localDate = LocalDate.parse(date);
            String weekKey = localDate.getYear() + "-" + localDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            if (!weeks.contains(weekKey)) {
                weeks.add(weekKey);
            }
        }
        return weeks.size();
    }

    private double calculateAverageSpending() {
        return expenseEntries.isEmpty() ? 0.0 : calculateTotalExpenses() / expenseEntries.size();
    }

    private double calculateLatestWeekSpending() {
        if (expenseEntries.isEmpty()) {
            return 0.0;
        }

        LocalDate latestDate = LocalDate.parse(expenseEntries.get(expenseEntries.size() - 1).date);
        int week = latestDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int year = latestDate.getYear();
        double total = 0.0;

        for (ExpenseEntry entry : expenseEntries) {
            LocalDate date = LocalDate.parse(entry.date);
            if (date.getYear() == year && date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == week) {
                total += entry.amount;
            }
        }
        return total;
    }

    private double predictFutureExpenses(int upcomingEntries) {
        return calculateAverageSpending() * upcomingEntries;
    }

    private double calculateOverspendingChance(double totalIncome, double totalExpenses, double averageSpending) {
        double base = budgetLimit > 0 ? budgetLimit : Math.max(totalIncome, totalExpenses + averageSpending);
        if (base <= 0) {
            return 0.0;
        }
        double projected = totalExpenses + (averageSpending * 5.0);
        return Math.min(100.0, (projected / base) * 100.0);
    }

    private String describeRisk(double percent) {
        if (percent >= 80.0) {
            return "High risk";
        }
        if (percent >= 50.0) {
            return "Moderate risk";
        }
        return "Low risk";
    }

    private String findTopIncomeSource() {
        if (incomeEntries.isEmpty()) {
            return "-";
        }

        LinkedHashMap<String, Double> totals = new LinkedHashMap<String, Double>();
        for (IncomeEntry entry : incomeEntries) {
            Double total = totals.get(entry.source);
            totals.put(entry.source, total == null ? entry.amount : total.doubleValue() + entry.amount);
        }
        return findHighestKey(totals);
    }

    private String findTopExpenseCategory() {
        if (expenseEntries.isEmpty()) {
            return "-";
        }

        LinkedHashMap<String, Double> totals = new LinkedHashMap<String, Double>();
        for (ExpenseEntry entry : expenseEntries) {
            Double total = totals.get(entry.category);
            totals.put(entry.category, total == null ? entry.amount : total.doubleValue() + entry.amount);
        }
        return findHighestKey(totals);
    }

    private String findHighestKey(LinkedHashMap<String, Double> totals) {
        String bestKey = "-";
        double bestValue = Double.MIN_VALUE;
        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            if (entry.getValue().doubleValue() > bestValue) {
                bestKey = entry.getKey();
                bestValue = entry.getValue().doubleValue();
            }
        }
        return bestKey;
    }

    private LinkedHashMap<String, ArrayList<ExpenseEntry>> buildExpenseGroups() {
        LinkedHashMap<String, ArrayList<ExpenseEntry>> groups = new LinkedHashMap<String, ArrayList<ExpenseEntry>>();
        for (ExpenseEntry entry : expenseEntries) {
            ArrayList<ExpenseEntry> list = groups.get(entry.category);
            if (list == null) {
                list = new ArrayList<ExpenseEntry>();
                groups.put(entry.category, list);
            }
            list.add(entry);
        }
        return groups;
    }

    private double calculateExpenseGroupTotal(ArrayList<ExpenseEntry> entries) {
        double total = 0.0;
        for (ExpenseEntry entry : entries) {
            total += entry.amount;
        }
        return total;
    }

    private static class ResponsivePagePanel extends JPanel implements Scrollable {
        ResponsivePagePanel(LayoutManager layout) {
            super(layout);
            setOpaque(false);
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
            return false;
        }
    }

    private static class ResponsiveGridPanel extends JPanel {
        private final int minCellWidth;
        private final int gap;

        ResponsiveGridPanel(int minCellWidth, int gap) {
            this.minCellWidth = minCellWidth;
            this.gap = gap;
            setOpaque(false);
            setLayout(null);
        }

        @Override
        public Dimension getPreferredSize() {
            int width = getParent() == null ? (minCellWidth * Math.max(1, getComponentCount())) : Math.max(getParent().getWidth(), minCellWidth);
            int columns = Math.max(1, Math.min(getComponentCount(), (width + gap) / (minCellWidth + gap)));
            int rows = Math.max(1, (int) Math.ceil(getComponentCount() / (double) columns));
            int cellWidth = Math.max(minCellWidth, (width - ((columns - 1) * gap)) / columns);
            int totalHeight = 0;

            for (int row = 0; row < rows; row++) {
                int rowHeight = 0;
                for (int column = 0; column < columns; column++) {
                    int index = (row * columns) + column;
                    if (index >= getComponentCount()) {
                        break;
                    }
                    Component component = getComponent(index);
                    Dimension preferred = component.getPreferredSize();
                    rowHeight = Math.max(rowHeight, Math.max(preferred.height, cellWidth / 2));
                }
                totalHeight += rowHeight;
                if (row < rows - 1) {
                    totalHeight += gap;
                }
            }

            return new Dimension(width, totalHeight);
        }

        @Override
        public void doLayout() {
            int count = getComponentCount();
            if (count == 0) {
                return;
            }

            int width = getWidth();
            int columns = Math.max(1, Math.min(count, (width + gap) / (minCellWidth + gap)));
            int cellWidth = Math.max(minCellWidth, (width - ((columns - 1) * gap)) / columns);
            int x = 0;
            int y = 0;
            int rowHeight = 0;

            for (int index = 0; index < count; index++) {
                if (index > 0 && index % columns == 0) {
                    x = 0;
                    y += rowHeight + gap;
                    rowHeight = 0;
                }

                Component component = getComponent(index);
                int preferredHeight = Math.max(component.getPreferredSize().height, cellWidth / 2);
                component.setBounds(x, y, cellWidth, preferredHeight);
                rowHeight = Math.max(rowHeight, preferredHeight);
                x += cellWidth + gap;
            }
        }
    }

    private static class ResponsiveSplitPanel extends JPanel {
        private static final int GAP = 18;
        private static final int STACK_BREAKPOINT = 1180;

        private final JPanel primaryPanel;
        private final JPanel secondaryPanel;
        private final int secondaryWidth;

        ResponsiveSplitPanel(JPanel primaryPanel, JPanel secondaryPanel, int secondaryWidth) {
            this.primaryPanel = primaryPanel;
            this.secondaryPanel = secondaryPanel;
            this.secondaryWidth = Math.max(300, secondaryWidth);
            setOpaque(false);
            setLayout(null);
            add(primaryPanel);
            add(secondaryPanel);
        }

        @Override
        public Dimension getPreferredSize() {
            int availableWidth = getParent() == null ? 1200 : Math.max(0, getParent().getWidth());
            int primaryHeight = primaryPanel.getPreferredSize().height;
            int secondaryHeight = secondaryPanel.getPreferredSize().height;

            if (availableWidth < STACK_BREAKPOINT) {
                int width = Math.max(availableWidth, secondaryWidth);
                return new Dimension(width, primaryHeight + GAP + secondaryHeight);
            }

            int width = Math.max(availableWidth, secondaryWidth + 340 + GAP);
            return new Dimension(width, Math.max(primaryHeight, secondaryHeight));
        }

        @Override
        public void doLayout() {
            int width = getWidth();
            int primaryHeight = primaryPanel.getPreferredSize().height;
            int secondaryHeight = secondaryPanel.getPreferredSize().height;

            if (width < STACK_BREAKPOINT) {
                primaryPanel.setBounds(0, 0, width, primaryHeight);
                secondaryPanel.setBounds(0, primaryHeight + GAP, width, secondaryHeight);
                return;
            }

            int resolvedSecondaryWidth = Math.min(this.secondaryWidth, Math.max(300, (int) Math.round(width * 0.34)));
            int resolvedPrimaryWidth = Math.max(320, width - resolvedSecondaryWidth - GAP);
            int sharedHeight = Math.max(primaryHeight, secondaryHeight);

            primaryPanel.setBounds(0, 0, resolvedPrimaryWidth, sharedHeight);
            secondaryPanel.setBounds(resolvedPrimaryWidth + GAP, 0, resolvedSecondaryWidth, sharedHeight);
        }
    }

    private SurfacePanel createSurface(LayoutManager layout) {
        SurfacePanel panel = new SurfacePanel(SURFACE, SURFACE_BORDER, 24, SHADOW);
        panel.setLayout(layout);
        return panel;
    }

    private SurfacePanel createSurface(LayoutManager layout, Color fillColor, Color borderColor, int radius) {
        SurfacePanel panel = new SurfacePanel(fillColor, borderColor, radius, SHADOW);
        panel.setLayout(layout);
        return panel;
    }

    private static class RoundedLineBorder extends AbstractBorder {
        private final Color color;
        private final int radius;
        private final int thickness;
        RoundedLineBorder(Color color, int radius, int thickness) {
            this.color = color;
            this.radius = radius;
            this.thickness = Math.max(1, thickness);
        }
        @Override
        public Insets getBorderInsets(Component component) {
            return new Insets(thickness, thickness, thickness, thickness);
        }
        @Override
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.left = thickness;
            insets.right = thickness;
            insets.top = thickness;
            insets.bottom = thickness;
            return insets;
        }
        @Override
        public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            for (int line = 0; line < thickness; line++) {
                g2.drawRoundRect(x + line, y + line, width - (line * 2) - 1, height - (line * 2) - 1, radius, radius);
            }
            g2.dispose();
        }
    }
}








