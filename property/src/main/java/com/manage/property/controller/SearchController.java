package com.manage.property.controller;

import com.manage.property.models.Property;
import com.manage.property.services.SearchService;
import com.manage.property.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/properties/search")
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Allow both users and admins
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Property>>> searchProperties(
            @RequestParam Map<String, Object> criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        Page<Property> result = searchService.searchProperties(criteria, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(new ApiResponse<>(result, "search result"));
    }

    /**
     * Search for properties based on criteria.
     *
     * @param criteria A map of search criteria (e.g., locationId, price range, type).
     * @return A list of matching properties.
     */
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<Property>>> searchProperties(@RequestParam Map<String, String> criteria) {
//        List<Property> properties = searchService.searchProperties(criteria);
//        return ResponseEntity.ok(new ApiResponse<>(properties, "search result"));
//    }

    /**
     * Shortlist a property for a user.
     *
     * @param userId     The ID of the user.
     * @param propertyId The ID of the property to shortlist.
     * @return True if the property was shortlisted, false otherwise.
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{propertyId}/shortlist")
    public ResponseEntity<ApiResponse<Boolean>> shortlistProperty(
            @RequestParam Long userId,
            @PathVariable Long propertyId) {
        boolean success = searchService.shortlistProperty(userId, propertyId);
        return ResponseEntity.ok(new ApiResponse<>(success," shortlist"));
    }

    /**
     * Get all shortlisted properties for a user.
     *
     * @param userId The user ID.
     * @return A list of shortlisted properties.
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/shortlisted")
    public ResponseEntity<ApiResponse<List<Property>>> getShortlistedProperties(@RequestParam Long userId) {
        List<Property> shortlistedProperties = searchService.getShortlistedProperties(userId);
        return ResponseEntity.ok(new ApiResponse<>(shortlistedProperties, "shortlisted property"));
    }
}
