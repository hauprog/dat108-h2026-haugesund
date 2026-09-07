package no.hvl.dat108.spill;

public class Skadeberegner {
    static int totalSkade = 0;

    static synchronized void registrer(int skade) {
        totalSkade +=skade;
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable rotte = () -> {
            for (int i = 0; i < 100_000; i++) {
                registrer(1);
            }
        };
        Thread a = new Thread(rotte);
        Thread b = new Thread(rotte);
        a.start();
        b.start();
        a.join();
        b.join();
        System.out.println("totalSkade = " + totalSkade + "  (forventet 200000)");
    }
}
