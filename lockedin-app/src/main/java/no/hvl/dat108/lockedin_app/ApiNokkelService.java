package no.hvl.dat108.lockedin_app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApiNokkelService {

    private final String nokkel;

    public ApiNokkelService(@Value("${lockedin.api-nokkel}") String nokkel){
        this.nokkel = nokkel;
    }

    public boolean erGyldig(String oppgitt){
        return nokkel.equals(oppgitt);  // false dersom null
    }

}
