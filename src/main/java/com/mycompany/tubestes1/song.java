/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tubestes1;

/**
 *
 * @author salma
 */
import java.util.Random;

public class song {
    public String artist, title, album;
    public String[] lyricBlocks;

    public song(String artist, String title, String album, String[] lyricBlocks) {
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
}
