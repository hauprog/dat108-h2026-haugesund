package no.hvl.dat108.minijam;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RekordController {

    // Én liste, delt av alle forespørslene (én tråd hver).
    // CopyOnWriteArrayList gjør at den er trådsikker (F17).
    // Den ligger bare i minnet, så en omstart av serveren tømmer den.
    private final List<Rekord> rekorder = new CopyOnWriteArrayList<>();

    /** Spillsiden. Selve spillet er JavaScript i static/spill.js. */
    @GetMapping("/")
    public String spill(Model model) {
        model.addAttribute("beste", beste());
        return "spill";
    }

    /** Skjemaet på spillsiden sender hit når spillet er over. */
    @PostMapping("/rekorder")
    public String nyRekord(@RequestParam String navn, @RequestParam int poeng) {
        // TODO sideoppdrag: serveren tar imot hva som helst. Prøv curl, og stopp juksen.
        rekorder.add(new Rekord(navn, poeng));
        return "redirect:/toppliste";
    }

    @GetMapping("/toppliste")
    public String toppliste(Model model) {
        model.addAttribute("rekorder", topp(10));
        return "toppliste";
    }

    private List<Rekord> topp(int antall) {
        return rekorder.stream()
                .sorted(Comparator.comparingInt(Rekord::poeng).reversed())
                .limit(antall)
                .toList();
    }

    private int beste() {
        return rekorder.stream()
                .mapToInt(Rekord::poeng)
                .max()
                .orElse(0);
    }
}
