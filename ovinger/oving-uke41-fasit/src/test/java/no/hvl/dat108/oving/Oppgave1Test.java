package no.hvl.dat108.oving;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Oppgave 1: GET /terning")
class Oppgave1Test extends OvingTest {

    private int kast(String sti) throws Exception {
        String svar = mvc.perform(get(sti))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return Integer.parseInt(svar.trim());
    }

    @Test
    @DisplayName("uten sider: alltid 1 til 6 (100 kast)")
    void sekssidet() throws Exception {
        for (int i = 0; i < 100; i++) {
            int verdi = kast("/terning");
            assertTrue(verdi >= 1 && verdi <= 6, "fikk " + verdi);
        }
    }

    @Test
    @DisplayName("sider=20: alltid 1 til 20, og ikke bare 1 til 6 (200 kast)")
    void tjuesidet() throws Exception {
        boolean overSeks = false;
        for (int i = 0; i < 200; i++) {
            int verdi = kast("/terning?sider=20");
            assertTrue(verdi >= 1 && verdi <= 20, "fikk " + verdi);
            if (verdi > 6) overSeks = true;
        }
        assertTrue(overSeks, "200 kast med d20, og ingen over 6: brukes sider?");
    }

    @Test
    @DisplayName("sider=1: alltid 1")
    void ensidet() throws Exception {
        mvc.perform(get("/terning").param("sider", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("sider=abc: 400 Bad Request, uten egen kode for det")
    void ikkeEtTall() throws Exception {
        mvc.perform(get("/terning").param("sider", "abc"))
                .andExpect(status().isBadRequest());
    }
}
