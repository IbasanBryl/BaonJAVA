package baon.ui;

import baon.data.AppDatabase;
import baon.data.RememberedLoginStore;
import baon.model.ExpenseEntry;
import baon.model.IncomeEntry;
import baon.model.SavingEntry;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
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
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.IsoFields;
import java.time.format.DateTimeParseException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import javax.swing.JTextArea;
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
    private static final int DASHBOARD_METRIC_CARD_MIN_HEIGHT = 278;
    private static final int SAVINGS_HISTORY_MIN_PAGE_SIZE = 8;
    private static final double BUDGET_ALERT_THRESHOLD_PERCENT = 85.0;
    private static final int MAX_STORED_BUDGET_ALERTS = 10;
    private static final String[] BUDGET_CATEGORY_OPTIONS = new String[] { "Food", "Transport", "Leisure", "School", "Other" };
    private static final String[] INCOME_SOURCE_OPTIONS = new String[] { "Allowance", "Salary", "Scholarship", "Gift", "Side Hustle", "Other" };
    private static final String[] SAVINGS_CATEGORY_OPTIONS = new String[] { "Emergency", "School", "Travel", "Gadgets", "Other" };
    private static final String BRAND_LOGO_PATH = "D:\\BaonJava\\lib\\bb-logo.png";
    private static final int BRAND_LOGO_SIZE = 34;
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
    private static final Color MANAGE_SURFACE = AppTheme.color("--main-manage-surface", "#FFFDF8");
    private static final Color MANAGE_BORDER = AppTheme.color("--main-manage-border", "#E7D7AE");
    private static final Color MANAGE_MUTED_SURFACE = AppTheme.color("--main-manage-muted-surface", "#FFF8EE");
    private static final Color MANAGE_CHIP_BACKGROUND = AppTheme.color("--main-manage-chip-background", "#F5FBF6");
    private static final Color MANAGE_CHIP_BORDER = AppTheme.color("--main-manage-chip-border", "#D7E9DB");
    private static final Color MANAGE_TITLE = AppTheme.color("--main-manage-title", "#111713");
    private static final Color MANAGE_SUBTITLE = AppTheme.color("--main-manage-subtitle", "#7A613E");
    private static final Color MANAGE_ACCENT = AppTheme.color("--main-manage-accent", "#5BA476");
    private static final Color MANAGE_ACCENT_DARK = AppTheme.color("--main-manage-accent-dark", "#356D4B");
    private static final Color MANAGE_SECONDARY_BUTTON_FILL = AppTheme.color("--main-manage-secondary-button-fill", "#FFF8EE");
    private static final Color MANAGE_SECONDARY_BUTTON_BORDER = AppTheme.color("--main-manage-secondary-button-border", "#E7D7AE");
    private static final Color MANAGE_SECONDARY_BUTTON_TEXT = AppTheme.color("--main-manage-secondary-button-text", "#111713");
    private static final Color NOTIFICATION_SURFACE = AppTheme.color("--main-notification-surface", "#FFFDF9");
    private static final Color NOTIFICATION_BORDER = AppTheme.color("--main-notification-border", "#E8D9B2");
    private static final Color NOTIFICATION_MUTED_SURFACE = AppTheme.color("--main-notification-muted-surface", "#FFF7EA");
    private static final Color NOTIFICATION_HOVER = AppTheme.color("--main-notification-hover", "#FFF2D9");
    private static final Color NOTIFICATION_UNREAD = AppTheme.color("--main-notification-unread", "#FFF3DE");
    private static final Color NOTIFICATION_UNREAD_TEXT = AppTheme.color("--main-notification-unread-text", "#C57A21");
    private static final int MANAGE_DIALOG_WIDTH = AppTheme.integer("--main-manage-dialog-width", 980);
    private static final int MANAGE_DIALOG_HEIGHT = AppTheme.integer("--main-manage-dialog-height", 820);
    private static final int MANAGE_DIALOG_MIN_WIDTH = AppTheme.integer("--main-manage-dialog-min-width", 760);
    private static final int MANAGE_DIALOG_MIN_HEIGHT = AppTheme.integer("--main-manage-dialog-min-height", 640);
    private static final int MANAGE_CARD_WIDTH = AppTheme.integer("--main-manage-card-width", 720);
    private static final int MANAGE_CONTENT_WIDTH = AppTheme.integer("--main-manage-content-width", 660);
    private static final int MANAGE_HEADER_GAP = AppTheme.integer("--main-manage-header-gap", 18);
    private static final int MANAGE_CARD_RADIUS = AppTheme.integer("--main-manage-card-radius", 24);
    private static final int MANAGE_SECTION_RADIUS = AppTheme.integer("--main-manage-section-radius", 20);
    private static final int MANAGE_FIELD_RADIUS = AppTheme.integer("--main-manage-field-radius", 20);
    private static final int MANAGE_INPUT_RADIUS = AppTheme.integer("--main-manage-input-radius", 22);
    private static final int MANAGE_BUTTON_RADIUS = AppTheme.integer("--main-manage-button-radius", 28);
    private static final int MANAGE_CARD_PADDING = AppTheme.integer("--main-manage-card-padding", 30);
    private static final int MANAGE_SECTION_PADDING = AppTheme.integer("--main-manage-section-padding", 18);
    private static final int MANAGE_FIELD_PADDING = AppTheme.integer("--main-manage-field-padding", 16);
    private static final int MANAGE_SECTION_GAP = AppTheme.integer("--main-manage-section-gap", 24);
    private static final int MANAGE_FIELD_GAP = AppTheme.integer("--main-manage-field-gap", 16);
    private static final int MANAGE_ACTION_GAP = AppTheme.integer("--main-manage-action-gap", 12);
    private static final int MANAGE_INPUT_HEIGHT = AppTheme.integer("--main-manage-input-height", 46);
    private static final int MANAGE_ACTION_HEIGHT = AppTheme.integer("--main-manage-action-height", 42);
    private static final int MANAGE_CHIP_FONT_SIZE = AppTheme.integer("--main-manage-chip-font-size", 12);
    private static final int MANAGE_TITLE_FONT_SIZE = AppTheme.integer("--main-manage-title-font-size", 34);
    private static final int MANAGE_SUBTITLE_FONT_SIZE = AppTheme.integer("--main-manage-subtitle-font-size", 17);
    private static final int MANAGE_SECTION_LABEL_FONT_SIZE = AppTheme.integer("--main-manage-section-label-font-size", 13);
    private static final int MANAGE_HEADING_FONT_SIZE = AppTheme.integer("--main-manage-heading-font-size", 22);
    private static final int MANAGE_FIELD_TITLE_FONT_SIZE = AppTheme.integer("--main-manage-field-title-font-size", 15);
    private static final int MANAGE_FIELD_NOTE_FONT_SIZE = AppTheme.integer("--main-manage-field-note-font-size", 12);
    private static final int MANAGE_BUTTON_FONT_SIZE = AppTheme.integer("--main-manage-button-font-size", 14);
    private static final int NOTIFICATION_DIALOG_WIDTH = AppTheme.integer("--main-notification-dialog-width", 620);
    private static final int NOTIFICATION_DIALOG_HEIGHT = AppTheme.integer("--main-notification-dialog-height", 560);
    private static final int NOTIFICATION_DIALOG_MIN_WIDTH = AppTheme.integer("--main-notification-dialog-min-width", 460);
    private static final int NOTIFICATION_DIALOG_MIN_HEIGHT = AppTheme.integer("--main-notification-dialog-min-height", 420);
    private static final int NOTIFICATION_BUTTON_WIDTH = AppTheme.integer("--main-notification-button-width", 54);
    private static final int NOTIFICATION_BUTTON_HEIGHT = AppTheme.integer("--main-notification-button-height", 44);
    private static final int NOTIFICATION_BUTTON_RADIUS = AppTheme.integer("--main-notification-button-radius", 20);
    private static final int NOTIFICATION_CARD_RADIUS = AppTheme.integer("--main-notification-card-radius", 22);
    private static final int NOTIFICATION_ITEM_RADIUS = AppTheme.integer("--main-notification-item-radius", 18);
    private static final int NOTIFICATION_SECTION_GAP = AppTheme.integer("--main-notification-section-gap", 18);

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

    private final JLabel dashboardIncomeValueLabel = new AutoScalingMetricLabel();
    private final JLabel dashboardIncomeBodyLabel = new JLabel();
    private final JLabel dashboardExpenseValueLabel = new AutoScalingMetricLabel();
    private final JLabel dashboardExpenseBodyLabel = new JLabel();
    private final JLabel dashboardRemainingValueLabel = new AutoScalingMetricLabel();
    private final JLabel dashboardRemainingBodyLabel = new JLabel();
    private final JLabel dashboardForecastValueLabel = new AutoScalingMetricLabel();
    private final JLabel dashboardForecastBodyLabel = new JLabel();

    private final JLabel weeklySpendingBadgeLabel = new JLabel();
    private final JPanel weeklySpendingContentPanel = new JPanel(new BorderLayout());
    private final WeeklyTrendPanel weeklyTrendPanel = new WeeklyTrendPanel();
    private final JLabel weeklyTotalValueLabel = new JLabel();
    private final JLabel weeklyAverageDayValueLabel = new JLabel();
    private final JLabel weeklyBusyDayValueLabel = new JLabel();
    private final JLabel weeklyActiveDaysValueLabel = new JLabel();
    private final JLabel categoryOverviewBadgeLabel = new JLabel();
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
    private final JButton notificationButton = new JButton();
    private final ArrayList<String> budgetAlertInbox = new ArrayList<String>();
    private boolean hasUnreadBudgetAlerts = false;

    private final DefaultTableModel incomeTableModel = createTableModel(new String[] { "Source", "Date", "Amount" });
    private final DefaultTableModel expenseTableModel = createTableModel(new String[] { "Category", "Item", "Date", "Amount" });
    private final DefaultTableModel savingsTableModel = createTableModel(new String[] { "Category", "Date", "Amount" });
    private final JTable incomeTable = new JTable(incomeTableModel);
    private final JTable expenseTable = new JTable(expenseTableModel);
    private final JTable savingsTable = new JTable(savingsTableModel);
    private final JScrollPane incomeTableScrollPane = createTableScrollPane(incomeTable);
    private final JScrollPane expenseTableScrollPane = createTableScrollPane(expenseTable);
    private final JScrollPane savingsTableScrollPane = createTableScrollPane(savingsTable);

    private double budgetLimit = 0.0;
    private final LinkedHashMap<String, Double> categoryBudgetLimits = new LinkedHashMap<String, Double>();    
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
        root.add(createContentArea(), BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel createContentArea() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        configureNotificationButton();
        panel.add(createPageScrollPane(), BorderLayout.CENTER);
        return panel;
    }

    private void configureNotificationButton() {
        for (java.awt.event.ActionListener listener : notificationButton.getActionListeners()) {
            notificationButton.removeActionListener(listener);
        }
        notificationButton.setFocusable(false);
        notificationButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        notificationButton.setIcon(new MailOutlineIcon(22, 16));
        notificationButton.setForeground(TEAL_DARK);
        notificationButton.setBackground(NOTIFICATION_MUTED_SURFACE);
        notificationButton.setOpaque(true);
        notificationButton.setContentAreaFilled(true);
        notificationButton.setFocusPainted(false);
        notificationButton.setText("");
        notificationButton.setPreferredSize(new Dimension(NOTIFICATION_BUTTON_WIDTH, NOTIFICATION_BUTTON_HEIGHT));
        notificationButton.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(NOTIFICATION_BORDER, NOTIFICATION_BUTTON_RADIUS, 1),
                new EmptyBorder(4, 8, 8, 8)));
        notificationButton.setToolTipText("Open notifications");
        notificationButton.addActionListener(event -> showBudgetAlertInboxDialog());
        notificationButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                notificationButton.setBackground(hasUnreadBudgetAlerts ? NOTIFICATION_HOVER : PAGE_BACKGROUND_SOFT);
            }

            @Override
            public void mouseExited(MouseEvent event) {
                refreshNotificationBar();
            }
        });
        refreshNotificationBar();
    }

    private void refreshContentHeader() {
        notificationButton.setVisible(true);
        notificationButton.revalidate();
        notificationButton.repaint();
    }

    private JPanel createContentHeaderBar() {
        JPanel panel = new JPanel(new BorderLayout(18, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 18, 0));

        JLabel titleLabel = new JLabel("Financial Overview");
        titleLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 46));
        titleLabel.setForeground(TEXT_PRIMARY);

        JPanel accessoryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        accessoryPanel.setOpaque(false);
        accessoryPanel.add(notificationButton);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(accessoryPanel, BorderLayout.EAST);
        return panel;
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
        brand.setFont(new Font(FONT_FAMILY, Font.BOLD, 22));
        brand.setForeground(SIDEBAR_TEXT);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        brand.setIcon(loadBrandLogoIcon());
        brand.setIconTextGap(8);
        brand.setHorizontalTextPosition(SwingConstants.RIGHT);
        brand.setVerticalTextPosition(SwingConstants.CENTER);

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
                new RoundedLineBorder(MENU_BADGE_BORDER, 16, 1),
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

    private Icon loadBrandLogoIcon() {
        ImageIcon icon = new ImageIcon(BRAND_LOGO_PATH);
        if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            return null;
        }
        Image scaled = icon.getImage().getScaledInstance(BRAND_LOGO_SIZE, BRAND_LOGO_SIZE, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
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
                new RoundedLineBorder(SIDEBAR_BUTTON, 18, 1),
                new EmptyBorder(8, 8, 8, 8)));
        menu.setLayout(new GridLayout(0, 1, 0, 8));
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
        item.setPreferredSize(new Dimension(182, 42));
        item.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(SIDEBAR_BUTTON, 18, 1),
                new EmptyBorder(8, 16, 8, 16)));
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
        final Color dialogBackground = PAGE_BACKGROUND_SOFT;
        final Color cardBackground = MANAGE_SURFACE;
        final Color cardBorder = MANAGE_BORDER;
        final Color fieldBackground = MANAGE_SURFACE;
        final Color fieldBorder = MANAGE_BORDER;
        final Color textPrimary = MANAGE_TITLE;
        final Color textSecondary = MANAGE_SUBTITLE;

        JDialog dialog = new JDialog(this, "Manage Account", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(true);
        dialog.setSize(MANAGE_DIALOG_WIDTH, MANAGE_DIALOG_HEIGHT);
        dialog.setMinimumSize(new Dimension(MANAGE_DIALOG_MIN_WIDTH, MANAGE_DIALOG_MIN_HEIGHT));
        dialog.setLocationRelativeTo(this);

        JPanel root = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(dialogBackground);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setBorder(new EmptyBorder(18, 18, 18, 18));

        SurfacePanel card = createSurface(new BorderLayout(0, MANAGE_SECTION_GAP), cardBackground, cardBorder, MANAGE_CARD_RADIUS);
        card.setBorder(new EmptyBorder(MANAGE_CARD_PADDING, MANAGE_CARD_PADDING, MANAGE_CARD_PADDING - 2, MANAGE_CARD_PADDING));
        card.setPreferredSize(new Dimension(MANAGE_CARD_WIDTH, 0));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.setMaximumSize(new Dimension(MANAGE_CONTENT_WIDTH, Integer.MAX_VALUE));
        content.setPreferredSize(new Dimension(MANAGE_CONTENT_WIDTH, content.getPreferredSize().height));

        JLabel chip = createManageChipLabel("PROFILE SETTINGS");

        JLabel title = new JLabel("Manage Account");
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, MANAGE_TITLE_FONT_SIZE));
        title.setForeground(textPrimary);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea subtitle = createWrappedTextArea(
                "Update how your account appears in the dashboard and keep your sign-in details organized.",
                new Font(FONT_FAMILY, Font.PLAIN, MANAGE_SUBTITLE_FONT_SIZE),
                textSecondary);

        JPanel detailsGroup = new JPanel();
        detailsGroup.setOpaque(false);
        detailsGroup.setLayout(new BoxLayout(detailsGroup, BoxLayout.Y_AXIS));
        detailsGroup.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel detailsLabel = createManageSectionLabel("ACCOUNT DETAILS");

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

        detailsGroup.add(detailsLabel);
        detailsGroup.add(Box.createVerticalStrut(12));
        detailsGroup.add(emailCard);
        detailsGroup.add(Box.createVerticalStrut(MANAGE_FIELD_GAP));
        detailsGroup.add(displayNameCard);

        SurfacePanel securityCard = createSurface(new BorderLayout(0, MANAGE_FIELD_GAP), cardBackground, cardBorder, MANAGE_SECTION_RADIUS);
        securityCard.setBorder(new EmptyBorder(MANAGE_SECTION_PADDING, MANAGE_SECTION_PADDING, MANAGE_SECTION_PADDING, MANAGE_SECTION_PADDING));
        securityCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel securityContent = new JPanel();
        securityContent.setOpaque(false);
        securityContent.setLayout(new BoxLayout(securityContent, BoxLayout.Y_AXIS));
        securityContent.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel securityTitle = createManageSectionLabel("SECURITY");

        JLabel securityHeading = new JLabel("Password");
        securityHeading.setFont(new Font(FONT_FAMILY, Font.BOLD, MANAGE_HEADING_FONT_SIZE));
        securityHeading.setForeground(textPrimary);
        securityHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea securityBody = createWrappedTextArea(
                "Change your password anytime to keep your account protected.",
                new Font(FONT_FAMILY, Font.PLAIN, MANAGE_SUBTITLE_FONT_SIZE - 1),
                textSecondary);

        JPanel securityActionWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        securityActionWrap.setOpaque(false);
        securityActionWrap.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton changePasswordButton = createManageActionButton("Change Password", MANAGE_ACCENT, MANAGE_ACCENT_DARK, Color.WHITE);
        changePasswordButton.setPreferredSize(new Dimension(180, MANAGE_INPUT_HEIGHT));
        changePasswordButton.setMaximumSize(new Dimension(180, MANAGE_INPUT_HEIGHT));
        changePasswordButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        changePasswordButton.addActionListener(event -> showChangePasswordDialog(dialog));
        securityActionWrap.add(changePasswordButton);

        securityContent.add(securityTitle);
        securityContent.add(Box.createVerticalStrut(8));
        securityContent.add(securityHeading);
        securityContent.add(Box.createVerticalStrut(10));
        securityContent.add(securityBody);
        securityContent.add(Box.createVerticalStrut(MANAGE_FIELD_GAP));
        securityContent.add(securityActionWrap);
        securityCard.add(securityContent, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, MANAGE_ACTION_GAP, 0));
        actions.setOpaque(false);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton cancelButton = createManageActionButton("Cancel", MANAGE_SECONDARY_BUTTON_FILL, MANAGE_SECONDARY_BUTTON_BORDER, MANAGE_SECONDARY_BUTTON_TEXT);
        cancelButton.setPreferredSize(new Dimension(132, MANAGE_ACTION_HEIGHT));
        cancelButton.setMaximumSize(new Dimension(132, MANAGE_ACTION_HEIGHT));
        cancelButton.addActionListener(event -> dialog.dispose());

        JButton saveButton = createManageActionButton("Save Changes", MANAGE_ACCENT,
                MANAGE_ACCENT_DARK, Color.WHITE);
        saveButton.setPreferredSize(new Dimension(168, MANAGE_ACTION_HEIGHT));
        saveButton.setMaximumSize(new Dimension(168, MANAGE_ACTION_HEIGHT));
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

        actions.add(saveButton);
        actions.add(cancelButton);

        content.add(chip);
        content.add(Box.createVerticalStrut(MANAGE_HEADER_GAP));
        content.add(title);
        content.add(Box.createVerticalStrut(12));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(MANAGE_CARD_PADDING));
        content.add(detailsGroup);
        content.add(Box.createVerticalStrut(MANAGE_FIELD_GAP + 2));
        content.add(securityCard);
        content.add(Box.createVerticalStrut(MANAGE_SECTION_GAP));
        content.add(actions);

        card.add(content, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(card);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        root.add(scrollPane, constraints);

        dialog.setContentPane(root);
        dialog.setVisible(true);
    }

    private JPanel createManageFieldCard(String titleText, JTextField field, String noteText,
            Color cardBackground, Color cardBorder, Color titleColor, Color noteColor) {
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(MANAGE_MUTED_SURFACE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(cardBorder, MANAGE_FIELD_RADIUS, 1),
                new EmptyBorder(MANAGE_FIELD_PADDING, MANAGE_FIELD_PADDING, MANAGE_FIELD_PADDING, MANAGE_FIELD_PADDING)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, MANAGE_FIELD_TITLE_FONT_SIZE));
        title.setForeground(titleColor);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea note = createWrappedTextArea(noteText, new Font(FONT_FAMILY, Font.PLAIN, MANAGE_FIELD_NOTE_FONT_SIZE), noteColor);

        card.add(title);
        card.add(Box.createVerticalStrut(10));
        card.add(field);
        card.add(Box.createVerticalStrut(10));
        card.add(note);
        return card;
    }
    private void styleManageTextField(JTextField field, boolean editable, Color fillColor, Color borderColor, Color textColor) {
        field.setEditable(editable);
        field.setFont(new Font(FONT_FAMILY, Font.PLAIN, MANAGE_FIELD_TITLE_FONT_SIZE));
        field.setBackground(editable ? fillColor : MANAGE_MUTED_SURFACE);
        field.setForeground(textColor);
        field.setCaretColor(SIDEBAR_TEXT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, MANAGE_INPUT_HEIGHT));
        field.setPreferredSize(new Dimension(0, MANAGE_INPUT_HEIGHT));
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(borderColor, MANAGE_INPUT_RADIUS, 1),
                new EmptyBorder(12, 18, 12, 18)));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
    private JButton createManageActionButton(String text, Color fillColor, Color borderColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, MANAGE_BUTTON_FONT_SIZE));
        button.setForeground(textColor);
        button.setBackground(fillColor);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(borderColor, MANAGE_BUTTON_RADIUS, 1),
                new EmptyBorder(11, 20, 11, 20)));
        button.setPreferredSize(new Dimension(152, MANAGE_ACTION_HEIGHT));
        installButtonHover(button, fillColor);
        return button;
    }

    private JLabel createManageChipLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setOpaque(true);
        label.setBackground(MANAGE_CHIP_BACKGROUND);
        label.setForeground(MANAGE_ACCENT_DARK);
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, MANAGE_CHIP_FONT_SIZE));
        label.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(MANAGE_CHIP_BORDER, 20, 1),
                new EmptyBorder(6, 14, 6, 14)));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel createManageSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, MANAGE_SECTION_LABEL_FONT_SIZE));
        label.setForeground(MANAGE_ACCENT_DARK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void installButtonHover(JButton button, Color baseColor) {
        final Color hoverColor = createButtonHoverColor(baseColor);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent event) {
                button.setBackground(baseColor);
            }
        });
    }

    private Color createButtonHoverColor(Color baseColor) {
        int average = (baseColor.getRed() + baseColor.getGreen() + baseColor.getBlue()) / 3;
        return average > 210 ? mixColor(baseColor, MANAGE_BORDER, 0.35f) : mixColor(baseColor, Color.BLACK, 0.12f);
    }

    private Color mixColor(Color first, Color second, float ratio) {
        float clamped = Math.max(0.0f, Math.min(1.0f, ratio));
        float inverse = 1.0f - clamped;
        int red = Math.round(first.getRed() * inverse + second.getRed() * clamped);
        int green = Math.round(first.getGreen() * inverse + second.getGreen() * clamped);
        int blue = Math.round(first.getBlue() * inverse + second.getBlue() * clamped);
        return new Color(red, green, blue, first.getAlpha());
    }

    private void showChangePasswordDialog(JDialog owner) {
        final Color dialogBackground = PAGE_BACKGROUND_SOFT;

        final Color cardBackground = MANAGE_SURFACE;
        final Color cardBorder = MANAGE_BORDER;
        final Color fieldBackground = MANAGE_SURFACE;
        final Color fieldBorder = MANAGE_BORDER;
        final Color textPrimary = MANAGE_TITLE;
        final Color textSecondary = MANAGE_SUBTITLE;

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
                g2.setPaint(dialogBackground);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setLayout(new BorderLayout());
        root.setBorder(new EmptyBorder(14, 14, 14, 14));

        JPanel content = new JPanel();
        content.setOpaque(true);
        content.setBackground(cardBackground);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(cardBorder, MANAGE_SECTION_RADIUS, 1),
                new EmptyBorder(MANAGE_SECTION_PADDING, MANAGE_SECTION_PADDING, MANAGE_SECTION_PADDING, MANAGE_SECTION_PADDING)));

        JLabel chip = createManageChipLabel("SECURITY");

        JLabel title = new JLabel("Change Password");
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, MANAGE_HEADING_FONT_SIZE));
        title.setForeground(textPrimary);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea subtitle = createWrappedTextArea(
                "Update your password to keep your account protected.",
                new Font(FONT_FAMILY, Font.PLAIN, MANAGE_FIELD_TITLE_FONT_SIZE - 1),
                textSecondary);

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

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, MANAGE_ACTION_GAP, 0));
        actions.setOpaque(false);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton cancelButton = createManageActionButton("Cancel", MANAGE_SECONDARY_BUTTON_FILL,
                MANAGE_SECONDARY_BUTTON_BORDER, MANAGE_SECONDARY_BUTTON_TEXT);
        cancelButton.setPreferredSize(new Dimension(124, MANAGE_ACTION_HEIGHT));
        cancelButton.addActionListener(event -> dialog.dispose());

        JButton updateButton = createManageActionButton("Update Password", MANAGE_ACCENT,
                MANAGE_ACCENT_DARK, Color.WHITE);
        updateButton.setPreferredSize(new Dimension(168, MANAGE_ACTION_HEIGHT));
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

            RememberedLoginStore.updatePasswordIfRemembered(accountEmail, password);
            passwordUpdated[0] = true;
            dialog.dispose();
        });

        actions.add(updateButton);
        actions.add(cancelButton);

        content.add(chip);
        content.add(Box.createVerticalStrut(MANAGE_HEADER_GAP - 4));
        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(MANAGE_FIELD_GAP));
        content.add(newPasswordCard);
        content.add(Box.createVerticalStrut(MANAGE_FIELD_GAP));
        content.add(confirmPasswordCard);
        content.add(Box.createVerticalStrut(MANAGE_SECTION_GAP));
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
        categoryBudgetLimits.clear();
        savingGoalTarget = 0.0;
        AppDatabase.resetFinancialDataForUser(accountEmail);
        refreshAllSections();
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

    private static String toWrappedHtml(String text, int width) {
        return "<html><body style='width: " + width + "px; margin: 0; padding: 0; text-align: left;'>"
                + escapeHtml(text)
                + "</body></html>";
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

        ResponsivePagePanel scrollContent = new ResponsivePagePanel(new BorderLayout());
        scrollContent.setOpaque(false);
        scrollContent.add(createContentHeaderBar(), BorderLayout.NORTH);
        scrollContent.add(pagePanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(scrollContent);
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
        JPanel page = new JPanel(new GridBagLayout());
        page.setOpaque(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;

        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 18, 0);
        page.add(createPageHeader("Dashboard",
                "See your latest balance, spending pace, and budget pressure in one place."), constraints);

        constraints.gridy = 1;
        constraints.insets = new Insets(18, 0, 0, 0);
        page.add(createDashboardMetricRow(), constraints);

        constraints.gridy = 2;
        constraints.insets = new Insets(18, 0, 0, 0);
        page.add(createPrimarySecondaryRow(createWeeklySpendingCard(), createCategoryOverviewCard(), 380), constraints);

        constraints.gridy = 3;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        page.add(Box.createVerticalGlue(), constraints);
        return page;
    }

    private JPanel createDashboardMetricRow() {
        ResponsiveGridPanel panel = new ResponsiveGridPanel(220, 16);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(createDashboardMetricCard("INCOME TRACKED", "Total Income", dashboardIncomeValueLabel,
                dashboardIncomeBodyLabel, SURFACE, SURFACE_BORDER, false));
        panel.add(createDashboardMetricCard("OUTGOING CASH", "Spent So Far", dashboardExpenseValueLabel,
                dashboardExpenseBodyLabel, SURFACE, SURFACE_BORDER, false));
        panel.add(createDashboardMetricCard("AVAILABLE NOW", "Left To Spend", dashboardRemainingValueLabel,
                dashboardRemainingBodyLabel, SURFACE, SURFACE_BORDER, false));
        panel.add(createDashboardMetricCard("MONTH-END VIEW", "Projected Left", dashboardForecastValueLabel,
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

        if (sectionTitle != null && description != null) {
            JLabel section = new JLabel(sectionTitle);
            section.setFont(new Font(FONT_FAMILY, Font.BOLD, 34));
            section.setForeground(TEXT_PRIMARY);
            section.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel body = new JLabel("<html>" + description + "</html>");
            body.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
            body.setForeground(TEXT_SECONDARY);
            body.setAlignmentX(Component.LEFT_ALIGNMENT);

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
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 46));
        valueLabel.setForeground(filled ? Color.WHITE : TEXT_PRIMARY);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        bodyLabel.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        bodyLabel.setForeground(filled ? FILLED_BODY_TEXT : TEXT_SECONDARY);
        bodyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyLabel.setVerticalAlignment(SwingConstants.TOP);

        JPanel stack = new JPanel();
        stack.setOpaque(false);
        stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));
        stack.setAlignmentX(Component.LEFT_ALIGNMENT);
        stack.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        stack.setBorder(new EmptyBorder(2, 0, 0, 0));
        stack.add(tag);
        stack.add(Box.createVerticalStrut(14));
        stack.add(title);
        stack.add(Box.createVerticalStrut(10));
        stack.add(valueLabel);
        stack.add(Box.createVerticalStrut(18));
        stack.add(bodyLabel);
        stack.add(Box.createVerticalGlue());

        panel.add(stack, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(0, DASHBOARD_METRIC_CARD_MIN_HEIGHT));
        return panel;
    }

    private JPanel createWeeklySpendingCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16), SURFACE, SURFACE_BORDER, 34);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = createCardHeader("Weekly Spending", "See how your spending moves through the week.",
                weeklySpendingBadgeLabel);
        weeklySpendingContentPanel.setOpaque(false);

        panel.add(header, BorderLayout.NORTH);
        panel.add(weeklySpendingContentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCategoryOverviewCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16), SURFACE, SURFACE_BORDER, 34);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = createCardHeader("By Category", "Your biggest spending groups at a glance.",
                categoryOverviewBadgeLabel);
        categoryOverviewContentPanel.setOpaque(false);

        panel.add(header, BorderLayout.NORTH);
        panel.add(categoryOverviewContentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDashboardNoticeStack() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        panel.add(createNoticeCard(dashboardBudgetNoticeLabel));
        panel.add(Box.createVerticalStrut(14));
        panel.add(createNoticeCard(dashboardInsightNoticeLabel));
        return panel;
    }

    private JPanel createNoticeCard(JLabel label) {
        SurfacePanel panel = createSurface(new BorderLayout(), SURFACE, SURFACE_BORDER, 18);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        panel.add(label, BorderLayout.CENTER);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        return panel;
    }

    private JPanel createIncomeRecordsCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16), SURFACE, SURFACE_BORDER, 34);
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
        stack.setAlignmentX(Component.LEFT_ALIGNMENT);
        stack.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

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

        JTextArea footnote = createWrappedTextArea(
                "Add your first income entry to start building a clearer picture.",
                new Font(FONT_FAMILY, Font.PLAIN, 13),
                TEXT_SECONDARY);
        stack.add(createColumnBlock(footnote));
        stack.add(Box.createVerticalGlue());

        panel.add(stack, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createExpenseRecordsCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16), SURFACE, SURFACE_BORDER, 34);
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
        stack.setAlignmentX(Component.LEFT_ALIGNMENT);
        stack.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

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
        expenseSummaryNoteLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        expenseSummaryNoteLabel.setHorizontalAlignment(SwingConstants.LEFT);
        expenseSummaryNoteLabel.setVerticalAlignment(SwingConstants.TOP);
        expenseSummaryNoteLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        stack.add(createColumnBlock(expenseSummaryNoteLabel));
        stack.add(Box.createVerticalStrut(10));

        JTextArea body = createWrappedTextArea(
                "Once you add expenses, each category total will appear here.",
                new Font(FONT_FAMILY, Font.PLAIN, 13),
                TEXT_SECONDARY);
        stack.add(createColumnBlock(body));
        stack.add(Box.createVerticalStrut(18));

        JTextArea extra = createWrappedTextArea(
                "Add an expense to see your category totals build up here.",
                new Font(FONT_FAMILY, Font.PLAIN, 13),
                TEXT_SECONDARY);
        stack.add(createColumnBlock(extra));
        stack.add(Box.createVerticalGlue());

        panel.add(stack, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBudgetProgressCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16), SURFACE, SURFACE_BORDER, 34);
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
        stack.setAlignmentX(Component.LEFT_ALIGNMENT);
        stack.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

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

        JTextArea note = createWrappedTextArea(
                "Add your first category budget to start tracking spending progress.",
                new Font(FONT_FAMILY, Font.PLAIN, 13),
                TEXT_SECONDARY);
        stack.add(createColumnBlock(note));
        stack.add(Box.createVerticalStrut(14));

        JPanel divider = new JPanel();
        divider.setOpaque(true);
        divider.setBackground(CARD_DIVIDER);
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setPreferredSize(new Dimension(0, 1));
        stack.add(divider);
        stack.add(Box.createVerticalStrut(14));

        JTextArea extra = createWrappedTextArea(
                "Budget progress and snapshot will compare here once you add categories.",
                new Font(FONT_FAMILY, Font.PLAIN, 13),
                TEXT_SECONDARY);
        stack.add(createColumnBlock(extra));
        stack.add(Box.createVerticalGlue());

        panel.add(stack, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSavingGoalProgressCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16), SURFACE_TINT, CARD_TINT_BORDER, 24);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new BorderLayout(12, 0));
        top.setOpaque(false);
        top.add(createCardTitle("Goal Progress", "Set a target amount to start tracking progress."), BorderLayout.CENTER);
        top.add(savingsStatusBadgeLabel, BorderLayout.EAST);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        savingsGoalTitleLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        savingsGoalTitleLabel.setForeground(TEXT_PRIMARY);
        savingsGoalTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        savingsGoalTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        savingsGoalTitleLabel.setVerticalAlignment(SwingConstants.TOP);
        savingsGoalTitleLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        savingsGoalBodyLabel.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        savingsGoalBodyLabel.setForeground(TEXT_SECONDARY);
        savingsGoalBodyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        savingsGoalBodyLabel.setHorizontalAlignment(SwingConstants.LEFT);
        savingsGoalBodyLabel.setVerticalAlignment(SwingConstants.TOP);
        savingsGoalBodyLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        styleProgressBar(savingsGoalProgressBar);

        JPanel percentWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        percentWrap.setOpaque(false);
        percentWrap.add(savingsPercentBadgeLabel);

        content.add(createColumnBlock(savingsGoalTitleLabel));
        content.add(Box.createVerticalStrut(10));
        content.add(createColumnBlock(savingsGoalBodyLabel));
        content.add(Box.createVerticalStrut(10));
        content.add(percentWrap);
        content.add(Box.createVerticalStrut(10));
        content.add(savingsGoalProgressBar);
        content.add(Box.createVerticalStrut(10));

        JTextArea footnote = createWrappedTextArea(
                "Set a target amount to start tracking progress.",
                new Font(FONT_FAMILY, Font.PLAIN, 13),
                TEXT_PRIMARY);
        footnote.setForeground(TEXT_PRIMARY);
        content.add(createColumnBlock(footnote));
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
        stack.setAlignmentX(Component.LEFT_ALIGNMENT);
        stack.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

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

        JTextArea note = createWrappedTextArea(
                "Set your goal to unlock a clearer savings snapshot.",
                new Font(FONT_FAMILY, Font.PLAIN, 13),
                TEXT_SECONDARY);
        stack.add(createColumnBlock(note));
        stack.add(Box.createVerticalGlue());

        panel.add(stack, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSavingHistoryCard() {
        SurfacePanel panel = createSurface(new BorderLayout(0, 16), SURFACE, SURFACE_BORDER, 34);
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
        SurfacePanel panel = createSurface(new BorderLayout(0, 16), SURFACE, SURFACE_BORDER, 34);
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

        header.add(createCardTitle(titleText, bodyText), BorderLayout.CENTER);
        if (badgeLabel != null) {
            header.add(badgeLabel, BorderLayout.EAST);
        }
        return header;
    }

    private JPanel createCardTitle(String titleText, String bodyText) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 22));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setHorizontalAlignment(SwingConstants.LEFT);
        title.setVerticalAlignment(SwingConstants.TOP);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JTextArea body = createWrappedTextArea(bodyText, new Font(FONT_FAMILY, Font.PLAIN, 13), TEXT_SECONDARY);

        panel.add(createColumnBlock(title));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createColumnBlock(body));
        return panel;
    }

    private JPanel createValueRow(String titleText, JLabel valueLabel, boolean highlighted, Color highlightColor) {
        Color fillColor = highlighted ? highlightColor : SURFACE;
        Color borderColor = highlighted ? highlightColor.darker() : CATEGORY_DEFAULT_BORDER;
        Color textColor = highlighted ? Color.WHITE : TEAL_DARK;

        SurfacePanel row = createSurface(new BorderLayout(), fillColor, borderColor, 16);
        row.setBorder(new EmptyBorder(14, 14, 14, 14));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));

        JPanel stack = new JPanel();
        stack.setOpaque(false);
        stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));
        stack.setAlignmentX(Component.LEFT_ALIGNMENT);
        stack.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JTextArea title = createWrappedTextArea(titleText, new Font(FONT_FAMILY, Font.BOLD, 14), textColor);

        valueLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 17));
        valueLabel.setForeground(highlighted ? Color.WHITE : TEXT_PRIMARY);
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        valueLabel.setVerticalAlignment(SwingConstants.TOP);
        valueLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        stack.add(createColumnBlock(title));
        stack.add(Box.createVerticalStrut(6));
        stack.add(createColumnBlock(valueLabel));
        row.add(stack, BorderLayout.CENTER);
        return row;
    }

    private JPanel createMiniStatTile(String titleText, JLabel valueLabel) {
        SurfacePanel panel = createSurface(new BorderLayout(0, 10), SURFACE, CARD_CREAM_BORDER, 16);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JTextArea title = createWrappedTextArea(titleText, new Font(FONT_FAMILY, Font.PLAIN, 12), TEXT_SECONDARY);

        valueLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 20));
        valueLabel.setForeground(TEXT_PRIMARY);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(createColumnBlock(title), BorderLayout.NORTH);
        panel.add(createColumnBlock(valueLabel), BorderLayout.CENTER);
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
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(TEAL, 32, 1),
                new EmptyBorder(10, 24, 10, 24)));
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
        JComboBox<String> sourceBox = new JComboBox<String>(INCOME_SOURCE_OPTIONS);
        JTextField dateField = new JTextField(LocalDate.now().toString());

        sourceBox.setEditable(true);
        styleDialogTextField(amountField);
        styleDialogComboBox(sourceBox);
        styleDialogTextField(dateField);

        JPanel form = createDialogForm(new String[] { "Amount", "Source Category", "Date" },
                new Component[] { amountField, sourceBox, dateField });
        showStyledInputDialog("Add Income",
                "Record allowance, salary, or side income and choose its source category up front.",
                form,
                sourceBox,
                new Dimension(460, 380),
                dialog -> {
                    Double amount = parseAmount(amountField.getText());
                    String source = String.valueOf(sourceBox.getEditor().getItem()).trim();
                    String date = dateField.getText().trim();

                    if (amount == null || amount.doubleValue() <= 0) {
                        showValidationMessage(dialog, "Enter a valid income amount greater than 0.");
                        return;
                    }
                    if (source.isEmpty()) {
                        showValidationMessage(dialog, "Choose or type an income source before saving.");
                        return;
                    }
                    if (!isValidDate(date)) {
                        showValidationMessage(dialog, "Use date format YYYY-MM-DD.");
                        return;
                    }

                    addIncomeEntry(new IncomeEntry(amount.doubleValue(), source, date));
                    dialog.dispose();
                    showPage(PAGE_INCOME);
                });
    }

    private void showExpenseDialog() {
        JComboBox<String> categoryBox = new JComboBox<String>(BUDGET_CATEGORY_OPTIONS);
        PromptTextField itemField = new PromptTextField("Type any item");
        JTextField amountField = new JTextField();
        JTextField dateField = new JTextField(LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        styleDialogComboBox(categoryBox);
        styleDialogTextField(itemField);
        styleDialogTextField(amountField);
        styleDialogTextField(dateField);

        JPanel form = createDialogForm(new String[] { "Category", "Item", "Amount", "Date" },
                new Component[] { categoryBox, itemField, amountField, dateField });
        showStyledInputDialog("Add Expense",
                "Track spending with a clean, lighter form that matches the budget dialog.",
                form,
                categoryBox,
                new Dimension(520, 430),
                dialog -> {
                    Double amount = parseAmount(amountField.getText());
                    String category = String.valueOf(categoryBox.getSelectedItem()).trim();
                    String item = itemField.getText().trim();
                    String date = normalizeExpenseDate(dateField.getText().trim());

                    if (category.isEmpty()) {
                        showValidationMessage(dialog, "Choose a category before saving.");
                        return;
                    }
                    if (item.isEmpty()) {
                        showValidationMessage(dialog, "Enter an item or short description.");
                        return;
                    }
                    if (amount == null || amount.doubleValue() <= 0) {
                        showValidationMessage(dialog, "Enter a valid expense amount greater than 0.");
                        return;
                    }
                    if (date == null) {
                        showValidationMessage(dialog, "Use date format MM/DD/YYYY or YYYY-MM-DD.");
                        return;
                    }

                    addExpenseEntry(new ExpenseEntry(amount.doubleValue(), category, item, date));
                    dialog.dispose();
                    showPage(PAGE_EXPENSES);
                });
    }
    private String normalizeExpenseDate(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(trimmed).toString();
        } catch (Exception ignored) {
            try {
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy");
                return LocalDate.parse(trimmed, formatter).toString();
            } catch (Exception ignoredToo) {
                return null;
            }
        }
    }

    private void showBudgetDialog() {
        JComboBox<String> categoryBox = new JComboBox<String>(BUDGET_CATEGORY_OPTIONS);
        JTextField amountField = new JTextField();
        styleDialogComboBox(categoryBox);
        styleDialogTextField(amountField);

        String initialCategory = BUDGET_CATEGORY_OPTIONS[0];
        Double existingAmount = categoryBudgetLimits.get(initialCategory);
        if (existingAmount != null) {
            amountField.setText(String.valueOf(existingAmount.doubleValue()));
        }
        categoryBox.addActionListener(event -> {
            String selectedCategory = String.valueOf(categoryBox.getSelectedItem());
            Double categoryAmount = categoryBudgetLimits.get(selectedCategory);
            amountField.setText(categoryAmount == null ? "" : String.valueOf(categoryAmount.doubleValue()));
        });

        JPanel form = createDialogForm(new String[] { "Category", "Budget Amount" }, new Component[] { categoryBox, amountField });
        showStyledInputDialog("Set Budget",
                "Choose the category you want to budget for and set its limit.",
                form,
                amountField,
                new Dimension(460, 340),
                dialog -> {
                    Double amount = parseAmount(amountField.getText());
                    if (amount == null || amount.doubleValue() <= 0) {
                        showValidationMessage(dialog, "Enter a valid budget amount greater than 0.");
                        return;
                    }

                    String category = String.valueOf(categoryBox.getSelectedItem());
                    categoryBudgetLimits.put(category, amount.doubleValue());
                    recalculateBudgetLimit();
                    handleFinancialDataChanged();
                    dialog.dispose();
                    showPage(PAGE_BUDGET);
                });
    }

    private void showSavingGoalDialog() {
        JTextField amountField = new JTextField(savingGoalTarget > 0 ? String.valueOf(savingGoalTarget) : "");
        styleDialogTextField(amountField);

        JPanel form = createDialogForm(new String[] { "Target Amount" }, new Component[] { amountField });
        showStyledInputDialog("Set Saving Goal",
                "Define the target amount you want to build toward.",
                form,
                amountField,
                new Dimension(440, 300),
                dialog -> {
                    Double amount = parseAmount(amountField.getText());
                    if (amount == null || amount.doubleValue() <= 0) {
                        showValidationMessage(dialog, "Enter a valid goal amount greater than 0.");
                        return;
                    }

                    savingGoalTarget = amount.doubleValue();
                    handleFinancialDataChanged();
                    dialog.dispose();
                    showPage(PAGE_SAVING_GOAL);
                });
    }

    private void showDailySavingsDialog() {
        JComboBox<String> categoryBox = new JComboBox<String>(SAVINGS_CATEGORY_OPTIONS);
        JTextField amountField = new JTextField();
        JTextField dateField = new JTextField(LocalDate.now().toString());

        categoryBox.setEditable(true);
        styleDialogComboBox(categoryBox);
        styleDialogTextField(amountField);
        styleDialogTextField(dateField);

        JPanel form = createDialogForm(new String[] { "Category", "Amount", "Date" },
                new Component[] { categoryBox, amountField, dateField });
        showStyledInputDialog("Add Daily Savings",
                "Log a savings deposit, choose its category, and keep your goal progress current.",
                form,
                categoryBox,
                new Dimension(460, 380),
                dialog -> {
                    Double amount = parseAmount(amountField.getText());
                    String category = String.valueOf(categoryBox.getEditor().getItem()).trim();
                    String date = dateField.getText().trim();

                    if (category.isEmpty()) {
                        showValidationMessage(dialog, "Choose or type a savings category before saving.");
                        return;
                    }
                    if (amount == null || amount.doubleValue() <= 0) {
                        showValidationMessage(dialog, "Enter a valid savings amount greater than 0.");
                        return;
                    }
                    if (!isValidDate(date)) {
                        showValidationMessage(dialog, "Use date format YYYY-MM-DD.");
                        return;
                    }

                    savingEntries.add(new SavingEntry(amount.doubleValue(), category, date));
                    handleFinancialDataChanged();
                    dialog.dispose();
                    showPage(PAGE_SAVING_GOAL);
                });
    }

    private JPanel createDialogForm(String[] labels, Component[] components) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (int index = 0; index < labels.length; index++) {
            if (index > 0) {
                panel.add(Box.createVerticalStrut(12));
            }

            JLabel label = createDialogFieldLabel(labels[index]);
            panel.add(label);
            panel.add(Box.createVerticalStrut(6));

            Component component = components[index];
            if (component instanceof JComponent) {
                ((JComponent) component).setAlignmentX(Component.LEFT_ALIGNMENT);
            }
            panel.add(component);
        }
        return panel;
    }

    private void showStyledInputDialog(String title, String subtitle, JComponent formContent, Component focusTarget,
            Dimension minimumSize, Consumer<JDialog> onConfirm) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BACKGROUND_SOFT);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        SurfacePanel card = createSurface(new BorderLayout(0, 18), SURFACE, SURFACE_BORDER, 22);
        card.setBorder(new EmptyBorder(18, 18, 16, 18));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel badge = createBadgeLabel("FINANCIAL FORM", SURFACE_BLUE, TEAL_DARK);
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 22));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("<html><body style='width: 340px'>" + escapeHtml(subtitle) + "</body></html>");
        subtitleLabel.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(badge);
        header.add(Box.createVerticalStrut(12));
        header.add(titleLabel);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitleLabel);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        formContent.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(formContent);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        JButton cancelButton = createDialogSecondaryButton("Cancel");
        cancelButton.addActionListener(event -> dialog.dispose());

        JButton confirmButton = createDialogPrimaryButton("OK");
        confirmButton.addActionListener(event -> onConfirm.accept(dialog));

        actions.add(cancelButton);
        actions.add(confirmButton);

        card.add(header, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);
        root.add(card, BorderLayout.CENTER);

        dialog.setContentPane(root);
        dialog.getRootPane().setDefaultButton(confirmButton);
        bindEscapeKey(dialog, cancelButton);
        dialog.pack();

        Dimension preferredSize = dialog.getSize();
        dialog.setSize(Math.max(minimumSize.width, preferredSize.width), Math.max(minimumSize.height, preferredSize.height));
        dialog.setLocationRelativeTo(this);
        if (focusTarget != null) {
            SwingUtilities.invokeLater(() -> focusTarget.requestFocusInWindow());
        }
        dialog.setVisible(true);
    }

    private JLabel createDialogFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        label.setForeground(TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleDialogTextField(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setPreferredSize(new Dimension(0, 42));
        field.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
        field.setBackground(SURFACE);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEAL);
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(SURFACE_BORDER, 18, 1),
                new EmptyBorder(8, 12, 8, 12)));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleDialogComboBox(JComboBox<String> comboBox) {
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        comboBox.setPreferredSize(new Dimension(300, 42));
        comboBox.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
        comboBox.setBackground(SURFACE);
        comboBox.setForeground(TEXT_PRIMARY);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(SURFACE_BORDER, 18, 1),
                new EmptyBorder(2, 10, 2, 10)));
        comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (comboBox.isEditable()) {
            Component editorComponent = comboBox.getEditor().getEditorComponent();
            if (editorComponent instanceof JTextField) {
                JTextField editor = (JTextField) editorComponent;
                editor.setBorder(new EmptyBorder(0, 0, 0, 0));
                editor.setBackground(SURFACE);
                editor.setForeground(TEXT_PRIMARY);
                editor.setCaretColor(TEAL);
                editor.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
            }
        }
    }

    private JButton createDialogPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(TEAL);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(TEAL_DARK, 28, 1),
                new EmptyBorder(9, 16, 9, 16)));
        button.setPreferredSize(new Dimension(116, 40));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                button.setBackground(new Color(102, 176, 124));
            }

            @Override
            public void mouseExited(MouseEvent event) {
                button.setBackground(TEAL);
            }
        });
        return button;
    }

    private JButton createDialogSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        button.setForeground(TEXT_PRIMARY);
        button.setBackground(PAGE_BACKGROUND_SOFT);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(SURFACE_BORDER, 28, 1),
                new EmptyBorder(9, 16, 9, 16)));
        button.setPreferredSize(new Dimension(116, 40));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                button.setBackground(new Color(247, 243, 233));
            }

            @Override
            public void mouseExited(MouseEvent event) {
                button.setBackground(PAGE_BACKGROUND_SOFT);
            }
        });
        return button;
    }

    private void bindEscapeKey(JDialog dialog, JButton cancelButton) {
        dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "dismiss-dialog");
        dialog.getRootPane().getActionMap().put("dismiss-dialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                cancelButton.doClick();
            }
        });
    }
    private void showValidationMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Validation", JOptionPane.WARNING_MESSAGE);
    }

    private void addIncomeEntry(IncomeEntry entry) {
        incomeEntries.add(entry);
        handleFinancialDataChanged();
    }

    private void addExpenseEntry(ExpenseEntry entry) {
        String budgetAlertMessage = buildExpenseBudgetAlertMessage(entry);
        expenseEntries.add(entry);
        if (budgetAlertMessage != null) {
            addBudgetAlert(budgetAlertMessage);
        }
        handleFinancialDataChanged();
        if (budgetAlertMessage != null) {
            JOptionPane.showMessageDialog(this, buildBudgetAlertHtml(budgetAlertMessage), "Budget Alert", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadStoredData() {
        AppDatabase.DatabaseState state = AppDatabase.loadForUser(accountEmail);
        incomeEntries.clear();
        incomeEntries.addAll(state.incomeEntries);
        expenseEntries.clear();
        expenseEntries.addAll(state.expenseEntries);
        savingEntries.clear();
        savingEntries.addAll(state.savingEntries);
        categoryBudgetLimits.clear();
        categoryBudgetLimits.putAll(state.categoryBudgetLimits);
        budgetLimit = state.budgetLimit;
        if (!categoryBudgetLimits.isEmpty()) {
            budgetLimit = calculateBudgetTotal();
        }
        savingGoalTarget = state.savingGoalTarget;
    }

    private void persistData() {
        AppDatabase.DatabaseState state = new AppDatabase.DatabaseState();
        state.incomeEntries.addAll(incomeEntries);
        state.expenseEntries.addAll(expenseEntries);
        state.savingEntries.addAll(savingEntries);
        state.categoryBudgetLimits.putAll(categoryBudgetLimits);
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
        refreshNotificationBar();
    }

    private void refreshDashboardSection() {
        double totalIncome = calculateTotalIncome();
        double totalExpenses = calculateTotalExpenses();
        double remainingBalance = totalIncome - totalExpenses;
        double projectedRemaining = remainingBalance - predictFutureExpenses(3);

        double latestWeekTotal = calculateLatestWeekSpending();
        LinkedHashMap<String, ArrayList<ExpenseEntry>> groupedExpenses = buildExpenseGroups();
        String topCategory = findTopExpenseCategory();
        double topCategoryTotal = "-".equals(topCategory) ? 0.0 : calculateExpenseTotalForCategory(topCategory);
        double topCategoryShare = totalExpenses <= 0.0 ? 0.0 : (topCategoryTotal / totalExpenses) * 100.0;

        dashboardIncomeValueLabel.setText(currencyFormat.format(totalIncome));
        dashboardIncomeBodyLabel.setText(toWrappedHtml(
                incomeEntries.isEmpty()
                        ? "Add allowance or income entries to start your snapshot."
                        : "Everything you have added as allowance or income.",
                200));
        dashboardExpenseValueLabel.setText(currencyFormat.format(totalExpenses));
        dashboardExpenseBodyLabel.setText(toWrappedHtml(
                expenseEntries.isEmpty()
                        ? "Log your expenses to reveal category and pace insights."
                        : "All recorded expenses across your categories.",
                200));
        dashboardRemainingValueLabel.setText(currencyFormat.format(remainingBalance));
        dashboardRemainingValueLabel.setForeground(remainingBalance >= 0.0 ? TEXT_PRIMARY : RED);
        dashboardRemainingBodyLabel.setText(toWrappedHtml(
                remainingBalance >= 0.0
                        ? "What is left after your recorded spending."
                        : "You have spent more than the income tracked so far.",
                200));
        dashboardRemainingBodyLabel.setForeground(remainingBalance >= 0.0 ? TEXT_SECONDARY : RED);
        dashboardForecastValueLabel.setText(currencyFormat.format(projectedRemaining));
        dashboardForecastBodyLabel.setText(toWrappedHtml(
                projectedRemaining >= 0.0
                        ? "Estimated left if you keep spending at the same pace."
                        : "Current pace may push your balance below zero this month.",
                200));

        styleBadgeLabel(weeklySpendingBadgeLabel,
                expenseEntries.isEmpty() ? "Waiting for data" : buildActiveDaysLabel(countActiveDays(buildLatestWeekSpendingValues())),
                SURFACE_TINT, TEAL_DARK);
        weeklySpendingContentPanel.removeAll();
        weeklySpendingContentPanel.add(createWeeklySummaryPanel(), BorderLayout.CENTER);

        styleBadgeLabel(categoryOverviewBadgeLabel,
                groupedExpenses.isEmpty() ? "No categories" : "Top share " + formatPercent(topCategoryShare),
                SURFACE_TINT, TEAL_DARK);
        categoryOverviewContentPanel.removeAll();
        if (expenseEntries.isEmpty()) {
            categoryOverviewContentPanel.add(createDonutEmptyPanel("No category data yet"), BorderLayout.CENTER);
        } else {
            categoryOverviewContentPanel.add(createCategoryListPanel(groupedExpenses), BorderLayout.CENTER);
        }

        weeklySpendingContentPanel.revalidate();
        weeklySpendingContentPanel.repaint();
        weeklyTrendPanel.repaint();
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
            expenseTableModel.addRow(new Object[] { entry.category, entry.item, entry.date, currencyFormat.format(entry.amount) });
        }

        expenseRecordsContentPanel.removeAll();
        if (expenseEntries.isEmpty()) {
            expenseRecordsContentPanel.add(createEmptyTableState(new String[] { "Category", "Item", "Date", "Amount" },
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
        double totalBudget = budgetLimit;
        double remainingBudget = totalBudget - totalExpenses;
        int budgetCategories = categoryBudgetLimits.size();
        double usedPercent = totalBudget > 0 ? Math.min(100.0, (totalExpenses / totalBudget) * 100.0) : 0.0;

        styleBadgeLabel(budgetCategoryBadgeLabel, budgetCategories > 0 ? budgetCategories + " categories" : "No categories",
                SURFACE_TINT, TEAL_DARK);
        budgetTotalBudgetedValueLabel.setText(currencyFormat.format(totalBudget));
        budgetTotalSpentValueLabel.setText(currencyFormat.format(totalExpenses));
        budgetRemainingValueLabel.setText(currencyFormat.format(Math.max(remainingBudget, 0.0)));
        budgetOverallUsedValueLabel.setText(String.format("%.0f%%", usedPercent));
        budgetClosestValueLabel.setText(totalBudget > 0 ? findMostPressuredBudgetCategory() : "-");

        budgetProgressContentPanel.removeAll();
        if (budgetCategories == 0) {
            if (totalBudget <= 0) {
                budgetProgressContentPanel.add(createLargeEmptyState("No budget categories yet",
                        "Add a budget category to start tracking your limits."));
            } else {
                budgetProgressContentPanel.add(createBudgetProgressRow("Overall budget", totalExpenses, totalBudget));
                budgetProgressContentPanel.add(Box.createVerticalStrut(12));
                budgetProgressContentPanel.add(createMutedNote(
                        "Add category budgets to compare each spending group against its own limit."));
            }
        } else {
            budgetProgressContentPanel.add(createBudgetProgressRow("Overall budget", totalExpenses, totalBudget));
            budgetProgressContentPanel.add(Box.createVerticalStrut(14));
            for (Map.Entry<String, Double> entry : categoryBudgetLimits.entrySet()) {
                String category = entry.getKey();
                double limit = entry.getValue() == null ? 0.0 : entry.getValue().doubleValue();
                budgetProgressContentPanel.add(createBudgetProgressRow(category,
                        calculateExpenseTotalForCategory(category), limit));
                budgetProgressContentPanel.add(Box.createVerticalStrut(12));
            }
            budgetProgressContentPanel.add(createMutedNote(
                    "Each category compares your recorded spending against the budget you set."));
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
        savingsGoalTitleLabel.setText(toWrappedHtml(
                savingGoalTarget > 0 ? "Saving goal in progress" : "No saving goal yet",
                240));
        savingsGoalBodyLabel.setText(toWrappedHtml(
                savingGoalTarget > 0
                        ? "You have saved " + currencyFormat.format(totalSaved) + " out of your target."
                        : "Set a target amount to start tracking progress.",
                240));
        savingsGoalProgressBar.setValue(progress);

        savingsTargetValueLabel.setText(currencyFormat.format(savingGoalTarget));
        savingsSavedValueLabel.setText(currencyFormat.format(totalSaved));
        savingsRemainingValueLabel.setText(currencyFormat.format(remaining));
        savingsEntriesValueLabel.setText(String.valueOf(savingEntries.size()));

        savingsHistoryContentPanel.removeAll();
        savingsHistoryPaginationPanel.removeAll();
        if (savingEntries.isEmpty()) {
            savingsHistoryContentPanel.add(createEmptyTableState(new String[] { "Category", "Date", "Amount" },
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
                savingsTableModel.addRow(new Object[] { entry.category, entry.date, currencyFormat.format(entry.amount) });
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
                new EmptyBorder(8, 16, 8, 16)));

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
        String riskLabel = describeRisk(overspendingChance);

        styleBadgeLabel(forecastRiskBadgeLabel, riskLabel, SURFACE_TINT, resolveRiskBadgeTextColor(riskLabel));
        forecastEstimatedRemainingValueLabel.setText(currencyFormat.format(estimatedRemaining));
        forecastHighRiskCategoryValueLabel.setText(findTopExpenseCategory());
        forecastProjectedDailyValueLabel.setText(currencyFormat.format(projectedDailySpend));
        forecastOverspendingPercentLabel.setText(String.format("%.0f%%", overspendingChance));
        forecastRiskProgressBar.setValue((int) Math.round(overspendingChance));

        forecastBreakdownContentPanel.removeAll();
        if (expenseEntries.isEmpty()) {
            forecastBreakdownContentPanel.add(createForecastBreakdownEmptyPanel(), BorderLayout.CENTER);
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
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        double[] weeklyValues = buildLatestWeekSpendingValues();
        double latestWeekTotal = sumValues(weeklyValues);
        double busiestDayAmount = findHighestValue(weeklyValues);
        int activeDays = countActiveDays(weeklyValues);

        weeklyTotalValueLabel.setText(currencyFormat.format(latestWeekTotal));
        weeklyAverageDayValueLabel.setText(currencyFormat.format(calculateAverageActiveDaySpend(weeklyValues)));
        weeklyBusyDayValueLabel.setText(findBusiestSpendingDay(weeklyValues));
        weeklyActiveDaysValueLabel.setText(activeDays + "/7");

        JLabel title = new JLabel(expenseEntries.isEmpty()
                ? "Your weekly pattern will appear here once you start logging expenses."
                : "Latest recorded week: " + currencyFormat.format(latestWeekTotal));
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel body = new JLabel(expenseEntries.isEmpty()
                ? "Add dated expenses to reveal which days drain your baon fastest."
                : "You spent on " + buildActiveDaysLabel(activeDays).toLowerCase() + ", and "
                        + findBusiestSpendingDay(weeklyValues) + " was the busiest day.");
        body.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        body.setForeground(TEXT_SECONDARY);
        body.setAlignmentX(Component.LEFT_ALIGNMENT);

        ResponsiveGridPanel stats = new ResponsiveGridPanel(140, 10);
        stats.add(createMiniStatTile("Week Total", weeklyTotalValueLabel));
        stats.add(createMiniStatTile("Active-Day Avg", weeklyAverageDayValueLabel));
        stats.add(createMiniStatTile("Busiest Day", weeklyBusyDayValueLabel));
        stats.add(createMiniStatTile("Days Used", weeklyActiveDaysValueLabel));

        weeklyTrendPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        weeklyTrendPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 210));

        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        panel.add(body);
        panel.add(Box.createVerticalStrut(14));
        panel.add(stats);
        panel.add(Box.createVerticalStrut(14));
        panel.add(weeklyTrendPanel);
        panel.add(Box.createVerticalStrut(12));
        panel.add(createHintStrip(expenseEntries.isEmpty()
                ? "The chart will use your latest recorded week once you have expense entries."
                : "Highest single day this week: " + currencyFormat.format(busiestDayAmount) + "."));
        if (expenseEntries.isEmpty()) {
            panel.add(Box.createVerticalStrut(12));
            panel.add(createMutedNote("A few entries across different days will make this trend much more useful."));
        } else if (activeDays <= 2) {
            panel.add(Box.createVerticalStrut(12));
            panel.add(createMutedNote("Only a couple of spending days are logged in the latest week, so this pattern may still shift quickly."));
        }
        return panel;
    }

    private JPanel createCategoryListPanel(LinkedHashMap<String, ArrayList<ExpenseEntry>> groupedExpenses) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        int wrapWidth = computeCategoryWrapWidth();
        if (groupedExpenses.isEmpty()) {
            JLabel label = new JLabel("No category data yet", SwingConstants.CENTER);
            label.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
            label.setForeground(TEXT_SECONDARY);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
            return panel;
        }

        double totalSpent = calculateTotalExpenses();
        Map.Entry<String, ArrayList<ExpenseEntry>> topEntry = groupedExpenses.entrySet().iterator().next();
        double topAmount = calculateExpenseGroupTotal(topEntry.getValue());
        double topShare = totalSpent <= 0.0 ? 0.0 : (topAmount / totalSpent) * 100.0;

        panel.add(createCategoryDonutSummary(topEntry.getKey(), topAmount, topShare));
        panel.add(Box.createVerticalStrut(8));

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setAlignmentX(Component.LEFT_ALIGNMENT);

        int shown = 0;
        for (Map.Entry<String, ArrayList<ExpenseEntry>> entry : groupedExpenses.entrySet()) {
            list.add(createCategoryRow(entry.getKey(), calculateExpenseGroupTotal(entry.getValue()), totalSpent,
                    categoryBudgetLimits.get(entry.getKey()), wrapWidth));
            list.add(Box.createVerticalStrut(8));
            shown++;
            if (shown >= 4) {
                break;
            }
        }

        panel.add(list);
        if (groupedExpenses.size() > shown) {
            panel.add(Box.createVerticalStrut(4));
            panel.add(createMutedNote("Showing the biggest categories first so the overview stays focused."));
        }
        return panel;
    }

    private JPanel createCategoryRow(String titleText, double amount, double totalSpent, Double categoryBudgetLimit, int wrapWidth) {
        SurfacePanel row = createSurface(new BorderLayout(0, 10), SURFACE, CATEGORY_DEFAULT_BORDER, 26);
        row.setBorder(new EmptyBorder(11, 14, 11, 14));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setAlignmentX(Component.LEFT_ALIGNMENT);

        int titleWrapWidth = Math.max(140, wrapWidth - 110);
        JLabel title = new JLabel(toWrappedHtml(titleText, titleWrapWidth));
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel value = new JLabel(currencyFormat.format(amount));
        value.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        value.setForeground(TEXT_PRIMARY);
        value.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel detail = new JLabel(toWrappedHtml(buildCategoryRowDetail(amount, totalSpent, categoryBudgetLimit), wrapWidth));
        detail.setFont(new Font(FONT_FAMILY, Font.PLAIN, 12));
        detail.setForeground(TEXT_SECONDARY);
        detail.setVerticalAlignment(SwingConstants.TOP);

        JProgressBar progressBar = new JProgressBar(0, 100);
        styleProgressBar(progressBar);
        progressBar.setValue((int) Math.min(100, Math.round(totalSpent <= 0.0 ? 0.0 : (amount / totalSpent) * 100.0)));
        progressBar.setForeground(resolveCategoryProgressColor(amount, totalSpent, categoryBudgetLimit));

        top.add(title);
        top.add(Box.createVerticalStrut(4));
        top.add(value);
        row.add(top, BorderLayout.NORTH);
        row.add(detail, BorderLayout.CENTER);
        row.add(progressBar, BorderLayout.SOUTH);
        return row;
    }

    private JPanel createCategoryDonutSummary(String topCategory, double topAmount, double topShare) {
        SurfacePanel panel = createSurface(new BorderLayout(14, 0), PAGE_BACKGROUND_SOFT, CARD_CREAM_BORDER, 26);
        panel.setBorder(new EmptyBorder(12, 14, 12, 14));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        DonutPlaceholderPanel donut = new DonutPlaceholderPanel(formatPercent(topShare), SURFACE, DONUT_OUTER, DONUT_RING, TEXT_SECONDARY);
        donut.setPreferredSize(new Dimension(142, 142));
        donut.setMinimumSize(new Dimension(142, 142));
        donut.setMaximumSize(new Dimension(142, 142));

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Top category share");
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        title.setForeground(TEXT_PRIMARY);

        JLabel category = new JLabel(toWrappedHtml(topCategory, 170));
        category.setFont(new Font(FONT_FAMILY, Font.BOLD, 20));
        category.setForeground(TEAL_DARK);

        JLabel detail = new JLabel(toWrappedHtml(
                currencyFormat.format(topAmount) + " spent so far | " + formatPercent(topShare) + " of total expenses.",
                170));
        detail.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        detail.setForeground(TEXT_SECONDARY);

        body.add(Box.createVerticalGlue());
        body.add(title);
        body.add(Box.createVerticalStrut(6));
        body.add(category);
        body.add(Box.createVerticalStrut(6));
        body.add(detail);
        body.add(Box.createVerticalGlue());

        panel.add(donut, BorderLayout.WEST);
        panel.add(body, BorderLayout.CENTER);
        return panel;
    }
    private JPanel createHintStrip(String text) {
        SurfacePanel panel = createSurface(new BorderLayout(), PAGE_BACKGROUND_SOFT, SURFACE_BORDER, 16);
        panel.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        label.setForeground(TEXT_SECONDARY);
        panel.add(label, BorderLayout.CENTER);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private JPanel createForecastBreakdownEmptyPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel visualRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        visualRow.setOpaque(false);
        visualRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        DonutPlaceholderPanel donut = new DonutPlaceholderPanel("", SURFACE, DONUT_OUTER, DONUT_RING, TEXT_SECONDARY);
        donut.setPreferredSize(new Dimension(170, 170));
        donut.setMinimumSize(new Dimension(170, 170));
        donut.setMaximumSize(new Dimension(170, 170));

        SurfacePanel statusChip = createSurface(new BorderLayout(), SURFACE, CARD_GOLD_BORDER, 44);
        statusChip.setPreferredSize(new Dimension(92, 92));
        statusChip.setMinimumSize(new Dimension(92, 92));
        statusChip.setMaximumSize(new Dimension(92, 92));

        JLabel chipLabel = new JLabel("No data yet", SwingConstants.CENTER);
        chipLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        chipLabel.setForeground(TEXT_SECONDARY);
        statusChip.add(chipLabel, BorderLayout.CENTER);

        visualRow.add(donut);
        visualRow.add(statusChip);

        JLabel label = new JLabel("No category data yet");
        label.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(visualRow);
        panel.add(Box.createVerticalStrut(12));
        panel.add(label);
        return panel;
    }
    private JPanel createDonutEmptyPanel(String text) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel donutRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        donutRow.setOpaque(false);
        donutRow.add(new DonutPlaceholderPanel("No data\nyet", SURFACE, DONUT_OUTER, DONUT_RING, TEXT_SECONDARY));
        panel.add(donutRow);
        panel.add(Box.createVerticalStrut(14));

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        label.setForeground(TEXT_SECONDARY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        return panel;
    }

    private JPanel createLargeEmptyState(String titleText, String bodyText) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JTextArea title = createWrappedTextArea(titleText, new Font(FONT_FAMILY, Font.BOLD, 20), TEXT_PRIMARY);
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 20));

        JTextArea body = createWrappedTextArea(bodyText, new Font(FONT_FAMILY, Font.PLAIN, 13), TEXT_SECONDARY);

        panel.add(Box.createVerticalStrut(24));
        panel.add(createColumnBlock(title));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createColumnBlock(body));
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
        body.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JTextArea title = createWrappedTextArea(titleText, new Font(FONT_FAMILY, Font.BOLD, 18), TEXT_PRIMARY);

        JTextArea text = createWrappedTextArea(bodyText, new Font(FONT_FAMILY, Font.PLAIN, 13), TEXT_SECONDARY);

        body.add(createColumnBlock(title));
        body.add(Box.createVerticalStrut(10));
        body.add(createColumnBlock(text));
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
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setAlignmentX(Component.LEFT_ALIGNMENT);
        top.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JTextArea title = createWrappedTextArea(titleText, new Font(FONT_FAMILY, Font.BOLD, 15), TEXT_PRIMARY);

        JLabel value = new JLabel(currencyFormat.format(spent) + " / " + currencyFormat.format(limit));
        value.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        value.setForeground(TEXT_SECONDARY);
        value.setAlignmentX(Component.LEFT_ALIGNMENT);
        value.setHorizontalAlignment(SwingConstants.LEFT);
        value.setVerticalAlignment(SwingConstants.TOP);
        value.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JProgressBar progressBar = new JProgressBar(0, 100);
        styleProgressBar(progressBar);
        progressBar.setValue((int) Math.min(100, Math.round((spent / Math.max(limit, 1.0)) * 100.0)));

        panel.add(top);
        top.add(createColumnBlock(title));
        top.add(Box.createVerticalStrut(4));
        top.add(createColumnBlock(value));
        panel.add(Box.createVerticalStrut(8));
        panel.add(progressBar);
        return panel;
    }

    private JPanel createMutedNote(String text) {
        JTextArea label = createWrappedTextArea(text, new Font(FONT_FAMILY, Font.PLAIN, 13), TEXT_SECONDARY);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(createColumnBlock(label), BorderLayout.CENTER);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private JPanel createColumnBlock(Component component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private JTextArea createWrappedTextArea(String text, Font font, Color color) {
        JTextArea area = new JTextArea(text == null ? "" : text);
        area.setEditable(false);
        area.setFocusable(false);
        area.setOpaque(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(font);
        area.setForeground(color);
        area.setBorder(BorderFactory.createEmptyBorder());
        area.setMargin(new Insets(0, 0, 0, 0));
        area.setAlignmentX(Component.LEFT_ALIGNMENT);
        area.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
        area.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return area;
    }

    private JLabel createWrappedBodyLabel(String text, int width) {
        JLabel label = new JLabel(toWrappedHtml(text, width));
        label.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        label.setForeground(TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return label;
    }

    private void applySidebarButtonStyle(JButton button, boolean active) {
        button.setBackground(active ? SIDEBAR_BUTTON_ACTIVE : SIDEBAR_BUTTON);
        button.setForeground(SIDEBAR_TEXT);
        button.setFont(new Font(FONT_FAMILY, active ? Font.BOLD : Font.PLAIN, 16));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(active ? SIDEBAR_BUTTON_ACTIVE : SIDEBAR_BUTTON, 30, 1),
                new EmptyBorder(10, 18, 10, 18)));
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
        refreshContentHeader();
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
        return sumValues(buildLatestWeekSpendingValues());
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

    private Color resolveRiskBadgeTextColor(String riskLabel) {
        if ("High risk".equals(riskLabel)) {
            return RED;
        }
        if ("Low risk".equals(riskLabel)) {
            return GREEN;
        }
        return ORANGE;
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

        ArrayList<Map.Entry<String, ArrayList<ExpenseEntry>>> sortedEntries =
                new ArrayList<Map.Entry<String, ArrayList<ExpenseEntry>>>(groups.entrySet());
        Collections.sort(sortedEntries, new Comparator<Map.Entry<String, ArrayList<ExpenseEntry>>>() {
            @Override
            public int compare(Map.Entry<String, ArrayList<ExpenseEntry>> left,
                    Map.Entry<String, ArrayList<ExpenseEntry>> right) {
                int amountComparison = Double.compare(calculateExpenseGroupTotal(right.getValue()),
                        calculateExpenseGroupTotal(left.getValue()));
                if (amountComparison != 0) {
                    return amountComparison;
                }
                return left.getKey().compareToIgnoreCase(right.getKey());
            }
        });

        LinkedHashMap<String, ArrayList<ExpenseEntry>> sortedGroups = new LinkedHashMap<String, ArrayList<ExpenseEntry>>();
        for (Map.Entry<String, ArrayList<ExpenseEntry>> entry : sortedEntries) {
            sortedGroups.put(entry.getKey(), entry.getValue());
        }
        return sortedGroups;
    }
    private double calculateExpenseGroupTotal(ArrayList<ExpenseEntry> entries) {
        double total = 0.0;
        for (ExpenseEntry entry : entries) {
            total += entry.amount;
        }
        return total;
    }

    private double calculateBudgetTotal() {
        double total = 0.0;
        for (Double amount : categoryBudgetLimits.values()) {
            if (amount != null) {
                total += amount.doubleValue();
            }
        }
        return total;
    }

    private double calculateExpenseTotalForCategory(String category) {
        double total = 0.0;
        for (ExpenseEntry entry : expenseEntries) {
            if (entry.category.equals(category)) {
                total += entry.amount;
            }
        }
        return total;
    }

    private String findMostPressuredBudgetCategory() {
        if (categoryBudgetLimits.isEmpty()) {
            return budgetLimit > 0 ? findTopExpenseCategory() : "-";
        }

        String bestCategory = "-";
        double bestRatio = -1.0;
        for (Map.Entry<String, Double> entry : categoryBudgetLimits.entrySet()) {
            double limit = entry.getValue() == null ? 0.0 : entry.getValue().doubleValue();
            if (limit <= 0.0) {
                continue;
            }
            double spent = calculateExpenseTotalForCategory(entry.getKey());
            double ratio = spent / limit;
            if (ratio > bestRatio) {
                bestRatio = ratio;
                bestCategory = entry.getKey();
            }
        }

        if (bestRatio < 0.0) {
            return "-";
        }
        return bestCategory;
    }

    private String buildExpenseBudgetAlertMessage(ExpenseEntry entry) {
        ArrayList<String> alerts = new ArrayList<String>();

        double previousTotalExpenses = calculateTotalExpenses();
        String overallAlert = buildBudgetThresholdAlert("Expenses", previousTotalExpenses, previousTotalExpenses + entry.amount, budgetLimit);
        if (overallAlert != null) {
            alerts.add(overallAlert);
        }

        Double categoryLimit = categoryBudgetLimits.get(entry.category);
        if (categoryLimit != null && categoryLimit.doubleValue() > 0.0) {
            double previousCategorySpent = calculateExpenseTotalForCategory(entry.category);
            String categoryAlert = buildBudgetThresholdAlert(entry.category + " budget", previousCategorySpent,
                    previousCategorySpent + entry.amount, categoryLimit.doubleValue());
            if (categoryAlert != null) {
                alerts.add(categoryAlert);
            }
        }

        if (alerts.isEmpty()) {
            return null;
        }

        StringBuilder message = new StringBuilder();
        for (int index = 0; index < alerts.size(); index++) {
            if (index > 0) {
                message.append("\n");
            }
            message.append(alerts.get(index));
        }
        return message.toString();
    }

    private String buildBudgetThresholdAlert(String label, double previousSpent, double newSpent, double limit) {
        if (limit <= 0.0) {
            return null;
        }

        double previousPercent = (previousSpent / limit) * 100.0;
        double newPercent = (newSpent / limit) * 100.0;
        if (previousPercent >= 100.0) {
            return null;
        }
        if (previousPercent < 100.0 && newPercent >= 100.0) {
            return label + " just went over by " + currencyFormat.format(newSpent - limit) + ".";
        }
        if (previousPercent < BUDGET_ALERT_THRESHOLD_PERCENT && newPercent >= BUDGET_ALERT_THRESHOLD_PERCENT) {
            return label + " is at " + formatPercent(newPercent) + " of the limit with only "
                    + currencyFormat.format(Math.max(0.0, limit - newSpent)) + " left.";
        }
        return null;
    }

    private void addBudgetAlert(String message) {
        if (message == null || message.trim().isEmpty()) {
            return;
        }
        budgetAlertInbox.add(0, message.trim());
        while (budgetAlertInbox.size() > MAX_STORED_BUDGET_ALERTS) {
            budgetAlertInbox.remove(budgetAlertInbox.size() - 1);
        }
        hasUnreadBudgetAlerts = true;
    }

    private void refreshNotificationBar() {
        notificationButton.setBackground(hasUnreadBudgetAlerts ? NOTIFICATION_UNREAD : NOTIFICATION_MUTED_SURFACE);
        notificationButton.setForeground(hasUnreadBudgetAlerts ? NOTIFICATION_UNREAD_TEXT : TEAL_DARK);
        notificationButton.setToolTipText(hasUnreadBudgetAlerts
                ? "You have " + budgetAlertInbox.size() + " new notification" + (budgetAlertInbox.size() == 1 ? "" : "s")
                : "Open notifications");
        refreshContentHeader();
    }

    private void showBudgetAlertInboxDialog() {
        hasUnreadBudgetAlerts = false;
        refreshNotificationBar();
        showNotificationInboxDialog();
    }

    private void showNotificationInboxDialog() {
        JDialog dialog = new JDialog(this, "Notifications", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(true);
        dialog.setSize(NOTIFICATION_DIALOG_WIDTH, NOTIFICATION_DIALOG_HEIGHT);
        dialog.setMinimumSize(new Dimension(NOTIFICATION_DIALOG_MIN_WIDTH, NOTIFICATION_DIALOG_MIN_HEIGHT));
        dialog.setLocationRelativeTo(this);

        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(PAGE_BACKGROUND_SOFT);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        SurfacePanel card = createSurface(new BorderLayout(0, NOTIFICATION_SECTION_GAP), NOTIFICATION_SURFACE, NOTIFICATION_BORDER, NOTIFICATION_CARD_RADIUS);
        card.setBorder(new EmptyBorder(22, 22, 20, 22));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel chip = createBadgeLabel("NOTIFICATIONS", MANAGE_CHIP_BACKGROUND, TEAL_DARK);
        chip.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Alert Mail");
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea subtitle = createWrappedTextArea(
                budgetAlertInbox.isEmpty()
                        ? "No notifications yet. Budget alerts will appear here when spending gets close to or exceeds a limit."
                        : "Recent budget alerts are listed below. Open this panel anytime from the header.",
                new Font(FONT_FAMILY, Font.PLAIN, 14),
                TEXT_SECONDARY);

        header.add(chip);
        header.add(Box.createVerticalStrut(14));
        header.add(title);
        header.add(Box.createVerticalStrut(10));
        header.add(subtitle);

        JPanel inboxList = new JPanel();
        inboxList.setOpaque(false);
        inboxList.setLayout(new BoxLayout(inboxList, BoxLayout.Y_AXIS));
        inboxList.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (budgetAlertInbox.isEmpty()) {
            inboxList.add(createLargeEmptyState("Inbox is clear",
                    "You are all caught up. New budget warnings will appear here automatically."));
        } else {
            for (int index = 0; index < budgetAlertInbox.size(); index++) {
                inboxList.add(createNotificationItemCard(index + 1, budgetAlertInbox.get(index)));
                if (index < budgetAlertInbox.size() - 1) {
                    inboxList.add(Box.createVerticalStrut(12));
                }
            }
        }

        JScrollPane scrollPane = new JScrollPane(inboxList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actions.setOpaque(false);

        JButton closeButton = createManageActionButton("Close", PAGE_BACKGROUND_SOFT, SURFACE_BORDER, TEXT_PRIMARY);
        closeButton.setPreferredSize(new Dimension(124, 42));
        closeButton.addActionListener(event -> dialog.dispose());
        actions.add(closeButton);

        card.add(header, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);

        root.add(card, BorderLayout.CENTER);
        dialog.setContentPane(root);
        dialog.setVisible(true);
    }

    private JPanel createNotificationItemCard(int index, String message) {
        SurfacePanel panel = createSurface(new BorderLayout(0, 10), NOTIFICATION_MUTED_SURFACE, NOTIFICATION_BORDER, NOTIFICATION_ITEM_RADIUS);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Alert " + index);
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 15));
        title.setForeground(TEAL_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea body = createWrappedTextArea(message, new Font(FONT_FAMILY, Font.PLAIN, 14), TEXT_PRIMARY);

        panel.add(title, BorderLayout.NORTH);
        panel.add(body, BorderLayout.CENTER);
        return panel;
    }

    private String buildBudgetAlertHtml(String message) {
        return "<html><body style='width: 320px'><b>Budget alert</b><br/><br/>"
                + escapeHtml(message).replace("\n", "<br/>") + "</body></html>";
    }

    private String buildBudgetAlertInboxHtml() {
        if (budgetAlertInbox.isEmpty()) {
            return "<html><body style='width: 320px'><b>No notifications yet.</b><br/><br/>Budget alerts will appear here when your spending gets close to or exceeds a limit.</body></html>";
        }

        StringBuilder html = new StringBuilder("<html><body style='width: 360px'><b>Recent budget alerts</b><br/><br/>");
        for (int index = 0; index < budgetAlertInbox.size(); index++) {
            if (index > 0) {
                html.append("<br/><br/>");
            }
            html.append(index + 1)
                    .append(". ")
                    .append(escapeHtml(budgetAlertInbox.get(index)).replace("\n", "<br/>"));
        }
        html.append("</body></html>");
        return html.toString();
    }

    private void recalculateBudgetLimit() {
        if (categoryBudgetLimits.isEmpty()) {
            return;
        }
        budgetLimit = calculateBudgetTotal();
    }

    private LocalDate findLatestExpenseDate() {
        if (expenseEntries.isEmpty()) {
            return null;
        }

        LocalDate latest = null;
        for (ExpenseEntry entry : expenseEntries) {
            LocalDate current = parseDateOrNull(entry.date);
            if (current == null) {
                continue;
            }
            if (latest == null || current.isAfter(latest)) {
                latest = current;
            }
        }
        return latest;
    }

    private double[] buildLatestWeekSpendingValues() {
        double[] values = new double[7];
        LocalDate latestDate = findLatestExpenseDate();
        if (latestDate == null) {
            return values;
        }

        int latestWeek = latestDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int latestWeekYear = latestDate.get(IsoFields.WEEK_BASED_YEAR);
        for (ExpenseEntry entry : expenseEntries) {
            LocalDate date = parseDateOrNull(entry.date);
            if (date == null) {
                continue;
            }
            if (date.get(IsoFields.WEEK_BASED_YEAR) == latestWeekYear
                    && date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == latestWeek) {
                values[date.getDayOfWeek().getValue() - 1] += entry.amount;
            }
        }
        return values;
    }

    private LocalDate parseDateOrNull(String rawDate) {
        if (rawDate == null || rawDate.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(rawDate.trim());
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    private double sumValues(double[] values) {
        double total = 0.0;
        for (double value : values) {
            total += value;
        }
        return total;
    }

    private int countActiveDays(double[] values) {
        int count = 0;
        for (double value : values) {
            if (value > 0.0) {
                count++;
            }
        }
        return count;
    }

    private double calculateAverageActiveDaySpend(double[] values) {
        int activeDays = countActiveDays(values);
        return activeDays == 0 ? 0.0 : sumValues(values) / activeDays;
    }

    private double findHighestValue(double[] values) {
        double highest = 0.0;
        for (double value : values) {
            highest = Math.max(highest, value);
        }
        return highest;
    }

    private String findBusiestSpendingDay(double[] values) {
        int busiestIndex = -1;
        double busiestValue = 0.0;
        for (int index = 0; index < values.length; index++) {
            if (values[index] > busiestValue) {
                busiestValue = values[index];
                busiestIndex = index;
            }
        }
        if (busiestIndex < 0) {
            return "-";
        }
        return new String[] { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" }[busiestIndex];
    }

    private double calculateBudgetUsagePercent(double totalExpenses) {
        if (budgetLimit <= 0.0) {
            return 0.0;
        }
        return (totalExpenses / budgetLimit) * 100.0;
    }

    private String formatPercent(double value) {
        return String.format("%.0f%%", value);
    }

    private String formatCompactPeso(double value) {
        double safeValue = Math.max(0.0, value);
        if (safeValue >= 1000000.0) {
            return String.format("P%.1fM", safeValue / 1000000.0);
        }
        if (safeValue >= 1000.0) {
            return String.format("P%.1fK", safeValue / 1000.0);
        }
        return String.format("P%,.0f", safeValue);
    }

    private String buildActiveDaysLabel(int activeDays) {
        if (activeDays == 1) {
            return "1 active day";
        }
        return activeDays + " active days";
    }

    private String buildMostPressuredCategoryText() {
        String category = findMostPressuredBudgetCategory();
        return "-".equals(category) ? "Your highest-pressure category" : category;
    }

    private String buildBudgetNoticeText(double totalExpenses) {
        double remaining = budgetLimit - totalExpenses;
        if (remaining < 0.0) {
            return "Budget alert: you are " + currencyFormat.format(Math.abs(remaining))
                    + " over budget, and " + buildMostPressuredCategoryText() + " needs attention first.";
        }
        double usedPercent = budgetLimit > 0.0 ? (totalExpenses / budgetLimit) * 100.0 : 0.0;
        if (budgetLimit > 0.0 && usedPercent >= BUDGET_ALERT_THRESHOLD_PERCENT) {
            return "Budget warning: only " + currencyFormat.format(remaining)
                    + " is left before you go over, and " + buildMostPressuredCategoryText() + " is the closest limit.";
        }
        return "Budget status: " + currencyFormat.format(remaining)
                + " is still available, and " + buildMostPressuredCategoryText() + " is closest to its limit.";
    }

    private String buildDashboardInsightText(double totalIncome, double totalExpenses, double latestWeekTotal, String topCategory) {
        if (incomeEntries.isEmpty() && expenseEntries.isEmpty()) {
            return "Add income and expense entries to unlock clearer trend and category insights.";
        }
        if (expenseEntries.isEmpty()) {
            return "Income is tracked, but you need expenses before the overview can show pace and category pressure.";
        }
        if (incomeEntries.isEmpty()) {
            return "Expenses are tracked, but adding income will make the remaining balance and forecast more meaningful.";
        }
        return "Latest week: " + currencyFormat.format(latestWeekTotal)
                + ". " + topCategory + " leads a total of " + currencyFormat.format(totalExpenses)
                + " spent against " + currencyFormat.format(totalIncome) + " income.";
    }

    private int computeCategoryWrapWidth() {
        int available = categoryOverviewContentPanel.getWidth();
        if (available <= 0) {
            return 210;
        }
        int horizontalPadding = 72;
        return Math.max(180, Math.min(320, available - horizontalPadding));
    }
    private String buildCategoryBudgetSummary(String category, double amount) {
        Double limit = categoryBudgetLimits.get(category);
        if (limit == null || limit.doubleValue() <= 0.0) {
            return "No category budget yet.";
        }
        double remaining = limit.doubleValue() - amount;
        if (remaining >= 0.0) {
            return currencyFormat.format(remaining) + " left in budget.";
        }
        return currencyFormat.format(Math.abs(remaining)) + " over budget.";
    }

    private String buildCategoryRowDetail(double amount, double totalSpent, Double categoryBudgetLimit) {
        String detail = formatPercent(totalSpent <= 0.0 ? 0.0 : (amount / totalSpent) * 100.0) + " of total";
        if (categoryBudgetLimit != null && categoryBudgetLimit.doubleValue() > 0.0) {
            double remaining = categoryBudgetLimit.doubleValue() - amount;
            if (remaining >= 0.0) {
                detail += " | " + formatPercent((amount / categoryBudgetLimit.doubleValue()) * 100.0) + " of budget";
            } else {
                detail += " | " + currencyFormat.format(Math.abs(remaining)) + " over";
            }
        } else {
            detail += " | no budget set";
        }
        return detail;
    }

    private Color resolveCategoryProgressColor(double amount, double totalSpent, Double categoryBudgetLimit) {
        if (categoryBudgetLimit != null && categoryBudgetLimit.doubleValue() > 0.0) {
            double budgetPercent = (amount / categoryBudgetLimit.doubleValue()) * 100.0;
            if (budgetPercent >= 100.0) {
                return RED;
            }
            if (budgetPercent >= 85.0) {
                return ORANGE;
            }
        }
        double share = totalSpent <= 0.0 ? 0.0 : (amount / totalSpent) * 100.0;
        return share >= 40.0 ? GOLD : TEAL;
    }

    private class WeeklyTrendPanel extends JPanel {
        WeeklyTrendPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(0, 210));
            setMinimumSize(new Dimension(0, 210));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 210));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int chartLeft = 52;
            int chartRight = Math.max(chartLeft + 40, width - 18);
            int chartTop = 18;
            int chartBottom = Math.max(chartTop + 80, height - 42);
            int chartHeight = chartBottom - chartTop;
            int chartWidth = chartRight - chartLeft;
            Color gridColor = new Color(229, 217, 194);

            String[] days = new String[] { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
            double[] values = buildLatestWeekSpendingValues();
            double maxValue = 0.0;
            for (double value : values) {
                maxValue = Math.max(maxValue, value);
            }
            double scaleMax = maxValue <= 0.0 ? 1.0 : maxValue;
            int busiestIndex = -1;
            for (int index = 0; index < values.length; index++) {
                if (values[index] == maxValue && maxValue > 0.0) {
                    busiestIndex = index;
                    break;
                }
            }

            int slotWidth = chartWidth / 7;
            int barWidth = Math.max(18, (int) Math.round(slotWidth * 0.42));
            int barMaxHeight = Math.max(18, chartHeight - 34);

            g2.setColor(gridColor);
            g2.setFont(new Font(FONT_FAMILY, Font.BOLD, 12));
            for (int row = 0; row < 3; row++) {
                int y = chartTop + (row * (chartHeight / 2));
                g2.drawLine(chartLeft, y, chartRight, y);

                double axisValue = row == 0 ? maxValue : (row == 1 ? maxValue / 2.0 : 0.0);
                String label = formatCompactPeso(axisValue);
                int labelWidth = g2.getFontMetrics().stringWidth(label) + 14;
                int labelHeight = 20;
                int labelX = 10;
                int labelY = y - (labelHeight / 2);
                g2.setColor(PAGE_BACKGROUND_SOFT);
                g2.fillRoundRect(labelX, labelY, labelWidth, labelHeight, 18, 18);
                g2.setColor(DONUT_RING);
                g2.drawRoundRect(labelX, labelY, labelWidth, labelHeight, 18, 18);
                g2.setColor(TEXT_SECONDARY);
                g2.drawString(label, labelX + 7, labelY + 14);
                g2.setColor(gridColor);
            }

            for (int index = 0; index < 7; index++) {
                int centerX = chartLeft + (slotWidth * index) + (slotWidth / 2);
                int barHeight = (int) Math.round((values[index] / scaleMax) * barMaxHeight);
                int barX = centerX - (barWidth / 2);
                int barY = chartBottom - barHeight;

                if (values[index] > 0.0) {
                    Color activeBarColor = index == busiestIndex ? GOLD : TEAL;
                    Color activeShadow = index == busiestIndex ? new Color(243, 178, 79, 95) : new Color(71, 139, 141, 80);
                    g2.setColor(activeShadow);
                    g2.fillRoundRect(barX + 3, barY + 3, barWidth, barHeight, 14, 14);
                    g2.setColor(activeBarColor);
                    g2.fillRoundRect(barX, barY, barWidth, barHeight, 14, 14);
                } else {
                    g2.setColor(new Color(71, 139, 141, 32));
                    g2.fillRoundRect(barX, chartBottom - 12, barWidth, 12, 14, 14);
                }

                String day = days[index];
                int dayWidth = g2.getFontMetrics().stringWidth(day);
                g2.setColor(TEXT_SECONDARY);
                g2.drawString(day, centerX - (dayWidth / 2), height - 14);
            }

            if (expenseEntries.isEmpty()) {
                g2.setColor(new Color(123, 94, 52, 180));
                g2.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
                String message = "No spending yet";
                int messageWidth = g2.getFontMetrics().stringWidth(message);
                g2.drawString(message, (width - messageWidth) / 2, chartTop + (chartHeight / 2) + 8);
            }

            g2.dispose();
        }
    }

    private static class PromptTextField extends JTextField {
        private final String prompt;

        PromptTextField(String prompt) {
            this.prompt = prompt == null ? "" : prompt;
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            if (!getText().isEmpty() || isFocusOwner() || prompt.isEmpty()) {
                return;
            }

            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(getFont());
            g2.setColor(TEXT_SECONDARY);
            Insets insets = getInsets();
            int baseline = (getHeight() + g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2;
            g2.drawString(prompt, insets.left + 2, baseline);
            g2.dispose();
        }
    }

    private static class AutoScalingMetricLabel extends JLabel {
        private static final int MAX_FONT_SIZE = 46;
        private static final int MIN_FONT_SIZE = 20;

        @Override
        public void setText(String text) {
            super.setText(text);
            SwingUtilities.invokeLater(this::updateFontToFit);
        }

        @Override
        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(x, y, width, height);
            updateFontToFit();
        }

        private void updateFontToFit() {
            String text = getText();
            Font fittedFont = new Font(FONT_FAMILY, Font.BOLD, MAX_FONT_SIZE);
            if (text == null || text.isEmpty() || getWidth() <= 0) {
                setFont(fittedFont);
                return;
            }

            int availableWidth = Math.max(1, getWidth() - 24);
            int size = MAX_FONT_SIZE;
            while (size > MIN_FONT_SIZE) {
                fittedFont = new Font(FONT_FAMILY, Font.BOLD, size);
                FontMetrics metrics = getFontMetrics(fittedFont);
                if (metrics.stringWidth(text) <= availableWidth) {
                    break;
                }
                size--;
            }

            if (!fittedFont.equals(getFont())) {
                setFont(fittedFont);
            }
        }
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
            setAlignmentX(Component.LEFT_ALIGNMENT);
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
        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
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
        private static final int STACK_BREAKPOINT = 1320;

        private final JPanel primaryPanel;
        private final JPanel secondaryPanel;
        private final int secondaryWidth;

        ResponsiveSplitPanel(JPanel primaryPanel, JPanel secondaryPanel, int secondaryWidth) {
            this.primaryPanel = primaryPanel;
            this.secondaryPanel = secondaryPanel;
            this.secondaryWidth = Math.max(260, secondaryWidth);
            setOpaque(false);
            setLayout(null);
            setAlignmentX(Component.LEFT_ALIGNMENT);
            add(primaryPanel);
            add(secondaryPanel);
        }

        @Override
        public Dimension getPreferredSize() {
            int availableWidth = getParent() == null ? 1200 : Math.max(0, getParent().getWidth());
            int primaryHeight = primaryPanel.getPreferredSize().height;
            int secondaryHeight = secondaryPanel.getPreferredSize().height;

            if (availableWidth < STACK_BREAKPOINT) {
                int width = Math.max(availableWidth, 0);
                return new Dimension(width, primaryHeight + GAP + secondaryHeight);
            }

            int width = Math.max(availableWidth, secondaryWidth + 300 + GAP);
            return new Dimension(width, Math.max(primaryHeight, secondaryHeight));
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
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

            int resolvedSecondaryWidth = Math.min(this.secondaryWidth, Math.max(260, (int) Math.round(width * 0.32)));
            int resolvedPrimaryWidth = Math.max(300, width - resolvedSecondaryWidth - GAP);
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

    private static class MailOutlineIcon implements Icon {
        private final int width;
        private final int height;

        MailOutlineIcon(int width, int height) {
            this.width = width;
            this.height = height;
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
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setColor(component.getForeground());
            g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int left = x + 1;
            int top = y + 2;
            int boxWidth = width - 3;
            int boxHeight = height - 4;
            g2.drawRoundRect(left, top, boxWidth, boxHeight, 4, 4);
            g2.drawLine(left + 1, top + 1, left + (boxWidth / 2), top + (boxHeight / 2) + 1);
            g2.drawLine(left + boxWidth - 1, top + 1, left + (boxWidth / 2), top + (boxHeight / 2) + 1);
            g2.dispose();
        }
    }
}
