package de.htwberlin.sealevel.repository;

import de.htwberlin.sealevel.model.Sealevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

/**
 * Sealevel repository or DAO, which extends the JPARepository
 * @Repository telling SpringBoot that this is indeed a Repository
 */
@Repository
public interface SealevelRepository extends JpaRepository<Sealevel, Long> {
    Optional<Sealevel> findByDate(Date d);

    @Query("SELECT AVG(sealevel) FROM Sealevel WHERE date BETWEEN ?1 AND ?2")
    double findByYear(Date yearStart, Date yearEnd);
}
