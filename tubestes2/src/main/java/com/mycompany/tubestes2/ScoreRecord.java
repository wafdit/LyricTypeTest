/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tubestes2;

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