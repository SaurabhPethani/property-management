package com.manage.property.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "shortlisted_properties",
        indexes = {
                @Index(name = "idx_shortlisted_properties_user", columnList = "user_id"),
                @Index(name = "idx_shortlisted_properties_property", columnList = "property_id")
        })
@Data
public class ShortlistedProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }


}
