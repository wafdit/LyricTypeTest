package com.mycompany.LyricType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;
import java.sql.SQLException;

/**
 * Profile panel styled after MonkeyType's profile page.
 */
public class ProfilePanel extends UIComponent {
    private JLabel usernameLabel;
    private JLabel levelLabel;
    private JProgressBar levelBar;
    private JLabel testsStartedVal;
    private JLabel testsCompletedVal;
    private JLabel avgWpmVal;
    private JLabel bestWpmVal;
    private JLabel avgAccVal;
    private WpmChart chart;
    private static final Color CARD_BG   = new Color(0x1C, 0x14, 0x11);
    private static final Color DIVIDER   = new Color(0xFF, 0xFF, 0xFF, 18);
    private static final Color STAT_MUTED = new Color(0xFF, 0xF3, 0xEC, 90);

    public ProfilePanel(Runnable onBack, Runnable onLogout) {
        super(Theme.BG);
        setLayout(new BorderLayout(0, 0));
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        JPanel headerCard = buildHeaderCard();
        headerCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        chart = new WpmChart();
        chart.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setOpaque(false);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        RoundedButton backBtn = new RoundedButton("Back");
        backBtn.addActionListener(e -> onBack.run());
        RoundedButton logoutBtn = new RoundedButton("Logout") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(
                    Theme.HIGHLIGHT_ERR.getRed(),
                    Theme.HIGHLIGHT_ERR.getGreen(),
                    Theme.HIGHLIGHT_ERR.getBlue(), 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(Theme.HIGHLIGHT_ERR);
                g2.setStroke(new java.awt.BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        logoutBtn.setForeground(Theme.HIGHLIGHT_ERR);
        logoutBtn.setOpaque(false);
        logoutBtn.addActionListener(e -> onLogout.run());

        btnRow.add(backBtn);
        btnRow.add(logoutBtn);

        content.add(headerCard);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(chart);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(btnRow);
        JPanel fixedWidth = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        fixedWidth.setOpaque(false);
        content.setMaximumSize(new Dimension(860, Integer.MAX_VALUE));
        content.setPreferredSize(new Dimension(860, content.getPreferredSize().height));
        fixedWidth.add(content);

        JScrollPane scroll = new JScrollPane(fixedWidth);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }
    private JPanel buildHeaderCard() {
        JPanel card = new JPanel(new BorderLayout(20, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 24));
        JPanel leftCol = new JPanel();
        leftCol.setLayout(new BoxLayout(leftCol, BoxLayout.Y_AXIS));
        leftCol.setOpaque(false);

        AvatarPanel avatar = new AvatarPanel(64);
        avatar.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameLabel = new JLabel("—");
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        usernameLabel.setForeground(Theme.TEXT_MAIN);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        levelLabel = new JLabel("Level 1");
        levelLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        levelLabel.setForeground(STAT_MUTED);
        levelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        levelBar = new JProgressBar(0, 300);
        levelBar.setValue(0);
        levelBar.setStringPainted(false);
        levelBar.setOpaque(false);
        levelBar.setForeground(Theme.ACCENT);
        levelBar.setBackground(new Color(0x40, 0x30, 0x2A));
        levelBar.setMaximumSize(new Dimension(180, 5));
        levelBar.setPreferredSize(new Dimension(180, 5));
        levelBar.setBorder(null);
        levelBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftCol.add(avatar);
        leftCol.add(Box.createRigidArea(new Dimension(0, 10)));
        leftCol.add(usernameLabel);
        leftCol.add(Box.createRigidArea(new Dimension(0, 4)));
        leftCol.add(levelLabel);
        leftCol.add(Box.createRigidArea(new Dimension(0, 6)));
        leftCol.add(levelBar);
        JPanel statsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statsRow.setOpaque(false);

        testsStartedVal   = makeStatVal("0");
        testsCompletedVal = makeStatVal("0");
        avgWpmVal         = makeStatVal("0");
        bestWpmVal        = makeStatVal("0");
        avgAccVal         = makeStatVal("0%");

        statsRow.add(statBlock("tests started",   testsStartedVal));
        statsRow.add(vertDivider());
        statsRow.add(statBlock("tests completed", testsCompletedVal));
        statsRow.add(vertDivider());
        statsRow.add(statBlock("avg wpm",         avgWpmVal));
        statsRow.add(vertDivider());
        statsRow.add(statBlock("best wpm",        bestWpmVal));
        statsRow.add(vertDivider());
        statsRow.add(statBlock("avg accuracy",    avgAccVal));

        card.add(leftCol,  BorderLayout.WEST);
        card.add(statsRow, BorderLayout.CENTER);
        return card;
    }

    private JPanel statBlock(String title, JLabel valLabel) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(4, 22, 4, 22));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        titleLbl.setForeground(STAT_MUTED);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        valLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(titleLbl);
        p.add(Box.createRigidArea(new Dimension(0, 4)));
        p.add(valLabel);
        return p;
    }

    private JLabel makeStatVal(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 30));
        l.setForeground(Theme.TEXT_MAIN);
        return l;
    }

    private Component vertDivider() {
        JPanel d = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(DIVIDER);
                g.fillRect(0, 8, 1, getHeight() - 16);
            }
        };
        d.setOpaque(false);
        d.setPreferredSize(new Dimension(1, 60));
        return d;
    }
    public void updateMetrics(String username, List<ScoreRecord> records) {
        usernameLabel.setText(username);

        try {
            UserProgress progress = DatabaseManager.getUserProgress(username);
            int lvl = progress.level;
            int pts = progress.points;
            int ptsInLevel = pts % 300;
            levelLabel.setText("Level " + lvl + "  ·  " + ptsInLevel + " / 300 pts");
            levelBar.setValue(ptsInLevel);
        } catch (SQLException e) {
            levelLabel.setText("Level 1");
            levelBar.setValue(0);
        }

        int total = records == null ? 0 : records.size();
        testsStartedVal.setText(String.valueOf(total));
        testsCompletedVal.setText(String.valueOf(total));

        if (total == 0) {
            avgWpmVal.setText("0");
            bestWpmVal.setText("0");
            avgAccVal.setText("0%");
            chart.setData(new java.util.ArrayList<>());
            return;
        }

        int sumWpm = 0, maxWpm = 0;
        double sumAcc = 0;
        for (ScoreRecord r : records) {
            sumWpm += r.wpm;
            if (r.wpm > maxWpm) maxWpm = r.wpm;
            sumAcc += r.accuracy;
        }

        avgWpmVal.setText(String.valueOf(sumWpm / total));
        bestWpmVal.setText(String.valueOf(maxWpm));
        avgAccVal.setText(String.format("%.1f%%", sumAcc / total));
        chart.setData(records);
    }

    @Override public void render(Graphics g) {}
    private static class AvatarPanel extends JPanel {
        private final int size;
        AvatarPanel(int size) {
            this.size = size;
            setOpaque(false);
            setPreferredSize(new Dimension(size, size));
            setMaximumSize(new Dimension(size, size));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0x35, 0x26, 0x22));
            g2.fillOval(0, 0, size, size);
            g2.setColor(new Color(Theme.ACCENT.getRed(), Theme.ACCENT.getGreen(), Theme.ACCENT.getBlue(), 80));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(1, 1, size - 2, size - 2);
            g2.setColor(new Color(Theme.TEXT_MAIN.getRed(), Theme.TEXT_MAIN.getGreen(), Theme.TEXT_MAIN.getBlue(), 140));
            int hs = size / 4;
            int hx = size / 2 - hs / 2, hy = size / 4;
            g2.fillOval(hx, hy, hs, hs);
            int bw = size / 2, bh = size / 3;
            int bx = size / 2 - bw / 2, by = hy + hs + 2;
            g2.fillArc(bx, by, bw, bh, 0, 180);
            g2.dispose();
        }
    }
    private static class WpmChart extends JPanel {
        private List<ScoreRecord> data = new java.util.ArrayList<>();

        private static final Color LINE_WPM  = new Color(0xA8, 0xC4, 0xE0);
        private static final Color LINE_ACC  = new Color(0x7A, 0x8A, 0x6A);
        private static final Color DOT_WPM   = new Color(0xC8, 0xD8, 0xF0, 180);
        private static final Color GRID_CLR  = new Color(0xFF, 0xFF, 0xFF, 14);
        private static final Color CHART_BG  = new Color(0x1C, 0x14, 0x11);

        WpmChart() {
            setOpaque(false);
            setPreferredSize(new Dimension(800, 220));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        }

        void setData(List<ScoreRecord> d) {
            this.data = d;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int W = getWidth(), H = getHeight();
            int padL = 46, padR = 46, padT = 18, padB = 32;
            int cW = W - padL - padR, cH = H - padT - padB;
            g2.setColor(CHART_BG);
            g2.fillRoundRect(0, 0, W, H, 16, 16);
            drawLegend(g2, W - padR - 130, padT);

            if (data == null || data.isEmpty()) {
                g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
                g2.setColor(new Color(Theme.TEXT_MAIN.getRed(), Theme.TEXT_MAIN.getGreen(), Theme.TEXT_MAIN.getBlue(), 80));
                String msg = "No data yet — complete a typing test to see your chart";
                int tw = g2.getFontMetrics().stringWidth(msg);
                g2.drawString(msg, (W - tw) / 2, H / 2);
                g2.dispose();
                return;
            }
            int maxWpm = data.stream().mapToInt(r -> r.wpm).max().orElse(1);
            maxWpm = Math.max(maxWpm + 10, 60);
            g2.setStroke(new BasicStroke(1f));
            g2.setFont(new Font("Monospaced", Font.PLAIN, 10));
            int gridLines = 5;
            for (int i = 0; i <= gridLines; i++) {
                int y = padT + (int)((double) i / gridLines * cH);
                g2.setColor(GRID_CLR);
                g2.drawLine(padL, y, padL + cW, y);
                int wpmTick = (int)(maxWpm * (1.0 - (double) i / gridLines));
                g2.setColor(new Color(LINE_WPM.getRed(), LINE_WPM.getGreen(), LINE_WPM.getBlue(), 150));
                g2.drawString(String.valueOf(wpmTick), 4, y + 4);
                int accTick = (int)(100.0 * (1.0 - (double) i / gridLines));
                g2.setColor(new Color(LINE_ACC.getRed(), LINE_ACC.getGreen(), LINE_ACC.getBlue(), 150));
                String accStr = accTick + "%";
                g2.drawString(accStr, W - padR + 4, y + 4);
            }

            int n = data.size();
            double step = (n <= 1) ? cW : (double) cW / (n - 1);
            int[] xp = new int[n], ywpm = new int[n], yacc = new int[n];
            for (int i = 0; i < n; i++) {
                xp[i]   = padL + (int)(i * step);
                ywpm[i] = padT + (int)((1.0 - (double) data.get(i).wpm / maxWpm) * cH);
                yacc[i] = padT + (int)((1.0 - data.get(i).accuracy / 100.0) * cH);
            }
            drawSmoothLine(g2, xp, yacc, n,
                new Color(LINE_ACC.getRed(), LINE_ACC.getGreen(), LINE_ACC.getBlue(), 130), 1.8f);
            drawSmoothLine(g2, xp, ywpm, n, LINE_WPM, 2.2f);
            g2.setColor(DOT_WPM);
            for (int i = 0; i < n; i++) {
                g2.fillOval(xp[i] - 3, ywpm[i] - 3, 6, 6);
            }
            g2.setFont(new Font("Monospaced", Font.PLAIN, 10));
            g2.setColor(new Color(Theme.TEXT_MAIN.getRed(), Theme.TEXT_MAIN.getGreen(), Theme.TEXT_MAIN.getBlue(), 60));
            int labelStep = Math.max(1, n / 8);
            for (int i = 0; i < n; i += labelStep) {
                g2.drawString(String.valueOf(i + 1), xp[i] - 3, H - 8);
            }

            g2.dispose();
        }

        private void drawSmoothLine(Graphics2D g2, int[] x, int[] y, int n, Color c, float width) {
            if (n < 2) return;
            g2.setColor(c);
            g2.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            GeneralPath path = new GeneralPath();
            path.moveTo(x[0], y[0]);
            for (int i = 1; i < n; i++) {
                float cx1 = x[i-1] + (x[i] - x[i-1]) / 2f;
                path.curveTo(cx1, y[i-1], cx1, y[i], x[i], y[i]);
            }
            g2.draw(path);
        }

        private void drawLegend(Graphics2D g2, int x, int y) {
            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2.setColor(LINE_WPM);
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(x, y + 6, x + 18, y + 6);
            g2.setColor(new Color(Theme.TEXT_MAIN.getRed(), Theme.TEXT_MAIN.getGreen(), Theme.TEXT_MAIN.getBlue(), 140));
            g2.drawString("wpm", x + 22, y + 10);
            g2.setColor(LINE_ACC);
            g2.drawLine(x + 60, y + 6, x + 78, y + 6);
            g2.setColor(new Color(Theme.TEXT_MAIN.getRed(), Theme.TEXT_MAIN.getGreen(), Theme.TEXT_MAIN.getBlue(), 140));
            g2.drawString("accuracy", x + 82, y + 10);
        }
    }
}