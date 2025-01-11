package com.manage.property.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
@Table(name = "users",
        indexes = {
                @Index(name = "idx_users", columnList = "username", unique = true)
        })
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Property> properties;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShortlistedProperty> shortlistedProperties;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public void setShortlistedProperties(List<ShortlistedProperty> shortlistedProperties) {
        this.shortlistedProperties = shortlistedProperties;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public List<ShortlistedProperty> getShortlistedProperties() {
        return shortlistedProperties;
    }
}

