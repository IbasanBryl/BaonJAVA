package baon.app;

import baon.backend.BackendServer;
import baon.ui.AppTheme;
import baon.ui.LoginFrame;

import java.awt.Font;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {
        try {
            configureLookAndFeel();
        } catch (Exception ignored) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception fallbackIgnored) {
                // Fall back to Swing's default look and feel when the system one is unavailable.
            }
        }

        BackendServer.start(8080);
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    private static void configureLookAndFeel() {
        FlatLightLaf.setup();
        increaseGlobalFontSize(1f);
        UIManager.put("Component.arc", AppTheme.integer("--ui-component-arc", 16));
        UIManager.put("Button.arc", AppTheme.integer("--ui-button-arc", 16));
        UIManager.put("TextComponent.arc", AppTheme.integer("--ui-text-component-arc", 14));
        UIManager.put("ProgressBar.arc", AppTheme.integer("--ui-progress-arc", 16));
        UIManager.put("ScrollBar.width", AppTheme.integer("--ui-scrollbar-width", 12));
        UIManager.put("Table.showHorizontalLines", Boolean.TRUE);
        UIManager.put("Table.showVerticalLines", Boolean.FALSE);
        UIManager.put("TitlePane.unifiedBackground", Boolean.TRUE);
    }

    private static void increaseGlobalFontSize(float delta) {
        Font baseFont = UIManager.getFont("defaultFont");
        if (baseFont == null) {
            baseFont = UIManager.getFont("Label.font");
        }
        if (baseFont != null) {
            UIManager.put("defaultFont", new FontUIResource(baseFont.deriveFont(baseFont.getSize2D() + delta)));
        }
    }
}
