package baon.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class SurfacePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final Color fillColor;
    private final Color borderColor;
    private final int radius;
    private final Color shadowColor;

    public SurfacePanel(Color fillColor, Color borderColor, int radius, Color shadowColor) {
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.radius = radius;
        this.shadowColor = shadowColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(shadowColor);
        g2.fillRoundRect(4, 7, getWidth() - 8, getHeight() - 10, radius, radius);
        g2.setColor(fillColor);
        g2.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 14, radius, radius);
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 9, getHeight() - 15, radius, radius);
        g2.dispose();
        super.paintComponent(graphics);
    }
}

