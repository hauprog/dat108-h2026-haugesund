# Minijam fredag 16. oktober

Et lite nettleserspill i en Spring-applikasjon. Startprosjektet virker som det er: spill Myntregn, lagre resultatet og se topplisten. Så bytter dere ut spillet med deres eget.

## Før fredag

1. **Åpne** mappen `minijam` i IntelliJ (File > Open, velg mappen). Vent til Maven er ferdig nederst til høyre. Første gang laster den ned en del.
2. **Kjør** `MinijamApplication` og åpne <http://localhost:8080>.
3. **Spill én runde** og lagre på topplisten. Virker det, er dere klare.

## Slik henger det sammen

```
GET  /            RekordController.spill()      templates/spill.html + static/spill.js
                  spillet kjører i nettleseren; når det er over, vises skjemaet
POST /rekorder    RekordController.nyRekord()   navn og poeng fra skjemaet
                  302 redirect:/toppliste
GET  /toppliste   RekordController.toppliste()  templates/toppliste.html, topp 10
```

| Fil | Hva | Skal dere endre den? |
|---|---|---|
| `static/spill.js` | spillet | ja, det meste av dagen |
| `static/stil.css` | utseendet | ja |
| `templates/spill.html` | siden rundt spillet, og skjemaet | litt: tittel, og det spillet deres trenger |
| `templates/toppliste.html` | topplisten | gjerne |
| `RekordController.java` | lagrer og sorterer resultatene | i sideoppdragene |

## Velg ditt eget eventyr

- **Bare JavaScript.** Start fra scratch, eller fra `spill.js`. Lag en mappe med en HTML-fil og en JS-fil, og åpne HTML-filen rett i nettleseren. Ingen Spring nødvendig.
- **I startprosjektet.** Bytt ut Myntregn med ditt eget spill. Topplisten virker allerede; du trenger bare å sette poengene.
- **Begge deler.** Lag spillet fritt, og flytt det inn i startprosjektet når det kan spilles. Sideoppdrag S0 viser hvordan.

Alle tre er like gode. Å bli bedre i JavaScript er også et godt mål for dagen.

## Fire ting å få med

Dette er anbefalinger, ikke krav. Myntregn i `spill.js` viser ett eksempel på hver, merket «Punkt 1» til «Punkt 4» i kommentarene. `tegn()` er funksjonen som oppdaterer siden ut fra `tilstand`, som i `Spill`-klassen i F14.

1. **Tilstand og DOM:** alt som vises, tegnes fra tilstand-objektet av `tegn()`.
2. **Array-metoder:** minst én liste i tilstanden som behandles med `filter`, `map`, `find`, `reduce` eller `sort`.
3. **Tid:** noe skjer uten at spilleren gjør noe (`setInterval`, `setTimeout` eller `requestAnimationFrame`).
4. **Serveren** (i startprosjektet): når spillet er over, sendes resultatet til serveren og vises på topplisten. Dette er ferdig; behold skjemaet `#ferdig` og feltet `poeng`, og sett poengene fra ditt eget spill.

## Arbeidsflyt

- **JavaScript og CSS:** lagre filen og trykk F5 i nettleseren. Ingen Build eller omstart er nødvendig.
- **Maler (`templates/`):** Ctrl+F9 (Build), så F5.
- **Java:** Ctrl+F9 starter serveren på nytt. Topplisten ligger bare i minnet, så den tømmes.
- **Feil i JavaScript:** F12, fanen Console. Står det ingenting der, legg inn `console.log(tilstand)`.
- **Uten server:** åpne `templates/spill.html` rett fra disken i nettleseren. Spillet virker, men skjemaet kan ikke lagres. Nyttig hvis det er problemer med Spring.

## Sideoppdrag

Små frivillige utfordringer med hint, på 5 til 20 minutter. Sideoppdragene og vriene anbefales etter at dere har et minste spillbart produkt (MSP), eller når dere står fast.

- **Spillet (J1 til J9):** piltaster, pause, kollisjon, jevn bevegelse, lyd, og utstyr fra Læringslab.
- **Serveren (S0 til S6):** små doser av F16 til F18 i startprosjektet: stoppe juks, flash-melding, `@RequestParam`, `@PathVariable` med lenke, JSON til spillet, og `@Valid` som i Oblig4.
