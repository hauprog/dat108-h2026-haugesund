package no.hvl.dat108.lockedin_app;

import no.hvl.dat108.lockedin_app.spill.Skatt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HilsenController {

    private final SykehusService sykehus;

    public HilsenController(SykehusService sykehus){
        this.sykehus = sykehus;
    }

    @GetMapping("/status")
    @ResponseBody
    public String status(){
        int antall = sykehus.finnRom(7)
                .map(rom -> rom.angripbare().size())
                .orElse(0);
        return "Operasjonsstue 7 har " + antall + " angripbare objekter. "
                + "(SykehusService " + sykehus.id() + ")";
    }

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
