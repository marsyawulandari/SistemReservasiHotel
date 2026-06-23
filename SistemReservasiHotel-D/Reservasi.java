import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

class HotelException extends Exception {
    public HotelException(String pesan) { super(pesan); }
}

class TanggalTidakValidException extends HotelException {
    public TanggalTidakValidException() {
        super("Tanggal check-out harus setelah tanggal check-in.");
    }
}

class Reservasi {
    private String        kode;
    private Tamu          tamu;
    private Kamar         kamar;
    private LocalDate     tanggalCheckin;
    private LocalDate     tanggalCheckout;
    private int           jumlahMalam;
    private double        totalBiaya;
    private String        status;
    private Pembayaran    pembayaran;
    private LocalDateTime waktuBuat;

    public Reservasi(Tamu tamu, Kamar kamar,
                     LocalDate checkin, LocalDate checkout)
            throws TanggalTidakValidException {

        if (!checkout.isAfter(checkin))
            throw new TanggalTidakValidException();

        this.kode            = "RSV-" + UUID.randomUUID()
                                            .toString().substring(0,8).toUpperCase();
        this.tamu            = tamu;
        this.kamar           = kamar;
        this.tanggalCheckin  = checkin;
        this.tanggalCheckout = checkout;
        this.jumlahMalam     = (int) ChronoUnit.DAYS.between(checkin, checkout);
        this.totalBiaya      = jumlahMalam * kamar.getHarga();
        this.status          = "Aktif";
        this.pembayaran      = null;
        this.waktuBuat       = LocalDateTime.now();
    }

    public String  getKode()        { return kode; }
    public Tamu    getTamu()        { return tamu; }
    public Kamar   getKamar()       { return kamar; }
    public double  getTotalBiaya()  { return totalBiaya; }
    public String  getStatus()      { return status; }
    public int     getJumlahMalam() { return jumlahMalam; }

    public void setPembayaran(Pembayaran p) { this.pembayaran = p; }

    public void batalkan() {
        this.status = "Dibatalkan";
        this.kamar.setTersedia(true);
    }

    public void cetakTagihan() {
        DateTimeFormatter fmtTgl   = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter fmtWaktu = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String g  = "=".repeat(57);
        String gd = "-".repeat(57);

        System.out.println("\n" + g);
        System.out.println("        TAGIHAN RESERVASI HOTEL BINTANG 5");
        System.out.println(g);
        System.out.printf ("  Kode Reservasi : %s%n",  kode);
        System.out.printf ("  Dibuat Pada    : %s%n",  waktuBuat.format(fmtWaktu));
        System.out.printf ("  Status         : %s%n",  status);
        System.out.println(gd);
        System.out.println("  DATA TAMU");
        System.out.println("  " + tamu);
        System.out.println(gd);
        System.out.println("  DETAIL KAMAR");
        System.out.println(kamar.info());
        System.out.println("  Fasilitas      : " + String.join(", ", kamar.fasilitas()));
        System.out.println(gd);
        System.out.println("  DETAIL MENGINAP");
        System.out.printf ("  Check-in       : %s%n",       tanggalCheckin.format(fmtTgl));
        System.out.printf ("  Check-out      : %s%n",       tanggalCheckout.format(fmtTgl));
        System.out.printf ("  Jumlah Malam   : %d malam%n", jumlahMalam);
        System.out.printf ("  Harga/Malam    : Rp %,.0f%n", kamar.getHarga());
        System.out.printf ("  Total Biaya    : Rp %,.0f%n", totalBiaya);
        System.out.println(gd);
        System.out.println("  PEMBAYARAN     : " +
            (pembayaran != null ? pembayaran : "Belum dibayar"));
        System.out.println(g);
    }
}