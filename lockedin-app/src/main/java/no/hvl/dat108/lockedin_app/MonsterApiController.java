package no.hvl.dat108.lockedin_app;

import no.hvl.dat108.lockedin_app.spill.Rom;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// Monstrene i et rom som en REST-ressurs. Fylles ut i live-koding 2 (F22).
//   samlingen: /api/rom/{nr}/monstre
//   ett monster: /api/rom/{nr}/monstre/{navn}
public class MonsterApiController {

    // Det klienten sender med POST. Jackson lager den fra JSON-en.
    public record NyttMonster(String navn, int hp) {}

    private final SykehusService sykehus;

    public MonsterApiController(SykehusService sykehus) {
        this.sykehus = sykehus;
    }

    // GET    samlingen:    200 og alle monstrene
    // GET    ett monster:  200 og monsteret, eller 404
    // POST   samlingen:    201, Location og det nye monsteret
    // DELETE ett monster:  204 uten kropp, eller 404

    // Ukjent rom: 404 for hele ressursen, som i SpillApiController
    private Rom finnRom(int nr) {
        return sykehus.finnRom(nr).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Rom " + nr + " finnes ikke."));
    }
}
