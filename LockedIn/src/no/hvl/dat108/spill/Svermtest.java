package no.hvl.dat108.spill;

public class Svermtest {

    public static void main(String[] args) throws InterruptedException {
        Monster sjefsfiende = new Monster("Sjefsfiende", 1_000_000); // Hvilken sjefsfiende møter Ole?

        Runnable helt = () -> {
            for (int i = 0; i < 100_000; i++) {
                sjefsfiende.taSkade(1);
            }
        };

        Thread ole = new Thread(helt);
        Thread helt2 = new Thread(helt);               // Hvilken helt får Ole hjelp av i siste liten?
        ole.start();
        helt2.start();
        ole.join();
        helt2.join();

        System.out.println(sjefsfiende.navn() + " har " + sjefsfiende.hp()
                + " hp igjen  (forventet 800000)");
    }
}
