package de.htwberlin.sealevel.controller;
import de.htwberlin.sealevel.model.Sealevel;
import de.htwberlin.sealevel.repository.SealevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            return sealevelDataEntry.orElseThrow();
        } catch (ParseException e) {
            LOG.warn("Could not parse date parameter!");
            return null;
        }
    }

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

}
