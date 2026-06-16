/*
 * Click nbfs:
 * Click nbfs:
 */
package com.mycompany.LyricType;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author salma
 */
public class SettingsPanel extends UIComponent {
    private ModernComponents.ModernDropdown kbDropdown;
    private ThemeDropdown themeDropdown;

    public SettingsPanel(java.util.function.BiConsumer<String, String> onSave, Runnable onBack) {
        super(Theme.BG);
        setLayout(new GridBagLayout());

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        JLabel title = new JLabel("⚙ Settings");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Theme.ACCENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel optionsGrid = new JPanel(new GridLayout(2, 2, 30, 20));
        optionsGrid.setOpaque(false);
        optionsGrid.setBorder(BorderFactory.createEmptyBorder(30, 0, 40, 0));

        JLabel kbLabel = new JLabel("Keyboard layout:");
        kbLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        kbLabel.setForeground(Theme.TEXT_MAIN);

        String[] layouts = {"QWERTY", "QWERTZ", "AZERTY", "Dvorak", "Colemak"};
        kbDropdown = new ModernComponents.ModernDropdown(layouts);

        JLabel themeLabel = new JLabel("Theme:");
        themeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        themeLabel.setForeground(Theme.TEXT_MAIN);

        String[] themes = {"Coffee", "Blueberry", "Forest", "Cotton Candy"};
        themeDropdown = new ThemeDropdown(themes);

        optionsGrid.add(kbLabel);
        optionsGrid.add(kbDropdown);
        optionsGrid.add(themeLabel);
        optionsGrid.add(themeDropdown);

        RoundedButton saveBtn = new RoundedButton("Save & Back");
        saveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveBtn.addActionListener(e -> {
            onSave.accept(kbDropdown.getSelectedItem(), themeDropdown.getSelectedItem());
            onBack.run();
        });

        container.add(title);
        container.add(optionsGrid);
        container.add(saveBtn);

        add(container);
    }

    public void updateCurrentConfig(String currentLayout, String currentTheme) {
        kbDropdown.setSelectedItem(currentLayout);
        themeDropdown.setSelectedItem(currentTheme);
    }

    @Override
    public void render(Graphics g) {}
    private static class ThemeDropdown extends JPanel {
        private final String[] options;
        private int selectedIndex = 0;
        private boolean hovered = false;

        public ThemeDropdown(String[] options) {
            this.options = options;
            setOpaque(false);
            setPreferredSize(new Dimension(190, 40));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseClicked(java.awt.event.MouseEvent e) { showPopup(); }
                @Override public void mouseEntered(java.awt.event.MouseEvent e) { hovered = true; repaint(); }
                @Override public void mouseExited(java.awt.event.MouseEvent e) { hovered = false; repaint(); }
            });
        }

        private void showPopup() {
            JPopupMenu popup = new JPopupMenu() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Theme.BG_DARK);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                    g2.dispose();
                }
            };
            popup.setOpaque(false);
            popup.setBorderPainted(false);
            popup.setBackground(Theme.BG_DARK);
            popup.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

            for (int i = 0; i < options.length; i++) {
                final int idx = i;
                final boolean isSelected = (i == selectedIndex);
                JMenuItem item = new JMenuItem(options[i]) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        if (isArmed()) g2.setColor(new Color(255, 255, 255, 15));
                        else if (isSelected) g2.setColor(new Color(255, 255, 255, 10));
                        else g2.setColor(Theme.BG_DARK);
                        
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
                        g2.setColor(isArmed() || isSelected ? Theme.ACCENT : Theme.INPUT_TEXT);
                        FontMetrics fm = g2.getFontMetrics();
                        int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                        g2.drawString(getText(), 14, ty);
                        drawCircles(g2, getText(), getWidth() - 75, getHeight()/2);
                        g2.dispose();
                    }
                };
                item.setOpaque(false);
                item.setBorderPainted(false);
                item.setPreferredSize(new Dimension(190, 36));
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

        private void drawCircles(Graphics2D g2, String theme, int xOffset, int cy) {
            Color[] colors;
            switch(theme) {
                case "Blueberry": colors = new Color[]{new Color(0x16161A), new Color(0x7F5AF0), new Color(0x2CB67D), new Color(0xFFFFFF)}; break;
                case "Forest": colors = new Color[]{new Color(0x004643), new Color(0xF9BC60), new Color(0xE16162), new Color(0xFFFFFF)}; break;
                case "Cotton Candy": colors = new Color[]{new Color(0xFEC7D7), new Color(0xA786DF), new Color(0x0E172C), new Color(0xFFFFFF)}; break;
                default: colors = new Color[]{new Color(0x55423D), new Color(0xFFC0AD), new Color(0xE78FB3), new Color(0xFFFFFF)}; break;
            }
            for (int i = 0; i < colors.length; i++) {
                g2.setColor(colors[i]);
                g2.fillOval(xOffset + (i * 12), cy - 6, 12, 12);
                g2.setColor(new Color(0,0,0,100));
                g2.drawOval(xOffset + (i * 12), cy - 6, 12, 12);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Theme.BG_DARK);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            if (hovered) {
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            }

            g2.setColor(Theme.ACCENT);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
            g2.setColor(Theme.INPUT_TEXT);
            FontMetrics fm = g2.getFontMetrics();
            int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(options[selectedIndex], 14, ty);

            drawCircles(g2, options[selectedIndex], getWidth() - 75, getHeight()/2);

            g2.dispose();
        }
    }
}