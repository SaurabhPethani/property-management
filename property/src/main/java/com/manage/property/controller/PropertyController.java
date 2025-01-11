package com.manage.property.controller;

import com.manage.property.models.Property;
import com.manage.property.repository.ShortlistedPropertyRepository;
import com.manage.property.services.PropertyService;
import com.manage.property.services.SearchService;
import com.manage.property.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private ShortlistedPropertyRepository shortlistedPropertyRepository;

    /**
     * Create a new property listing.
     *
     * @param property The property details.
     * @param userId   The ID of the user creating the property.
     * @return The created property.
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')") // Restrict to users with the 'USER' role
    public ResponseEntity<ApiResponse<Property>> createProperty(@RequestBody Property property, @RequestParam Long userId) {
        Property newProperty = propertyService.addProperty(userId, property);
        return ResponseEntity.ok(new ApiResponse<>(newProperty, "created "));
    }

    /**
     * Update the status of a property.
     *
     * @param propertyId The ID of the property to update.
     * @param status     The new status of the property (e.g., "sold").
     * @param userId     The ID of the user updating the property.
     * @return True if the status is updated, false otherwise.
     */
    @PreAuthorize("hasRole('USER')") // Restrict to users with the 'USER' role
    @PutMapping("/{propertyId}/status")
    public ResponseEntity<ApiResponse<Boolean>> updatePropertyStatus(
            @PathVariable Long propertyId,
            @RequestParam String status,
            @RequestParam Long userId) {
        boolean updated = propertyService.updatePropertyStatus(propertyId, status, userId);
        return ResponseEntity.ok(new ApiResponse<>(updated, "updated"));
    }

    /**
     * Get all properties created by a specific user.
     *
     * @param userId The user ID.
     * @return The list of properties.
     */
    @PreAuthorize("hasRole('USER')") // Restrict to users with the 'USER' role
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Property>>> getUserProperties(@PathVariable Long userId) {
        List<Property> properties = propertyService.getUserProperties(userId);
        return ResponseEntity.ok( new ApiResponse<>(properties, "found properties"));
    }

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Property>> getPropertyById(@PathVariable("id") Long propertyId) {
        Optional<Property> property = propertyService.getPropertyById(propertyId);

        if (property.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(property.get(), "get prop"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Property>> updateProperty(
            @PathVariable("id") Long propertyId,
            @RequestBody Property propertyDetails
    ) {
        try {
            Property updatedProperty = propertyService.updateProperty(propertyId, propertyDetails);
            return ResponseEntity.ok(new ApiResponse<>(updatedProperty, "update"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> deleteProperty(@PathVariable("id") Long propertyId) {
        try {
            propertyService.deleteProperty(propertyId);
            return ResponseEntity.ok(new ApiResponse<>(propertyId, "deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
