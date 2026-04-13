package baon.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.border.AbstractBorder;

public class RoundedLineBorder extends AbstractBorder {
    private final Color color;
    private final int radius;
    private final int thickness;

    public RoundedLineBorder(Color color, int radius, int thickness) {
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
