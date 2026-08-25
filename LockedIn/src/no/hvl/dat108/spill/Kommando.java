package no.hvl.dat108.spill;

@FunctionalInterface
public interface Kommando {
    void utfoer(Helt helt, Rom rom);
}
