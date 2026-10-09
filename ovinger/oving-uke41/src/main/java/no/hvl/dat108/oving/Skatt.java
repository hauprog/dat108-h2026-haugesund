package no.hvl.dat108.oving;

// En record blir gjort om til JSON av seg selv: {"navn":"ostekake","verdi":7}
public record Skatt(String navn, int verdi) {
}
