package no.hvl.dat108.lockedin_app;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

// En rad i tabellen SPILLER. Hibernate lager tabellen ut fra klassen.
@Entity
public class Spiller {

    @Id
    @GeneratedValue
    private Long id;              // primærnøkkel, databasen gir verdien

    private String navn;
    private String passord;       // klartekst til vi lærer hashing

    protected Spiller() {
        // JPA trenger en konstruktør uten parametre
    }

    public Spiller(String navn, String passord) {
        this.navn = navn;
        this.passord = passord;
    }

    public Long getId() { return id; }
    public String getNavn() { return navn; }
    public String getPassord() { return passord; }
}
