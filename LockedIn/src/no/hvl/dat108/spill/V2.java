package no.hvl.dat108.spill;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/*
 *  De fire store:
 *
 *  Grensesnitt       Metode    Fasong
 *  Predicate<T>      test      T  -> boolean     "er dette sant om ...?"
 *  Function<T, R>    apply     T  -> R           "gjoer om ... til ..."
 *  Consumer<T>       accept    T  -> ingenting   "gjoer noe med ..."
 *  Supplier<T>       get       () -> T           "gi meg en ..."
 */
public class V2 {

    public static void main(String[] args) {

        Angripbar rottekongen = new Monster("Rottekonge", 500);
        Skatt gullet = new Skatt("gull som brenner", 250);

        // 1. Predicate: tar noe inn, svarer ja eller nei

        Predicate<Angripbar> lever = a -> a.lever();

        boolean rottekongenLever = lever.test(rottekongen);
        System.out.println("Lever Rottekongen? " + rottekongenLever);

        // 2. Function: tar noe inn, gjoer det om til noe annet

        Function<Skatt, String> beskriv = s -> s.navn() + " (" + s.verdi() + ")";

        String tekst = beskriv.apply(gullet);
        System.out.println(tekst);

        // 3. Consumer: tar noe inn, gjoer noe, gir ingenting tilbake

        Consumer<Angripbar> ropTil = a -> System.out.println(a.navn() + " snur seg.");

        ropTil.accept(rottekongen);

        // 4. Supplier: tar ingenting inn, leverer noe ut

        Supplier<Integer> terning = () -> new Random().nextInt(6) + 1;

        int kast = terning.get();
        System.out.println("Terningen viser: " + kast);

        // 5. Metodereferanser: lambdaen videresender bare ett kall

        Predicate<Angripbar> leverRef = Angripbar::lever;
        Function<String, Integer> tall = Integer::parseInt;
        Consumer<String> skriv = System.out::println;
        Supplier<List<Skatt>> nyListe = ArrayList::new;

        System.out.println("Med referanse: " + leverRef.test(rottekongen));
    }
}