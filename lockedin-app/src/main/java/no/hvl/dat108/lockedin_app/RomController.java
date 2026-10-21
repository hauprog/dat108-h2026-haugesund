package no.hvl.dat108.lockedin_app;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import no.hvl.dat108.lockedin_app.spill.Monster;
import no.hvl.dat108.lockedin_app.spill.Rom;
import no.hvl.dat108.lockedin_app.spill.Skatt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("skjema", new MonsterSkjema());
        return "rom";
    }

    @GetMapping("/rom")
    public String gammelAdresse(){
        return "redirect:/rom/7";
    }

    @PostMapping("/rom/{nr}/monstre")
    public String nyttMonster(@PathVariable int nr,
                              @Valid @ModelAttribute("skjema") MonsterSkjema skjema,
                              BindingResult feil,
                              Model model,
                              RedirectAttributes ra,
                              HttpSession sesjon){
        // Skjemaet er skjult for den som ikke er logget inn, men curl
        // bryr seg ikke om det. Serveren må sjekke selv.
        String helt = (String) sesjon.getAttribute("helt");
        if (helt == null) {
            ra.addFlashAttribute("feilmelding",
                    "Logg inn for å legge til monstre.");
            return "redirect:/innlogging";
        }

        Rom rom = finnRom(nr);
        if(feil.hasErrors()){
            return visRomMedFeil(nr, rom, "Monsteret er ugyldig. "
                    + "Sjekk navn, hp og tilstand.", model);
        }

        if (finnesAllerede(rom, skjema.getNavn())) {
            return visRomMedFeil(nr, rom, "Det finnes allerede et monster "
                    + "som heter " + skjema.getNavn() + " her.", model);
        }

        Monster monster = new Monster(skjema.getNavn(), skjema.getHp());
        if (skjema.getTilstand().equals("skadet")){
            monster.taSkade(skjema.getHp() / 2);
        }

        rom.leggTil(monster);

        ra.addFlashAttribute("melding", monster.navn() + " er lagt til av "
                + helt + ".");
        return "redirect:/rom/" + nr;
    }

    private static boolean finnesAllerede(Rom rom, String navn) {
        return rom.finnFoerste(a -> a.navn().equalsIgnoreCase(navn))
                .isPresent();
    }

    private String visRomMedFeil(int nr, Rom rom, String feilmelding, Model model) {
        model.addAttribute("nr", nr);
        model.addAttribute("rom", rom);
        model.addAttribute("feilmelding", feilmelding);
        return "rom";
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
