import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {
    private static final Color PAGE_BACKGROUND = css("#F3F4F6");
    private static final Color FORM_BACKGROUND = css("#FFFFFF");
    private static final Color FORM_BORDER = css("#D1D5DB");
    private static final Color FORM_SHADOW = new Color(15, 23, 42, 22);
    private static final Color TEXT_PRIMARY = css("#0F172A");
    private static final Color TEXT_SECONDARY = css("#6B7280");
    private static final Color FIELD_BACKGROUND = css("#FFFFFF");
    private static final Color FIELD_BORDER = css("#D1D5DB");
    private static final Color ACCENT = css("#3B82F6");
    private static final Color SUCCESS = css("#10B981");
    private static final Color ERROR = css("#DC2626");

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JLabel statusLabel = new JLabel("Sign in to continue", SwingConstants.LEFT);

    public LoginFrame() {
        super("Finance Hub Login");
        configureFrame();
    }

    private void configureFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setContentPane(createRoot());
    }

    private JPanel createRoot() {
        GradientPanel root = new GradientPanel();
        root.setLayout(new BorderLayout());
        root.setBorder(new EmptyBorder(32, 32, 32, 32));

        JPanel shell = new JPanel(new GridLayout(1, 2, 24, 24));
        shell.setOpaque(false);
        shell.add(createInfoPanel());
        shell.add(createFormPanel());

        root.add(shell, BorderLayout.CENTER);
        return root;
    }

    private JPanel createInfoPanel() {
        HeroPanel panel = new HeroPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(36, 36, 36, 36));

        JLabel eyebrow = new JLabel("WELCOME BACK");
        eyebrow.setFont(new Font("Segoe UI", Font.BOLD, 13));
        eyebrow.setForeground(css("#DBEAFE"));
        eyebrow.setAlignmentX(LEFT_ALIGNMENT);

        JLabel title = new JLabel("<html>Manage your finances with a clean desktop workflow.</html>");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel body = new JLabel("<html>Use the login form to access your dashboard. This starter flow is styled like a modern web sign-in page.</html>");
        body.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        body.setForeground(css("#EFF6FF"));
        body.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(eyebrow);
        panel.add(Box.createVerticalStrut(18));
        panel.add(title);
        panel.add(Box.createVerticalStrut(16));
        panel.add(body);
        panel.add(Box.createVerticalGlue());
        panel.add(createFeaturePill("Demo login: admin / password123"));
        panel.add(Box.createVerticalStrut(12));
        panel.add(createFeaturePill("Validation checks blank and incorrect entries"));

        return panel;
    }

    private JPanel createFormPanel() {
        FormCardPanel panel = new FormCardPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(36, 36, 36, 36));

        JLabel title = new JLabel("Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Enter your username and password");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        JLabel usernameLabel = createFieldLabel("Username");
        styleTextField(usernameField, "Enter your username");

        JLabel passwordLabel = createFieldLabel("Password");
        stylePasswordField(passwordField, "Enter your password");

        JButton loginButton = createLoginButton();

        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setForeground(TEXT_SECONDARY);
        statusLabel.setAlignmentX(LEFT_ALIGNMENT);

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
        panel.add(Box.createVerticalStrut(18));
        panel.add(statusLabel);
        panel.add(Box.createVerticalGlue());
        panel.add(createFooterNote());

        getRootPane().setDefaultButton(loginButton);
        return panel;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private void styleTextField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        field.setPreferredSize(new Dimension(0, 46));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(css("#2563EB")),
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
        label.setBackground(new Color(255, 255, 255, 34));
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setBorder(new EmptyBorder(12, 14, 12, 14));

        panel.add(label);
        return panel;
    }

    private JLabel createFooterNote() {
        JLabel note = new JLabel("Use demo credentials to test validation.");
        note.setFont(new Font("Segoe UI", Font.PLAIN, 12));
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

    private static Color css(String hex) {
        return Color.decode(hex);
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
}
