package no.hvl.dat108.lockedin_app;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class InnloggingController {

    // Passord i klartekst i koden er bare for å vise flyten.
    // Hashing av passord kommer senere i emnet.
    private static final Map<String, String> HELTER = Map.of(
            "Ole", "ostekake",
            "Jonny", "gulrotkake");

    @GetMapping("/innlogging")
    public String innloggingSide() {
        return "logginn";
    }

    @PostMapping("/innlogging")
    public String loggInn(@RequestParam String navn,
                          @RequestParam String passord,
                          HttpServletRequest request,
                          RedirectAttributes ra) {
        if (!passord.equals(HELTER.get(navn))) {
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
