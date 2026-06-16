package com.mycompany.LyricType;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class LeaderboardPanel extends UIComponent {
    private JPanel rankContainer;

    public LeaderboardPanel(Runnable onBack) {
        super(Theme.BG);
        setLayout(new GridBagLayout());

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);

        JLabel title = new JLabel("🏆 GLOBAL LEADERBOARD");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Theme.ACCENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        rankContainer = new JPanel();
        rankContainer.setLayout(new BoxLayout(rankContainer, BoxLayout.Y_AXIS));
        rankContainer.setOpaque(false);
        rankContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton backBtn = new RoundedButton("Back");
        backBtn.addActionListener(e -> onBack.run());
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        wrapper.add(title);
        wrapper.add(Box.createRigidArea(new Dimension(0, 25)));
        wrapper.add(rankContainer);
        wrapper.add(Box.createRigidArea(new Dimension(0, 35)));
        wrapper.add(backBtn);

        add(wrapper);
    }

    public void refreshLeaderboard() {
        rankContainer.removeAll();
        try {
            List<LeaderboardRow> leaderboard = DatabaseManager.getLeaderboard();
            int rank = 1;
            
            JPanel header = new JPanel(new GridLayout(1, 4, 30, 0));
            header.setOpaque(false);
            header.setPreferredSize(new Dimension(500, 30));
            header.add(createLabel("RANK", true));
            header.add(createLabel("USER", true));
            header.add(createLabel("LEVEL", true));
            header.add(createLabel("POINTS", true));
            rankContainer.add(header);
            rankContainer.add(Box.createRigidArea(new Dimension(0, 10)));

            for (LeaderboardRow row : leaderboard) {
                JPanel rowPanel = new RoundedPanel(15, Theme.BG_DARK);
                rowPanel.setLayout(new GridLayout(1, 4, 30, 0));
                rowPanel.setPreferredSize(new Dimension(500, 45));
                rowPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

                rowPanel.add(createLabel("#" + rank++, false));
                rowPanel.add(createLabel(row.username, false));
                rowPanel.add(createLabel("Lv. " + row.level, false));
                rowPanel.add(createLabel(row.points + " pts", false));

                rankContainer.add(rowPanel);
                rankContainer.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        } catch (SQLException e) {
            JLabel errLabel = new JLabel("Failed to load the leaderboard :(");
            errLabel.setForeground(Theme.TEXT_MUTED);
            errLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
            rankContainer.add(errLabel);
        }
        rankContainer.revalidate();
        rankContainer.repaint();
    }

    private JLabel createLabel(String text, boolean isHeader) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", isHeader ? Font.BOLD : Font.PLAIN, 15));
        lbl.setForeground(isHeader ? Theme.TEXT_MUTED : Theme.INPUT_TEXT);
        return lbl;
    }

    @Override
    public void render(Graphics g) {}
}