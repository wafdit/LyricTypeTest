/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tubestes2;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ROG ZEPHYRUS
 */
public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:lyrictype.db";

    static {
        // Melakukan inisialisasi tabel saat aplikasi pertama kali berjalan
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            
            // Tabel Pengguna dengan Poin dan Level
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "username TEXT PRIMARY KEY, " +
                    "password TEXT NOT NULL, " +
                    "points INTEGER DEFAULT 0, " +
                    "level INTEGER DEFAULT 1)");

            // Tabel Riwayat Skor
            stmt.execute("CREATE TABLE IF NOT EXISTS scores (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT, " +
                    "wpm INTEGER, " +
                    "accuracy REAL, " +
                    "FOREIGN KEY(username) REFERENCES users(username))");
            
        } catch (SQLException e) {
            System.err.println("Gagal menginisialisasi database: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static boolean registerUser(String username, String password) throws SQLException {
        String query = "INSERT INTO users (username, password, points, level) VALUES (?, ?, 0, 1)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        }
    }

    public static boolean loginUser(String username, String password) throws SQLException {
        String query = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password").equals(password);
                }
            }
        }
        return false;
    }

    public static void saveGameResult(String username, int wpm, double accuracy, int pointsGained) throws SQLException {
        String insertScore = "INSERT INTO scores (username, wpm, accuracy) VALUES (?, ?, ?)";
        String updateProgress = "UPDATE users SET points = points + ? WHERE username = ?";
        
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); // Transaksi atomik
            try (PreparedStatement pstmtScore = conn.prepareStatement(insertScore);
                 PreparedStatement pstmtUser = conn.prepareStatement(updateProgress)) {
                
                // 1. Simpan skor riwayat
                pstmtScore.setString(1, username);
                pstmtScore.setInt(2, wpm);
                pstmtScore.setDouble(3, accuracy);
                pstmtScore.executeUpdate();

                // 2. Tambah poin progression
                pstmtUser.setInt(1, pointsGained);
                pstmtUser.setString(2, username);
                pstmtUser.executeUpdate();

                conn.commit();
                updateUserLevel(username); // Rekalkulasi level setelah poin bertambah
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private static void updateUserLevel(String username) throws SQLException {
        String selectPoints = "SELECT points FROM users WHERE username = ?";
        String updateLevel = "UPDATE users SET level = ? WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmtSel = conn.prepareStatement(selectPoints);
             PreparedStatement pstmtUpd = conn.prepareStatement(updateLevel)) {
            
            pstmtSel.setString(1, username);
            try (ResultSet rs = pstmtSel.executeQuery()) {
                if (rs.next()) {
                    int totalPoints = rs.getInt("points");
                    // Formula Level: Setiap kelipatan 300 poin naik 1 level
                    int newLevel = (totalPoints / 300) + 1;
                    
                    pstmtUpd.setInt(1, newLevel);
                    pstmtUpd.setString(2, username);
                    pstmtUpd.executeUpdate();
                }
            }
        }
    }

    public static UserProgress getUserProgress(String username) throws SQLException {
        String query = "SELECT points, level FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new UserProgress(rs.getInt("points"), rs.getInt("level"));
                }
            }
        }
        return new UserProgress(0, 1);
    }

    public static List<ScoreRecord> getUserScores(String username) throws SQLException {
        List<ScoreRecord> records = new ArrayList<>();
        String query = "SELECT wpm, accuracy FROM scores WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(new ScoreRecord(rs.getInt("wpm"), rs.getDouble("accuracy")));
                }
            }
        }
        return records;
    }

    public static List<LeaderboardRow> getLeaderboard() throws SQLException {
        List<LeaderboardRow> list = new ArrayList<>();
        String query = "SELECT username, level, points FROM users ORDER BY level DESC, points DESC LIMIT 5";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new LeaderboardRow(rs.getString("username"), rs.getInt("level"), rs.getInt("points")));
            }
        }
        return list;
    }
}

// Objek Representasi Tambahan
class UserProgress {
    int points; int level;
    public UserProgress(int points, int level) { this.points = points; this.level = level; }
}

class LeaderboardRow {
    String username; int level; int points;
    public LeaderboardRow(String username, int level, int points) {
        this.username = username; this.level = level; this.points = points;
    }
}
