package no.hvl.dat108.lockedin_app.spiller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Legger inn spillerne når appen har startet (F20). Flyttet hit fra
// LockedinAppApplication før F24: en test av bare web-laget
// (@WebMvcTest) laster alltid hovedklassen, men ikke databasen. Lå denne
// i hovedklassen, ville testen feilet fordi SpillerRepository mangler.
@Component
public class Startdata implements CommandLineRunner {

    private final SpillerRepository spillere;

    public Startdata(SpillerRepository spillere) {
        this.spillere = spillere;
    }

    @Override
    public void run(String... args) {
        spillere.save(new Spiller("Ole", "ostekake"));
        spillere.save(new Spiller("Jonny", "gulrotkake"));
    }
}
