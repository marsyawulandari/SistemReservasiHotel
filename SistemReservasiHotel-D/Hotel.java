import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class KamarTidakTersediaException extends HotelException {
    public KamarTidakTersediaException(String nomorKamar) {
        super("Kamar " + nomorKamar + " tidak tersedia atau sedang dipesan.");
    }
}

class ReservasiTidakDitemukanException extends HotelException {
    public ReservasiTidakDitemukanException(String kode) {
        super("Reservasi dengan kode '" + kode + "' tidak ditemukan.");
    }
}

class Hotel {
    private String          nama;
    private String          alamat;
    private List<Kamar>     daftarKamar;
    private List<Reservasi> daftarReservasi;

    public Hotel(String nama, String alamat) {
        this.nama            = nama;
        this.alamat          = alamat;
        this.daftarKamar     = new ArrayList<>();
        this.daftarReservasi = new ArrayList<>();
    }

    public void tambahKamar(Kamar k) { 
        daftarKamar.add(k); 
    }

    public List<Kamar> cariKamarTersedia() {
        List<Kamar> hasil = new ArrayList<>();
        for (Kamar k : daftarKamar) {
            if (k.isTersedia()) {
                hasil.add(k);
            }
        }
        return hasil; // Digunakan agar warning kuning hilang
    }

    public Kamar cariKamarByNomor(String nomor) {
        for (Kamar k : daftarKamar) {
            if (k.getNomor().equalsIgnoreCase(nomor)) {
                return k;
            }
        }
        return null;
    }

    public Reservasi buatReservasi(Tamu tamu, String nomorKamar,
                                   LocalDate checkin, LocalDate checkout)
            throws HotelException {
        Kamar kamar = cariKamarByNomor(nomorKamar);
        if (kamar == null || !kamar.isTersedia()) {
            throw new KamarTidakTersediaException(nomorKamar);
        }
        Reservasi r = new Reservasi(tamu, kamar, checkin, checkout);
        kamar.setTersedia(false);
        daftarReservasi.add(r);
        return r;
    }

    public Reservasi cariReservasi(String kode)
            throws ReservasiTidakDitemukanException {
        for (Reservasi r : daftarReservasi) {
            if (r.getKode().equalsIgnoreCase(kode)) {
                return r;
            }
        }
        throw new ReservasiTidakDitemukanException(kode);
    }

    public void batalkanReservasi(String kode)
            throws ReservasiTidakDitemukanException {
        cariReservasi(kode).batalkan();
        System.out.println("  Reservasi " + kode + " berhasil dibatalkan.");
    }

    public void tampilkanSemuaKamar() {
        System.out.println("\n" + "=".repeat(57));
        System.out.println("  DAFTAR KAMAR -- " + nama);
        System.out.println("=".repeat(57));
        for (Kamar k : daftarKamar) {
            System.out.println(k.info());
        }
        System.out.println("=".repeat(57));
    }

    public void tampilkanSemuaReservasi() {
        System.out.println("\n" + "=".repeat(57));
        System.out.println("  SEMUA RESERVASI AKTIF -- " + nama);
        System.out.println("=".repeat(57));
        boolean ada = false;
        for (Reservasi r : daftarReservasi) {
            if (r.getStatus().equals("Aktif")) {
                System.out.printf("  [%s] %s | Kamar %s | %d malam | Rp %,.0f%n",
                    r.getKode(), r.getTamu().getNama(),
                    r.getKamar().getNomor(), r.getJumlahMalam(), r.getTotalBiaya());
                ada = true;
            }
        }
        if (!ada) {
            System.out.println("  Tidak ada reservasi aktif.");
        }
        System.out.println("=".repeat(57));
    }

    public String getNama() { 
        return nama; 
    }
    public String getAlamat() { 
        return alamat; 
    }
}