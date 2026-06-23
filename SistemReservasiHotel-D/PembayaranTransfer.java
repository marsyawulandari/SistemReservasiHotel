import java.time.LocalDateTime;
class PembayaranTransfer extends Pembayaran {
    private String namaBank;   // [2] ENCAPSULATION: private
    private String noRekening;
 
    public PembayaranTransfer(double jumlah, String namaBank, String noRekening) {
        super(jumlah);
        this.namaBank   = namaBank;
        this.noRekening = noRekening;
    }
 
    @Override // [4] POLYMORPHISM
    public boolean prosesPembayaran() {
        System.out.printf("  Transfer ke %s | No. Rek: %s%n", namaBank, noRekening);
        System.out.printf("  Nominal: Rp %,.0f%n", jumlah);
        status     = "Lunas";
        waktuBayar = LocalDateTime.now();
        System.out.println("  [OK] Pembayaran transfer dikonfirmasi.");
        return true;
    }
 
    @Override
    public String namaMetode() { return "Transfer Bank (" + namaBank + ")"; }
}