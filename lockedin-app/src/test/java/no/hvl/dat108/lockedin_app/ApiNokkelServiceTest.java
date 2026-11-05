package no.hvl.dat108.lockedin_app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Live 2 (F24), trinn 1: enhetstest med ren JUnit. ApiNokkelService har
// ingen avhengigheter, så testen lager den selv med new. Ingen Spring.
class ApiNokkelServiceTest {
    ApiNokkelService nokler = new ApiNokkelService("dukommerdegaldriinn108%");

    @Test
    void riktigNokkelErGyldig(){
        assertTrue(nokler.erGyldig("dukommerdegaldriinn108%"));
    }

    @Test
    void feilNokkelErUgyldig(){
        assertFalse(nokler.erGyldig("dukomegaldinn2%"));
    }

    @Test
    void manglendeNokkelErUgyldig(){
        assertFalse(nokler.erGyldig(null));
    }
}
