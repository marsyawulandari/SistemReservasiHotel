import java.time.LocalDateTime;    

abstract class Pembayaran {
    protected double        jumlah;
    protected String        status;
    protected LocalDateTime waktuBayar;

    public Pembayaran(double jumlah) {
        this.jumlah    = jumlah;
        this.status    = "Belum Dibayar";
        this.waktuBayar = null;
    }
 
    // Abstract method — wajib di-override subclass
    public abstract boolean prosesPembayaran();
    public abstract String  namaMetode();
 
    public String getStatus() { return status; }
    public double getJumlah() { return jumlah; }
 
    @Override
    public String toString() {
        return String.format("[%s] Rp %,.0f -- %s", namaMetode(), jumlah, status);
    }
}