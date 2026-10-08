package no.hvl.dat108.lockedin_app;

import no.hvl.dat108.lockedin_app.spill.Monster;
import no.hvl.dat108.lockedin_app.spill.Rom;
import no.hvl.dat108.lockedin_app.spill.Skatt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RomController {

    private final Rom rom = lagRom();

    @GetMapping("/rom")
    public String rom(Model model){
        model.addAttribute("rom", rom);
        return "rom";
    }

    private static Rom lagRom(){
        Rom rom = new Rom("Operasjonsstue 7", h -> {});
        Monster rottekonge = new Monster("Rottekonge", 20);
        rottekonge.taSkade(20);
        rom.leggTil(rottekonge);
        rom.leggTil(new Monster("RottekongeSinHammer", 3));
        rom.leggTil(new Monster("Skrujern", 255));
        rom.leggTil(new Skatt("mynter", 3));
        rom.leggTil(new Skatt("lommerusk", 7));
        return rom;
    }
}
