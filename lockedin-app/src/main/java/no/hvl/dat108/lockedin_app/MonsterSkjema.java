package no.hvl.dat108.lockedin_app;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Det skjemaet sender, med reglene. Spring fyller det via set-metodene,
// og th:field i malen leser det via get-metodene.
public class MonsterSkjema {

    @NotNull
    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[A-ZÆØÅ].*")      // stor forbokstav
    private String navn;

    @NotNull
    @Min(1)
    @Max(999)
    private Integer hp;          // Integer, ikke int: tomt felt blir null

    @NotNull                               // @Pattern alene godtar null
    @Pattern(regexp = "frisk|skadet")
    private String tilstand = "frisk";

    public String getNavn() { return navn; }
    public void setNavn(String navn) { this.navn = navn; }

    public Integer getHp() { return hp; }
    public void setHp(Integer hp) { this.hp = hp; }

    public String getTilstand() { return tilstand; }
    public void setTilstand(String tilstand) { this.tilstand = tilstand; }
}
