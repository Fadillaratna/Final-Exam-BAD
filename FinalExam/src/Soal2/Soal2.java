package Soal2;

class Kendaraan {

    String merk;

    public Kendaraan(String merk) {
        this.merk = merk;
    }

    public void infoKendaraan() {
        System.out.println("Kendaraan merk: " + merk);
    }
}

class Mobil extends Kendaraan {

    int jumlahRoda;

    public Mobil(String merk, int jumlahRoda) {
        super(merk);
        this.jumlahRoda = jumlahRoda; 
    }

    public void infoMobil() {
        System.out.println("Mobil dengan merk: " + merk + ", Jumlah roda: " + jumlahRoda);
    }
}

public class Soal2 {

    public static void main(String[] args) {
        Mobil mobil = new Mobil("Minicooper", 4);
        mobil.infoMobil();
    }

}
