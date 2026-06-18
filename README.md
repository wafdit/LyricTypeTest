![LyricType Banner]([https://files.oaiusercontent.com/file-v8wI4?se=2026-06-19T00%3A00%3A00Z&sp=r&sv=2024-08-04&sr=b&rscc=max-age%3D3628800%2C%20immutable&rscd=attachment%3B%20filename%3DChatGPT%20Image%20Jun%2018%2C%202026%2C%2005_30_29%20PM.pn](https://github.com/user-attachments/assets/f3b25d68-7acc-4786-8215-c0268a0efaa2))
# 🎵 LyricType - *Feel the Rhythm, Master the Keys*

**Aplikasi typing test berbasis Java Swing** yang mengubah latihan mengetik biasa menjadi pengalaman musik yang imersif dan menyenangkan.

---

## ✨ Fitur Utama

- **🎤 Lirik Dinamis** — Potongan lirik lagu populer yang diambil secara acak dari database
- **⏱️ Timer 30 Detik** — Mode waktu intens untuk menguji kecepatan mengetik (*Timer selector coming soon*)
- **📊 Analisis Akurat**:
  - **WPM** (Words Per Minute)
  - **Akurasi** real-time dengan deteksi kesalahan
  - **Leaderboard & Progress Tracking**
- **🎨 UI Modern Dengan Berbagai Tema**
- **🏆 Sistem Akun** — Registrasi, login, penyimpanan score, poin, dan level permanen
- **⌨️ Virtual Keyboard** — Virtual keyboard yang setiap tombolnya ter-highlight secara live ketika mengetik
- **⌨️ Keyboard Layout Selector** — Terdapat bermacam-macam layot keyboard yang tersedia untuk virtual keyboard seperti QWERTY, QWERTZ, AZERTY, Dvorak, dan Colemak 

---

## 🏷️ Tools

![Java](https://img.shields.io/badge/Java-25+-ED8B00?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?logo=apachemaven&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-4479A1?logo=mysql&logoColor=white)
![Swing](https://img.shields.io/badge/Java%20Swing-UI-007396)

---

## 📋 Deskripsi Proyek

LyricType menggabungkan latihan mengetik klasik dengan elemen musik yang menyenangkan. Aplikasi ini dirancang untuk meningkatkan kecepatan dan akurasi mengetik sambil menikmati lirik lagu dari artis ternama.

---

## 📁 Struktur Repository

```bash
LyricTypeTest/
├── src/main/java/com/mycompany/lyrictypetest/
│   ├── Main.java
│   ├── LoginFrame.java
│   ├── TypingTestFrame.java
│   ├── DatabaseManager.java
│   └── ...
├── sql/
│   └── init.sql
├── pom.xml
└── README.md
```

---

## 🚀 Cara Menjalankan

### Prasyarat
- **JDK 25** atau lebih tinggi
- **Apache Maven**
- **XAMPP** (untuk database MySQL)

### Setup Database (Penting!)

1. Jalankan **XAMPP** dan nyalakan modul **MySQL**.
2. Buka **phpMyAdmin** (`http://localhost/phpmyadmin`).
3. Buat database baru bernama **`db_lyrictype`**.
4. Jalankan query SQL berikut:

```sql
CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    points INT DEFAULT 0,
    level INT DEFAULT 1
);

CREATE TABLE scores (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    wpm INT,
    accuracy DOUBLE,
    play_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);
```

---

### Menjalankan Aplikasi

```bash
# Clone repository
git clone https://github.com/wafdit/LyricTypeTest.git

# Masuk ke direktori proyek
cd LyricTypeTest

# Build dan jalankan
mvn clean compile exec:java -Dexec.mainClass="com.mycompany.tubestes1.Tubestes1"
```

---

## 🎮 Controls

- **Ketik langsung** → Timer otomatis mulai
- **Backspace** → Hapus karakter salah

---

## 🎵 Daftar Lagu Saat Ini

1. **Daniel Caesar** — *Who Knows*
2. **Frank Ocean** — *Pink + White*
3. **Laufey** — *Promise*

*(Lyrics Fetcher coming soon)*

---

**Made with ❤️ for music & typing enthusiasts**

⭐ **Jangan lupa beri star repository** jika proyek ini bermanfaat bagi Anda!
