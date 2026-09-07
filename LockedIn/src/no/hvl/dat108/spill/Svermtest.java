package no.hvl.dat108.spill;

public class Svermtest {

    public static void main(String[] args) throws InterruptedException {
        Monster sjefsfiende = new Monster("Kurt Kubein", 1_000_000); // Hvilken sjefsfiende møter Ole?

        System.out.println("En massiv " + sjefsfiende.navn() + " kryper ut fra et mørkt hjørne.");
        System.out.println("Jonny stormer inn en sidedør for å hjelpe, og skremmer vettet av Ole, " +
                "som fortsatt ikke forstår hva som skjer.");
        System.out.println("Han roper 'Drikk denne!' og kaster en magisk trylledrikk bort til Ole.");
        System.out.println("Han ser ikke helt hvordan ting kan bli verre enn de er, så han drikker drikken" +
                " - og får plutselig en ekstrem følelse av fart.");

        Runnable helt = () -> {
            for (int i = 0; i < 100_000; i++) {
                sjefsfiende.taSkade(1);
            }
        };

        Thread ole = new Thread(helt);
        Thread jonny_uten_h = new Thread(helt);               // Hvilken helt får Ole hjelp av i siste liten?
        ole.start();
        jonny_uten_h.start();
        ole.join();
        jonny_uten_h.join();

        System.out.println(sjefsfiende.navn() + " har " + sjefsfiende.hp()
                + " hp igjen  (forventet 800000)");
    }
}
