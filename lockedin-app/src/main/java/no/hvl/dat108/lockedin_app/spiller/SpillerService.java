package no.hvl.dat108.lockedin_app.spiller;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Passordsjekken, flyttet hit fra InnloggingController før F24. Kode i en
// kontroller er tungvint å teste. I en service kan den testes alene, med
// et falskt repository (Mockito).
// Klartekst til F26 (passord): da blir dette hash og salt, og testene sier
// fra hvis innloggingen slutter å virke.
@Service
public class SpillerService {

    private final SpillerRepository spillere;

    public SpillerService(SpillerRepository spillere) {
        this.spillere = spillere;
    }

    // true bare når spilleren finnes og passordet er riktig
    public boolean riktigPassord(String navn, String passord) {
        return spillere.findByNavn(navn)
                .map(s -> s.getPassord().equals(passord))
                .orElse(false);
    }

    public List<Spiller> alle(){
        return spillere.findAll();
    }

    public Optional<Spiller> finn(Long id){
        return spillere.findById(id);
    }
}
