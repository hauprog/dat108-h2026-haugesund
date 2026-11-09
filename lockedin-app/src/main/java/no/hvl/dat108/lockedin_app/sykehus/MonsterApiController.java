package no.hvl.dat108.lockedin_app.sykehus;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import no.hvl.dat108.lockedin_app.spill.Monster;
import no.hvl.dat108.lockedin_app.spill.Rom;
import no.hvl.dat108.lockedin_app.tilgang.ApiNokkelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// Monstrene i et rom som en REST-ressurs. Fylles ut i live-koding 2 (F22).
//   samlingen: /api/rom/{nr}/monstre
//   ett monster: /api/rom/{nr}/monstre/{navn}
@RestController
@RequestMapping("/api/rom/{nr}/monstre")
public class MonsterApiController {

    // Det klienten sender med POST. Jackson lager den fra JSON-en.
    public record NyttMonster(
            @NotBlank @Size(min = 2, max = 30)
            @Pattern(regexp = "^[A-ZÆØÅ].*") String navn,
            @Min(1) @Max(Integer.MAX_VALUE) int hp
    ) {}

    private final SykehusService sykehus;
    private final ApiNokkelService nokler;

    public MonsterApiController(SykehusService sykehus,
                                ApiNokkelService nokler) {
        this.sykehus = sykehus;
        this.nokler = nokler;
    }

    // GET    samlingen:    200 og alle monstrene
    @GetMapping
    public List<MonsterVisning> alle(@PathVariable int nr){
        return finnRom(nr).angripbare().stream()
                .map(MonsterVisning::fra) // Gjør om fra Optional<Angripbar> til Optional<MonsterVisning> (DTO)
                .toList();
    }

    // GET    ett monster:  200 og monsteret, eller 404
    @GetMapping("/{navn}")
    public ResponseEntity<MonsterVisning> ett(
            @PathVariable int nr, @PathVariable String navn){
        return finnRom(nr).finnFoerste(a -> a.navn().equalsIgnoreCase(navn))
                .map(MonsterVisning::fra) // Gjør om fra Optional<Angripbar> til Optional<MonsterVisning> (DTO)
                .map(ResponseEntity::ok) // Pakk inn i et ResponseEntity-objekt med passende statuskode.
                .orElse(ResponseEntity.notFound().build()); // Om monsteret ikke har blitt funnet, returner med en passende statuskode og tom kropp.
    }


    // POST   samlingen:    201, Location og det nye monsteret
    @PostMapping
    public ResponseEntity<MonsterVisning> nytt(
            @PathVariable int nr, @Valid @RequestBody NyttMonster ny,
            @RequestHeader (value = "Api-Key", required = false) String nokkel,
            HttpSession sesjon) {
        sjekkTilgang(nokkel, sesjon);

        Rom rom = finnRom(nr); // Hent en referanse til rommet monsteret skal legges til i.

        // Sjekk om monsteret finnes allerede, og gi i så fall en passende feilmelding.
        if (rom.finnFoerste(a -> a.navn().equalsIgnoreCase(ny.navn()))
                .isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    ny.navn() + " finnes allerede i rommet.");
        }

        // Opprett monsteret og legg til i rommet
        Monster monster = new Monster(ny.navn(), ny.hp());
        rom.leggTil(monster);

        //Sett opp en URI som kan returneres for å vise hvor monsteret finnes.
        URI adresse = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{navn}")
                .buildAndExpand(monster.navn())
                .encode()
                .toUri();

        return ResponseEntity.created(adresse)
                .body(MonsterVisning.fra(monster));
    }

    private void sjekkTilgang(String nokkel, HttpSession sesjon) {
        boolean innlogget = sesjon.getAttribute("helt") != null;
        if(!innlogget && !nokler.erGyldig(nokkel)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Mangler gyldig API-nøkkel.");
        }
    }

    // DELETE ett monster:  204 uten kropp, eller 404
    @DeleteMapping("/{navn}")
    public ResponseEntity<Void> fordriv(
            @PathVariable int nr, @PathVariable String navn,
    @RequestHeader (value = "Api-Key", required = false) String nokkel,
            HttpSession sesjon) {
        sjekkTilgang(nokkel, sesjon);
        boolean fjernet = finnRom(nr).angripbare()
                .removeIf(a -> a.navn().equalsIgnoreCase(navn));
        return fjernet
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // Ukjent rom: 404 for hele ressursen, som i SpillApiController
    private Rom finnRom(int nr) {
        return sykehus.finnRom(nr).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Rom " + nr + " finnes ikke."));
    }
}
