package no.hvl.dat108.oving;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/*
 * Oeving uke 41: seks oppgaver.
 *
 * Slik jobber du:
 *   1. Velg en oppgave under (Åpne TODO-vinduet i IntelliJ ved å gå til
 *      View ➔ Tool Windows ➔ TODO).
 *   2. Kjoer den tilhørende testen: hoeyreklikk Oppgave1Test i src/test/java, Run.
 *      Roed foer du begynner, groenn naar oppgaven er loest.
 *   3. Sitter du fast? Det ligger hint i tre trinn paa
 *      ukessiden (lenke paa Canvas).
 *   4. Om du vil se det i nettleseren: kjoer OvingApplication,
 *      og aapne http://localhost:8080
 *
 * Alle seks er ferdige naar alle testene er groenne
 * (hoeyreklikk src/test/java, Run 'All Tests').
 */
@Controller
public class OvingController {

    // Eksempel, ferdig: GET /hei?navn=Ole  ->  "Hei, Ole!"
    @GetMapping("/hei")
    @ResponseBody
    public String hei(@RequestParam(defaultValue = "fremmede") String navn) {
        return "Hei, " + navn + "!";
    }

    // TODO Oppgave 1: GET /terning?sider=20  ->  et tilfeldig tall fra 1 til 20, som tekst.
    //  Uten sider: en vanlig sekssidet terning.

    // TODO Oppgave 2: GET /rop?tekst=hei&ganger=3  ->  "HEI HEI HEI"
    //  tekst er paakrevd. Uten ganger: én gang.

    // Skattene oppgave 3 til 5 jobber med. Legg merke til at de ikke er sortert.
    private final List<Skatt> skatter = List.of(
            new Skatt("ostekake", 7),
            new Skatt("gull som brenner", 250),
            new Skatt("gulrotkake", 6),
            new Skatt("kongekrone", 1200),
            new Skatt("rusten skalpell", 120));

    // TODO Oppgave 3: GET /api/skatter  ->  alle skattene som JSON, dyreste foerst.

    // TODO Oppgave 4: GET /api/skatter/dyreste  ->  bare den dyreste skatten, som JSON.

    // TODO Oppgave 5: GET /api/skatter/sok?min=100  ->  skattene med verdi 100 eller mer,
    //  dyreste foerst. Uten min: alle.

    // Helten oppgave 6 jobber med.
    private final Helt ole = new Helt("Ole", 100);

    // TODO Oppgave 6: GET /api/helt  ->  {"navn":"Ole","hp":100,"maxHp":100,"lever":true}
    //  (rekkefoelgen paa noeklene spiller ingen rolle)
}
