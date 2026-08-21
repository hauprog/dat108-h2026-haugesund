package no.hvl.dat108.spill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Helt helt = new Helt("Ole", 5);

        // Oppretter et Rom-objekt ved inline lambda-deklarasjon av Hendelse som argument 2.
        Rom operasjonsstue3 = new Rom("Operasjonsstue 3",
                h -> System.out.println("Døren brenner ned bak " + h.navn() + "."));

        // Oppretter et Rom-objekt ved å sende inn en Hendelse-variabel som argument 2
        // som har fått sin verdi/implementasjon ved hjelp av et lambda-uttrykk.
        Hendelse noeSomSkjer = h -> System.out.println("Døren knirker bak " + h.navn() + ".");
        Rom operasjonsstue5 = new Rom("Operasjonsstue 5", noeSomSkjer);

        // Oppretter et Rom-objekt ved å definere en Hendelse som en anonym funksjon i
        // argument 2.
        Rom operasjonsstue7 = new Rom("Operasjonsstue 7", new Hendelse() {
            @Override
            public void skjer(Helt h) {
                System.out.println("Døren låser seg bak " + h.navn() + ".");
            }
        });

        // Oppretter to Hendelser som blir definert vha. lambda
        Hendelse doerFryserTilIs = h -> System.out.println("Døren fryser til is og blir stengt bak " + h.navn() +".");
        Hendelse doerRevetNed = h -> System.out.println("Døren blir revet ned og faller av hengslene bak " + h.navn() +".");
        // To tilfeldige tall valgt blant klassen (kan bruke Random-biblioteket for tilfeldige tall hver gang.
        int jasonTall = 5;
        int bartekTall = 4;
        // Velger en tilfeldig Hendelse som blir lagt til som argument 2 for Rom-objektet.
        // Bruker "tertiary operator" for å velge Hendelse ("if this" ? "do this" : "else this")
        Rom operasjonsstue34 = new Rom("operasjonsstue 34",
                jasonTall > bartekTall ? doerFryserTilIs : doerRevetNed);

        Monster rottekonge = new Monster("Rottekonge", 500);
        Angripbar rottedronning = new Monster("Rottedronning", 500);

        operasjonsstue7.leggTil(rottekonge);
        operasjonsstue7.leggTil(rottedronning);
        operasjonsstue7.leggTil(new Monster("Rottebarn", 9999));
        operasjonsstue7.leggTil(new Skatt("gull som brenner", 250));

        Map<String, Skatt> lager = new HashMap<>();
        for (Skatt s : operasjonsstue7.skatter()) {
            lager.put(s.navn(), s);
        }

        operasjonsstue7.gaaInn(helt);

        Monster rottebarn = operasjonsstue7.monstre().get(1);
        int skade = helt.angrip(rottebarn);
        System.out.println("Du gjør " + skade + " skade. "
                + rottebarn + " har " + rottebarn.hp() + " hp igjen.");

        List<Rom> verden = List.of(operasjonsstue7);
        System.out.println("Rom i verdenen: " + verden.size());
    }
}