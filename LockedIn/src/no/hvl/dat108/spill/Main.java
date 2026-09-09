package no.hvl.dat108.spill;

import java.util.*;
import java.util.function.BiConsumer;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        // Oppretter en helt, med en terning gitt som en lambda som implementerer Supplier.
        Helt helt = new Helt("Ole", 5, () -> new Random().nextInt(6) + 1);

        // Oppretter et Rom-objekt ved å definere en Hendelse som en anonym klasse ved å implementere skjer()-funksjonen i argument 2.
        Rom operasjonsstue7 = new Rom("Operasjonsstue 7", new Hendelse() {
            @Override
            public void skjer(Helt h) {
                System.out.println("Døren låser seg bak " + h.navn() + ".");
            }
        });

        // Oppretter monstre
        Monster rottekonge = new Monster("Rottekonge", 20);
        Monster rottedronning = new Monster("Rottedronning", 500);
        Monster rottebarn = new Monster("Rottebarn", 9999);

        // Legger monstre til i rommet
        operasjonsstue7.leggTil(rottekonge);
        operasjonsstue7.leggTil(rottedronning);
        operasjonsstue7.leggTil(rottebarn);

        // Oppretter og legger til skatter i rommet direkte som argument
        operasjonsstue7.leggTil(new Skatt("gull som brenner", 250));
        operasjonsstue7.leggTil(new Skatt("ostekake", Integer.MAX_VALUE));
        operasjonsstue7.leggTil(new Skatt("gulrotkake", Integer.MAX_VALUE - 1));

        // Oppretter et lager for skatter og legger så alle skattene inn.
        Map<String, Skatt> lager = new HashMap<>();
        for (Skatt s : operasjonsstue7.skatter()) {
            lager.put(s.navn(), s);
        }

        // Alt er klart og helten går nå inn i rommet
        operasjonsstue7.gaaInn(helt);

        // Vi lager en liste over alle rommene, for senere bruk (og for repetisjon av List.of())
        List<Rom> verden = List.of(operasjonsstue7);

        // Skriver ut skatten med lengst navn i verdenen. Trenger flatMap fordi det er snakk om lister i liste.
        Optional<String> lengst = verden.stream()
                .flatMap(r -> r.skatter().stream())
                .map(Skatt::navn)
                .reduce((a, b) -> a.length() >= b.length() ? a : b);
        System.out.println("Lengste skattenavn i sykehuset: " + lengst.orElse("ingen"));

        // Vi lager et Map av kommandoer, hvor nøkkelen er kommandoen gitt som tekststreng
        // og selve handlingen knyttet til kommandoen blir definert som en BiConsumer
        // som tar inn helten og rommet helten er i, for så å gjøre noe med dette.
        // Helten kunne fått Rom som en feltvariabel, og vi kunne da heller hatt en Consumer.
        Map<String, BiConsumer<Helt, Rom>> kommandoer = new HashMap<>();
        kommandoer.put("angrip", (h, r) -> r.angripFoerste(h));
        kommandoer.put("se", (h, r) -> r.visInnhold());
        kommandoer.put("finn", (h, r) ->
                System.out.println("I live i rommet: " + r.finn(Angripbar::lever)));
        kommandoer.put("finnmedmaxhp", (h, r) ->
                System.out.println("I live i rommet med max hp: "
                        + r.finn(a -> a.maxHP() == a.hp())));
        kommandoer.put("rapport", (h, r) -> System.out.println(r.rapport()));
        kommandoer.put("status", (h, r) -> {
           List<String> topp3 = r.skatter().stream()
                   .sorted(Comparator.comparingInt(Skatt::verdi).reversed())
                   .limit(3)
                   .map(Skatt::navn)
                   .toList();
            System.out.println("Topp 3 skatter: " + topp3);
        });

        // Oppretter en separat kommando som vil bli brukt som en default-verdi, som vi sender inn som eget
        // argument, som vil bli brukt om ingen verdi i "kommandoer" matcher.
        BiConsumer<Helt, Rom> ugyldigKommando = (h, r) -> System.out.println("Ugyldig kommando");

        // Oppretter en tråd som skal skape atmosfære i rommet ved å fortelle om bakgrunnsaktivitet.
        Thread atmosfaere = new Thread(() -> {
            while (helt.lever()){
                System.out.println("Lysrøret blinker.");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        // Oppretter en tråd som skal sørge for at rottene angriper helten, samtidig som man kan utføre
        // kommandoer i main-tråden uten å bli avbrutt.
        Thread rottetraad = new Thread(() -> {
            while (helt.lever()){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    return;
                }

                operasjonsstue7.finnFoerste(Angripbar::lever).ifPresent(rotte ->{
                    helt.taSkade(3);
                    System.out.println(rotte.navn() + " 360-noscoper " + helt.navn() + " under kneet! ("
                            + helt.hp() + " hp igjen)");
                });
            }
        });

        // Starter trådene - merk start(), ikke run()
        atmosfaere.start();
        rottetraad.start();

        // Oppretter et Scanner-objekt som skal bli brukt til å lese inndata.
        Scanner inn = new Scanner(System.in);

        // Starter en løkke som skal håndtere spillflyten
        while(true){

            if(!helt.lever()){
                System.out.println("You died.");
                break;
            }
            System.out.print("> ");
            String kommando = inn.nextLine().trim().toLowerCase();

            if (kommando.equals("q")) {
                break;
            }
            kommandoer.getOrDefault(kommando, ugyldigKommando).accept(helt, operasjonsstue7);
        }

        // Avbryter trådene, venter på rottetråd, og skriver ut avslutningsmelding
        atmosfaere.interrupt();
        rottetraad.interrupt();
        rottetraad.join();
        System.out.println("Du forlater sykehuset.");

    }
}