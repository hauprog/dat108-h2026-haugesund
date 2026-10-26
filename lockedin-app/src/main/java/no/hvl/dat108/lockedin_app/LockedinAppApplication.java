package no.hvl.dat108.lockedin_app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.UrlHandlerFilter;

@SpringBootApplication
public class LockedinAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(LockedinAppApplication.class, args);
	}

	// /rom/7/ (med skråstrek til slutt) sendes videre til /rom/7 med 308.
	@Bean
	public UrlHandlerFilter urlHandlerFilter(){
		return UrlHandlerFilter.trailingSlashHandler("/**")
				.redirect(HttpStatus.PERMANENT_REDIRECT)
				.build();
	}

	// Kjøres én gang når appen har startet, og legger inn spillerne.
	// CommandLineRunner er et funksjonelt grensesnitt (modul 1).
	@Bean
	public CommandLineRunner leggInnSpillere(SpillerRepository spillere){
		return args -> {
			spillere.save(new Spiller("Ole", "ostekake"));
			spillere.save(new Spiller("Jonny", "gulrotkake"));
		};
	}
}

