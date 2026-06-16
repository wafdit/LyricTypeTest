package com.mycompany.LyricType;
import java.util.ArrayList;
import java.util.List;

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
        StringBuilder sb = new StringBuilder();
        for (String block : lyricBlocks) {
            sb.append(block).append(" ");
        }
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
