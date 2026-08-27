package no.hvl.dat108.spill;

import java.util.Random;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class Helt implements Angripbar {
    private final String navn;
    private int styrke;
    private int hp = 100;
    private final int maxHP = 100;
    //private final Random terning = new Random();
    private final IntSupplier terning;

    public Helt(String navn, int styrke, IntSupplier terning) {
        this.navn = navn;
        this.styrke = styrke;
        this.terning = terning;
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

    @Override
    public int hp() {
        return hp;
    }

    @Override
    public int maxHP() {
        return maxHP;
    }
}
