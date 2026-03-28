package baon.ui;

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
        setPreferredSize(new Dimension(160, 160));
        setMinimumSize(new Dimension(160, 160));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(Math.min(getWidth(), getHeight()), 160);
        if (size <= 0) {
            size = 160;
        }
        int x = Math.max(0, (getWidth() - size) / 2);
        int y = Math.max(0, (getHeight() - size) / 2);
        int outer = size - 2;
        int ringInset = Math.max(10, (int) Math.round(size * 0.12));
        int centerInset = Math.max(24, (int) Math.round(size * 0.27));

        g2.setColor(outerColor);
        g2.fillOval(x, y, outer, outer);
        g2.setColor(ringColor);
        g2.fillOval(x + ringInset, y + ringInset, outer - (ringInset * 2), outer - (ringInset * 2));
        g2.setColor(centerColor);
        g2.fillOval(x + centerInset, y + centerInset, outer - (centerInset * 2), outer - (centerInset * 2));
        g2.setColor(textColor);
        g2.setFont(new Font(FONT_FAMILY, Font.BOLD, 12));

        String[] lines = text.split("\\n");
        int totalTextHeight = lines.length * 16;
        int textY = y + (size / 2) - (totalTextHeight / 2) + 12;
        for (String line : lines) {
            int lineWidth = g2.getFontMetrics().stringWidth(line);
            g2.drawString(line, x + (size / 2) - (lineWidth / 2), textY);
            textY += 16;
        }
        g2.dispose();
    }
}
