package no.hvl.dat108.minijam;

/** Ett resultat på topplisten. Thymeleaf leser navn() og poeng() som ${r.navn} og ${r.poeng}. */
public record Rekord(String navn, int poeng) {
}
