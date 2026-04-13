package baon.ui;

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
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
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
}
