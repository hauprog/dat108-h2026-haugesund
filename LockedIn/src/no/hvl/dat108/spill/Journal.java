package no.hvl.dat108.spill;

import java.util.ArrayList;
import java.util.List;

public class Journal {
    private int totalSkade = 0;
    private final List<String> handlinger = new ArrayList<>();

    public synchronized void registrer(String rotte, int skade) {
        totalSkade += skade;
        handlinger.add(rotte + ": " + skade);
        notifyAll();
    }

    public synchronized int totalSkade() {
        return totalSkade;
    }

    public synchronized List<String> handlinger() {
        return new ArrayList<>(handlinger);
    }

    // Neste hendelse, eller null hvis journalen er tom. Byttes ut i steg 2.
    public synchronized String taUt() throws InterruptedException{
        while (handlinger.isEmpty()){
            wait();
        }
        return handlinger.removeFirst();
    }
}