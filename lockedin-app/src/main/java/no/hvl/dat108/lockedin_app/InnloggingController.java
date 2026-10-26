package no.hvl.dat108.lockedin_app;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InnloggingController {

    private final SpillerRepository spillere;

    public InnloggingController(SpillerRepository spillere) {
        this.spillere = spillere;
    }

    @GetMapping("/innlogging")
    public String innloggingSide() {
        return "logginn";
    }

    @PostMapping("/innlogging")
    public String loggInn(@RequestParam String navn,
                          @RequestParam String passord,
                          HttpServletRequest request,
                          RedirectAttributes ra) {
        boolean riktig = spillere.findByNavn(navn)
                .map(s -> s.getPassord().equals(passord))
                .orElse(false);
        if (!riktig) {
            ra.addFlashAttribute("feilmelding",
                    "Ukjent helt eller feil passord.");
            return "redirect:/innlogging";
        }

        // Kast en gammel sesjon, så id-en byttes ved innlogging.
        HttpSession gammel = request.getSession(false);    // null om ingen
        if (gammel != null) {
            gammel.invalidate();
        }
        HttpSession sesjon = request.getSession(true);     // lager en ny
        sesjon.setMaxInactiveInterval(10 * 60);            // 10 minutter
        sesjon.setAttribute("helt", navn);
        return "redirect:/rom/7";
    }

    @PostMapping("/utlogging")
    public String loggUt(HttpSession sesjon, RedirectAttributes ra) {
        sesjon.invalidate();
        ra.addFlashAttribute("melding", "Du er logget ut.");
        return "redirect:/innlogging";
    }
}

