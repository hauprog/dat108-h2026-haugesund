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

    public int hp(){
        return hp;
    }

    public int maxHP(){ return maxHP;}

    @Override
    public String toString() {
        return navn + " (" + hp + " hp)";
    }
}
