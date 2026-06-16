/*
 * Click nbfs:
 * Click nbfs:
 */
package com.mycompany.LyricType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author salma
 */
public class VirtualKeyboard extends JPanel{
    private char activeKey = '\0';
    private int activeKeyCode = -1;
    private final Map<String, Rectangle> keyBounds = new HashMap<>();
    private int lastWidth = -1;
    
    private String[][] currentLayout;
    private final String[][] qwerty = {
        {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]"},
        {"a", "s", "d", "f", "g", "h", "j", "k", "l", ";", "'"},
        {"z", "x", "c", "v", "b", "n", "m", ",", ".", "/"},
        {"space"}
    };

    private final String[][] qwertz = {
        {"q", "w", "e", "r", "t", "z", "u", "i", "o", "p", "[", "]"},
        {"a", "s", "d", "f", "g", "h", "j", "k", "l", ";", "'"},
        {"y", "x", "c", "v", "b", "n", "m", ",", ".", "/"},
        {"space"}
    };

    private final String[][] azerty = {
        {"a", "z", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]"},
        {"q", "s", "d", "f", "g", "h", "j", "k", "l", ";", "'"},
        {"w", "x", "c", "v", "b", "n", "m", ",", ".", "/"},
        {"space"}
    };

    private final String[][] dvorak = {
        {"'", ",", ".", "p", "y", "f", "g", "c", "r", "l", "[", "]"},
        {"a", "o", "e", "u", "i", "d", "h", "t", "n", "s", "-"},
        {";", "q", "j", "k", "x", "b", "m", "w", "v", "z"},
        {"space"}
    };

    private final String[][] colemak = {
        {"q", "w", "f", "p", "g", "j", "l", "u", "y", ";", "[", "]"},
        {"a", "r", "s", "t", "d", "h", "n", "e", "i", "o", "'"},
        {"z", "x", "c", "v", "b", "k", "m", ",", ".", "/"},
        {"space"}
    };

    public VirtualKeyboard() {
        this.currentLayout = qwerty;
        setPreferredSize(new Dimension(680, 190)); 
        setOpaque(false);
    }

    /**
     * Mengubah preferensi layout keyboard secara real-time.
     * * @param layoutName nama layout pilihan (QWERTY, QWERTZ, dll)
     */
    public void changeLayout(String layoutName) {
        switch (layoutName.toUpperCase()) {
            case "QWERTZ": this.currentLayout = qwertz; break;
            case "AZERTY": this.currentLayout = azerty; break;
            case "DVORAK": this.currentLayout = dvorak; break;
            case "COLEMAK": this.currentLayout = colemak; break;
            default: this.currentLayout = qwerty; break;
        }
        calculateLayout(getWidth());
        repaint();
    }

    public void pressKey(char c, int keyCode) {
        this.activeKey = Character.toLowerCase(c);
        this.activeKeyCode = keyCode;
        repaint();
    }

    public void releaseKey() {
        this.activeKey = '\0';
        this.activeKeyCode = -1;
        repaint();
    }

    private void calculateLayout(int panelWidth) {
        keyBounds.clear();
        int keySize = 36;
        int gap = 6;
        int startY = 10;

        for (int row = 0; row < currentLayout.length; row++) {
            int rowWidth;
            if (row == 3) {
                rowWidth = 280;
            } else {
                rowWidth = (currentLayout[row].length * keySize) + ((currentLayout[row].length - 1) * gap);
            }

            int startX = (panelWidth - rowWidth) / 2;

            for (int col = 0; col < currentLayout[row].length; col++) {
                String key = currentLayout[row][col];
                if (key.equals("space")) {
                    keyBounds.put(key, new Rectangle(startX, startY, 280, keySize));
                } else {
                    keyBounds.put(key, new Rectangle(startX + col * (keySize + gap), startY, keySize, keySize));
                }
            }
            startY += keySize + gap;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getWidth() != lastWidth) {
            calculateLayout(getWidth());
            lastWidth = getWidth();
        }
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("SansSerif", Font.BOLD, 13)); 
        FontMetrics fm = g2.getFontMetrics();

        for (Map.Entry<String, Rectangle> entry : keyBounds.entrySet()) {
            String key = entry.getKey();
            Rectangle r = entry.getValue();

            boolean isPressed = false;
            if (key.equals("space") && (activeKey == ' ' || activeKeyCode == KeyEvent.VK_SPACE)) {
                isPressed = true;
            } else if (key.length() == 1 && activeKey == key.charAt(0)) {
                isPressed = true;
            }

            if (isPressed) {
                g2.setColor(Theme.ACCENT);
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 8, 8);
                g2.setColor(Theme.BUTTON_TEXT);
            } else {
                g2.setColor(Theme.BG_DARK);
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 8, 8);
                g2.setColor(Theme.BG);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(r.x, r.y, r.width, r.height, 8, 8);
                g2.setColor(Theme.INPUT_TEXT);
            }

            String displayTxt = key.equals("space") ? "" : key.toUpperCase();
            int tx = r.x + (r.width - fm.stringWidth(displayTxt)) / 2;
            int ty = r.y + (r.height - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(displayTxt, tx, ty);
        }
    }
}