/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tubestes1;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author salma
 */

public class typingUI extends JPanel {
    private typing currentSession;

    public typingUI() {
        setBackground(theme.BG);
    }

    public void setSession(typing session) {
        this.currentSession = session;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentSession == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setFont(new Font("Monospaced", Font.BOLD, 28));
        FontMetrics fm = g2.getFontMetrics();

        int lineHeight = 55;
        int activeLineIdx = currentSession.getActiveLineIndex();
        
        int startY = (getHeight() / 2) - (lineHeight / 2) - 20;
        int globalCharIdx = 0;

        for (int i = 0; i < currentSession.lines.size(); i++) {
            String line = currentSession.lines.get(i);
            
            if (i < activeLineIdx - 1 || i > activeLineIdx + 2) {
                globalCharIdx += line.length();
                continue; 
            }

            int yPos = startY + ((i - activeLineIdx) * lineHeight);
            int lineWidth = fm.stringWidth(line);
            int xPos = (getWidth() / 2) - (lineWidth / 2);

            if (i < activeLineIdx) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));
            else if (i == activeLineIdx) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            else if (i == activeLineIdx + 1) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            else g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));

            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                int charWidth = fm.charWidth(c);
                
                boolean isTyped = globalCharIdx < currentSession.typedText.length();
                boolean isCursor = globalCharIdx == currentSession.typedText.length() && !currentSession.status.equals("finished");

                if (isTyped) {
                    char typedChar = currentSession.typedText.charAt(globalCharIdx);
                    if (typedChar == c) {
                        g2.setColor(theme.TEXT_MAIN);
                    } else {
                        g2.setColor(theme.HIGHLIGHT_ERR);
                        if (c == ' ') { 
                            g2.fillRect(xPos, yPos - fm.getAscent() + 5, charWidth, fm.getAscent() + 5);
                            g2.setColor(theme.BG_DARK); 
                        }
                    }
                } else {
                    g2.setColor(theme.TEXT_MUTED); 
                }

                g2.drawString(String.valueOf(c), xPos, yPos);

                if (isCursor) {
                    g2.setColor(theme.ACCENT);
                    g2.fillRect(xPos - 2, yPos - fm.getAscent() + 5, 3, fm.getAscent() + 5);
                }

                xPos += charWidth;
                globalCharIdx++;
            }
        }
    }
}
