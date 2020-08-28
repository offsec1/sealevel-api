package de.htwberlin.sealevel.controller;

import de.htwberlin.sealevel.model.Sealevel;
import de.htwberlin.sealevel.repository.SealevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Rest-Controller, which is responsible for all request to /sealevel
 * @RestController Annotation declares this class as a controller
 */
@RestController
public class SealevelController {

    /**
     * slf4j Logger to log messages with more detail
     */
    private static final Logger LOG = LoggerFactory.getLogger(SealevelController.class);

    /**
     * @Autowired Annotation to initialize SealevelRepository
     */
    @Autowired
    private SealevelRepository sealevelRepository;

    /**
     * Request method to retrieve sealevel data
     *
     * @GetMapping Annotation to expose the rest endpoint to /sealevel
     * @param d Requested date for sealevel information
     * @return Sealevel object containin all necessary information (--> Sealevel class)
     */
    @GetMapping("/sealevel")
    public Sealevel getSealevel(@RequestParam("date") String d) {
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(d);
            Optional<Sealevel> sealevelDataEntry = sealevelRepository.findByDate(date);
            if (sealevelDataEntry.isPresent())
                return sealevelDataEntry.get();
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        } catch (ParseException e) {
            LOG.warn("Could not parse date parameter!");
            return null;
        }
    }

    /**
     * returns the average sealevel of the given year
     * @param year to compute the average sealevel of
     * @return Sealevel with no other information then the avg sealevel
     */
    @GetMapping("/sealevel/average")
    public Sealevel getAverageSealevelByYear(@RequestParam("year") String year) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startOfYear = formatter.parse(year + "-01-01");
            Date endOfYear = formatter.parse(year + "-12-31");
            double averageSealevel = sealevelRepository.findByYear(startOfYear, endOfYear);
            Sealevel s = new Sealevel();
            s.setSealevel((float) averageSealevel);
            return s;
        } catch (Exception e) {
            LOG.warn("Could not parse date parameter!");
            return null;
        }
    }

    /**
     * Get average sealevel data for given year in a format unity can work with
     * @param year  to compute the average sealevel of
     * @return double between 0 and 1
     */
    @GetMapping("/sealevel/average/unity")
    public double getAverageSealevelByYearForUnity(@RequestParam("year") String year) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startOfYear = formatter.parse(year + "-01-01");
            Date endOfYear = formatter.parse(year + "-12-31");

            double averageSealevel = sealevelRepository.findByYear(startOfYear, endOfYear);

            return normalizeSealevelForUnity(averageSealevel); // normalize value
        } catch (Exception e) {
            LOG.warn("Could not return sealevel data in unity format: ", e);
            return -1;
        }
    }

    /**
     * Get all available average sealevel data in a format unity can work with
     * @return hashmap with all the years available and the corresponding sealevel in unity format
     */
    @GetMapping("/sealevel/average/unity/all")
    public HashMap<Integer, Double> getAverageSealevelAllYearsForUnity() {
        List<SealevelRepository.AvgSealevel> allAverageSealevels = sealevelRepository.findAllAverageSealevels();
        HashMap<Integer, Double> unityValues = new HashMap<>();

        for (SealevelRepository.AvgSealevel s: allAverageSealevels) {
            unityValues.put(s.getDateYear(), normalizeSealevelForUnity(s.getAvgSealevel()));
        }
        return unityValues;
    }

    /**
     * Normalize the sealevel data to a value between 0 and 1 to make it easier to use in unity
     * @param currentSealevel sealevel
     * @return double between 0 and 1
     */
    private double normalizeSealevelForUnity(double currentSealevel) {
        double minSealevel = sealevelRepository.findMinSealevel();
        double maxSealevel = sealevelRepository.findMaxSealevel();

        return (currentSealevel - minSealevel) / (maxSealevel - minSealevel);
    }
}
