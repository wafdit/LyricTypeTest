/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tubestes2;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
/**
 *
 * @author salma
 */
public class Tubestes2 extends JFrame {
    public static final int TEST_DURATION = 30;
    private Typing currentSession;
    private List<Song> songDB;
    
    // In-Memory Database (Menggantikan JDBC sementara)
    private Map<String, String> usersDB = new HashMap<>();
    private Map<String, List<ScoreRecord>> userScoresDB = new HashMap<>();
    private String currentUser = null; 

    // Komponen UI
    private int timeLeft;
    private Timer gameTimer;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private typingUI typingPanel;
    private ResultPanel resultsPanel;
    private AuthPanel authPanel;
    private ProfilePanel profilePanel;
    private VirtualKeyboard virtualKeyboard;
    
    private JLabel timerLabel;
    private JLabel stateReadyLabel;
    private JLabel stateTypingLabel;
    private JLabel stateResultsLabel;
    private RoundedButton profileMenuBtn;

    public Tubestes2() {
        setTitle("LyricType - OOP Edition");
        setSize(1000, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG);

        initDatabase(); // Memasukkan lirik manual
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
                        updateHeaderState("typing");
                    }
                    if (currentSession.getStatus().equals("finished")) {
                        showResults();
                    }
                    typingPanel.repaint();
                }
            }
        });

        setFocusable(true);
        loadNewSong(); // Langsung masuk ke layar bermain
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 10));

        // --- PANEL HEADER ---
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

        stateReadyLabel = new JLabel("READY");
        stateTypingLabel = new JLabel("TYPING");
        stateResultsLabel = new JLabel("RESULTS");

        Font stateFont = new Font("SansSerif", Font.BOLD, 12);
        stateReadyLabel.setFont(stateFont);
        stateTypingLabel.setFont(stateFont);
        stateResultsLabel.setFont(stateFont);

        profileMenuBtn = new RoundedButton("👤 Profil");
        profileMenuBtn.setPreferredSize(new Dimension(130, 35));
        profileMenuBtn.addActionListener(e -> handleProfileMenuClick());

        rightHeader.add(timerLabel);
        rightHeader.add(stateReadyLabel);
        rightHeader.add(stateTypingLabel);
        rightHeader.add(stateResultsLabel);
        rightHeader.add(profileMenuBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightHeader, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- MANAJEMEN KARTU LAYAR ---
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

        typingPanel = new typingUI();
        resultsPanel = new ResultPanel(this::loadNewSong);

        JPanel gameplayContainer = new JPanel(new BorderLayout());
        gameplayContainer.setOpaque(false);
        gameplayContainer.add(typingPanel, BorderLayout.CENTER);
        
        virtualKeyboard = new VirtualKeyboard();
        JPanel kbWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        kbWrapper.setOpaque(false);
        kbWrapper.add(virtualKeyboard);
        gameplayContainer.add(kbWrapper, BorderLayout.SOUTH);

        mainPanel.add(gameplayContainer, "typing");
        mainPanel.add(authPanel, "auth");
        mainPanel.add(resultsPanel, "results");
        mainPanel.add(profilePanel, "profile");

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

    // Menggunakan HashMap untuk validasi Login lokal
    private void handleLogin(String username, String password) {
        if (usersDB.containsKey(username) && usersDB.get(username).equals(password)) {
            this.currentUser = username;
            profileMenuBtn.setText("👤 " + username);
            cardLayout.show(mainPanel, "typing");
            requestFocusInWindow();
        } else {
            authPanel.setStatus("Username atau Password salah!", true);
        }
    }

    // Menggunakan HashMap untuk penyimpanan Register lokal
    private void handleRegister(String username, String password) {
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            authPanel.setStatus("Kolom input tidak boleh kosong!", true);
            return;
        }
        if (usersDB.containsKey(username)) {
            authPanel.setStatus("Username sudah terdaftar!", true);
            return;
        }
        
        usersDB.put(username, password);
        userScoresDB.put(username, new ArrayList<>()); // Inisialisasi riwayat kosong
        
        this.currentUser = username;
        profileMenuBtn.setText("👤 " + username);
        cardLayout.show(mainPanel, "typing");
        requestFocusInWindow();
    }

    private void openProfileView() {
        if (currentUser == null) return;
        List<ScoreRecord> scores = userScoresDB.getOrDefault(currentUser, new ArrayList<>());
        profilePanel.updateMetrics(currentUser, scores);
        cardLayout.show(mainPanel, "profile");
    }

    public void loadNewSong() {
        gameTimer.stop();
        timeLeft = TEST_DURATION;
        timerLabel.setText(" ⏱ " + timeLeft + "s ");
        updateHeaderState("idle");
        
        Song randomSong = songDB.get(new Random().nextInt(songDB.size()));
        currentSession = new Typing(randomSong);
        
        typingPanel.setSession(currentSession);
        cardLayout.show(mainPanel, "typing");
        typingPanel.repaint();
        requestFocusInWindow();
    }

    private void showResults() {
        gameTimer.stop();
        updateHeaderState("finished");
        resultsPanel.updateStats(currentSession);
        
        // Simpan skor otomatis ke HashMap jika user sudah login
        if (currentUser != null) {
            userScoresDB.get(currentUser).add(new ScoreRecord(
                (int) Math.round(currentSession.calculateWPM()), 
                currentSession.calculateAccuracy()
            ));
        }
        cardLayout.show(mainPanel, "results");
    }

    private void updateHeaderState(String state) {
        stateReadyLabel.setForeground(state.equals("idle") ? Theme.ACCENT : Theme.TEXT_MUTED);
        stateTypingLabel.setForeground(state.equals("typing") ? Theme.ACCENT : Theme.TEXT_MUTED);
        stateResultsLabel.setForeground(state.equals("finished") ? Theme.ACCENT : Theme.TEXT_MUTED);
    }

    // Mengembalikan inisialisasi data lirik orisinal manual
    private void initDatabase() {
        songDB = new ArrayList<>();
        
        songDB.add(new Song("Daniel Caesar", "Who Knows", "Son of Spergy", new String[]{
            "I'll probably be a waste of your time, but who knows?",
            "Chances are I'll step out of line, but who knows?",
            "Lately, you've set up in my mind.",
            "Yeah, girl, you, and I like that.",
            "Lately, I've been thinking that perhaps I am a coward.",
            "Hiding in a disguise of an ever-giving flower.",
            "Incompetent steward of all of that sweet, sweet power.",
            "Yesterday was feeling so good, now it's gone."
        }));

        songDB.add(new Song("Frank Ocean", "Pink + White", "Blonde", new String[]{
            "That's the way everyday goes.",
            "Every time we have no control.",
            "If the sky is pink and white.",
            "If the ground is black and yellow.",
            "It's the same way you showed me.",
            "Nod my head, don't close my eyes.",
            "Halfway on a slow move."
        }));

        songDB.add(new Song("Laufey", "Promise", "Bewitched", new String[]{
            "I made a promise to distance myself.",
            "Took a flight, through aurora skies.",
            "Honestly, I didn't think about how we didn't say goodbye.",
            "Just see you very soon.",
            "It hurts to be something, it's worse to be nothing with you.",
            "So I didn't call you for sixteen long days."
        }));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Tubestes2().setVisible(true));
    }
}

// Kelas pembantu (dapat diletakkan di dalam file yang sama atau dipisah)
class ScoreRecord {
    int wpm;
    double accuracy;
    public ScoreRecord(int wpm, double accuracy) {
        this.wpm = wpm;
        this.accuracy = accuracy;
    }
}
