package com.mycompany.LyricType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Modern UI components with full rounded corner support.
 */
public class ModernComponents {
    public static class ModernTextField extends JTextField {
        private static final int RADIUS = 14;

        public ModernTextField(int cols) {
            super(cols);
            setOpaque(false);
            setBackground(Theme.BG_DARK);
            setForeground(Theme.INPUT_TEXT);
            setCaretColor(Theme.ACCENT);
            setFont(new Font("SansSerif", Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Theme.BG_DARK);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS, RADIUS);
            Color border = isFocusOwner() ? Theme.ACCENT : new Color(Theme.ACCENT.getRed(), Theme.ACCENT.getGreen(), Theme.ACCENT.getBlue(), 90);
            g2.setColor(border);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS, RADIUS);
            g2.dispose();
            super.paintComponent(g);
        }
    }
    public static class ModernPasswordField extends JPasswordField {
        private static final int RADIUS = 14;

        public ModernPasswordField(int cols) {
            super(cols);
            setOpaque(false);
            setBackground(Theme.BG_DARK);
            setForeground(Theme.INPUT_TEXT);
            setCaretColor(Theme.ACCENT);
            setFont(new Font("SansSerif", Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Theme.BG_DARK);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS, RADIUS);
            Color border = isFocusOwner() ? Theme.ACCENT : new Color(Theme.ACCENT.getRed(), Theme.ACCENT.getGreen(), Theme.ACCENT.getBlue(), 90);
            g2.setColor(border);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS, RADIUS);
            g2.dispose();
            super.paintComponent(g);
        }
    }
    public static class ModernDropdown extends JPanel {
        private final String[] options;
        private int selectedIndex = 0;
        private boolean hovered = false;

        private static final int RADIUS = 14;

        public ModernDropdown(String[] options) {
            this.options = options;
            setOpaque(false);
            setPreferredSize(new Dimension(190, 40));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { showPopup(); }
                @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            });
        }

        private void showPopup() {
            JPopupMenu popup = new JPopupMenu() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Theme.BG_DARK);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS, RADIUS);
                    g2.dispose();
                }
            };
            popup.setOpaque(false);
            popup.setBorderPainted(false);
            popup.setBackground(Theme.BG_DARK);
            popup.setBorder(BorderFactory.createCompoundBorder(
                new RoundedPopupBorder(RADIUS, Theme.ACCENT),
                BorderFactory.createEmptyBorder(4, 0, 4, 0)
            ));

            for (int i = 0; i < options.length; i++) {
                final int idx = i;
                final boolean isSelected = (i == selectedIndex);
                JMenuItem item = new JMenuItem(options[i]) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        if (isArmed()) {
                            g2.setColor(new Color(255, 255, 255, 20));
                        } else if (isSelected) {
                            g2.setColor(new Color(255, 255, 255, 10));
                        } else {
                            g2.setColor(Theme.BG_DARK);
                        }
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
                        g2.setColor(isArmed() || isSelected ? Theme.ACCENT : Theme.INPUT_TEXT);
                        FontMetrics fm = g2.getFontMetrics();
                        int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                        g2.drawString(getText(), 14, ty);
                        g2.dispose();
                    }
                };
                item.setOpaque(false);
                item.setBorderPainted(false);
                item.setFont(new Font("SansSerif", Font.PLAIN, 14));
                item.setForeground(Theme.INPUT_TEXT);
                item.setBackground(Theme.BG_DARK);
                item.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
                item.addActionListener(e -> { selectedIndex = idx; repaint(); });
                popup.add(item);
            }

            popup.show(this, 0, getHeight() + 4);
        }

        public String getSelectedItem() { return options[selectedIndex]; }

        public void setSelectedItem(String value) {
            for (int i = 0; i < options.length; i++) {
                if (options[i].equals(value)) { selectedIndex = i; repaint(); break; }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Theme.BG_DARK);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS, RADIUS);
            if (hovered) {
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS, RADIUS);
            }
            g2.setColor(new Color(Theme.ACCENT.getRed(), Theme.ACCENT.getGreen(), Theme.ACCENT.getBlue(), 100));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS, RADIUS);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
            g2.setColor(Theme.INPUT_TEXT);
            FontMetrics fm = g2.getFontMetrics();
            int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(options[selectedIndex], 14, ty);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2.setColor(Theme.ACCENT);
            FontMetrics afm = g2.getFontMetrics();
            String arrow = "▾";
            int ax = getWidth() - afm.stringWidth(arrow) - 12;
            g2.drawString(arrow, ax, ty);

            g2.dispose();
        }
        private static class RoundedPopupBorder implements javax.swing.border.Border {
            private final int r; private final Color c;
            RoundedPopupBorder(int r, Color c) { this.r = r; this.c = c; }
            @Override public Insets getBorderInsets(Component comp) { return new Insets(r/2, r/2, r/2, r/2); }
            @Override public boolean isBorderOpaque() { return false; }
            @Override public void paintBorder(Component comp, Graphics g, int x, int y, int w, int h) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 80));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(x, y, w-1, h-1, r, r);
                g2.dispose();
            }
        }
    }
}
