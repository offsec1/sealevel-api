package de.htwberlin.sealevel.repository;

import de.htwberlin.sealevel.model.Sealevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Sealevel repository or DAO, which extends the JPARepository
 *
 * @Repository telling SpringBoot that this is indeed a Repository
 */
@Repository
public interface SealevelRepository extends JpaRepository<Sealevel, Long> {
    Optional<Sealevel> findByDate(Date d);

    @Query("SELECT AVG(sealevel) FROM Sealevel WHERE date BETWEEN ?1 AND ?2")
    double findByYear(Date yearStart, Date yearEnd);

    @Query("SELECT MIN(sealevel) FROM Sealevel")
    double findMinSealevel();

    @Query("SELECT MAX(sealevel) FROM Sealevel")
    double findMaxSealevel();

    /**
     * Get the average sealevel per year for all years found in the database
     * @return list of average sealevels
     */
    @Query(value = "SELECT AVG(sealevel) as avgSealevel, extract(year from date) as dateYear FROM sealevel\n" +
            "GROUP BY dateYear\n" +
            "ORDER BY dateYear asc", nativeQuery = true)
    List<AvgSealevel> findAllAverageSealevels();

    interface AvgSealevel {
        Double getAvgSealevel();
        Integer getDateYear();
    }

}
