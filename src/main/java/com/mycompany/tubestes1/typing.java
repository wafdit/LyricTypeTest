/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tubestes1;

/**
 *
 * @author salma
 */
import java.util.ArrayList;
import java.util.List;

public class typing {
    public song currentSong;
    public String targetText;
    public StringBuilder typedText;
    public List<String> lines;
    
    public long startTime;
    public long endTime;
    public String status;
    public int totalKeystrokes;
    public int mistakes;

    public typing(song track) {
        this.currentSong = track;
        this.targetText = track.generateSnippet();
        this.lines = breakIntoLines(targetText, 45);
        this.typedText = new StringBuilder();
        this.status = "idle";
        this.totalKeystrokes = 0;
        this.mistakes = 0;
    }

    private List<String> breakIntoLines(String text, int maxLen) {
        List<String> result = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String wordWithSpace = (i == words.length - 1) ? words[i] : words[i] + " ";
            if (currentLine.length() + wordWithSpace.length() > maxLen && currentLine.length() > 0) {
                result.add(currentLine.toString());
                currentLine = new StringBuilder(wordWithSpace);
            } else {
                currentLine.append(wordWithSpace);
            }
        }
        if (currentLine.length() > 0) result.add(currentLine.toString());
        return result;
    }

    public void start() {
        if (status.equals("idle")) {
            startTime = System.currentTimeMillis();
            status = "typing";
        }
    }

    public void finish() {
        if (status.equals("typing")) {
            endTime = System.currentTimeMillis();
            status = "finished";
        }
    }

    public void handleBackspace() {
        if (typedText.length() > 0 && !status.equals("finished")) {
            typedText.deleteCharAt(typedText.length() - 1);
        }
    }

    public void handleCharacter(char c) {
        if (status.equals("finished")) return;
        if (status.equals("idle")) start();

        totalKeystrokes++;
        if (typedText.length() < targetText.length()) {
            char targetChar = targetText.charAt(typedText.length());
            if (c != targetChar) mistakes++;
            
            typedText.append(c);
        }

        if (typedText.length() == targetText.length()) finish();
    }

    public int getActiveLineIndex() {
        int charCount = 0;
        for (int i = 0; i < lines.size(); i++) {
            charCount += lines.get(i).length();
            if (typedText.length() < charCount) return i;
        }
        return Math.max(0, lines.size() - 1);
    }

    public int[] getStats(int timeLeft, int testDuration) {
        long timeUsed = (endTime > 0 ? endTime : System.currentTimeMillis()) - startTime;
        double minutes = (timeUsed / 1000.0) / 60.0;
        if (timeLeft == 0) minutes = testDuration / 60.0;

        int correctChars = 0;
        for (int i = 0; i < typedText.length(); i++) {
            if (typedText.charAt(i) == targetText.charAt(i)) correctChars++;
        }

        int wpm = minutes > 0 ? (int) Math.round((correctChars / 5.0) / minutes) : 0;
        int accuracy = totalKeystrokes > 0 
            ? Math.max(0, (int) Math.round(((totalKeystrokes - mistakes) / (double) totalKeystrokes) * 100)) 
            : 0;

        return new int[]{wpm, accuracy};
    }
}
