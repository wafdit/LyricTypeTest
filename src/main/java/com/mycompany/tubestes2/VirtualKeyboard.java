/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tubestes2;
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
    
    // Layout QWERTY lengkap (tanpa modifier keys untuk estetika minimalis)
    private final String[][] layout = {
        {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]"},
        {"a", "s", "d", "f", "g", "h", "j", "k", "l", ";", "'"},
        {"z", "x", "c", "v", "b", "n", "m", ",", ".", "/"},
        {"space"}
    };
    
    private final Map<String, Rectangle> keyBounds = new HashMap<>();
    private int lastWidth = -1; // Deteksi perubahan ukuran panel

    public VirtualKeyboard() {
        // Memberikan ruang vertikal yang cukup untuk 4 baris tombol
        setPreferredSize(new Dimension(800, 280)); 
        setOpaque(false);
    }

    public void pressKey(char c, int keyCode) {
        this.activeKey = Character.toLowerCase(c);
        this.activeKeyCode = keyCode;
        repaint(); // Update visual langsung
    }

    public void releaseKey() {
        this.activeKey = '\0';
        this.activeKeyCode = -1;
        repaint(); // Kembalikan ke visual normal
    }

    private void calculateLayout(int panelWidth) {
        keyBounds.clear();
        int keySize = 50;  // Ukuran kotak tombol yang lebih besar & modern
        int gap = 10;      // Jarak antar tombol
        int startY = 20;   // Padding atas

        for (int row = 0; row < layout.length; row++) {
            int rowWidth;
            
            if (row == 3) {
                rowWidth = 380; // Lebar tombol spasi dibuat statis memanjang
            } else {
                rowWidth = (layout[row].length * keySize) + ((layout[row].length - 1) * gap);
            }

            // Memusatkan baris (Centering). 
            // Karena jumlah tombol tiap baris berbeda, trik ini otomatis 
            // menciptakan efek susunan bergeser (staggered) layaknya keyboard mekanik.
            int startX = (panelWidth - rowWidth) / 2;

            for (int col = 0; col < layout[row].length; col++) {
                String key = layout[row][col];
                if (key.equals("space")) {
                    keyBounds.put(key, new Rectangle(startX, startY, 380, keySize));
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
        
        // Kalkulasi ulang tata letak HANYA jika ukuran panel/window berubah.
        // Ini adalah teknik optimasi agar tidak terus-menerus membuat objek Rectangle baru.
        if (getWidth() != lastWidth) {
            calculateLayout(getWidth());
            lastWidth = getWidth();
        }
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setFont(new Font("SansSerif", Font.BOLD, 18)); // Font yang lebih tegas
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

            // Menerapkan Color Scheme dari kelas Theme
            if (isPressed) {
                // Style saat tombol ditekan
                g2.setColor(Theme.ACCENT);
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 14, 14);
                
                g2.setColor(Theme.BG_DARK); // Teks berubah gelap
            } else {
                // Style normal (Idle)
                g2.setColor(Theme.BG_DARK);
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 14, 14);
                
                // Outline tipis (opsional, memisahkan tombol dengan background panel utama)
                g2.setColor(Theme.BG);
                g2.setStroke(new BasicStroke(2.0f));
                g2.drawRoundRect(r.x, r.y, r.width, r.height, 14, 14);
                
                g2.setColor(Theme.TEXT_MAIN); // Teks warna utama (terang)
            }

            // Menggambar teks label pas di tengah-tengah tombol
            String displayTxt = key.equals("space") ? "" : key.toUpperCase();
            int tx = r.x + (r.width - fm.stringWidth(displayTxt)) / 2;
            int ty = r.y + (r.height - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(displayTxt, tx, ty);
        }
    }
}
