# 🎵 LyricType - *Feel the Rhythm, Master the Keys*

**LyricType** adalah aplikasi *typing test* berbasis Java Swing yang dirancang untuk melatih kecepatan mengetik Anda menggunakan potongan lirik lagu populer. Berbeda dengan pengetikan kata acak biasa, LyricType menghadirkan pengalaman yang lebih emosional dan ritmis.

---

## ✨ Fitur Utama

* **Lirik Dinamis**: Mengambil potongan lirik secara acak dari database lagu sehingga teks tidak pernah membosankan.
* **Tantangan 30 Detik**: Uji kecepatan Anda dalam batas waktu yang intens.
* **Analisis Akurat**:
    * **WPM (Words Per Minute)**: Dihitung berdasarkan karakter benar per 5 karakter standar.
    * **Akurasi**: Melacak setiap kesalahan pengetikan secara *real-time*.
* **UI Modern & Estetik**: 
    * Desain *dark mode* dengan palet warna yang nyaman di mata.
    * Komponen kustom seperti tombol bulat (*rounded buttons*) dan kartu statistik.
* **Visual Album Art**: Menampilkan informasi album dan gambar sampul secara dinamis saat hasil akhir muncul.

---

## 🎵 Daftar Lagu (Database)

Saat ini tersedia lirik dari artis-artis berikut:
1.  **Daniel Caesar** - *Who Knows*
2.  **Frank Ocean** - *Pink + White*
3.  **Laufey** - *Promise*

---

## 🚀 Cara Menjalankan

### Prasyarat
* Java Development Kit (JDK) 25 atau lebih tinggi.
* Apache Maven terinstal.

### Langkah-langkah
1.  **Clone repositori ini**:
    ```bash
    git clone [https://github.com/username/lyrictypetest.git](https://github.com/username/lyrictypetest.git)
    ```
2.  **Masuk ke direktori proyek**:
    ```bash
    cd lyrictypetest
    ```
3.  **Build dan jalankan aplikasi**:
    ```bash
    mvn clean compile exec:java -Dexec.mainClass="com.mycompany.tubestes1.Tubestes1"
    ```

---

## 🎮 Kontrol Permainan

* **Ketik**: Mulai mengetik untuk mengaktifkan timer secara otomatis.
* **Backspace**: Menghapus karakter yang salah (sebelum sesi selesai).
* **Tab**: Memuat ulang lagu baru secara instan jika ingin mengganti tantangan.

---

### 📝 Catatan Pengembang
Aplikasi ini menggunakan sistem *CardLayout* untuk transisi yang mulus antara layar pengetikan dan layar hasil statistik. Setiap komponen visual seperti `StatCard` dan `typingUI` digambar secara manual untuk memastikan tampilan yang unik dan modern.

> "Music expresses that which cannot be said and on which it is impossible to be silent." - *Victor Hugo*

---
**Dibuat oleh:** [salma]
