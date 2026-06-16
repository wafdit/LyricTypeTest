/*
 * Click nbfs:
 * Click nbfs:
 */
package com.mycompany.LyricType;
import java.awt.Color;

public class Theme {
    public static Color BG = new Color(0x55, 0x42, 0x3D);
    public static Color BG_DARK = new Color(0x14, 0x0D, 0x0B);
    public static Color TEXT_MAIN = new Color(0xFF, 0xF3, 0xEC);
    public static Color TEXT_MUTED = new Color(0xFF, 0xF3, 0xEC, 70);
    public static Color ACCENT = new Color(0xFF, 0xC0, 0xAD);
    public static Color HIGHLIGHT_ERR = new Color(0xE7, 0x8F, 0xB3);
    public static Color TERTIARY = new Color(0x96, 0x56, 0xA1);
    public static Color BUTTON_TEXT = new Color(0x14, 0x0D, 0x0B);
    public static Color INPUT_TEXT = new Color(0xFF, 0xF3, 0xEC);

    public static String currentTheme = "Coffee";

    public static void setTheme(String name) {
        currentTheme = name;
        switch (name) {
            case "Blueberry":
                BG = new Color(0x16, 0x16, 0x1A);
                BG_DARK = new Color(0x01, 0x01, 0x01);
                TEXT_MAIN = new Color(0xFF, 0xFF, 0xFE);
                TEXT_MUTED = new Color(0x94, 0xA1, 0xB2);
                ACCENT = new Color(0x7F, 0x5A, 0xF0);
                HIGHLIGHT_ERR = new Color(0x7F, 0x5A, 0xF0);
                TERTIARY = new Color(0x2C, 0xB6, 0x7D);
                BUTTON_TEXT = new Color(0xFF, 0xFF, 0xFE);
                INPUT_TEXT = new Color(0xFF, 0xFF, 0xFE);
                break;
            case "Forest":
                BG = new Color(0x00, 0x46, 0x43);
                BG_DARK = new Color(0x00, 0x1E, 0x1D);
                TEXT_MAIN = new Color(0xFF, 0xFF, 0xFE);
                TEXT_MUTED = new Color(0xAB, 0xD1, 0xC6);
                ACCENT = new Color(0xF9, 0xBC, 0x60);
                HIGHLIGHT_ERR = new Color(0xE1, 0x61, 0x62);
                TERTIARY = new Color(0xF9, 0xBC, 0x60);
                BUTTON_TEXT = new Color(0x00, 0x1E, 0x1D);
                INPUT_TEXT = new Color(0xFF, 0xFF, 0xFE);
                break;
            case "Cotton Candy":
                BG = new Color(0xFE, 0xC7, 0xD7);
                BG_DARK = new Color(0x0E, 0x17, 0x2C);
                TEXT_MAIN = new Color(0x0E, 0x17, 0x2C);
                TEXT_MUTED = new Color(0x0E, 0x17, 0x2C, 150);
                ACCENT = new Color(0x0E, 0x17, 0x2C);
                HIGHLIGHT_ERR = new Color(0xA7, 0x86, 0xDF);
                TERTIARY = new Color(0xA7, 0x86, 0xDF);
                BUTTON_TEXT = new Color(0xFF, 0xFF, 0xFE);
                INPUT_TEXT = new Color(0xF9, 0xF8, 0xFC);
                break;
            default:
                BG = new Color(0x55, 0x42, 0x3D);
                BG_DARK = new Color(0x14, 0x0D, 0x0B);
                TEXT_MAIN = new Color(0xFF, 0xF3, 0xEC);
                TEXT_MUTED = new Color(0xFF, 0xF3, 0xEC, 70);
                ACCENT = new Color(0xFF, 0xC0, 0xAD);
                HIGHLIGHT_ERR = new Color(0xE7, 0x8F, 0xB3);
                TERTIARY = new Color(0x96, 0x56, 0xA1);
                BUTTON_TEXT = new Color(0x14, 0x0D, 0x0B);
                INPUT_TEXT = new Color(0xFF, 0xF3, 0xEC);
                break;
        }
    }
}
