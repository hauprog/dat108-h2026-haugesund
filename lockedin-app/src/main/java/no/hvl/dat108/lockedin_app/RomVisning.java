package no.hvl.dat108.lockedin_app;

import no.hvl.dat108.lockedin_app.spill.Angripbar;
import no.hvl.dat108.lockedin_app.spill.Rom;
import no.hvl.dat108.lockedin_app.spill.Skatt;

import java.util.List;

// Det spillet i nettleseren får vite om et rom, som JSON.
// Jackson trenger records eller getX()-metoder. Monster har navn() og
// hp(), så vi kopierer verdiene over i en record (F16, Objekter blir JSON).
public record RomVisning(int nr,
                         String navn,
                         List<MonsterVisning> monstre,
                         List<Skatt> skatter) {

    public record MonsterVisning(String navn, int hp, int maxHP,
                                 boolean lever) {

        static MonsterVisning fra(Angripbar a) {
            return new MonsterVisning(a.navn(), a.hp(), a.maxHP(),
                    a.lever());
        }
    }

    public static RomVisning fra(int nr, Rom rom) {
        List<MonsterVisning> monstre = rom.angripbare().stream()
                .map(MonsterVisning::fra)
                .toList();
        return new RomVisning(nr, rom.navn(), monstre,
                List.copyOf(rom.skatter()));
    }
}
