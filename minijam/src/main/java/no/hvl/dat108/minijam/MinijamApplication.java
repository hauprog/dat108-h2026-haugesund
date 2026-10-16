package no.hvl.dat108.minijam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.UrlHandlerFilter;

@SpringBootApplication
public class MinijamApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinijamApplication.class, args);
    }

    // /toppliste/ (med skråstrek til slutt) sendes videre til /toppliste med 308.
    // Uten denne gir skråstreken 404. 308 beholder metoden, så en POST
    // forblir en POST. Spring Boot registrerer filteret av seg selv.
    @Bean
    public UrlHandlerFilter urlHandlerFilter() {
        return UrlHandlerFilter.trailingSlashHandler("/**")
                .redirect(HttpStatus.PERMANENT_REDIRECT)
                .build();
    }
}
