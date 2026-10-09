package no.hvl.dat108.oving;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Oppgave 6: GET /api/helt")
class Oppgave6Test extends OvingTest {

    @Test
    @DisplayName("navn, hp, maxHp og lever som JSON")
    void helten() throws Exception {
        mvc.perform(get("/api/helt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.navn").value("Ole"))
                .andExpect(jsonPath("$.hp").value(100))
                .andExpect(jsonPath("$.maxHp").value(100))
                .andExpect(jsonPath("$.lever").value(true));
    }
}
