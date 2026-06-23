import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class InputTidakValidException extends Exception {
    public InputTidakValidException(String pesan) {
        super(pesan);
    }
}

public class HotelReservasi {
 
    static Scanner scanner = new Scanner(System.in);
 
    static LocalDate inputTanggal(String prompt) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(scanner.nextLine().trim(), fmt);
            } catch (DateTimeParseException e) {
                System.out.println("  Format salah. Gunakan DD-MM-YYYY (contoh: 15-07-2025)");
            }
        }
    }
 
    static int inputInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int n = Integer.parseInt(scanner.nextLine().trim());
                if (n >= min && n <= max) return n;
                System.out.printf("  Pilihan harus antara %d dan %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  Masukkan angka yang valid.");
            }
        }
    }
 
    static void menuReservasiBaru(Hotel hotel) {
        System.out.println("\n--- RESERVASI BARU ---");
        try {
            System.out.print("  Nama Tamu       : "); String nama  = scanner.nextLine().trim();
            System.out.print("  No. KTP         : "); String ktp   = scanner.nextLine().trim();
            System.out.print("  No. Telepon     : "); String telp  = scanner.nextLine().trim();
            System.out.print("  Email           : "); String email = scanner.nextLine().trim();
 
            if (nama.isEmpty() || ktp.isEmpty())
                throw new InputTidakValidException("Nama dan No. KTP tidak boleh kosong.");
 
            Tamu tamu = new Tamu(nama, ktp, telp, email);
 
            hotel.tampilkanSemuaKamar();
            if (hotel.cariKamarTersedia().isEmpty()) {
                System.out.println("  Tidak ada kamar yang tersedia."); return;
            }
 
            System.out.print("  Pilih No. Kamar : ");
            String noKamar = scanner.nextLine().trim().toUpperCase();
            LocalDate checkin  = inputTanggal("  Check-in  (DD-MM-YYYY): ");
            LocalDate checkout = inputTanggal("  Check-out (DD-MM-YYYY): ");
 
            Reservasi reservasi = hotel.buatReservasi(tamu, noKamar, checkin, checkout);
            System.out.println("\n  [OK] Reservasi berhasil! Kode: " + reservasi.getKode());
            reservasi.cetakTagihan();
 
            System.out.println("\n--- METODE PEMBAYARAN ---");
            System.out.println("  1. Tunai");
            System.out.println("  2. Transfer Bank");
            System.out.println("  3. Kartu Kredit");
            int pil   = inputInt("  Pilih metode (1-3): ", 1, 3);
            double tot = reservasi.getTotalBiaya();
            Pembayaran bayar = null;
 
            if (pil == 1) {
                System.out.printf("  Uang diterima (Total: Rp %,.0f): Rp ", tot);
                double uang = Double.parseDouble(scanner.nextLine().trim().replace(",", ""));
                bayar = new PembayaranTunai(tot, uang);
            } else if (pil == 2) {
                System.out.print("  Nama Bank    : "); String bank  = scanner.nextLine().trim();
                System.out.print("  No. Rekening : "); String noRek = scanner.nextLine().trim();
                bayar = new PembayaranTransfer(tot, bank, noRek);
            } else {
                System.out.print("  No. Kartu Kredit : "); String noKartu  = scanner.nextLine().trim();
                System.out.print("  Nama Pemegang    : "); String pemegang = scanner.nextLine().trim();
                bayar = new PembayaranKartuKredit(tot, noKartu, pemegang);
            }
 
            System.out.println();
            if (bayar != null && bayar.prosesPembayaran()) {
                reservasi.setPembayaran(bayar);
                System.out.println("\n  [OK] Reservasi dan pembayaran selesai!");
                reservasi.cetakTagihan();
            }
 
        } catch (KamarTidakTersediaException | TanggalTidakValidException
                | InputTidakValidException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        } catch (HotelException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("\n  [ERROR] Jumlah uang tidak valid.");
        } finally {
            System.out.println("  --------------------------------");
        }
    }
 
    static void menuCekReservasi(Hotel hotel) {
        System.out.println("\n--- CEK RESERVASI ---");
        System.out.print("  Masukkan Kode Reservasi: ");
        String kode = scanner.nextLine().trim().toUpperCase();
        try {
            hotel.cariReservasi(kode).cetakTagihan();
        } catch (ReservasiTidakDitemukanException e) {
            System.out.println("  [ERROR] " + e.getMessage());
        }
    }
 
    static void menuBatalkanReservasi(Hotel hotel) {
        System.out.println("\n--- BATALKAN RESERVASI ---");
        System.out.print("  Masukkan Kode Reservasi: ");
        String kode = scanner.nextLine().trim().toUpperCase();
        try {
            System.out.print("  Yakin batalkan " + kode + "? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                hotel.batalkanReservasi(kode);
            } else {
                System.out.println("  Pembatalan dibatalkan.");
            }
        } catch (ReservasiTidakDitemukanException e) {
            System.out.println("  [ERROR] " + e.getMessage());
        }
    }
 
    public static void main(String[] args) {
        Hotel hotel = new Hotel("Grand Nusantara Hotel", "Jl. Sudirman No. 1, Jakarta");
 
        hotel.tambahKamar(new KamarStandard("101", 1, 350_000));
        hotel.tambahKamar(new KamarStandard("102", 1, 350_000));
        hotel.tambahKamar(new KamarStandard("103", 1, 350_000));
        hotel.tambahKamar(new KamarDeluxe  ("201", 2, 650_000));
        hotel.tambahKamar(new KamarDeluxe  ("202", 2, 650_000));
        hotel.tambahKamar(new KamarDeluxe  ("203", 2, 700_000));
        hotel.tambahKamar(new KamarSuite   ("301", 3, 1_200_000));
        hotel.tambahKamar(new KamarSuite   ("302", 3, 1_500_000));
 
        System.out.println("\n" + "=".repeat(57));
        System.out.println("   Selamat Datang di " + hotel.getNama());
        System.out.println("   Sistem Reservasi Hotel -- Berbasis OOP Java");
        System.out.println("=".repeat(57));
 
        while (true) {
            System.out.println("\n+==========================+");
            System.out.println("|        MENU UTAMA        |");
            System.out.println("|==========================|");
            System.out.println("|  1. Buat Reservasi Baru  |");
            System.out.println("|  2. Cek Reservasi        |");
            System.out.println("|  3. Batalkan Reservasi   |");
            System.out.println("|  4. Lihat Semua Kamar    |");
            System.out.println("|  5. Lihat Semua Reservasi|");
            System.out.println("|  0. Keluar               |");
            System.out.println("+==========================+");
            System.out.print("  Pilih menu: ");
 
            switch (scanner.nextLine().trim()) {
                case "1": menuReservasiBaru(hotel);       break;
                case "2": menuCekReservasi(hotel);        break;
                case "3": menuBatalkanReservasi(hotel);   break;
                case "4": hotel.tampilkanSemuaKamar();    break;
                case "5": hotel.tampilkanSemuaReservasi();break;
                case "0":
                    System.out.println("\n  Terima kasih. Sampai jumpa!\n");
                    scanner.close();
                    return;
                default:
                    System.out.println("  Menu tidak valid. Pilih 0-5.");
            }
        }
    }
}