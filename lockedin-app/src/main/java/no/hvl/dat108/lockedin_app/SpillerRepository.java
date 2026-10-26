package no.hvl.dat108.lockedin_app;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Bare et grensesnitt: Spring lager klassen som implementerer det.
// save, findAll, findById og flere følger med fra JpaRepository.
public interface SpillerRepository extends JpaRepository<Spiller, Long> {

    // Spring lager spørringen ut fra navnet på metoden:
    // select ... from spiller where navn = ?
    Optional<Spiller> findByNavn(String navn);
}
