/*
 * Click nbfs:
 * Click nbfs:
 */
package com.mycompany.LyricType;

/**
 *
 * @author ROG ZEPHYRUS
 */
public class ScoreRecord {
    /**
     * Nilai kecepatan mengetik dalam satuan Word Per Minute (WPM).
     */
    public int wpm;

    /**
     * Persentase akurasi ketepatan pengetikan pengguna.
     */
    public double accuracy;

    /**
     * Konstruktor untuk membuat objek catatan skor baru.
     *
     * @param wpm nilai kecepatan mengetik yang dicapai
     * @param accuracy tingkat akurasi pengetikan dalam persen
     */
    public ScoreRecord(int wpm, double accuracy) {
        this.wpm = wpm;
        this.accuracy = accuracy;
    }
}