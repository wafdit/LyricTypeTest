/*
 * Click nbfs:
 * Click nbfs:
 */
package com.mycompany.LyricType;
import java.awt.*;
/**
 *
 * @author salma
 */
public class RoundedPanel extends UIComponent {
    private int radius;

    public RoundedPanel(int radius, Color bgColor) {
        super(bgColor);
        this.radius = radius;
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (bgColor != null) {
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
        g2.dispose();
    }
}
