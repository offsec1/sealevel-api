package de.htwberlin.sealevel.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Sealevel data model
 * @Entity Annotation for database connection
 */
@Entity
public class Sealevel {

    /**
     * @Id announces attribute as id
     * @GeneratedValue announces that the attribute is to be autogenerated by the database
     */
    @Id
    @GeneratedValue
    private Long id;

    private float sealevel;
    private double longitude;
    private double latitude;
    private Date date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getSealevel() {
        return sealevel;
    }

    public void setSealevel(float sealevel) {
        this.sealevel = sealevel;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
