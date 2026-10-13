package no.hvl.dat108.lockedin_app;

import no.hvl.dat108.lockedin_app.spill.Monster;
import no.hvl.dat108.lockedin_app.spill.Rom;
import no.hvl.dat108.lockedin_app.spill.Skatt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Controller
public class RomController {

    //private final Rom rom = lagRom();
    private final Map<Integer, Rom> rommene = Map.of(
            7, operasjonsstue7(),
            3, do3());



    @GetMapping("/rom/{nr}")
    public String rom(@PathVariable int nr, Model model){
        model.addAttribute("nr", nr);
        model.addAttribute("rom", finnRom(nr));
        return "rom";
    }

    @GetMapping("/rom")
    public String gammelAdresse(){
        return "redirect:/rom/7";
    }

    private Rom finnRom(int nr){
        Rom rom = rommene.get(nr);
        if (rom == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Rom " + nr + " finnes ikke.");
        }
        return rom;
    }

    private static Rom operasjonsstue7(){
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

    private static Rom do3(){
        Rom rom = new Rom("Do 3", h -> {});
        rom.leggTil(new Monster("Beist", 32));
        rom.leggTil(new Skatt("dorull", 1));
        return rom;
    }
}
