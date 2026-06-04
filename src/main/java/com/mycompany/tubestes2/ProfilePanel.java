/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tubestes2;
import javax.swing.*;
import java.awt.*;
import java.util.List;
/**
 *
 * @author salma
 */
public class ProfilePanel extends UIComponent{
    private JLabel nameLabel;
    private JLabel avgWpmLabel;
    private JLabel maxWpmLabel;
    private JLabel avgAccLabel;
    private JLabel totalGamesLabel;

    public ProfilePanel(Runnable onBack) {
        super(Theme.BG);
        setLayout(new GridBagLayout());

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setOpaque(false);

        nameLabel = new JLabel("Profil Pengguna");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        nameLabel.setForeground(Theme.ACCENT);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel cardGrid = new JPanel(new GridLayout(2, 2, 15, 15));
        cardGrid.setOpaque(false);

        avgWpmLabel = new JLabel("0");
        maxWpmLabel = new JLabel("0");
        avgAccLabel = new JLabel("0%");
        totalGamesLabel = new JLabel("0");

        cardGrid.add(new StatCard("RATA-RATA WPM", avgWpmLabel));
        cardGrid.add(new StatCard("WPM TERTINGGI", maxWpmLabel));
        cardGrid.add(new StatCard("RATA-RATA AKURASI", avgAccLabel));
        cardGrid.add(new StatCard("TOTAL BERMAIN", totalGamesLabel));

        RoundedButton backBtn = new RoundedButton("Kembali ke Menu");
        backBtn.addActionListener(e -> onBack.run());
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(nameLabel);
        box.add(Box.createRigidArea(new Dimension(0, 30)));
        box.add(cardGrid);
        box.add(Box.createRigidArea(new Dimension(0, 30)));
        box.add(backBtn);

        add(box);
    }

    public void updateMetrics(String username, List<ScoreRecord> records) {
        nameLabel.setText("Profil: " + username);
        
        if (records == null || records.isEmpty()) {
            avgWpmLabel.setText("0");
            maxWpmLabel.setText("0");
            avgAccLabel.setText("0%");
            totalGamesLabel.setText("0");
            return;
        }

        int totalGames = records.size();
        int sumWpm = 0;
        int maxWpm = 0;
        double sumAcc = 0;

        for (ScoreRecord r : records) {
            sumWpm += r.wpm;
            if (r.wpm > maxWpm) maxWpm = r.wpm;
            sumAcc += r.accuracy;
        }

        avgWpmLabel.setText(String.valueOf(sumWpm / totalGames));
        maxWpmLabel.setText(String.valueOf(maxWpm));
        avgAccLabel.setText(String.format("%.1f%%", sumAcc / totalGames));
        totalGamesLabel.setText(String.valueOf(totalGames));
    }

    @Override
    public void render(Graphics g) {
        // Tata letak visual diatur secara modular melalui sub-komponen Swing
    }
}
