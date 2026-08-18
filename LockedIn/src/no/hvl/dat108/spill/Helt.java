package no.hvl.dat108.spill;

import java.util.Random;

public class Helt implements Angripbar {
    private final String navn;
    private int styrke;
    private final Random terning = new Random();

    public Helt(String navn, int styrke) {
        this.navn = navn;
        this.styrke = styrke;
    }

    public int angrip(Angripbar offer){
        int skade = styrke;
        offer.taSkade(skade);
        return skade;
    }

    @Override
    public void taSkade(int mengde) {

    }

    @Override
    public boolean lever() {
        return false;
    }

    @Override
    public String navn() {
        return navn;
    }
}
