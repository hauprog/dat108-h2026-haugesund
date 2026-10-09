package no.hvl.dat108.oving;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/*
 * Felles oppsett for testene. MockMvc later som om den er en nettleser:
 * den sender forespoersler til OvingController uten at serveren startes.
 *
 * Roedt med "Status expected:<200> but was:<404>": stien finnes ikke ennda.
 * Roedt med "but was:<500>": metoden finnes, men kaster en exception.
 * Les meldingen nederst i Run-vinduet, den sier hva som var forventet.
 */
abstract class OvingTest {

    protected MockMvc mvc;

    @BeforeEach
    void oppsett() {
        mvc = MockMvcBuilders.standaloneSetup(new OvingController()).build();
    }
}
