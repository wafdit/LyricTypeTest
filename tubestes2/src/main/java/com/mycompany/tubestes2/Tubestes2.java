/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tubestes2;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 *
 * @author salma
 */
public class Tubestes2 extends JFrame {
    public static final int TEST_DURATION = 30;
    private Typing currentSession;
    private List<Song> songDB;
    private String currentUser = null;

    private int timeLeft;
    private Timer gameTimer;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private typingUI typingPanel;
    private ResultPanel resultsPanel;
    private AuthPanel authPanel;
    private ProfilePanel profilePanel;
    private LeaderboardPanel leaderboardPanel;
    private VirtualKeyboard virtualKeyboard;
    
    private JLabel timerLabel;
    private JButton typingModeBtn;
    private RoundedButton profileMenuBtn;
    private RoundedButton leaderboardBtn;

    public Tubestes2() {
        setTitle("LyricType - OOP Edition");
        setSize(1000, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG);

        initDatabase();
        initComponents();
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                virtualKeyboard.pressKey(e.getKeyChar(), e.getKeyCode());
                if (currentSession == null || currentSession.getStatus().equals("finished")) return;
                
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    currentSession.handleBackspace();
                    typingPanel.repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    loadNewSong();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                virtualKeyboard.releaseKey();
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (currentSession == null || currentSession.getStatus().equals("finished")) return;
                
                char c = e.getKeyChar();
                if (c >= 32 && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    currentSession.handleCharacter(Character.toLowerCase(c));
                    if (currentSession.getStatus().equals("typing") && !gameTimer.isRunning()) {
                        gameTimer.start();
                        typingModeBtn.setForeground(Theme.ACCENT);
                    }
                    if (currentSession.getStatus().equals("finished")) {
                        showResults();
                    }
                    typingPanel.repaint();
                }
            }
        });

        setFocusable(true);
        loadNewSong();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 45, 0, 45));
        
        JLabel titleLabel = new JLabel("♫ LyricType");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Theme.ACCENT);

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightHeader.setOpaque(false);

        timerLabel = new JLabel(" ⏱ " + TEST_DURATION + "s ");
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
        timerLabel.setForeground(Theme.HIGHLIGHT_ERR);
        timerLabel.setOpaque(true);
        timerLabel.setBackground(Theme.BG_DARK);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        typingModeBtn = new JButton("TYPING");
        typingModeBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        typingModeBtn.setForeground(Theme.ACCENT);
        typingModeBtn.setBorderPainted(false);
        typingModeBtn.setContentAreaFilled(false);
        typingModeBtn.setFocusPainted(false);
        typingModeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        typingModeBtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "typing");
            requestFocusInWindow();
        });

        profileMenuBtn = new RoundedButton("👤 Profile");
        profileMenuBtn.setPreferredSize(new Dimension(130, 35));
        profileMenuBtn.addActionListener(e -> handleProfileMenuClick());

        leaderboardBtn = new RoundedButton("🏆 Leaderboard");
        leaderboardBtn.setPreferredSize(new Dimension(150, 35));
        leaderboardBtn.addActionListener(e -> {
            gameTimer.stop();
            leaderboardPanel.refreshLeaderboard();
            cardLayout.show(mainPanel, "leaderboard");
        });

        rightHeader.add(timerLabel);
        rightHeader.add(typingModeBtn);
        rightHeader.add(leaderboardBtn);
        rightHeader.add(profileMenuBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightHeader, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Theme.BG);

        authPanel = new AuthPanel(this::handleLogin, this::handleRegister, () -> {
            cardLayout.show(mainPanel, "typing");
            requestFocusInWindow();
        });
        
        profilePanel = new ProfilePanel(() -> {
            cardLayout.show(mainPanel, "typing");
            requestFocusInWindow();
        });

        leaderboardPanel = new LeaderboardPanel(() -> {
            cardLayout.show(mainPanel, "typing");
            requestFocusInWindow();
        });

        typingPanel = new typingUI();
        resultsPanel = new ResultPanel(this::loadNewSong);

        // --- KONFIGURASI LAYOUT ARENA BERMAIN (MONKEYTYPE STYLE) ---
        JPanel gameplayContainer = new JPanel(new GridBagLayout());
        gameplayContainer.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        // 1. Spacer Atas (Menekan teks turun agar simetris di tengah)
        gbc.gridy = 0;
        gbc.weighty = 0.12;
        gbc.fill = GridBagConstraints.BOTH;
        gameplayContainer.add(Box.createVerticalGlue(), gbc);

        // 2. Panel Utama Teks Lirik Pengetikan
        gbc.gridy = 1;
        gbc.weighty = 0.38;
        gbc.fill = GridBagConstraints.BOTH;
        gameplayContainer.add(typingPanel, gbc);
        
        // 3. Pembungkus Kompaktibilitas Keyboard & Tombol Restart Rapat
        virtualKeyboard = new VirtualKeyboard();
        JPanel kbWrapper = new JPanel();
        kbWrapper.setLayout(new BoxLayout(kbWrapper, BoxLayout.Y_AXIS));
        kbWrapper.setOpaque(false);
        
        virtualKeyboard.setAlignmentX(Component.CENTER_ALIGNMENT);
        kbWrapper.add(virtualKeyboard);
        kbWrapper.add(Box.createRigidArea(new Dimension(0, 12))); // Jarak pendek persis di bawah spacebar

        JButton restartBtn = new JButton("↻ Restart Test");
        restartBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        restartBtn.setForeground(Theme.TEXT_MUTED);
        restartBtn.setBorderPainted(false);
        restartBtn.setContentAreaFilled(false);
        restartBtn.setFocusPainted(false);
        restartBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        restartBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartBtn.addActionListener(e -> loadNewSong());
        kbWrapper.add(restartBtn);

        gbc.gridy = 2;
        gbc.weighty = 0.35;
        gbc.fill = GridBagConstraints.NONE; // Mengunci ukuran vertikal agar tidak melar otomatis
        gbc.anchor = GridBagConstraints.NORTH; // Menarik posisi mepet ke bagian bawah teks lirik
        gameplayContainer.add(kbWrapper, gbc);

        // 4. Spacer Bawah (Penyeimbang posisi simetris vertikal)
        gbc.gridy = 3;
        gbc.weighty = 0.15;
        gbc.fill = GridBagConstraints.BOTH;
        gameplayContainer.add(Box.createVerticalGlue(), gbc);

        mainPanel.add(gameplayContainer, "typing");
        mainPanel.add(authPanel, "auth");
        mainPanel.add(resultsPanel, "results");
        mainPanel.add(profilePanel, "profile");
        mainPanel.add(leaderboardPanel, "leaderboard");

        add(mainPanel, BorderLayout.CENTER);

        gameTimer = new Timer(1000, e -> {
            if (timeLeft > 0) {
                timeLeft--;
                timerLabel.setText(" ⏱ " + timeLeft + "s ");
                if (timeLeft == 0) {
                    currentSession.finish();
                    showResults();
                }
            }
        });
    }

    private void handleProfileMenuClick() {
        gameTimer.stop();
        if (currentUser == null) {
            authPanel.clearFields();
            authPanel.setStatus("Silakan masuk atau daftarkan akun baru.", false);
            cardLayout.show(mainPanel, "auth");
        } else {
            openProfileView();
        }
    }

    private void handleLogin(String username, String password) {
        try {
            if (DatabaseManager.loginUser(username, password)) {
                this.currentUser = username;
                profileMenuBtn.setText("👤 " + username);
                cardLayout.show(mainPanel, "typing");
                requestFocusInWindow();
            } else {
                authPanel.setStatus("Username atau Password salah!", true);
            }
        } catch (SQLException e) {
            authPanel.setStatus("Database Error: " + e.getMessage(), true);
        }
    }

    private void handleRegister(String username, String password) {
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            authPanel.setStatus("Kolom input tidak boleh kosong!", true);
            return;
        }
        try {
            if (DatabaseManager.registerUser(username, password)) {
                this.currentUser = username;
                profileMenuBtn.setText("👤 " + username);
                cardLayout.show(mainPanel, "typing");
                requestFocusInWindow();
            } else {
                authPanel.setStatus("Pendaftaran gagal dilakukan!", true);
            }
        } catch (SQLException e) {
            authPanel.setStatus("User sudah ada atau DB Error!", true);
        }
    }

    private void openProfileView() {
        if (currentUser == null) return;
        try {
            List<ScoreRecord> scores = DatabaseManager.getUserScores(currentUser);
            profilePanel.updateMetrics(currentUser, scores);
            cardLayout.show(mainPanel, "profile");
        } catch (SQLException e) {
            System.err.println("Gagal memuat profil: " + e.getMessage());
        }
    }

    public void loadNewSong() {
        gameTimer.stop();
        timeLeft = TEST_DURATION;
        timerLabel.setText(" ⏱ " + timeLeft + "s ");
        typingModeBtn.setForeground(Theme.TEXT_MUTED);
        
        Song randomSong = songDB.get(new Random().nextInt(songDB.size()));
        currentSession = new Typing(randomSong);
        
        typingPanel.setSession(currentSession);
        cardLayout.show(mainPanel, "typing");
        typingPanel.repaint();
        requestFocusInWindow();
    }

    private void showResults() {
        gameTimer.stop();
        typingModeBtn.setForeground(Theme.TEXT_MUTED);
        resultsPanel.updateStats(currentSession);
        
        int wpm = (int) Math.round(currentSession.calculateWPM());
        double acc = currentSession.calculateAccuracy();

        int basePoints = (int) (wpm * (acc / 100.0));
        int speedBonus = wpm > 65 ? (wpm - 65) * 4 : 0;
        int totalGained = (basePoints + speedBonus) * 3;

        if (currentUser != null) {
            try {
                DatabaseManager.saveGameResult(currentUser, wpm, acc, totalGained);
            } catch (SQLException e) {
                System.err.println("Gagal menyimpan perkembangan skor: " + e.getMessage());
            }
        }
        cardLayout.show(mainPanel, "results");
    }

    private void initDatabase() {
        songDB = new ArrayList<>();
        songDB.add(new Song("Daniel Caesar", "Who Knows", "Son of Spergy", new String[]{
            "I'll probably be a waste of your time, but who knows?",
            "Chances are I'll step out of line, but who knows?",
            "Lately, you've set up in my mind."
        }));
        songDB.add(new Song("Frank Ocean", "Pink + White", "Blonde", new String[]{
            "That's the way everyday goes.", "Every time we have no control."
        }));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Tubestes2().setVisible(true));
    }
}