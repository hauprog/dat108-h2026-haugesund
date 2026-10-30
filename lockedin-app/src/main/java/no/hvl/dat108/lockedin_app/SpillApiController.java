package no.hvl.dat108.lockedin_app;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

// Data til spillet i nettleseren (static/spill.js). Svarer med JSON, ikke
// med en side. Samme rom som RomController viser, fra samme SykehusService:
// et monster som lages i skjemaet på /rom/7, dukker opp i spillet.
@RestController
public class SpillApiController {

    private final SykehusService sykehus;

    public SpillApiController(SykehusService sykehus) {
        this.sykehus = sykehus;
    }

    @GetMapping("/api/rom/{nr}")
    public RomVisning rom(@PathVariable int nr) {
        return sykehus.finnRom(nr)
                .map(rom -> RomVisning.fra(nr, rom))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Rom " + nr + " finnes ikke."));
    }
}
