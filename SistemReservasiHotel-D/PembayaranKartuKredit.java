import java.time.LocalDateTime;
class PembayaranKartuKredit extends Pembayaran {
    private String empatDigitAkhir; // [2] ENCAPSULATION: private
    private String namaPemegang;
 
    public PembayaranKartuKredit(double jumlah, String noKartu, String namaPemegang) {
        super(jumlah);
        this.empatDigitAkhir = noKartu.length() >= 4
            ? noKartu.substring(noKartu.length() - 4) : noKartu;
        this.namaPemegang = namaPemegang;
    }
 
    @Override // [4] POLYMORPHISM
    public boolean prosesPembayaran() {
        System.out.printf("  Kartu Kredit ****%s a.n. %s%n",
                          empatDigitAkhir, namaPemegang);
        System.out.printf("  Memproses Rp %,.0f...%n", jumlah);
        status     = "Lunas";
        waktuBayar = LocalDateTime.now();
        System.out.println("  [OK] Transaksi kartu kredit disetujui.");
        return true;
    }
 
    @Override
    public String namaMetode() {
        return "Kartu Kredit (****" + empatDigitAkhir + ")";
    }
}