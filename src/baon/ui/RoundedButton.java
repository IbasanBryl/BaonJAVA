package baon.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;

public class RoundedButton extends JButton {
    private final int arc;
    private Shape shape;

    public RoundedButton() {
        this(null, 24);
    }

    public RoundedButton(String text) {
        this(text, 24);
    }

    public RoundedButton(String text, int arc) {
        super(text);
        this.arc = Math.max(12, arc);
        setOpaque(false);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color fill = resolveFillColor();
        int width = Math.max(0, getWidth() - 1);
        int height = Math.max(0, getHeight() - 5);

        if (width > 0 && height > 0) {
            if (isEnabled()) {
                g2.setColor(withAlpha(Color.BLACK, 24));
                g2.fillRoundRect(1, 4, width - 1, height, arc, arc);
            }

            g2.setPaint(new GradientPaint(
                    0,
                    0,
                    brighten(fill, 0.06f),
                    0,
                    Math.max(1, height),
                    darken(fill, 0.06f)));
            g2.fillRoundRect(0, 0, width, height, arc, arc);
            g2.setColor(withAlpha(Color.WHITE, isEnabled() ? 54 : 30));
            g2.drawRoundRect(1, 1, Math.max(0, width - 2), Math.max(0, height - 2), arc - 2, arc - 2);
        }

        g2.dispose();
        super.paintComponent(graphics);
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || shape.getBounds().width != getWidth() || shape.getBounds().height != getHeight()) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc);
        }
        return shape.contains(x, y);
    }

    private Color resolveFillColor() {
        Color background = getBackground();
        if (background == null) {
            background = new Color(220, 220, 220);
        }
        if (!isEnabled()) {
            return blend(background, Color.WHITE, 0.32f);
        }
        if (getModel().isPressed()) {
            return darken(background, 0.12f);
        }
        if (getModel().isRollover()) {
            return brighten(background, 0.05f);
        }
        return background;
    }

    private static Color brighten(Color color, float ratio) {
        int red = color.getRed() + Math.round((255 - color.getRed()) * ratio);
        int green = color.getGreen() + Math.round((255 - color.getGreen()) * ratio);
        int blue = color.getBlue() + Math.round((255 - color.getBlue()) * ratio);
        return new Color(clamp(red), clamp(green), clamp(blue), color.getAlpha());
    }

    private static Color darken(Color color, float ratio) {
        int red = color.getRed() - Math.round(color.getRed() * ratio);
        int green = color.getGreen() - Math.round(color.getGreen() * ratio);
        int blue = color.getBlue() - Math.round(color.getBlue() * ratio);
        return new Color(clamp(red), clamp(green), clamp(blue), color.getAlpha());
    }

    private static Color blend(Color color, Color other, float ratio) {
        float inverse = 1.0f - ratio;
        int red = Math.round((color.getRed() * inverse) + (other.getRed() * ratio));
        int green = Math.round((color.getGreen() * inverse) + (other.getGreen() * ratio));
        int blue = Math.round((color.getBlue() * inverse) + (other.getBlue() * ratio));
        int alpha = Math.round((color.getAlpha() * inverse) + (other.getAlpha() * ratio));
        return new Color(clamp(red), clamp(green), clamp(blue), clamp(alpha));
    }

    private static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), clamp(alpha));
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
