package no.hvl.dat108.lockedin_app.demo;

import no.hvl.dat108.lockedin_app.tilgang.PassordService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// F26, live 2: et ordlisteangrep mot en lekket tabell med spillere.
// Kjøres som et vanlig Java-program (grønn pil ved main), uten Spring.
// Verste tilfelle: angriperen har hele tabellen og vet hvilken algoritme
// vi bruker. Bare passordene er ukjente.
public class OrdlisteAngrep {

    // Passordene angriperen ikke kjenner. Ole og Heksen har samme passord.
    private static final Map<String, String> PASSORD = new LinkedHashMap<>();

    static {
        PASSORD.put("Ole", "ostekake");
        PASSORD.put("Jonny", "gulrotkake");
        PASSORD.put("Heksen", "ostekake");
        PASSORD.put("Vampyren", "blodbank-ved-midnatt-13");
    }

    public static void main(String[] args) throws IOException {
        List<String> ordliste = lesOrdliste();
        System.out.println("Ordlisten har " + ordliste.size() + " ord.\n");
        utenSalt(ordliste);
        medSalt(ordliste);
    }

    // Del 1: SHA-256, uten salt, én runde. Slik gjorde mange det før.
    static void utenSalt(List<String> ordliste) {
        System.out.println("== Del 1: SHA-256 uten salt ==");
        Map<String, String> lekket = new LinkedHashMap<>();
        PASSORD.forEach((navn, passord) -> lekket.put(navn, sha256(passord)));
        lekket.forEach((navn, hash) -> System.out.println(
                navn + ": " + hash.substring(0, 16) + "..."));

        long start = System.nanoTime();
        // Hash hvert ord én gang. Da har vi en tabell fra hash til ord, og
        // den virker mot alle spillerne på én gang.
        Map<String, String> oppslag = new HashMap<>();
        for (String ord : ordliste) {
            oppslag.put(sha256(ord), ord);
        }
        lekket.forEach((navn, hash) -> System.out.println(navn + " -> "
                + oppslag.getOrDefault(hash, "(ikke i listen)")));
        double ms = (System.nanoTime() - start) / 1e6;
        System.out.printf("Tid: %.1f ms%n%n", ms);
    }

    // Del 2: PBKDF2 med et tilfeldig salt per spiller, som PassordService
    static void medSalt(List<String> ordliste) {
        System.out.println("== Del 2: PBKDF2 med salt, "
                + PassordService.ITERASJONER + " runder ==");
        PassordService ps = new PassordService();
        Map<String, String[]> lekket = new LinkedHashMap<>();  // {salt, hash}
        PASSORD.forEach((navn, passord) -> {
            String salt = ps.genererTilfeldigSalt();
            String hash = ps.hashMedSalt(passord, salt);
            lekket.put(navn, new String[] {salt, hash});
        });
        lekket.forEach((navn, lagret) -> System.out.println(
                navn + ": " + lagret[1].substring(0, 16) + "..."));

        // Ole og Heksen har nå ulik hash, og tabellen fra del 1 er verdiløs.
        // Hver spiller må angripes for seg, med sitt eget salt.
        String[] ole = lekket.get("Ole");
        long start = System.nanoTime();
        int forsok = 0;
        for (String ord : ordliste) {
            forsok++;
            if (ps.validerMedSalt(ord, ole[0], ole[1])) {
                System.out.println("Ole -> " + ord + " etter " + forsok
                        + " forsøk");
                break;
            }
        }
        double sekunder = (System.nanoTime() - start) / 1e9;
        double perForsok = sekunder / forsok;
        System.out.printf("Tid: %.1f s, %.2f s per forsøk%n",
                sekunder, perForsok);
        System.out.printf("10 000 vanlige passord mot alle %d: ca. %.0f min%n",
                lekket.size(), perForsok * 10_000 * lekket.size() / 60);
    }

    static String sha256(String tekst) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha.digest(tekst.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    static List<String> lesOrdliste() throws IOException {
        String fil = "/demo/ordliste.txt";
        try (InputStream inn = OrdlisteAngrep.class.getResourceAsStream(fil)) {
            return new String(inn.readAllBytes(), StandardCharsets.UTF_8)
                    .lines()
                    .map(String::strip)
                    .filter(linje -> !linje.isEmpty() && !linje.startsWith("#"))
                    .toList();
        }
    }
}
