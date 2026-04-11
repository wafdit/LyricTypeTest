/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tubestes1;

/**
 *
 * @author salma
 */
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ResultsPanel extends JPanel {
    private JLabel wpmValueLabel;
    private JLabel accValueLabel;
    private JLabel timeValueLabel;

    private JLabel songTitleLabel;
    private JLabel songArtistLabel;
    private JLabel songAlbumLabel;
    private JLabel imageLabel;

    public ResultsPanel(Runnable onNextSong) {
        setBackground(theme.BG);
        setLayout(new GridBagLayout()); 
        
        JPanel contentWrapper = new JPanel(new GridLayout(1, 2, 60, 0));
        contentWrapper.setOpaque(false);

        // --- LEFT COLUMN (STATS) ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Great Job!");
        title.setFont(new Font("SansSerif", Font.BOLD, 38));
        title.setForeground(theme.TEXT_MAIN);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Here's your typing performance.");
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        subtitle.setForeground(theme.TERTIARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel statsGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        statsGrid.setOpaque(false);
        statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        wpmValueLabel = new JLabel("0");
        accValueLabel = new JLabel("0%");
        statsGrid.add(new StatCard("WPM", wpmValueLabel));
        statsGrid.add(new StatCard("ACCURACY", accValueLabel));

        timeValueLabel = new JLabel("Completed in 0s");
        timeValueLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        timeValueLabel.setForeground(theme.TEXT_MUTED);

        RoundedPanel timePill = new RoundedPanel(20, theme.BG);
        timePill.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 8));
        timePill.setBorder(BorderFactory.createLineBorder(theme.BG_DARK, 1, true));
        timePill.add(timeValueLabel);
        timePill.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedButton nextBtn = new RoundedButton("Type Next Song");
        nextBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        nextBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        nextBtn.addActionListener(e -> onNextSong.run());

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(title);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(subtitle);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        leftPanel.add(statsGrid);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(timePill);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        leftPanel.add(nextBtn);
        leftPanel.add(Box.createVerticalGlue());

        // --- RIGHT COLUMN (MUSIC PLAYER) ---
        RoundedPanel rightPanel = new RoundedPanel(40, theme.BG_DARK);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(220, 220));
        imageLabel.setMaximumSize(new Dimension(220, 220));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        songTitleLabel = new JLabel("Song Title");
        songTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        songTitleLabel.setForeground(theme.TEXT_MAIN);
        songTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        songArtistLabel = new JLabel("Artist Name");
        songArtistLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        songArtistLabel.setForeground(theme.ACCENT);
        songArtistLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        songAlbumLabel = new JLabel("ALBUM NAME");
        songAlbumLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        songAlbumLabel.setForeground(theme.TEXT_MUTED);
        songAlbumLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel progressBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(theme.BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.setColor(theme.HIGHLIGHT_ERR);
                g2.fillRoundRect(0, 0, (int)(getWidth() * 0.35), getHeight(), getHeight(), getHeight());
                g2.dispose();
            }
        };
        progressBar.setPreferredSize(new Dimension(250, 6));
        progressBar.setMaximumSize(new Dimension(250, 6));
        progressBar.setOpaque(false);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(imageLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        rightPanel.add(songTitleLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(songArtistLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(songAlbumLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        rightPanel.add(progressBar);

        contentWrapper.add(leftPanel);
        contentWrapper.add(rightPanel);
        add(contentWrapper);
    }

    private String getCoverUrl(String title) {
        if (title.equals("Who Knows")) return "[https://images.unsplash.com/photo-1502862985311-64d1f274a7b7?auto=format&fit=crop&q=80&w=300](https://images.unsplash.com/photo-1502862985311-64d1f274a7b7?auto=format&fit=crop&q=80&w=300)";
        if (title.equals("Pink + White")) return "[https://images.unsplash.com/photo-1500462918059-b1a0cb512f1d?auto=format&fit=crop&q=80&w=300](https://images.unsplash.com/photo-1500462918059-b1a0cb512f1d?auto=format&fit=crop&q=80&w=300)";
        return "[https://images.unsplash.com/photo-1516280440502-0c9f80720525?auto=format&fit=crop&q=80&w=300](https://images.unsplash.com/photo-1516280440502-0c9f80720525?auto=format&fit=crop&q=80&w=300)"; 
    }

    public void updateStats(typing session, int timeLeft, int testDuration) {
        int[] stats = session.getStats(timeLeft, testDuration);
        wpmValueLabel.setText(String.valueOf(stats[0]));
        accValueLabel.setText(stats[1] + "%");
        timeValueLabel.setText("Completed in " + (testDuration - timeLeft) + "s");

        songTitleLabel.setText(session.currentSong.title);
        songArtistLabel.setText(session.currentSong.artist);
        songAlbumLabel.setText(session.currentSong.album.toUpperCase());

        imageLabel.setIcon(null);
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() {
                try {
                    Image rawImg = ImageIO.read(new URL(getCoverUrl(session.currentSong.title)))
                                          .getScaledInstance(220, 220, Image.SCALE_SMOOTH);
                    
                    BufferedImage rounded = new BufferedImage(220, 220, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = rounded.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.fillRoundRect(0, 0, 220, 220, 30, 30);
                    g2.setComposite(AlphaComposite.SrcIn);
                    g2.drawImage(rawImg, 0, 0, null);
                    g2.dispose();
                    return new ImageIcon(rounded);
                } catch (Exception e) {
                    return null; 
                }
            }
            @Override
            protected void done() {
                try { imageLabel.setIcon(get()); } catch(Exception e) {}
            }
        }.execute();
    }
}
