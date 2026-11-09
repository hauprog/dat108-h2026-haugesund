package no.hvl.dat108.lockedin_app.spill;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// En stub vi skriver selv, før Mockito (F24). Lagt inn ferdig før F25.
class HeltTest {

    // Terningen kommer inn som en IntSupplier (modul 1). Testen gir Ole en
    // stub, en terning som alltid slår 6, og svaret blir likt hver gang.
    @Test
    void angrepMedSekserGirForutsigbarSkade() {
        Helt ole = new Helt("Ole", 5, () -> 6);
        Monster rotte = new Monster("Rotte", 20);

        int skade = ole.angrip(rotte);

        assertEquals(16, skade);        // 5 * 2 + 6
        assertEquals(4, rotte.hp());
    }
}
