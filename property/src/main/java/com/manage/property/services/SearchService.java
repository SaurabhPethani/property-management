package com.manage.property.services;

import com.manage.property.util.Util;
import com.manage.property.models.Property;
import com.manage.property.models.ShortlistedProperty;
import com.manage.property.repository.PropertyRepository;
import com.manage.property.repository.ShortlistedPropertyRepository;
import com.manage.property.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SearchService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShortlistedPropertyRepository shortlistedPropertyRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private Util util;

    @Cacheable(value = "propertyPages", key = "#criteria + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<Property> searchProperties(
            Map<String, Object> criteria,
            int page,
            int size,
            String sortBy,
            String sortDirection
    ) {
        String type =  (String) criteria.getOrDefault("type", null);
        Double minPrice =  (Double) criteria.getOrDefault("minPrice", null);
        Double maxPrice = (Double) criteria.getOrDefault("maxPrice", null);
        Long location = (Long) criteria.getOrDefault("locationId", null);
        // Build sorting criteria
        Sort sort = sortDirection.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Pagination and sorting
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        // Delegate to repository with filters
        return propertyRepository.findByFilters(location, minPrice, maxPrice, type, pageRequest);
    }
    public List<Property> searchProperties(Map<String, String> criteria) {

        String cacheKey = util.generateCacheKey(criteria);

        // Check if search results are cached
        List<Property> cachedResults = (List<Property>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedResults != null) {
            return cachedResults; // Return cached results
        }

        Long locationId = criteria.containsKey("locationId") ? Long.valueOf(criteria.get("locationId")) : null;
        Double minPrice = criteria.containsKey("minPrice") ? Double.valueOf(criteria.get("minPrice")) : null;
        Double maxPrice = criteria.containsKey("maxPrice") ? Double.valueOf(criteria.get("maxPrice")) : null;
        String type = criteria.getOrDefault("type", null);


        List<Property> properties = propertyRepository.searchProperties(locationId, minPrice, maxPrice, type);

        redisTemplate.opsForValue().set(cacheKey, properties, Duration.ofMinutes(10)); // Cache for 10 minutes

        return properties;
    }

    public boolean shortlistProperty(Long userId, Long propertyId) {
        // Check if property exists
        Optional<Property> propertyOpt = propertyRepository.findById(propertyId);
        if (propertyOpt.isEmpty()) {
            return false; // Property does not exist
        }

        // Add to shortlist
        ShortlistedProperty shortlistedProperty = new ShortlistedProperty();
        shortlistedProperty.setUser(userRepository.findById(userId).get());
        shortlistedProperty.setProperty(propertyOpt.get());
        shortlistedPropertyRepository.save(shortlistedProperty);

        return true;
    }

    public List<Property> getShortlistedProperties(Long userId) {
        // Retrieve shortlisted property IDs
        List<Long> propertyIds = shortlistedPropertyRepository.findPropertyIdsByUserId(userId);

        // Fetch properties by IDs
        return propertyRepository.findAllById(propertyIds);
    }
}