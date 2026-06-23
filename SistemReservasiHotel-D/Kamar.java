abstract class Kamar {
    // [2] ENCAPSULATION — private fields
    private String  nomor;
    private int     lantai;
    protected double hargaPerMalam; // protected agar subclass bisa akses
    private boolean tersedia;
 
    public Kamar(String nomor, int lantai, double hargaPerMalam) {
        this.nomor         = nomor;
        this.lantai        = lantai;
        this.hargaPerMalam = hargaPerMalam;
        this.tersedia      = true;
    }
 
    // Getter & Setter — [2] ENCAPSULATION
    public String  getNomor()     { return nomor; }
    public int     getLantai()    { return lantai; }
    public double  getHarga()     { return hargaPerMalam; }
    public boolean isTersedia()   { return tersedia; }
    public void    setTersedia(boolean s) { this.tersedia = s; }
 
    // Abstract methods — [5] ABSTRACTION
    public abstract String   tipeKamar();
    public abstract String[] fasilitas();
 
    public String info() {
        String st = tersedia ? "[Tersedia]" : "[Dipesan] ";
        return String.format("  Kamar %-4s | Lantai %d | %-10s | Rp %,-12.0f/malam | %s",
            nomor, lantai, tipeKamar(), hargaPerMalam, st);
    }
 
    @Override
    public String toString() { return info(); }
}
 
// [3] INHERITANCE — Subclass Kamar
class KamarStandard extends Kamar {
    public KamarStandard(String nomor, int lantai, double harga) {
        super(nomor, lantai, harga);
    }
    @Override public String   tipeKamar() { return "Standard"; }
    @Override public String[] fasilitas() {
        return new String[]{"AC", "TV", "Kamar Mandi", "WiFi"};
    }
}
 
class KamarDeluxe extends Kamar {
    public KamarDeluxe(String nomor, int lantai, double harga) {
        super(nomor, lantai, harga);
    }
    @Override public String   tipeKamar() { return "Deluxe"; }
    @Override public String[] fasilitas() {
        return new String[]{"AC", "TV 55\"", "Kamar Mandi", "WiFi", "Mini Bar", "Balkon"};
    }
}
 
class KamarSuite extends Kamar {
    public KamarSuite(String nomor, int lantai, double harga) {
        super(nomor, lantai, harga);
    }
    @Override public String   tipeKamar() { return "Suite"; }
    @Override public String[] fasilitas() {
        return new String[]{"AC", "Smart TV", "Jacuzzi", "WiFi",
                            "Mini Bar", "Ruang Tamu", "Dapur Kecil", "View Kota"};
    }
}