# Lydeffekter til arenaen

Spillet virker uten lydfiler: da lager `lyd.js` en enkel tone i stedet.

Legg filene her, og ta bort `//` foran linjen i `FILER` øverst i `lyd.js`:

| Fil | Når |
|---|---|
| `slag.ogg` | spilleren slår |
| `treff.ogg` | monsteret blir truffet |
| `drept.ogg` | monsteret dør |
| `loot.ogg` | en skatt plukkes opp |
| `feil.ogg` | noe gikk galt |

Forslag, begge CC0:

- [RPG Audio](https://kenney.nl/assets/rpg-audio) og
  [Impact Sounds](https://kenney.nl/assets/impact-sounds) fra Kenney.
- [sfxr.me](https://sfxr.me/): lag dine egne retrolyder («Hit», «Pickup»,
  «Explosion»), og last ned som WAV. Da må endelsen i `FILER` være `.wav`.

Hold filene korte (under ett sekund) og små. Skriv i `KILDER.md` hvor
lydene kom fra.
