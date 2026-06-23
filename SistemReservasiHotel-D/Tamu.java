class Tamu {
    private String nama;       // [2] private
    private String noKtp;
    private String noTelepon;
    private String email;
 
    public Tamu(String nama, String noKtp, String noTelepon, String email) {
        this.nama      = nama;
        this.noKtp     = noKtp;
        this.noTelepon = noTelepon;
        this.email     = email;
    }
 
    public String getNama()      { return nama; }
    public String getNoKtp()     { return noKtp; }
    public String getNoTelepon() { return noTelepon; }
    public String getEmail()     { return email; }
 
    @Override
    public String toString() {
        return String.format("Nama: %s | KTP: %s | Telp: %s | Email: %s",
            nama, noKtp, noTelepon, email);
    }
}
 
