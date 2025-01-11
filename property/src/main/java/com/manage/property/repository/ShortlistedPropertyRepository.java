package com.manage.property.repository;

import com.manage.property.models.ShortlistedProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShortlistedPropertyRepository extends JpaRepository<ShortlistedProperty, Long> {

    // Find all property IDs shortlisted by a user
    @Query("SELECT sp.property FROM ShortlistedProperty sp WHERE sp.user.id = :userId")
    List<Long> findPropertyIdsByUserId(@Param("userId") Long userId);

    // Check if a property is already shortlisted by a user
    @Query("SELECT COUNT(sp) > 0 FROM ShortlistedProperty sp WHERE sp.user.id = :userId AND sp.property.propertyId = :propertyId")
    boolean existsByUserIdAndPropertyId(@Param("userId") Long userId, @Param("propertyId") Long propertyId);
}
