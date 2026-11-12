package no.hvl.dat108.lockedin_app.spiller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/spillere")
public class SpillerApiController {

    private final SpillerService spillere;

    public SpillerApiController(SpillerService spillere){
        this.spillere = spillere;
    }

    @GetMapping
    public List<SpillerVisning> alle(){
        return spillere.alle().stream()
                .map(SpillerVisning::fra)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpillerVisning> en(@PathVariable Long id) {
        return spillere.finn(id)
                .map(s -> ResponseEntity.ok(SpillerVisning.fra(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    public record SpillerVisning(Long id, String navn){
        static SpillerVisning fra(Spiller s){
            return new SpillerVisning(s.getId(), s.getNavn());
        }
    }
}
