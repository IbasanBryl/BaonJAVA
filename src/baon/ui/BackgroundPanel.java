package baon.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final Color baseColor;
    private final Color stripeColor;
    private final Color orbColor;

    public BackgroundPanel(Color baseColor, Color stripeColor, Color orbColor) {
        this.baseColor = baseColor;
        this.stripeColor = stripeColor;
        this.orbColor = orbColor;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        g2.setPaint(new GradientPaint(
                0,
                0,
                blend(baseColor, Color.WHITE, 0.08f),
                width,
                Math.max(1, height),
                blend(baseColor, stripeColor, 0.12f)));
        g2.fillRect(0, 0, width, height);

        Color washTop = withAlpha(stripeColor, Math.max(35, Math.min(120, stripeColor.getAlpha())));
        Color washBottom = withAlpha(stripeColor, 0);
        g2.setPaint(new GradientPaint(0, 0, washTop, 0, Math.max(1, Math.round(height * 0.6f)), washBottom));
        g2.fillRect(0, 0, width, height);

        float orbRadius = Math.max(width, height) * 0.66f;
        g2.setPaint(new RadialGradientPaint(
                new Point2D.Float(width * 0.88f, height * 0.08f),
                orbRadius,
                new float[] { 0.0f, 0.45f, 1.0f },
                new Color[] {
                        withAlpha(orbColor, Math.max(60, orbColor.getAlpha())),
                        withAlpha(orbColor, Math.max(18, orbColor.getAlpha() / 2)),
                        withAlpha(orbColor, 0)
                }));
        g2.fillRect(0, 0, width, height);

        for (int x = -40; x < width + 120; x += 150) {
            g2.setColor(withAlpha(Color.WHITE, 28));
            g2.fillRoundRect(x, -10, 44, height + 20, 24, 24);
        }

        float lowerGlowRadius = Math.max(width, height) * 0.36f;
        g2.setPaint(new RadialGradientPaint(
                new Point2D.Float(width * 0.18f, height * 0.74f),
                lowerGlowRadius,
                new float[] { 0.0f, 1.0f },
                new Color[] {
                        withAlpha(stripeColor, 35),
                        withAlpha(stripeColor, 0)
                }));
        g2.fillRect(0, 0, width, height);
        g2.dispose();
    }

    private static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, alpha)));
    }

    private static Color blend(Color color, Color other, double ratio) {
        double inverse = 1.0 - ratio;
        int red = (int) Math.round((color.getRed() * inverse) + (other.getRed() * ratio));
        int green = (int) Math.round((color.getGreen() * inverse) + (other.getGreen() * ratio));
        int blue = (int) Math.round((color.getBlue() * inverse) + (other.getBlue() * ratio));
        int alpha = (int) Math.round((color.getAlpha() * inverse) + (other.getAlpha() * ratio));
        return new Color(red, green, blue, alpha);
    }
}
