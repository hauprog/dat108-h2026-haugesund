package no.hvl.dat108.oving;

// Helten fra LockedIn, uten terning. Legg merke til  at metodenavnene er
// navn() og hp(), ikke getNavn() og getHp(). Dette har betydning i oppgave 6.
public class Helt {
    private final String navn;
    private final int maxHp;
    private int hp;

    public Helt(String navn, int maxHp) {
        this.navn = navn;
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    public synchronized void taSkade(int mengde) {
        hp = Math.max(0, hp - mengde);
    }

    public String navn() { return navn; }
    public synchronized int hp() { return hp; }
    public int maxHp() { return maxHp; }
    public synchronized boolean lever() { return hp > 0; }
}
