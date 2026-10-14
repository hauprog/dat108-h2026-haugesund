package no.hvl.dat108.oving;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Oppgave 2: GET /rop")
class Oppgave2Test extends OvingTest {

    @Test
    @DisplayName("tekst=hei&ganger=3 gir \"HEI HEI HEI\"")
    void treGanger() throws Exception {
        mvc.perform(get("/rop").param("tekst", "hei").param("ganger", "3"))
                .andExpect(status().isOk())
                .andExpect(content().string("HEI HEI HEI"));
    }

    @Test
    @DisplayName("uten ganger: én gang, \"HALLO\"")
    void enGang() throws Exception {
        mvc.perform(get("/rop").param("tekst", "hallo"))
                .andExpect(status().isOk())
                .andExpect(content().string("HALLO"));
    }

    @Test
    @DisplayName("uten tekst: 400 Bad Request")
    void manglerTekst() throws Exception {
        mvc.perform(get("/rop"))
                .andExpect(status().isBadRequest());
    }
}
