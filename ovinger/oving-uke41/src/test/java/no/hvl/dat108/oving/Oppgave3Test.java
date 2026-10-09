package no.hvl.dat108.oving;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Oppgave 3: GET /api/skatter")
class Oppgave3Test extends OvingTest {

    @Test
    @DisplayName("svarer med JSON")
    void erJson() throws Exception {
        mvc.perform(get("/api/skatter"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("alle fem skattene er med")
    void alleFem() throws Exception {
        mvc.perform(get("/api/skatter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    @DisplayName("dyreste foerst: kongekrone, gull, skalpell, ostekake, gulrotkake")
    void sortert() throws Exception {
        mvc.perform(get("/api/skatter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].navn").value("kongekrone"))
                .andExpect(jsonPath("$[0].verdi").value(1200))
                .andExpect(jsonPath("$[1].navn").value("gull som brenner"))
                .andExpect(jsonPath("$[2].navn").value("rusten skalpell"))
                .andExpect(jsonPath("$[3].navn").value("ostekake"))
                .andExpect(jsonPath("$[4].navn").value("gulrotkake"));
    }
}
