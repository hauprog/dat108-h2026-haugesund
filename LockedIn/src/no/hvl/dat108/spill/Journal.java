package no.hvl.dat108.spill;

import java.util.ArrayList;
import java.util.List;

public class Journal {
    private static final int KAPASITET = 5;

    private int totalSkade = 0;
    private final List<String> handlinger = new ArrayList<>();

    // Produsenten: legg inn, og si fra. Venter hvis journalen er full.
    public synchronized void registrer(String rotte, int skade) throws InterruptedException {
        while (handlinger.size() >= KAPASITET) {
            wait();                          // full: vent til noen tar ut
        }
        totalSkade += skade;
        handlinger.add(rotte + ": " + skade);
        notifyAll();                         // vekk alle i venterommet
    }

    public synchronized int totalSkade() {
        return totalSkade;
    }

    public synchronized List<String> handlinger() {
        return new ArrayList<>(handlinger);
    }

    // Konsumenten: ta ut, og si fra. Venter hvis journalen er tom.
    public synchronized String taUt() throws InterruptedException {
        while (handlinger.isEmpty()) {
            wait();                          // tom: vent til noen legger inn
        }
        String neste = handlinger.remove(0);
        notifyAll();                         // noen kan vente på plass
        return neste;
    }
}