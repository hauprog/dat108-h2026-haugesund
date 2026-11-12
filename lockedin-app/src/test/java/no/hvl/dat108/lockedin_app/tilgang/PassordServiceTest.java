package no.hvl.dat108.lockedin_app.tilgang;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Enhetstester av PassordService (F26), med ren JUnit: klassen har ingen
// avhengigheter. Hver hash tar litt tid (600 000 runder), så testene er
// tregere enn de andre. Det er prisen for en treg hash.
class PassordServiceTest {

    PassordService passord = new PassordService();
    String salt;
    String hash;

    @BeforeEach
    void lagreOle() {
        salt = passord.genererTilfeldigSalt();
        hash = passord.hashMedSalt("ostekake", salt);
    }

    @Test
    void riktigPassordGodtas() {
        assertTrue(passord.validerMedSalt("ostekake", salt, hash));
    }

    @Test
    void feilPassordAvvises() {
        assertFalse(passord.validerMedSalt("gulrotkake", salt, hash));
    }

    // Saltet er grunnen til at to spillere med samme passord får ulik hash
    @Test
    void sammePassordGirUlikHashMedUliktSalt() {
        String annetSalt = passord.genererTilfeldigSalt();

        assertNotEquals(salt, annetSalt);
        assertNotEquals(hash, passord.hashMedSalt("ostekake", annetSalt));
    }

    @Test
    void hashenErIkkePassordet() {
        assertNotEquals("ostekake", hash);
        assertEquals(64, hash.length());           // 256 bit som hex
    }

    @Test
    void nullPassordKasterUnntak() {
        assertThrows(IllegalArgumentException.class,
                () -> passord.validerMedSalt(null, salt, hash));
    }
}
