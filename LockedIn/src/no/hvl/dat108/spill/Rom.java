package no.hvl.dat108.spill;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Rom {
    private final String navn;
    private final List<Angripbar> angripbare = new ArrayList<>();
    private final List<Skatt> skatter = new ArrayList<>();
    private final Hendelse vedInngang;

    public Rom(String navn, Hendelse vedInngang) {
        this.navn = navn;
        this.vedInngang = vedInngang;
    }

    public void leggTil(Skatt s){skatter.add(s);}
    public void leggTil(Angripbar a){angripbare.add(a);}

    public void gaaInn(Helt helt) {
        System.out.println("Du er i: " + navn);
        vedInngang.skjer(helt);
        visInnhold();
    }

    public void visInnhold(){
        //System.out.println("Monster her:" + monstre);
        System.out.println("Angripbare objekter her:" + angripbare);
        System.out.println("Skatter her:" + skatter);
    }

    public List<Angripbar> finn(Predicate<Angripbar> kriterium){
        return angripbare.stream().filter(kriterium).toList();
    }

    public void angripFoerste(Helt helt){
        for (Angripbar a : angripbare){
            if(a.lever()){
                int skade = helt.angrip(a);
                System.out.println(helt.navn() + " gjør " + skade + " skade på " + a.navn() + ".");
                if (!a.lever()){
                    System.out.println(a.navn() + " er drept.");
                }
                return;
            }

        }
        System.out.println("Det er ingenting å angripe her.");
    }

    //public List<Monster> monstre() {return monstre;}
    public List<Angripbar> angripbar() {return angripbare;}
    public List<Skatt> skatter() {return skatter;}
}
