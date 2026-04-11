/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tubestes1;

/**
 *
 * @author salma
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tubestes1 extends JFrame {

    public static final int TEST_DURATION = 30;

    // --- APP STATE ---
    private typing currentSession;
    private int timeLeft;
    private Timer gameTimer;
    private List<song> songDB;

    // --- UI COMPONENTS ---
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private typingUI typingPanel;
    private ResultsPanel resultsPanel;
    
    // Header Components
    private JLabel timerLabel;
    private JLabel stateReadyLabel;
    private JLabel stateTypingLabel;
    private JLabel stateResultsLabel;

    public Tubestes1() {
        setTitle("LyricType - Pure Java Edition");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(theme.BG);

        initDatabase();
        initComponents();
        loadNewSong();
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (currentSession == null || currentSession.status.equals("finished")) return;
                
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    currentSession.handleBackspace();
                    typingPanel.repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    loadNewSong();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (currentSession == null || currentSession.status.equals("finished")) return;
                
                char c = e.getKeyChar();
                if (c >= 32 && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    currentSession.handleCharacter(Character.toLowerCase(c));
                    if (currentSession.status.equals("typing") && !gameTimer.isRunning()) {
                        gameTimer.start();
                        updateHeaderState("typing");
                    }
                    if (currentSession.status.equals("finished")) {
                        showResults();
                    }
                    typingPanel.repaint();
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }

    private void initDatabase() {
        songDB = new ArrayList<>();
        
        songDB.add(new song("Daniel Caesar", "Who Knows", "Son of Spergy", new String[]{
            "I'll probably be a waste of your time, but who knows?",
            "Chances are I'll step out of line, but who knows?",
            "Lately, you've set up in my mind.",
            "Yeah, girl, you, and I like that.",
            "Lately, I've been thinking that perhaps I am a coward.",
            "Hiding in a disguise of an ever-giving flower.",
            "Incompetent steward of all of that sweet, sweet power.",
            "Yesterday was feeling so good, now it's gone."
        }));

        songDB.add(new song("Frank Ocean", "Pink + White", "Blonde", new String[]{
            "That's the way everyday goes.",
            "Every time we have no control.",
            "If the sky is pink and white.",
            "If the ground is black and yellow.",
            "It's the same way you showed me.",
            "Nod my head, don't close my eyes.",
            "Halfway on a slow move."
        }));

        songDB.add(new song("Laufey", "Promise", "Bewitched", new String[]{
            "I made a promise to distance myself.",
            "Took a flight, through aurora skies.",
            "Honestly, I didn't think about how we didn't say goodbye.",
            "Just see you very soon.",
            "It hurts to be something, it's worse to be nothing with you.",
            "So I didn't call you for sixteen long days."
        }));
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(theme.BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 45, 0, 45));
        
        JLabel titleLabel = new JLabel("♫ LyricType");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(theme.ACCENT);

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightHeader.setOpaque(false);

        timerLabel = new JLabel(" ⏱ " + TEST_DURATION + "s ");
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
        timerLabel.setForeground(theme.HIGHLIGHT_ERR);
        timerLabel.setOpaque(true);
        timerLabel.setBackground(theme.BG_DARK); 
        timerLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        stateReadyLabel = new JLabel("READY");
        stateTypingLabel = new JLabel("TYPING");
        stateResultsLabel = new JLabel("RESULTS");

        Font stateFont = new Font("SansSerif", Font.BOLD, 12);
        stateReadyLabel.setFont(stateFont);
        stateTypingLabel.setFont(stateFont);
        stateResultsLabel.setFont(stateFont);

        rightHeader.add(timerLabel);
        rightHeader.add(stateReadyLabel);
        rightHeader.add(stateTypingLabel);
        rightHeader.add(stateResultsLabel);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightHeader, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(theme.BG);

        typingPanel = new typingUI();
        resultsPanel = new ResultsPanel(this::loadNewSong); 

        mainPanel.add(typingPanel, "typing");
        mainPanel.add(resultsPanel, "results");

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

    private void updateHeaderState(String state) {
        stateReadyLabel.setForeground(state.equals("idle") ? theme.ACCENT : theme.TEXT_MUTED);
        stateTypingLabel.setForeground(state.equals("typing") ? theme.ACCENT : theme.TEXT_MUTED);
        stateResultsLabel.setForeground(state.equals("finished") ? theme.ACCENT : theme.TEXT_MUTED);
    }

    private void loadNewSong() {
        gameTimer.stop();
        timeLeft = TEST_DURATION;
        timerLabel.setText(" ⏱ " + timeLeft + "s ");
        updateHeaderState("idle");
        
        song randomSong = songDB.get(new Random().nextInt(songDB.size()));
        currentSession = new typing(randomSong);
        
        typingPanel.setSession(currentSession);
        
        cardLayout.show(mainPanel, "typing");
        typingPanel.repaint();
        requestFocusInWindow();
    }

    private void showResults() {
        gameTimer.stop();
        updateHeaderState("finished");
        resultsPanel.updateStats(currentSession, timeLeft, TEST_DURATION);
        cardLayout.show(mainPanel, "results");
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } 
        catch (Exception e) { e.printStackTrace(); }

        SwingUtilities.invokeLater(() -> {
            new Tubestes1().setVisible(true);
        });
    }
}
