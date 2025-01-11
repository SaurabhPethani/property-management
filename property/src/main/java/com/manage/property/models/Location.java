package com.manage.property.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "locations",
        indexes = {
                @Index(name = "idx_locations_city", columnList = "city"),
                @Index(name = "idx_locations_zip_code", columnList = "zip_code"),
                @Index(name = "idx_locations_lat_long", columnList = "latitude, longitude")
        })
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;

    @NotNull
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @NotNull
    @Column(name = "state", nullable = false, length = 100)
    private String state;

    @NotNull
    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @NotNull
    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;

    @NotNull
    @Min(value = -90)
    @Max(value = 90)
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull
    @Min(value = -180)
    @Max(value = 180)
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @OneToMany(mappedBy = "locationId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Property> properties;

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}

