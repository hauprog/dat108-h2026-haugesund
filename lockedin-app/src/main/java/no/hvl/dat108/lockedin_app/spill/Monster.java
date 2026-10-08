package no.hvl.dat108.spill;

public class Monster implements Angripbar{
    private final String navn;
    private int hp;
    private final int maxHP;

    public Monster(String navn, int hp) {
        this.navn = navn;
        this.maxHP = hp;
        this.hp = hp;
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
    public synchronized int hp(){
        return hp;
    }

    @Override
    public int maxHP(){ return maxHP;}

    @Override
    public String toString() {
        return navn + " (" + hp + " hp)";
    }
}
