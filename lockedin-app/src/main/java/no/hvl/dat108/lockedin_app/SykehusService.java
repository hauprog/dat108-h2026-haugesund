package no.hvl.dat108.lockedin_app;

import no.hvl.dat108.lockedin_app.spill.Monster;
import no.hvl.dat108.lockedin_app.spill.Rom;
import no.hvl.dat108.lockedin_app.spill.Skatt;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

//@Scope("prototype") // om vi vil ha unike objekter for hver injeksjon
@Service
public class SykehusService {
    //private final Rom rom = lagRom();
    private final Map<Integer, Rom> rommene = Map.of(
            7, operasjonsstue7(),
            3, do3(),
            13, likhuset13());

    public SykehusService(){
        System.out.println("Sykehusservice laget: " + id());
    }

    public Optional<Rom> finnRom(int nr){
        return Optional.ofNullable(rommene.get(nr));
    }

    public String id(){
        return Integer.toHexString(System.identityHashCode(this));
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

    // Halloween (F22): rom 13, med monstre å legge til og fordrive
    // gjennom API-et.
    private static Rom likhuset13(){
        Rom rom = new Rom("Likhuset", h -> {});
        rom.leggTil(new Monster("Gresskarhodet", 31));
        rom.leggTil(new Monster("Spøkelset", 13));
        rom.leggTil(new Monster("Vampyren", 66));
        rom.leggTil(new Skatt("godteri", 31));
        return rom;
    }

    private static Rom do3(){
        Rom rom = new Rom("Do 3", h -> {});
        rom.leggTil(new Monster("Beist", 32));
        rom.leggTil(new Skatt("dorull", 1));
        return rom;
    }
}

