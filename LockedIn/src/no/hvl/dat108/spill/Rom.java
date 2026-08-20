package no.hvl.dat108.spill;

import java.util.ArrayList;
import java.util.List;

public class Rom {
    private final String navn;
    private final List<Monster> monstre = new ArrayList<>();
    private final List<Angripbar> angripbare = new ArrayList<>();
    private final List<Skatt> skatter = new ArrayList<>();
    private final Hendelse vedInngang;

    public Rom(String navn, Hendelse vedInngang) {
        this.navn = navn;
        this.vedInngang = vedInngang;
    }

    public void leggTil(Monster m){monstre.add(m);}
    public void leggTil(Skatt s){skatter.add(s);}
    public void leggTil(Angripbar a){angripbare.add(a);}

    public void gaaInn(Helt helt) {
        System.out.println("Du er i:" + navn);
        vedInngang.skjer(helt);
        System.out.println("Monster her:" + monstre);
        System.out.println("Skatter her:" + skatter);
    }

    public List<Monster> monstre() {return monstre;}
    public List<Angripbar> angripbar() {return angripbare;}
    public List<Skatt> skatter() {return skatter;}
}
