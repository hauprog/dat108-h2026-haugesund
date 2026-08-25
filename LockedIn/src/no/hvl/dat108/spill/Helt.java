package no.hvl.dat108.spill;

import java.util.Random;

public class Helt implements Angripbar {
    private final String navn;
    private int styrke;
    private int hp = 100;
    //private final Random terning = new Random();

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
        hp = Math.max(0, hp - mengde);
    }

    @Override
    public boolean lever() {
        return hp > 0;
    }

    @Override
    public String navn() {
        return navn;
    }
}
