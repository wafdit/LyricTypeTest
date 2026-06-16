/*
 * Click nbfs:
 */

package com.mycompany.LyricType;
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
    private SettingsPanel settingsPanel;
    private VirtualKeyboard virtualKeyboard;
    
    private RoundedButton typingModeBtn;
    private RoundedButton profileMenuBtn;
    private RoundedButton leaderboardBtn;
    
    private String activeLayoutName = "QWERTY";

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
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 45, 0, 45));
        
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftHeader.setOpaque(false);

        JLabel titleLabel = new JLabel("♫ LyricType");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Theme.ACCENT);
        
        JButton settingsBtn = new JButton("⚙");
        settingsBtn.setFont(new Font("SansSerif", Font.PLAIN, 24));
        settingsBtn.setForeground(Theme.TEXT_MUTED);
        settingsBtn.setBorderPainted(false);
        settingsBtn.setContentAreaFilled(false);
        settingsBtn.setFocusPainted(false);
        settingsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        settingsBtn.addActionListener(e -> {
            gameTimer.stop();
            settingsPanel.updateCurrentConfig(activeLayoutName, Theme.currentTheme);
            cardLayout.show(mainPanel, "settings");
        });

        leftHeader.add(titleLabel);
        leftHeader.add(settingsBtn);

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightHeader.setOpaque(false);

        typingModeBtn = new RoundedButton("⌨ Typing");
        typingModeBtn.setPreferredSize(new Dimension(110, 35));
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

        rightHeader.add(typingModeBtn);
        rightHeader.add(leaderboardBtn);
        rightHeader.add(profileMenuBtn);

        headerPanel.add(leftHeader, BorderLayout.WEST);
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
        }, this::handleLogout);

        leaderboardPanel = new LeaderboardPanel(() -> {
            cardLayout.show(mainPanel, "typing");
            requestFocusInWindow();
        });

        settingsPanel = new SettingsPanel((layout, theme) -> {
            this.activeLayoutName = layout;
            virtualKeyboard.changeLayout(layout);
            if (!Theme.currentTheme.equals(theme)) {
                Theme.setTheme(theme);
                String user = this.currentUser;
                this.dispose();
                Tubestes2 newInstance = new Tubestes2();
                if (user != null) {
                    newInstance.setCurrentUser(user);
                }
                newInstance.setVisible(true);
            }
        }, () -> {
            cardLayout.show(mainPanel, "typing");
            requestFocusInWindow();
        });

        typingPanel = new typingUI();
        resultsPanel = new ResultPanel(this::loadNewSong);
        JPanel gameplayContainer = new JPanel(new GridBagLayout());
        gameplayContainer.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        gbc.gridy = 0;
        gbc.weighty = 0.15;
        gameplayContainer.add(Box.createVerticalGlue(), gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.35;
        gbc.fill = GridBagConstraints.BOTH;
        gameplayContainer.add(typingPanel, gbc);
        
        virtualKeyboard = new VirtualKeyboard();
        JPanel kbWrapper = new JPanel();
        kbWrapper.setLayout(new BoxLayout(kbWrapper, BoxLayout.Y_AXIS));
        kbWrapper.setOpaque(false);
        
        virtualKeyboard.setAlignmentX(Component.CENTER_ALIGNMENT);
        kbWrapper.add(virtualKeyboard);
        kbWrapper.add(Box.createRigidArea(new Dimension(0, 5)));

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

        gbc.gridy = 3;
        gbc.weighty = 0.30;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTH;
        gameplayContainer.add(kbWrapper, gbc);

        gbc.gridy = 4;
        gbc.weighty = 0.15;
        gbc.fill = GridBagConstraints.BOTH;
        gameplayContainer.add(Box.createVerticalGlue(), gbc);

        mainPanel.add(gameplayContainer, "typing");
        mainPanel.add(authPanel, "auth");
        mainPanel.add(resultsPanel, "results");
        mainPanel.add(profilePanel, "profile");
        mainPanel.add(leaderboardPanel, "leaderboard");
        mainPanel.add(settingsPanel, "settings");

        add(mainPanel, BorderLayout.CENTER);

        gameTimer = new Timer(1000, e -> {
            if (timeLeft > 0) {
                timeLeft--;
                typingPanel.setTimerText(String.valueOf(timeLeft));
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
            cardLayout.show(mainPanel, "auth");
        } else {
            openProfileView();
        }
    }


    public void setCurrentUser(String username) {
        this.currentUser = username;
        profileMenuBtn.setText("👤 " + username);
    }

    private void handleLogin(String username, String password) {
        try {
            if (DatabaseManager.loginUser(username, password)) {
                setCurrentUser(username);
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
                setCurrentUser(username);
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

    private void handleLogout() {
        currentUser = null;
        profileMenuBtn.setText("👤 Profile");
        cardLayout.show(mainPanel, "typing");
        requestFocusInWindow();
    }

    public void loadNewSong() {
        gameTimer.stop();
        timeLeft = TEST_DURATION;
        typingPanel.setTimerText(String.valueOf(timeLeft));
        Song randomSong;
        do {
            randomSong = songDB.get(new Random().nextInt(songDB.size()));
        } while (songDB.size() > 1 && currentSession != null && randomSong.getTitle().equals(currentSession.getCurrentSong().getTitle()));
        
        currentSession = new Typing(randomSong);
        typingPanel.setSession(currentSession);
        
        cardLayout.show(mainPanel, "typing");
        typingPanel.revalidate();
        typingPanel.repaint();
        mainPanel.revalidate();
        mainPanel.repaint();
        requestFocusInWindow();
    }

    private void showResults() {
        gameTimer.stop();
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
            "Lately, you've set up in my mind. Yesterday was feeling so good, now it's gone."
        }));
        songDB.add(new Song("Frank Ocean", "Pink + White", "Blonde", new String[]{
            "That's the way everyday goes. Every time we have no control.",
            "If the sky is pink and white. If the ground is black and yellow."
        }));
        songDB.add(new Song("Laufey", "Promise", "Bewitched", new String[]{
            "I made a promise to distance myself. Took a flight, through aurora skies.",
            "Honestly, I didn't think about how we didn't say goodbye. Just see you very soon."
        }));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Tubestes2().setVisible(true));
    }
}