package no.hvl.dat108.lockedin_app;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import no.hvl.dat108.lockedin_app.spill.Monster;
import no.hvl.dat108.lockedin_app.spill.Rom;
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

    public MonsterApiController(SykehusService sykehus) {
        this.sykehus = sykehus;
    }

    // GET    samlingen:    200 og alle monstrene
    @GetMapping
    public List<RomVisning.MonsterVisning> alle(@PathVariable int nr){
        return finnRom(nr).angripbare().stream()
                .map(RomVisning.MonsterVisning::fra)
                .toList();
    }

    // GET    ett monster:  200 og monsteret, eller 404
    @GetMapping("/{navn}")
    public ResponseEntity<RomVisning.MonsterVisning> ett(
            @PathVariable int nr, @PathVariable String navn){
        return finnRom(nr).finnFoerste(a -> a.navn().equalsIgnoreCase(navn))
                .map(RomVisning.MonsterVisning::fra)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // POST   samlingen:    201, Location og det nye monsteret
    @PostMapping
    public ResponseEntity<RomVisning.MonsterVisning> nytt(
            @PathVariable int nr, @Valid @RequestBody NyttMonster ny) {
        Rom rom = finnRom(nr);
        if (rom.finnFoerste(a -> a.navn().equalsIgnoreCase(ny.navn()))
                .isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    ny.navn() + " finnes allerede i rommet.");
        }
        Monster monster = new Monster(ny.navn(), ny.hp());
        rom.leggTil(monster);
        URI adresse = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{navn}")
                .buildAndExpand(monster.navn())
                .encode()
                .toUri();
        return ResponseEntity.created(adresse)
                .body(RomVisning.MonsterVisning.fra(monster));
    }

    // DELETE ett monster:  204 uten kropp, eller 404
    @DeleteMapping("/{navn}")
    public ResponseEntity<Void> fordriv(
            @PathVariable int nr, @PathVariable String navn) {
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
