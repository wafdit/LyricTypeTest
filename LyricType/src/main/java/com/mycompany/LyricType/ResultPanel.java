package com.mycompany.LyricType;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.InputStream;

/**
 * Results panel - MonkeyType-inspired layout with local album art.
 */
public class ResultPanel extends UIComponent {
    private String wpmLabel;
    private String accLabel;

    private JLabel wpmValueLabel;
    private JLabel accValueLabel;

    private JLabel songTitleLabel;
    private JLabel songArtistLabel;
    private JLabel songAlbumLabel;
    private AlbumArtPanel albumArtPanel;
    private static final Color CARD_BG   = new Color(0x1C, 0x14, 0x11);
    private static final Color STAT_CARD = new Color(0x28, 0x1C, 0x18);

    public ResultPanel(Runnable onNextSong) {
        super(Theme.BG);
        setLayout(new GridBagLayout());

        JPanel contentWrapper = new JPanel(new GridBagLayout());
        contentWrapper.setOpaque(false);
        JPanel leftCard = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        leftCard.setLayout(new BoxLayout(leftCard, BoxLayout.Y_AXIS));
        leftCard.setOpaque(false);
        leftCard.setBorder(BorderFactory.createEmptyBorder(32, 36, 32, 36));

        JLabel title = new JLabel("Great Job!");
        title.setFont(new Font("SansSerif", Font.BOLD, 34));
        title.setForeground(Theme.TEXT_MAIN);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Here's your typing performance.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Theme.TERTIARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel statsRow = new JPanel(new GridLayout(1, 2, 14, 0));
        statsRow.setOpaque(false);
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsRow.setMaximumSize(new Dimension(320, 90));

        wpmValueLabel = new JLabel("0");
        accValueLabel = new JLabel("0%");
        statsRow.add(buildStatPill("wpm",      wpmValueLabel));
        statsRow.add(buildStatPill("accuracy", accValueLabel));



        RoundedButton nextBtn = new RoundedButton("Type Next Song");
        nextBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        nextBtn.addActionListener(e -> onNextSong.run());

        leftCard.add(title);
        leftCard.add(Box.createRigidArea(new Dimension(0, 4)));
        leftCard.add(subtitle);
        leftCard.add(Box.createRigidArea(new Dimension(0, 28)));
        leftCard.add(statsRow);
        leftCard.add(Box.createRigidArea(new Dimension(0, 24)));
        leftCard.add(nextBtn);
        JPanel rightCard = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        rightCard.setLayout(new BoxLayout(rightCard, BoxLayout.Y_AXIS));
        rightCard.setOpaque(false);
        rightCard.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));

        albumArtPanel = new AlbumArtPanel(200);
        albumArtPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        songTitleLabel = new JLabel("Song Title");
        songTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        songTitleLabel.setForeground(Theme.TEXT_MAIN);
        songTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        songArtistLabel = new JLabel("Artist");
        songArtistLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        songArtistLabel.setForeground(Theme.ACCENT);
        songArtistLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        songAlbumLabel = new JLabel("ALBUM");
        songAlbumLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        songAlbumLabel.setForeground(new Color(Theme.TEXT_MAIN.getRed(),
                Theme.TEXT_MAIN.getGreen(), Theme.TEXT_MAIN.getBlue(), 80));
        songAlbumLabel.setAlignmentX(Component.CENTER_ALIGNMENT);



        rightCard.add(albumArtPanel);
        rightCard.add(Box.createRigidArea(new Dimension(0, 20)));
        rightCard.add(songTitleLabel);
        rightCard.add(Box.createRigidArea(new Dimension(0, 4)));
        rightCard.add(songArtistLabel);
        rightCard.add(Box.createRigidArea(new Dimension(0, 4)));
        rightCard.add(songAlbumLabel);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(0, 0, 0, 20);
        gc.fill = GridBagConstraints.VERTICAL;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridy = 0;
        gc.gridx = 0;
        contentWrapper.add(leftCard, gc);
        gc.insets = new Insets(0, 0, 0, 0);
        gc.gridx = 1;
        contentWrapper.add(rightCard, gc);

        add(contentWrapper);
    }

    private JPanel buildStatPill(String label, JLabel valueLabel) {
        JPanel pill = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(STAT_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
            }
        };
        pill.setLayout(new BoxLayout(pill, BoxLayout.Y_AXIS));
        pill.setOpaque(false);
        pill.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 46));
        valueLabel.setForeground(Theme.TEXT_MAIN);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lbl.setForeground(new Color(Theme.TEXT_MAIN.getRed(),
                Theme.TEXT_MAIN.getGreen(), Theme.TEXT_MAIN.getBlue(), 90));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        pill.add(valueLabel);
        pill.add(Box.createRigidArea(new Dimension(0, 2)));
        pill.add(lbl);
        return pill;
    }

    public void updateStats(Typing t) {
        this.wpmLabel = String.valueOf((int) Math.round(t.calculateWPM()));
        this.accLabel = String.valueOf((int) Math.round(t.calculateAccuracy()));
        wpmValueLabel.setText(this.wpmLabel);
        accValueLabel.setText(this.accLabel + "%");

        Song song = t.getCurrentSong();
        songTitleLabel.setText(song.getTitle());
        songArtistLabel.setText(song.getArtist());
        songAlbumLabel.setText(song.getAlbum().toUpperCase());
        String coverPath = getCoverPath(song.getTitle());
        albumArtPanel.loadImage(coverPath);
    }

    private String getCoverPath(String title) {
        switch (title) {
            case "Pink + White": return "/covers/blonde.jpg";
            case "Who Knows":   return "/covers/son_of_spergy.png";
            case "Promise":     return "/covers/bewitched.png";
            default:            return null;
        }
    }

    @Override public void render(Graphics g) {}
    static class AlbumArtPanel extends JPanel {
        private BufferedImage image;
        private final int size;

        AlbumArtPanel(int size) {
            this.size = size;
            setOpaque(false);
            setPreferredSize(new Dimension(size, size));
            setMaximumSize(new Dimension(size, size));
        }

        void loadImage(String resourcePath) {
            image = null;
            repaint();
            if (resourcePath == null) return;
            new SwingWorker<BufferedImage, Void>() {
                @Override protected BufferedImage doInBackground() throws Exception {
                    InputStream is = ResultPanel.class.getResourceAsStream(resourcePath);
                    if (is == null) return null;
                    return ImageIO.read(is);
                }
                @Override protected void done() {
                    try { image = get(); repaint(); } catch (Exception ignored) {}
                }
            }.execute();
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            if (image != null) {
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, size, size, 18, 18));
                g2.drawImage(image, 0, 0, size, size, null);
            } else {
                g2.setColor(new Color(0x35, 0x26, 0x22));
                g2.fillRoundRect(0, 0, size, size, 18, 18);
                g2.setColor(new Color(Theme.TEXT_MAIN.getRed(),
                        Theme.TEXT_MAIN.getGreen(), Theme.TEXT_MAIN.getBlue(), 40));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 40));
                g2.drawString("♫", size / 2 - 12, size / 2 + 14);
            }
            g2.dispose();
        }
    }
}
