package baon.ui;

import java.awt.Color;
import java.awt.GradientPaint;
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
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = Math.max(0, getWidth() - 1);
        int height = Math.max(0, getHeight() - 8);
        if (width <= 0 || height <= 0) {
            g2.dispose();
            return;
        }

        Color softShadow = withAlpha(shadowColor, Math.max(10, shadowColor.getAlpha() / 2));
        Color strongShadow = withAlpha(shadowColor, Math.max(18, shadowColor.getAlpha() + 6));

        g2.setColor(softShadow);
        g2.fillRoundRect(2, 6, width - 1, height + 1, radius + 6, radius + 6);
        g2.setColor(strongShadow);
        g2.fillRoundRect(6, 10, Math.max(0, width - 12), Math.max(0, height - 2), radius, radius);

        g2.setPaint(new GradientPaint(
                0,
                0,
                lift(fillColor, 0.05f),
                0,
                Math.max(1, height),
                fillColor));
        g2.fillRoundRect(0, 0, width, height, radius, radius);

        g2.setPaint(new GradientPaint(
                0,
                0,
                withAlpha(Color.WHITE, Math.max(18, fillColor.getAlpha() / 5)),
                0,
                Math.max(1, height / 2),
                withAlpha(Color.WHITE, 0)));
        g2.fillRoundRect(0, 0, width, height, radius, radius);

        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, width - 1, height - 1, radius, radius);
        g2.setColor(withAlpha(Color.WHITE, 70));
        g2.drawRoundRect(1, 1, Math.max(0, width - 3), Math.max(0, height - 3), radius - 2, radius - 2);
        g2.dispose();
    }

    private static Color lift(Color color, float amount) {
        int red = color.getRed() + Math.round((255 - color.getRed()) * amount);
        int green = color.getGreen() + Math.round((255 - color.getGreen()) * amount);
        int blue = color.getBlue() + Math.round((255 - color.getBlue()) * amount);
        return new Color(clamp(red), clamp(green), clamp(blue), color.getAlpha());
    }

    private static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), clamp(alpha));
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}

