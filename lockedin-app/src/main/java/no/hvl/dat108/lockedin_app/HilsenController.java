package no.hvl.dat108.lockedin_app;

import no.hvl.dat108.lockedin_app.spill.Skatt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HilsenController {

    @GetMapping("/hei")
    @ResponseBody
    public String hei(@RequestParam(defaultValue = "DU") String navn){
        String traad = Thread.currentThread().getName();
        return "Hei, " + navn + "! Velkommen til Galehuset. (svart av traad " + traad + ")";
    }

    @GetMapping("/api/skatter")
    @ResponseBody
    public List<Skatt> skatter() {
        return List.of(
                new Skatt("gull som brenner", 250),
                new Skatt("ostekake", 7),
                new Skatt("gulrotkake", 6));
    }
}
