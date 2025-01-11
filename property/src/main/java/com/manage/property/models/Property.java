package com.manage.property.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "properties",
        indexes = {
                @Index(name = "idx_properties_price", columnList = "price"),
                @Index(name = "idx_properties_status", columnList = "status"),
                @Index(name = "idx_properties_location", columnList = "location_id")
        })
@Data
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long propertyId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location locationId;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @NotNull
    @Column(name = "status", nullable = false, length = 20)
    private String status = "available";

    @Column(name = "timestamp", nullable = false)
    private java.time.LocalDateTime timestamp = java.time.LocalDateTime.now();

    public void setUser(User user) {
        this.user = user;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public void setLocationId(Location locationId) {
        this.locationId = locationId;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public User getUser() {
        return user;
    }

    public Location getLocationId() {
        return locationId;
    }

    public Double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
