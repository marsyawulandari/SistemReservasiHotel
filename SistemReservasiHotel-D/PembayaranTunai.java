import java.time.LocalDateTime; 

class PembayaranTunai extends Pembayaran {
    private double uangDiterima; // [2] ENCAPSULATION: private
 
    public PembayaranTunai(double jumlah, double uangDiterima) {
        super(jumlah);
        this.uangDiterima = uangDiterima;
    }
 
    @Override // [4] POLYMORPHISM
    public boolean prosesPembayaran() {
        if (uangDiterima >= jumlah) {
            double kembalian = uangDiterima - jumlah;
            status     = "Lunas";
            waktuBayar = LocalDateTime.now();
            System.out.println("  [OK] Pembayaran tunai berhasil.");
            System.out.printf ("  [OK] Kembalian: Rp %,.0f%n", kembalian);
            return true;
        }
        System.out.printf("  [X] Uang tidak cukup. Kurang: Rp %,.0f%n",
                          jumlah - uangDiterima);
        return false;
    }
 
    @Override
    public String namaMetode() { return "Tunai"; }
}