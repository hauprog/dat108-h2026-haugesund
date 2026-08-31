package no.hvl.dat108.spill;

import java.util.*;
import java.util.function.BiConsumer;

public class Main {
    public static void main(String[] args) {
        Helt helt = new Helt("Ole", 5, () -> new Random().nextInt(6) + 1);

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
        // Bruker "ternary operator" for å velge Hendelse ("if this" ? "do this" : "else this")
        Rom operasjonsstue34 = new Rom("operasjonsstue 34",
                jasonTall > bartekTall ? doerFryserTilIs : doerRevetNed);

        Monster rottekonge = new Monster("Rottekonge", 20);
        Monster rottedronning = new Monster("Rottedronning", 500);

        operasjonsstue7.leggTil(rottekonge);
        operasjonsstue7.leggTil(rottedronning);
        operasjonsstue7.leggTil(new Monster("Rottebarn", 9999));
        operasjonsstue7.leggTil(new Skatt("gull som brenner", 250));

        // Legger til objekter i Operasjonsstue 7
        operasjonsstue7.leggTil(new Skatt("ostekake", Integer.MAX_VALUE));
        operasjonsstue7.leggTil(new Skatt("gulrotkake", Integer.MAX_VALUE - 1));

        Map<String, Skatt> lager = new HashMap<>();
        for (Skatt s : operasjonsstue7.skatter()) {
            lager.put(s.navn(), s);
        }

        operasjonsstue7.gaaInn(helt);


        List<Rom> verden = List.of(operasjonsstue7);
        System.out.println("Rom i verdenen: " + verden.size());


        Map<String, BiConsumer<Helt, Rom>> kommandoer = new HashMap<>();
        kommandoer.put("angrip", (h, r) -> r.angripFoerste(h));
        kommandoer.put("se", (h, r) -> r.visInnhold());
        kommandoer.put("finn", (h, r) ->
                System.out.println("I live i rommet: " + r.finn(Angripbar::lever)));
        kommandoer.put("finnmedmaxhp", (h, r) ->
                System.out.println("I live i rommet med max hp: "
                        + r.finn(a -> a.maxHP() == a.hp())));
        kommandoer.put("status", (h, r) -> {
           List<String> topp3 = r.skatter().stream()
                   .sorted(Comparator.comparingInt(Skatt::verdi).reversed())
                   .limit(3)
                   .map(Skatt::navn)
                   .toList();
        });

        BiConsumer<Helt, Rom> ugyldigKommando = (h, r) -> System.out.println("Ugyldig kommando");




        Scanner inn = new Scanner(System.in);

        while(true){
            System.out.print("> ");
            String kommando = inn.nextLine().trim().toLowerCase();

            if (kommando.equals("q")) {
                break;
            }
            kommandoer.getOrDefault(kommando, ugyldigKommando).accept(helt, operasjonsstue7);


        }


    }
}