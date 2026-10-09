package no.hvl.dat108.oving;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Oppgave 4: GET /api/skatter/dyreste")
class Oppgave4Test extends OvingTest {

    @Test
    @DisplayName("ett objekt, ikke en liste: kongekrone, 1200")
    void dyreste() throws Exception {
        mvc.perform(get("/api/skatter/dyreste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.navn").value("kongekrone"))
                .andExpect(jsonPath("$.verdi").value(1200));
    }
}
