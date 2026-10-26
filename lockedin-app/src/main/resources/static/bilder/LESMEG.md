# Bilder til arenaen

Spillet virker uten bilder: da vises forbokstaven i en sirkel.

Vi bruker grafikk laget av mennesker, med en lisens som tillater det.
Forslag: [Tiny Dungeon](https://kenney.nl/assets/tiny-dungeon) fra Kenney,
som er CC0 (fritt fram, også uten å nevne hvem som laget det).

1. Last ned pakken, og velg figurer fra `Tiles/` (16 x 16 piksler).
2. Kopier dem hit, og gi dem korte navn: `rotte.png`, `skrujern.png`.
3. Koble navn til fil i `BILDER` øverst i `spill.js`:

   ```js
   const BILDER = {
       "Rottekonge": "bilder/rotte.png",
   };
   ```

4. `STANDARDBILDE` brukes for monstre som ikke står i `BILDER`, for
   eksempel de som lages i skjemaet på `/rom/7`.

Figurene skaleres opp til 64 x 64 med `image-rendering: pixelated`, så de
holder seg skarpe. Skriv i `KILDER.md` hvor bildene kom fra.
