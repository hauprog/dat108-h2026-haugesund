package no.hvl.dat108.spill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Helt helt = new Helt("Ole", 5);

        Rom operasjonsstue = new Rom("Operasjonsstue 7", new Hendelse() {
            @Override
            public void skjer(Helt h) {
                System.out.println("Døren låser seg bak " + h.navn() + ".");
            }
        });

        Monster rottekonge = new Monster("Rottekonge", 500);
        Angripbar rottedronning = new Monster("Rottedronning", 500);

        operasjonsstue.leggTil(rottekonge);
        operasjonsstue.leggTil(rottedronning);
        operasjonsstue.leggTil(new Monster("Rottebarn", 9999));
        operasjonsstue.leggTil(new Skatt("gull som brenner", 250));

        Map<String, Skatt> lager = new HashMap<>();
        for (Skatt s : operasjonsstue.skatter()) {
            lager.put(s.navn(), s);
        }

        operasjonsstue.gaaInn(helt);

        Monster rottebarn = operasjonsstue.monstre().get(1);
        int skade = helt.angrip(rottebarn);
        System.out.println("Du gjør " + skade + " skade. "
                + rottebarn + " har " + rottebarn.hp() + " hp igjen.");

        List<Rom> verden = List.of(operasjonsstue);
        System.out.println("Rom i verdenen: " + verden.size());
    }
}