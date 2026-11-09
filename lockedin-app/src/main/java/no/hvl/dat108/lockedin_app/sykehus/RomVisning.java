package no.hvl.dat108.lockedin_app.sykehus;

import no.hvl.dat108.lockedin_app.spill.Rom;
import no.hvl.dat108.lockedin_app.spill.Skatt;

import java.util.List;

// Det spillet i nettleseren får vite om et rom, som JSON.
// Monstrene sendes som MonsterVisning, ikke som Monster-objekter.
public record RomVisning(int nr,
                         String navn,
                         List<MonsterVisning> monstre,
                         List<Skatt> skatter) {

    public static RomVisning fra(int nr, Rom rom) {
        List<MonsterVisning> monstre = rom.angripbare().stream()
                .map(MonsterVisning::fra)
                .toList();
        return new RomVisning(nr, rom.navn(), monstre,
                List.copyOf(rom.skatter()));
    }
}
