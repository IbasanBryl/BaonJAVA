import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

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
        g2.setColor(baseColor);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(stripeColor);
        for (int x = -120; x < getWidth() + 120; x += 180) {
            g2.fillRoundRect(x, -30, 120, getHeight() + 60, 80, 80);
        }

        g2.setColor(orbColor);
        g2.fillOval(getWidth() - 380, -60, 320, 320);
        g2.dispose();
    }
}
