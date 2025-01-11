package com.manage.property.repository;

import com.manage.property.models.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p from Property p where p.user.id = :userId")
    List<Property> findByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Property p WHERE (:locationId IS NULL OR p.locationId = :locationId) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:type IS NULL OR p.type = :type) ")
    List<Property> searchProperties(@Param("locationId") Long locationId,
                                    @Param("minPrice") Double minPrice,
                                    @Param("maxPrice") Double maxPrice,
                                    @Param("type") String type);

    @Query("SELECT p FROM Property p WHERE " +
            "(:location IS NULL OR p.locationId = :locationId) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:type IS NULL OR p.type = :type)")
    Page<Property> findByFilters(
            @Param("locationId") Long locationId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("type") String type,
            Pageable pageable
    );
}
