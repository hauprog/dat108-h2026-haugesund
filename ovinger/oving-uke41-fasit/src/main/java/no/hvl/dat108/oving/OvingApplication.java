package no.hvl.dat108.oving;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Kjoer denne (groenn pil) og aapne http://localhost:8080 i nettleseren.
// Testene trenger IKKE at denne kjoerer.
@SpringBootApplication
public class OvingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OvingApplication.class, args);
    }
}
