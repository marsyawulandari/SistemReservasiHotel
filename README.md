# 🏨 Sistem Reservasi Hotel Berbasis OOP (Java)

> Proyek UAS Mata Kuliah **Pemrograman Berorientasi Objek (PBO)**
> Aplikasi *console-based* untuk simulasi pengelolaan kamar dan reservasi hotel menggunakan Java murni.

---

## 📌 Deskripsi Proyek

**Sistem Reservasi Hotel** adalah aplikasi berbasis *Command Line Interface* (CLI) yang dibangun dengan bahasa **Java** untuk mensimulasikan proses bisnis reservasi kamar pada sebuah hotel bernama **Grand Nusantara Hotel**. Aplikasi ini memungkinkan pengguna untuk melihat daftar kamar, membuat reservasi baru, melakukan pembayaran dengan beberapa metode, mengecek detail reservasi, serta membatalkan reservasi yang sudah dibuat.

Seluruh proses bisnis (pengecekan ketersediaan kamar, perhitungan biaya menginap, validasi tanggal, hingga pencatatan status pembayaran) ditangani secara *in-memory* menggunakan struktur data `ArrayList`, tanpa database eksternal. Proyek ini dibuat sebagai implementasi nyata dari konsep-konsep dasar Pemrograman Berorientasi Objek: **Encapsulation, Inheritance, Polymorphism, Abstraction**, serta **Exception Handling** dan **Collection Framework**.

## 🎯 Tujuan Sistem

- Menerapkan konsep dasar OOP (Encapsulation, Inheritance, Polymorphism, Abstraction) dalam studi kasus nyata berupa sistem reservasi hotel.
- Mendemonstrasikan penggunaan *custom exception* untuk menangani kondisi-kondisi tidak normal dalam proses bisnis (kamar tidak tersedia, tanggal tidak valid, reservasi tidak ditemukan, input tidak valid).
- Mendemonstrasikan penggunaan **Java Collection Framework** (`List`, `ArrayList`) untuk mengelola data kamar dan reservasi secara dinamis.
- Menyediakan simulasi alur transaksi hotel yang utuh: pencarian kamar → reservasi → pembayaran → pencetakan tagihan → pengecekan/pembatalan.

## ✨ Fitur Utama

Fitur di bawah ini diambil langsung dari menu yang tersedia pada `HotelReservasi.main()` — tidak ada fitur tambahan di luar yang tercantum di sini.

| No  | Fitur                      | Keterangan                                                                                   |
| --- | --------------------------- | --------------------------------------------------------------------------------------------- |
| 1   | **Buat Reservasi Baru**     | Input data tamu, pemilihan kamar tersedia, input tanggal check-in/out, lalu pembayaran        |
| 2   | **Cek Reservasi**           | Menampilkan detail tagihan lengkap berdasarkan kode reservasi (format `RSV-XXXXXXXX`)         |
| 3   | **Batalkan Reservasi**      | Membatalkan reservasi aktif dan mengembalikan status kamar menjadi tersedia                   |
| 4   | **Lihat Semua Kamar**       | Menampilkan seluruh kamar beserta tipe, lantai, harga/malam, dan status ketersediaan           |
| 5   | **Lihat Semua Reservasi**   | Menampilkan seluruh reservasi yang masih berstatus **"Aktif"**                                |
| 0   | **Keluar**                  | Menutup aplikasi dan menutup koneksi `Scanner`                                                |

**Detail proses bisnis yang terverifikasi dari kode:**

- Hotel diinisialisasi dengan **8 kamar tetap** (hardcoded di `main()`): 3 `KamarStandard` (101, 102, 103 — Rp350.000/malam), 3 `KamarDeluxe` (201, 202 — Rp650.000/malam; 203 — Rp700.000/malam), 2 `KamarSuite` (301 — Rp1.200.000/malam; 302 — Rp1.500.000/malam).
- Kode reservasi dibuat otomatis menggunakan `UUID` dengan format `RSV-XXXXXXXX` (8 karakter pertama UUID, huruf kapital).
- Total biaya dihitung dengan rumus `jumlahMalam * hargaPerMalam`, di mana `jumlahMalam` dihitung dari selisih `checkout - checkin` (`ChronoUnit.DAYS.between`).
- Tersedia **3 metode pembayaran**: Tunai, Transfer Bank, dan Kartu Kredit — masing-masing berupa subclass dari `Pembayaran` (abstract class).
- Pembayaran tunai akan **gagal** (mengembalikan `false`) apabila uang yang diterima kurang dari total biaya; sistem tidak melempar *exception* pada kasus ini, hanya menampilkan pesan kekurangan dan tidak mencatat pembayaran.
- Validasi format tanggal wajib `DD-MM-YYYY`, dengan *retry loop* otomatis jika format salah (`DateTimeParseException` ditangkap di dalam `while(true)`).
- Validasi: tanggal check-out harus setelah check-in, nama & No. KTP tidak boleh kosong.
- Status kamar otomatis berubah menjadi **"Dipesan"** segera setelah reservasi dibuat (sebelum proses pembayaran selesai) — lihat bagian *Saran Perbaikan* untuk catatan terkait hal ini.

## 📁 Struktur Repository

Struktur berikut adalah struktur **apa adanya** dari source code yang dianalisis (flat structure, tanpa folder `src`, tanpa file build seperti `pom.xml`/`build.gradle`, dan **tanpa deklarasi `package`** sama sekali pada seluruh file `.java`):

```
SRH-2/
├── Hotel.java                     # Class Hotel + 2 custom exception
├── HotelReservasi.java            # Main class (entry point) + 1 custom exception
├── Kamar.java                     # Abstract class Kamar + 3 subclass kamar
├── Pembayaran.java                # Abstract class Pembayaran
├── PembayaranKartuKredit.java     # Subclass Pembayaran
├── PembayaranTransfer.java        # Subclass Pembayaran
├── PembayaranTunai.java           # Subclass Pembayaran
├── Reservasi.java                 # Class Reservasi + 2 custom exception (termasuk base exception)
└── Tamu.java                      # Class Tamu
```

> **Catatan struktur:** Karena seluruh file berada dalam *default package* (tidak ada `package` statement), kompilasi dapat dilakukan langsung dengan `javac *.java` pada satu folder yang sama. Untuk kebutuhan submission GitHub, disarankan menambahkan `README.md` (sudah dibuat di dokumen ini) dan opsional `.gitignore` untuk mengecualikan file hasil kompilasi (`*.class`).

## 🧩 Penjelasan Setiap Class

### 1. `Hotel` *(Hotel.java)*
Class inti yang merepresentasikan entitas hotel. Menyimpan `nama`, `alamat`, daftar kamar (`List<Kamar>`), dan daftar reservasi (`List<Reservasi>`). Bertugas sebagai *controller* utama proses bisnis: menambah kamar (`tambahKamar`), mencari kamar tersedia (`cariKamarTersedia`), mencari kamar berdasarkan nomor (`cariKamarByNomor`), membuat reservasi (`buatReservasi`), mencari reservasi (`cariReservasi`), membatalkan reservasi (`batalkanReservasi`), serta menampilkan seluruh kamar/reservasi ke layar.

### 2. `Kamar` *(Kamar.java — abstract class)*
Class abstrak yang menyimpan atribut umum sebuah kamar: `nomor`, `lantai`, `hargaPerMalam` (`protected`, agar dapat diakses subclass), dan `tersedia`. Mendeklarasikan dua method abstrak (`tipeKamar()` dan `fasilitas()`) yang wajib diimplementasikan oleh subclass-nya. Menyediakan method `info()` untuk menampilkan ringkasan kamar dalam format tabel teks.

- **`KamarStandard`** — tipe "Standard", fasilitas: AC, TV, Kamar Mandi, WiFi.
- **`KamarDeluxe`** — tipe "Deluxe", fasilitas: AC, TV 55", Kamar Mandi, WiFi, Mini Bar, Balkon.
- **`KamarSuite`** — tipe "Suite", fasilitas: AC, Smart TV, Jacuzzi, WiFi, Mini Bar, Ruang Tamu, Dapur Kecil, View Kota.

### 3. `Pembayaran` *(Pembayaran.java — abstract class)*
Class abstrak yang menyimpan atribut umum transaksi pembayaran: `jumlah`, `status` (default `"Belum Dibayar"`), dan `waktuBayar`. Mendeklarasikan dua method abstrak: `prosesPembayaran()` dan `namaMetode()`.

- **`PembayaranTunai`** — memvalidasi `uangDiterima` terhadap `jumlah`; menghitung kembalian jika cukup, atau gagal (return `false`) jika kurang.
- **`PembayaranTransfer`** — menyimpan `namaBank` dan `noRekening`; pembayaran selalu dianggap berhasil (simulasi).
- **`PembayaranKartuKredit`** — menyimpan 4 digit terakhir nomor kartu (`empatDigitAkhir`) dan `namaPemegang`; pembayaran selalu dianggap berhasil (simulasi).

### 4. `Reservasi` *(Reservasi.java)*
Merepresentasikan satu transaksi reservasi. Menyimpan `kode` (dibuat otomatis via `UUID`), referensi `Tamu`, referensi `Kamar`, tanggal check-in/check-out, `jumlahMalam`, `totalBiaya`, `status` (`"Aktif"`/`"Dibatalkan"`), referensi `Pembayaran` (nullable), dan `waktuBuat`. Constructor-nya melempar `TanggalTidakValidException` jika tanggal check-out tidak setelah check-in. Menyediakan method `batalkan()` (mengubah status & mengembalikan kamar menjadi tersedia) dan `cetakTagihan()` untuk mencetak struk/invoice lengkap ke konsol.

### 5. `Tamu` *(Tamu.java)*
Class data sederhana (POJO) untuk menyimpan informasi tamu: `nama`, `noKtp`, `noTelepon`, `email`. Hanya berisi constructor, getter, dan `toString()` — tidak memiliki business logic.

### 6. `HotelReservasi` *(HotelReservasi.java — main class)*
Entry point aplikasi (`public class` dengan method `main`). Berisi:
- Method utilitas input: `inputTanggal()` (dengan validasi format & retry loop) dan `inputInt()` (dengan validasi rentang angka).
- Method menu: `menuReservasiBaru()`, `menuCekReservasi()`, `menuBatalkanReservasi()`.
- `main()` — inisialisasi objek `Hotel` dan 8 kamar, lalu menjalankan loop menu interaktif berbasis `Scanner` sampai pengguna memilih keluar (`0`).

### 7. Custom Exception Classes
| Exception | Lokasi File | Induk | Dipicu Saat |
|---|---|---|---|
| `HotelException` | Reservasi.java | `Exception` | Base class untuk seluruh exception bisnis hotel |
| `TanggalTidakValidException` | Reservasi.java | `HotelException` | Tanggal check-out tidak setelah check-in |
| `KamarTidakTersediaException` | Hotel.java | `HotelException` | Kamar tidak ditemukan atau sudah dipesan |
| `ReservasiTidakDitemukanException` | Hotel.java | `HotelException` | Kode reservasi tidak ditemukan saat dicari/dibatalkan |
| `InputTidakValidException` | HotelReservasi.java | `Exception` (langsung, bukan `HotelException`) | Nama atau No. KTP dikosongkan saat input reservasi baru |

## 🧠 Konsep OOP yang Diterapkan

| Konsep | Implementasi Nyata dalam Kode |
|---|---|
| **Encapsulation** | Seluruh atribut pada `Kamar`, `Tamu`, `Reservasi`, `Hotel`, dan seluruh subclass `Pembayaran` dideklarasikan `private` (atau `protected` khusus `hargaPerMalam` agar dapat diwariskan), diakses melalui getter/setter publik. Komentar `// [2] ENCAPSULATION` secara eksplisit ditandai di source code pada `Kamar.java`, `Tamu.java`, dan ketiga subclass `Pembayaran`. |
| **Inheritance** | Tiga hierarki pewarisan nyata: (1) `Kamar` → `KamarStandard`, `KamarDeluxe`, `KamarSuite`; (2) `Pembayaran` → `PembayaranTunai`, `PembayaranTransfer`, `PembayaranKartuKredit`; (3) `Exception` → `HotelException` → `TanggalTidakValidException`, `KamarTidakTersediaException`, `ReservasiTidakDitemukanException`. |
| **Polymorphism** | *Method overriding* pada `tipeKamar()` & `fasilitas()` (subclass `Kamar`), serta `prosesPembayaran()` & `namaMetode()` (subclass `Pembayaran`) — ditandai komentar `// [4] POLYMORPHISM` di source code. *Runtime polymorphism* juga terlihat pada `List<Kamar> daftarKamar` yang menyimpan campuran objek `KamarStandard`/`KamarDeluxe`/`KamarSuite`, dan variabel `Pembayaran bayar` yang dapat merujuk ke salah satu dari tiga subclass berdasarkan pilihan pengguna di runtime. |
| **Abstraction** | `Kamar` dan `Pembayaran` dideklarasikan sebagai `abstract class` dengan method abstrak (`tipeKamar()`, `fasilitas()`, `prosesPembayaran()`, `namaMetode()`) yang wajib diimplementasikan subclass — ditandai komentar `// [5] ABSTRACTION` di source code. *(Catatan: tidak ditemukan penggunaan `interface` di seluruh project ini — abstraksi diterapkan murni melalui abstract class.)* |
| **Exception Handling** | 5 custom exception (lihat tabel di atas), penggunaan `throws` pada method seperti `buatReservasi()` dan `cariReservasi()`, *multi-catch* (`catch (KamarTidakTersediaException \| TanggalTidakValidException \| InputTidakValidException e)`) di `menuReservasiBaru()`, blok `try-catch-finally`, serta penanganan exception bawaan Java (`NumberFormatException`, `DateTimeParseException`). |
| **Collection Framework** | `List<Kamar>` dan `List<Reservasi>` (diimplementasikan dengan `ArrayList`) pada class `Hotel`, diiterasi menggunakan `for-each loop`. *(Catatan: hanya `List`/`ArrayList` yang digunakan; tidak ditemukan penggunaan `Map`, `Set`, atau struktur collection lain.)* |

## 🚀 Cara Menjalankan Program

Program ini adalah aplikasi Java murni (tanpa dependency eksternal, tanpa Maven/Gradle) dan dijalankan melalui terminal/command line.

**Prasyarat:** JDK terinstal (disarankan JDK 11 atau lebih baru — proyek ini menggunakan fitur `java.time` seperti `LocalDate` dan `ChronoUnit`).

```bash
# 1. Clone repository
git clone <URL_REPOSITORY_GITHUB>
cd SRH-2

# 2. Compile seluruh file .java (default package, cukup satu folder)
javac *.java

# 3. Jalankan program (class HotelReservasi berisi method main)
java HotelReservasi
```

Setelah dijalankan, program akan menampilkan menu interaktif berbasis teks. Masukkan nomor menu (0–5) sesuai instruksi pada layar.

## 🖥️ Contoh Output

Contoh berikut adalah **hasil eksekusi nyata** program (telah diuji dengan `javac` + `java` pada source code yang diunggah), bukan rekaan.

**Tampilan awal & daftar kamar (Menu 4):**
```
=========================================================
   Selamat Datang di Grand Nusantara Hotel
   Sistem Reservasi Hotel -- Berbasis OOP Java
=========================================================

+==========================+
|        MENU UTAMA        |
|==========================|
|  1. Buat Reservasi Baru  |
|  2. Cek Reservasi        |
|  3. Batalkan Reservasi   |
|  4. Lihat Semua Kamar    |
|  5. Lihat Semua Reservasi|
|  0. Keluar               |
+==========================+
  Pilih menu: 4

=========================================================
  DAFTAR KAMAR -- Grand Nusantara Hotel
=========================================================
  Kamar 101  | Lantai 1 | Standard   | Rp 350,000     /malam | [Tersedia]
  Kamar 102  | Lantai 1 | Standard   | Rp 350,000     /malam | [Tersedia]
  Kamar 103  | Lantai 1 | Standard   | Rp 350,000     /malam | [Tersedia]
  Kamar 201  | Lantai 2 | Deluxe     | Rp 650,000     /malam | [Tersedia]
  Kamar 202  | Lantai 2 | Deluxe     | Rp 650,000     /malam | [Tersedia]
  Kamar 203  | Lantai 2 | Deluxe     | Rp 700,000     /malam | [Tersedia]
  Kamar 301  | Lantai 3 | Suite      | Rp 1,200,000   /malam | [Tersedia]
  Kamar 302  | Lantai 3 | Suite      | Rp 1,500,000   /malam | [Tersedia]
=========================================================
```

**Membuat reservasi baru + pembayaran tunai (Menu 1):**
```
--- RESERVASI BARU ---
  Nama Tamu       : Budi Santoso
  No. KTP         : 3201xxxxxxxx001
  No. Telepon     : 081234567890
  Email           : budi@email.com
  Pilih No. Kamar : 201
  Check-in  (DD-MM-YYYY): 15-07-2026
  Check-out (DD-MM-YYYY): 17-07-2026

  [OK] Reservasi berhasil! Kode: RSV-229BA416

=========================================================
        TAGIHAN RESERVASI HOTEL BINTANG 5
=========================================================
  Kode Reservasi : RSV-229BA416
  Status         : Aktif
---------------------------------------------------------
  DATA TAMU
  Nama: Budi Santoso | KTP: 3201xxxxxxxx001 | Telp: 081234567890 | Email: budi@email.com
---------------------------------------------------------
  DETAIL KAMAR
  Kamar 201  | Lantai 2 | Deluxe     | Rp 650,000     /malam | [Dipesan]
  Fasilitas      : AC, TV 55", Kamar Mandi, WiFi, Mini Bar, Balkon
---------------------------------------------------------
  DETAIL MENGINAP
  Check-in       : 15-07-2026
  Check-out      : 17-07-2026
  Jumlah Malam   : 2 malam
  Harga/Malam    : Rp 650,000
  Total Biaya    : Rp 1,300,000
=========================================================

--- METODE PEMBAYARAN ---
  1. Tunai
  2. Transfer Bank
  3. Kartu Kredit
  Pilih metode (1-3): 1
  Uang diterima (Total: Rp 1,300,000): Rp 1500000

  [OK] Pembayaran tunai berhasil.
  [OK] Kembalian: Rp 200,000

  [OK] Reservasi dan pembayaran selesai!
  PEMBAYARAN     : [Tunai] Rp 1,300,000 -- Lunas
=========================================================
```

**Contoh penanganan error (kamar tidak tersedia, kode reservasi tidak ditemukan, format tanggal salah):**
```
  Pilih No. Kamar : 999
  [ERROR] Kamar 999 tidak tersedia atau sedang dipesan.

--- CEK RESERVASI ---
  Masukkan Kode Reservasi: RSV-XXXXXXX
  [ERROR] Reservasi dengan kode 'RSV-XXXXXXX' tidak ditemukan.

  Check-in  (DD-MM-YYYY): abc-tanggal-salah
  Format salah. Gunakan DD-MM-YYYY (contoh: 15-07-2025)
  Check-in  (DD-MM-YYYY):
```

**Melihat semua reservasi aktif (Menu 5):**
```
=========================================================
  SEMUA RESERVASI AKTIF -- Grand Nusantara Hotel
=========================================================
  [RSV-09ED2A7C] Siti Aminah | Kamar 101 | 2 malam | Rp 700,000
=========================================================
```

## 👥 Anggota Kelompok

> Source code tidak memuat metadata nama anggota kelompok, sehingga tabel berikut disediakan sebagai **template kosong** yang wajib diisi oleh kelompok pengumpul sesuai data sebenarnya.

| No | Nama Lengkap | NIM | Peran/Kontribusi |
|----|--------------|-----|-------------------|
| 1  | `[Nama Mahasiswa 1]` | `[NIM]` | `[contoh: Class Hotel, Kamar, dan subclass-nya]` |
| 2  | `[Nama Mahasiswa 2]` | `[NIM]` | `[contoh: Class Pembayaran dan subclass-nya]` |
| 3  | `[Nama Mahasiswa 3]` | `[NIM]` | `[contoh: Class Reservasi, Tamu, dan main program]` |

## 📝 Kesimpulan

Proyek **Sistem Reservasi Hotel Berbasis OOP** ini berhasil mengimplementasikan keenam pilar konsep Pemrograman Berorientasi Objek yang diujikan: **Encapsulation** (atribut private/protected dengan akses melalui getter-setter), **Inheritance** (tiga hierarki pewarisan: kamar, pembayaran, dan exception), **Polymorphism** (method overriding pada subclass kamar dan pembayaran), **Abstraction** (abstract class `Kamar` dan `Pembayaran`), **Exception Handling** (5 custom exception dengan multi-catch), serta **Collection Framework** (`List`/`ArrayList` untuk mengelola data kamar dan reservasi secara dinamis).

Aplikasi ini berjalan stabil sebagai program CLI sederhana yang mensimulasikan alur bisnis hotel secara end-to-end: dari pencarian kamar, pembuatan reservasi, pemrosesan tiga metode pembayaran, hingga pencetakan tagihan, pengecekan, dan pembatalan reservasi — seluruhnya tervalidasi melalui eksekusi nyata program.

### 💡 Saran Perbaikan Desain (Opsional, untuk pengembangan lanjutan)

Beberapa catatan berikut ditemukan berdasarkan analisis kode dan dapat dijadikan bahan pengembangan, bukan merupakan kekurangan yang mengganggu fungsi utama aplikasi:

1. **Urutan reservasi-pembayaran kurang ideal**: kamar langsung ditandai `tersedia = false` saat `buatReservasi()` dipanggil — *sebelum* pembayaran benar-benar selesai. Jika pembayaran tunai gagal (uang kurang) dan pengguna tidak melakukan percobaan ulang, kamar tetap terkunci sebagai "Dipesan" tanpa pembayaran tercatat dan tanpa reservasi otomatis dibatalkan.
2. **Tidak ada persistensi data**: seluruh data (kamar, reservasi) disimpan di memori (`ArrayList`) dan hilang setiap program ditutup. Untuk pengembangan lanjutan dapat ditambahkan penyimpanan ke file atau database.
3. **`InputTidakValidException` tidak konsisten dengan hierarki exception lain**: exception ini meng-extend `Exception` secara langsung, terpisah dari `HotelException`, sehingga sedikit memutus konsistensi desain hierarki custom exception yang sudah dibangun.
4. **Tidak ada penggunaan interface**: abstraksi sudah baik melalui abstract class, namun penambahan interface (misalnya `Refundable` atau `Dapatdibatalkan`) dapat memperkaya demonstrasi konsep abstraksi multi-pewarisan perilaku.
5. **Validasi data tamu minim**: format No. KTP, No. Telepon, dan Email tidak divalidasi formatnya (hanya dicek tidak kosong untuk Nama dan No. KTP).

---
*Dokumentasi ini dihasilkan murni berdasarkan analisis langsung terhadap source code yang diunggah (9 file `.java`), dan telah diverifikasi melalui kompilasi (`javac`) serta eksekusi nyata (`java`) program.*
