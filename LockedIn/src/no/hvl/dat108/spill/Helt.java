package no.hvl.dat108.spill;

import java.util.Random;
import java.util.function.IntSupplier;

public class Helt implements Angripbar {
    private final String navn;
    private int styrke;
    private int hp = 100;
    private final int maxHP = 100;
    private final IntSupplier terning;

    public Helt(String navn, int styrke){
        this(navn, styrke, () -> new Random().nextInt(6) + 1);
    }

    public Helt(String navn, int styrke, IntSupplier terning) {
        this.navn = navn;
        this.styrke = styrke;
        this.terning = terning;
    }

    public static int beregnSkade(int styrke, int kast){
        return styrke * 2 + kast;
    }

    public int angrip(Angripbar offer){
        int skade = beregnSkade(styrke, terning.getAsInt());
        offer.taSkade(skade);
        return skade;
    }

    @Override
    public synchronized void taSkade(int mengde) {
        hp = Math.max(0, hp - mengde);
    }

    @Override
    public synchronized boolean lever() {
        return hp > 0;
    }

    @Override
    public String navn() {
        return navn;
    }

    @Override
    public synchronized int hp() {
        return hp;
    }

    @Override
    public int maxHP() {
        return maxHP;
    }
}
