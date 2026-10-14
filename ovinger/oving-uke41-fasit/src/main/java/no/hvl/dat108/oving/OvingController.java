package no.hvl.dat108.oving;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

// Fasit, oeving uke 41.
@Controller
public class OvingController {

    private static final Comparator<Skatt> DYRESTE_FOERST =
            Comparator.comparingInt(Skatt::verdi).reversed();

    private final Random random = new Random();  // trygg aa dele mellom traader

    @GetMapping("/hei")
    @ResponseBody
    public String hei(@RequestParam(defaultValue = "fremmede") String navn) {
        return "Hei, " + navn + "!";
    }

    // Oppgave 1. defaultValue er tekst, Spring gjoer den om til int.
    // "abc" kan ikke bli til en int, saa Spring svarer med status-
    // kode 400 foer metoden kalles.
    @GetMapping("/terning")
    @ResponseBody
    public String terning(@RequestParam(defaultValue = "6") int sider) {
        return String.valueOf(random.nextInt(sider) + 1);
    }

    // Oppgave 2. Uten defaultValue er tekst paakrevd: GET /rop gir 400.
    @GetMapping("/rop")
    @ResponseBody
    public String rop(@RequestParam String tekst,
                      @RequestParam(defaultValue = "1") int ganger) {
        return String.join(" ", Collections.nCopies(ganger, tekst.toUpperCase()));
    }

    private final List<Skatt> skatter = List.of(
            new Skatt("ostekake", 7),
            new Skatt("gull som brenner", 250),
            new Skatt("gulrotkake", 6),
            new Skatt("kongekrone", 1200),
            new Skatt("rusten skalpell", 120));

    // Oppgave 3. List<Skatt> blir et JSON-array, hver Skatt et objekt.
    @GetMapping("/api/skatter")
    @ResponseBody
    public List<Skatt> alle() {
        return skatter.stream().sorted(DYRESTE_FOERST).toList();
    }

    // Oppgave 4. Ett enkelt objekt ut, ikke en liste. orElseThrow: listen er aldri tom.
    @GetMapping("/api/skatter/dyreste")
    @ResponseBody
    public Skatt dyreste() {
        return skatter.stream().max(Comparator.comparingInt(Skatt::verdi)).orElseThrow();
    }

    // Oppgave 5. Uten spesisifesrt min blir den satt til 0,
    // slik at alle er med. Ingen treff gir [], ikke en feil.
    @GetMapping("/api/skatter/sok")
    @ResponseBody
    public List<Skatt> sok(@RequestParam(defaultValue = "0") int min) {
        return skatter.stream()
                .filter(s -> s.verdi() >= min)
                .sorted(DYRESTE_FOERST)
                .toList();
    }

    private final Helt ole = new Helt("Ole", 100);

    // Oppgave 6. Returnerer man Ole direkte, finner Jackson ingen getX()
    // og svaret blir en feil (500) eller {}. En record med det som skal ut.
    @GetMapping("/api/helt")
    @ResponseBody
    public HeltStatus helt() {
        return HeltStatus.fra(ole);
    }
}
