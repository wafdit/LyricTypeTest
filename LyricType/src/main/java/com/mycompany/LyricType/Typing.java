/*
 * Click nbfs:
 * Click nbfs:
 */
package com.mycompany.LyricType;
import java.util.List;
/**
 *
 * @author salma
 */
public class Typing implements StatCalculable {
    private Song currentSong;
    private StringBuilder typedText;
    private String status;
    private int mistakes;
    private String targetText;
    private List<String> lines;
    private long startTime;
    private long endTime;
    private int totalKeystrokes;

    public Typing(Song track) {
        this.currentSong = track;
        this.targetText = track.generateSnippet();
        this.lines = track.breakIntoLines(targetText, 45); 
        this.typedText = new StringBuilder();
        this.status = "idle";
        this.mistakes = 0;
        this.totalKeystrokes = 0;
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

    public String getStats() {
        return calculateWPM() + " WPM | " + calculateAccuracy() + "% Acc";
    }

    @Override
    public double calculateWPM() {
        long timeUsed = (endTime > 0 ? endTime : System.currentTimeMillis()) - startTime;
        double minutes = (timeUsed / 1000.0) / 60.0;
        if (minutes <= 0) minutes = 0.5;

        int correctChars = 0;
        for (int i = 0; i < typedText.length(); i++) {
            if (typedText.charAt(i) == targetText.charAt(i)) correctChars++;
        }
        return (correctChars / 5.0) / minutes;
    }

    @Override
    public double calculateAccuracy() {
        if (totalKeystrokes == 0) return 0.0;
        return Math.max(0, ((totalKeystrokes - mistakes) / (double) totalKeystrokes) * 100);
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

    public Song getCurrentSong(){
        return currentSong;
    }
    public String getTargetText(){
        return targetText;
    }
    public StringBuilder getTypedText(){
        return typedText;
    }
    public List<String> getLines(){
        return lines;
    }
    public String getStatus(){
        return status;
    }
}
