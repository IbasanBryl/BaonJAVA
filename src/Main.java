import javax.swing.SwingUtilities;
import javax.swing.UIManager;

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

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    private static void configureLookAndFeel() {
        FlatLightLaf.setup();
        UIManager.put("Component.arc", 16);
        UIManager.put("Button.arc", 16);
        UIManager.put("TextComponent.arc", 14);
        UIManager.put("ProgressBar.arc", 16);
        UIManager.put("ScrollBar.width", 12);
        UIManager.put("Table.showHorizontalLines", Boolean.TRUE);
        UIManager.put("Table.showVerticalLines", Boolean.FALSE);
        UIManager.put("TitlePane.unifiedBackground", Boolean.TRUE);
    }
}
