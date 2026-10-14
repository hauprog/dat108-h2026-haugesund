package no.hvl.dat108.oving;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Oppgave 5: GET /api/skatter/sok")
class Oppgave5Test extends OvingTest {

    @Test
    @DisplayName("min=100: tre skatter, dyreste foerst")
    void minst100() throws Exception {
        mvc.perform(get("/api/skatter/sok").param("min", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].navn").value("kongekrone"))
                .andExpect(jsonPath("$[2].navn").value("rusten skalpell"));
    }

    @Test
    @DisplayName("min=120: skalpellen er med (120 eller mer)")
    void grensenTeller() throws Exception {
        mvc.perform(get("/api/skatter/sok").param("min", "120"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @DisplayName("uten min: alle fem")
    void utenMin() throws Exception {
        mvc.perform(get("/api/skatter/sok"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    @DisplayName("min=10000: tom liste [], ikke en feil")
    void ingenTreff() throws Exception {
        mvc.perform(get("/api/skatter/sok").param("min", "10000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
