package no.hvl.dat108.lockedin_app;

import no.hvl.dat108.lockedin_app.spill.Angripbar;

// Det klienten får se av et monster, som JSON. Brukes både i et helt rom
// (RomVisning) og alene (MonsterApiController), derfor i egen fil.
// Monster har navn() og hp(), ikke getX(), så verdiene kopieres over i en
// record som Jackson kan lage JSON av (F16).
public record MonsterVisning(String navn, int hp, int maxHP, boolean lever) {

    public static MonsterVisning fra(Angripbar a) {
        return new MonsterVisning(a.navn(), a.hp(), a.maxHP(), a.lever());
    }
}
