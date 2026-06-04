/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tubestes2;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author salma
 */
public class Song {
    private String artist;
    private String title;
    private String album;
    private String[] lyricBlocks;

    public Song(String artist, String title, String album, String[] lyricBlocks) {
        this.artist = artist;
        this.title = title;
        this.album = album;
        this.lyricBlocks = lyricBlocks;
    }

    public String generateSnippet() {
        int startIdx = new Random().nextInt(lyricBlocks.length);
        StringBuilder sb = new StringBuilder();
        
        for (int i = startIdx; i < lyricBlocks.length; i++) sb.append(lyricBlocks[i]).append(" ");
        for (int i = 0; i < startIdx; i++) sb.append(lyricBlocks[i]).append(" ");
        
        return sb.toString().trim()
                .replaceAll("\\s+", " ")
                .replaceAll("[‘’]", "'")
                .replaceAll("[“”]", "\"")
                .toLowerCase();
    }

    
    public List<String> breakIntoLines(String text, int maxLen) {
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

    
    public String getArtist(){
        return artist;
    }
    public String getTitle(){
        return title;
    }
    public String getAlbum(){
        return album;
    }
}
