package no.hvl.dat108.lockedin_app.sykehus;

import no.hvl.dat108.lockedin_app.spill.Monster;
import no.hvl.dat108.lockedin_app.spill.Rom;
import no.hvl.dat108.lockedin_app.tilgang.ApiNokkelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// F26, live 1 (fra F24): web-laget testet med HTTP-forespørsler, uten å
// starte en server. Bare MonsterApiController lastes, og tjenestene er
// mocker.
@WebMvcTest(MonsterApiController.class)
class MonsterApiWebTest {

    // I testklasser er feltinjeksjon vanlig: JUnit lager testobjektet,
    // og Spring fyller inn feltene etterpå.
    @Autowired
    MockMvc mvc;

    @MockitoBean
    SykehusService sykehus;

    @MockitoBean ApiNokkelService nokler;

    static final String HEKSEN = """
        { "navn": "Heksen", "hp": 40 }""";

    @BeforeEach
    void likhuset(){
        Rom rom = new Rom("Likhuset", h-> {
            System.out.println("Ballonger kommer opp av gulvet.");
        });
        rom.leggTil(new Monster("FrodeGalenmann", 67));
        when(sykehus.finnRom(13)).thenReturn(Optional.of(rom));
        when(nokler.erGyldig("åpne")).thenReturn(true);
    }

    @Test
    void nyttMonsterGir201MedLocation() throws Exception{
        mvc.perform(post("/api/rom/13/monstre")
                    .header("Api-Key", "åpne")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(HEKSEN))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        endsWith("/api/rom/13/monstre/Heksen")))
                .andExpect(jsonPath("$.hp").value(40));
    }

    @Test
    void ugyldigMonsterGir400() throws Exception {
        mvc.perform(post("/api/rom/13/monstre")
                        .header("Api-Key", "åpne")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "navn": "Kakemonsteret", "hp": -5 }"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sammeNavnToGangerGir409() throws Exception {
        var forespoersel = post("/api/rom/13/monstre")
                .header("Api-Key", "åpne")
                .contentType(MediaType.APPLICATION_JSON)
                .content(HEKSEN);
        mvc.perform(forespoersel).andExpect(status().isCreated());
        mvc.perform(forespoersel).andExpect(status().isConflict());
    }

    @Test
    void utenNokkelGir401() throws Exception {
        mvc.perform(post("/api/rom/13/monstre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(HEKSEN))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void ukjentMonsterGir404() throws Exception {
        mvc.perform(get("/api/rom/13/monstre/Dracula"))
                .andExpect(status().isNotFound());
    }
    @Test
    void ikkeJsonGir415() throws Exception {
        mvc.perform(post("/api/rom/13/monstre")
                        .header("Api-Key", "åpne")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(HEKSEN))
                .andExpect(status().isUnsupportedMediaType());
    }
}
