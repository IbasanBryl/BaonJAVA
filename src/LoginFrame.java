import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {
    private static final String FONT_FAMILY = AppTheme.text("--font-family", "Segoe UI");
    private static final Color PAGE_BACKGROUND = AppTheme.color("--login-page-background", "#F3F4F6");
    private static final Color FORM_BACKGROUND = AppTheme.color("--login-form-background", "#FFFFFF");
    private static final Color FORM_BORDER = AppTheme.color("--login-form-border", "#D1D5DB");
    private static final Color FORM_SHADOW = AppTheme.color("--login-form-shadow", "rgba(15, 23, 42, 22)");
    private static final Color TEXT_PRIMARY = AppTheme.color("--login-text-primary", "#0F172A");
    private static final Color TEXT_SECONDARY = AppTheme.color("--login-text-secondary", "#6B7280");
    private static final Color FIELD_BACKGROUND = AppTheme.color("--login-field-background", "#FFFFFF");
    private static final Color FIELD_BORDER = AppTheme.color("--login-field-border", "#D1D5DB");
    private static final Color ACCENT = AppTheme.color("--login-accent", "#3B82F6");
    private static final Color ACCENT_DARK = AppTheme.color("--login-accent-dark", "#2563EB");
    private static final Color SUCCESS = AppTheme.color("--login-success", "#10B981");
    private static final Color ERROR = AppTheme.color("--login-error", "#DC2626");
    private static final Color HERO_EYEBROW = AppTheme.color("--login-hero-eyebrow", "#DBEAFE");
    private static final Color HERO_BODY = AppTheme.color("--login-hero-body", "#EFF6FF");
    private static final Color FEATURE_PILL_BACKGROUND = AppTheme.color("--login-feature-pill-background", "rgba(255, 255, 255, 34)");

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JLabel statusLabel = new JLabel(" ");
    private final HeroPanel infoPanel = createInfoPanel();
    private final FormCardPanel formPanel = createFormPanel();

    public LoginFrame() {
        super("Finance Hub Login");
        configureFrame();
    }

    private void configureFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(720, 540));
        setSize(1040, 640);
        setResizable(true);
        setLocationRelativeTo(null);
        setContentPane(createRoot());
    }

    private JPanel createRoot() {
        GradientPanel root = new GradientPanel();
        root.setLayout(new BorderLayout());
        root.setBorder(new EmptyBorder(26, 28, 26, 28));

        ResponsiveShellPanel shell = new ResponsiveShellPanel(infoPanel, formPanel);

        JScrollPane scrollPane = new JScrollPane(shell);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        root.add(scrollPane, BorderLayout.CENTER);
        return root;
    }

    private HeroPanel createInfoPanel() {
        HeroPanel panel = new HeroPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(38, 36, 36, 36));
        panel.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel eyebrow = new JLabel("WELCOME BACK");
        eyebrow.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        eyebrow.setForeground(HERO_EYEBROW);
        eyebrow.setAlignmentX(LEFT_ALIGNMENT);

        JLabel title = new JLabel("<html><div style='width: 17em;'>Manage your finances with a clean desktop workflow.</div></html>");
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 31));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel body = new JLabel("<html><div style='width: 26em;'>Use the login form to access your dashboard. Track spending, review your budget, and keep everything in one calm desktop space.</div></html>");
        body.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        body.setForeground(HERO_BODY);
        body.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(eyebrow);
        panel.add(Box.createVerticalStrut(20));
        panel.add(title);
        panel.add(Box.createVerticalStrut(18));
        panel.add(body);
        panel.add(Box.createVerticalGlue());
        panel.add(createFeaturePill("Demo login: admin / password123"));
        panel.add(Box.createVerticalStrut(12));
        panel.add(createFeaturePill("Validation checks blank and incorrect entries"));
        return panel;
    }

    private FormCardPanel createFormPanel() {
        FormCardPanel panel = new FormCardPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(36, 36, 34, 36));
        panel.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel eyebrow = createFormChip("STUDENT BUDGET HUB");
        JLabel title = new JLabel("Login");
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 30));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Enter your username and password");
        subtitle.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        JLabel usernameLabel = createFieldLabel("Username");
        styleTextField(usernameField, "Enter your username");

        JLabel passwordLabel = createFieldLabel("Password");
        stylePasswordField(passwordField, "Enter your password");

        JButton loginButton = createLoginButton();

        statusLabel.setFont(new Font(FONT_FAMILY, Font.PLAIN, 12));
        statusLabel.setForeground(TEXT_SECONDARY);
        statusLabel.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(eyebrow);
        panel.add(Box.createVerticalStrut(18));
        panel.add(title);
        panel.add(Box.createVerticalStrut(8));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(28));
        panel.add(usernameLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(18));
        panel.add(passwordLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(24));
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(16));
        panel.add(statusLabel);
        panel.add(Box.createVerticalStrut(14));
        panel.add(createFooterNote());
        panel.add(Box.createVerticalGlue());

        getRootPane().setDefaultButton(loginButton);
        return panel;
    }

    private JLabel createFormChip(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(59, 130, 246, 22));
        label.setForeground(ACCENT_DARK);
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 11));
        label.setBorder(new EmptyBorder(7, 10, 7, 10));
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private void styleTextField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        field.setPreferredSize(new Dimension(0, 46));
        field.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
        field.setBackground(FIELD_BACKGROUND);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER),
                new EmptyBorder(12, 14, 12, 14)));
        field.setToolTipText(placeholder);
        field.setAlignmentX(LEFT_ALIGNMENT);
    }

    private void stylePasswordField(JPasswordField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        field.setPreferredSize(new Dimension(0, 46));
        field.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
        field.setBackground(FIELD_BACKGROUND);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER),
                new EmptyBorder(12, 14, 12, 14)));
        field.setToolTipText(placeholder);
        field.setAlignmentX(LEFT_ALIGNMENT);
    }

    private JButton createLoginButton() {
        JButton button = new JButton("Login");
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        button.setPreferredSize(new Dimension(0, 48));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBackground(ACCENT);
        button.setForeground(Color.WHITE);
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_DARK),
                new EmptyBorder(12, 16, 12, 16)));
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.addActionListener(event -> handleLogin());
        return button;
    }

    private JPanel createFeaturePill(String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        panel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setBackground(FEATURE_PILL_BACKGROUND);
        label.setForeground(Color.WHITE);
        label.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        label.setBorder(new EmptyBorder(12, 14, 12, 14));

        panel.add(label);
        return panel;
    }

    private JLabel createFooterNote() {
        JLabel note = new JLabel("Use the demo account to test validation and open the dashboard.");
        note.setFont(new Font(FONT_FAMILY, Font.PLAIN, 12));
        note.setForeground(TEXT_SECONDARY);
        note.setAlignmentX(LEFT_ALIGNMENT);
        return note;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() && password.isEmpty()) {
            showStatus("Username and password are required.", ERROR);
            return;
        }

        if (username.isEmpty()) {
            showStatus("Please enter your username.", ERROR);
            return;
        }

        if (password.isEmpty()) {
            showStatus("Please enter your password.", ERROR);
            return;
        }

        if (username.equals("admin") && password.equals("password123")) {
            showStatus("Login successful. Opening dashboard...", SUCCESS);
            dispose();
            new MainFrame().setVisible(true);
            return;
        }

        showStatus("Invalid username or password.", ERROR);
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
            g2.setPaint(new GradientPaint(0, 0, Color.WHITE, getWidth(), getHeight(), PAGE_BACKGROUND));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    private static class HeroPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, ACCENT, getWidth(), getHeight(), SUCCESS));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 32, 32);
            g2.dispose();
        }
    }

    private static class FormCardPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth() - 1;
            int height = getHeight() - 7;

            g2.setColor(FORM_SHADOW);
            g2.fillRoundRect(4, 7, width - 8, height, 28, 28);

            g2.setColor(FORM_BACKGROUND);
            g2.fillRoundRect(0, 0, width, height, 28, 28);

            g2.setColor(FORM_BORDER);
            g2.drawRoundRect(0, 0, width, height, 28, 28);
            g2.dispose();
        }
    }

    private static class ResponsiveShellPanel extends JPanel implements Scrollable {
        private static final int STACK_BREAKPOINT = 920;
        private static final int PANEL_GAP = 24;
        private static final int HERO_MIN_WIDTH = 430;
        private static final int FORM_WIDTH = 360;

        private final JPanel heroPanel;
        private final JPanel formPanel;

        ResponsiveShellPanel(JPanel heroPanel, JPanel formPanel) {
            this.heroPanel = heroPanel;
            this.formPanel = formPanel;
            setOpaque(false);
            setLayout(null);
            add(heroPanel);
            add(formPanel);
        }

        @Override
        public Dimension getPreferredSize() {
            int availableWidth = getParent() == null ? 1040 : Math.max(0, getParent().getWidth());
            if (availableWidth <= 0) {
                availableWidth = 1040;
            }

            if (availableWidth < STACK_BREAKPOINT) {
                int width = Math.max(availableWidth, FORM_WIDTH);
                int heroHeight = Math.max(330, heroPanel.getPreferredSize().height);
                int formHeight = formPanel.getPreferredSize().height;
                return new Dimension(width, heroHeight + PANEL_GAP + formHeight);
            }

            int width = Math.max(availableWidth, HERO_MIN_WIDTH + FORM_WIDTH + PANEL_GAP);
            int heroWidth = Math.max(HERO_MIN_WIDTH, width - FORM_WIDTH - PANEL_GAP);
            int heroHeight = scaledHeroHeight(heroWidth);
            int formHeight = Math.max(formPanel.getPreferredSize().height, heroHeight);
            return new Dimension(width, formHeight);
        }

        @Override
        public void doLayout() {
            int width = getWidth();

            if (width < STACK_BREAKPOINT) {
                int heroHeight = Math.max(330, heroPanel.getPreferredSize().height);
                int formHeight = formPanel.getPreferredSize().height;
                int totalHeight = heroHeight + PANEL_GAP + formHeight;
                int startY = Math.max(0, (getHeight() - totalHeight) / 2);
                heroPanel.setBounds(0, startY, width, heroHeight);
                formPanel.setBounds(0, startY + heroHeight + PANEL_GAP, width, formHeight);
                return;
            }

            int formWidth = Math.min(FORM_WIDTH, Math.max(332, (int) Math.round(width * 0.34)));
            int heroWidth = Math.max(HERO_MIN_WIDTH, width - formWidth - PANEL_GAP);
            int heroHeight = scaledHeroHeight(heroWidth);
            int formHeight = Math.max(formPanel.getPreferredSize().height, heroHeight);
            int startY = Math.max(0, (getHeight() - formHeight) / 2);

            heroPanel.setBounds(0, startY, heroWidth, heroHeight);
            formPanel.setBounds(heroWidth + PANEL_GAP, startY, formWidth, formHeight);
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

        private int scaledHeroHeight(int width) {
            return Math.max(360, Math.min(520, (int) Math.round(width * 0.62)));
        }
    }
}




