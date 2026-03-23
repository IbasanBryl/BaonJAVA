import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class DonutPlaceholderPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final String FONT_FAMILY = AppTheme.text("--font-family", "Segoe UI");

    private final String text;
    private final Color centerColor;
    private final Color outerColor;
    private final Color ringColor;
    private final Color textColor;

    public DonutPlaceholderPanel(String text, Color centerColor, Color outerColor, Color ringColor, Color textColor) {
        this.text = text;
        this.centerColor = centerColor;
        this.outerColor = outerColor;
        this.ringColor = ringColor;
        this.textColor = textColor;
        setOpaque(false);
        setPreferredSize(new Dimension(120, 120));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(outerColor);
        g2.fillOval(0, 0, 118, 118);
        g2.setColor(ringColor);
        g2.fillOval(14, 14, 90, 90);
        g2.setColor(centerColor);
        g2.fillOval(26, 26, 66, 66);
        g2.setColor(textColor);
        g2.setFont(new Font(FONT_FAMILY, Font.BOLD, 12));

        String[] lines = text.split("\\n");
        int y = 55 - ((lines.length - 1) * 8);
        for (String line : lines) {
            int width = g2.getFontMetrics().stringWidth(line);
            g2.drawString(line, 59 - (width / 2), y);
            y += 16;
        }
        g2.dispose();
    }
}
