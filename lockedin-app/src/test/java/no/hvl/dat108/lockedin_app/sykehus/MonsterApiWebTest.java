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

}
