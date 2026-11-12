package no.hvl.dat108.lockedin_app.tilgang;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HexFormat;

// Passord med salt (F26). Samme tre metoder som PassordService i Bergen
// (LP), så eksemplene ser like ut der og her.
// Algoritmen er PBKDF2 med HMAC-SHA256: hashen regnes ut mange ganger på
// rad. Det er tregt med vilje, for da koster hver gjetning en angriper
// like mye tid som én innlogging koster oss.
@Service
public class PassordService {

    // OWASP (2026): minst 600 000 runder for PBKDF2-HMAC-SHA256.
    // Flere runder gir tregere innlogging, men også tregere angrep.
    public static final int ITERASJONER = 600_000;
    private static final int SALT_BYTE = 16;       // 128 bit
    private static final int HASH_BIT = 256;

    private final SecureRandom tilfeldig = new SecureRandom();
    private final HexFormat hex = HexFormat.of();

    // Et nytt, tilfeldig salt for hver spiller. Saltet er ikke hemmelig:
    // det lagres rett ved siden av hashen.
    public String genererTilfeldigSalt() {
        byte[] salt = new byte[SALT_BYTE];
        tilfeldig.nextBytes(salt);
        return hex.formatHex(salt);                // 32 heksadesimale tegn
    }

    // Samme passord og samme salt gir alltid samme hash. Det er det
    // innloggingen bygger på.
    public String hashMedSalt(String passord, String salt) {
        if (passord == null || salt == null) {
            throw new IllegalArgumentException(
                    "Passord og salt kan ikke være null.");
        }
        PBEKeySpec spek = new PBEKeySpec(passord.toCharArray(),
                hex.parseHex(salt), ITERASJONER, HASH_BIT);
        try {
            SecretKeyFactory pbkdf2 =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return hex.formatHex(pbkdf2.generateSecret(spek).getEncoded());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("PBKDF2 mangler i Java.", e);
        } finally {
            spek.clearPassword();
        }
    }

    // Hasher det oppgitte passordet med det lagrede saltet, og sammenligner
    // med den lagrede hashen. Selve passordet lagres aldri.
    public boolean validerMedSalt(String passord, String salt, String hash) {
        if (passord == null || salt == null || hash == null) {
            throw new IllegalArgumentException(
                    "Passord, salt og hash kan ikke være null.");
        }
        String beregnet = hashMedSalt(passord, salt);
        // isEqual bruker like lang tid uansett hvor tidlig strengene er
        // ulike, så tiden avslører ikke hvor mange tegn som stemte.
        return MessageDigest.isEqual(
                beregnet.getBytes(StandardCharsets.UTF_8),
                hash.getBytes(StandardCharsets.UTF_8));
    }
}
