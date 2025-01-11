package com.manage.property.services;

import com.manage.property.models.Location;
import com.manage.property.models.Property;
import com.manage.property.repository.LocationRepository;
import com.manage.property.repository.PropertyRepository;
import com.manage.property.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.*;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    public Property addProperty(Long userId, Property propertyDetails) {
        propertyDetails.setUser(userRepository.findById(userId).get());
        propertyDetails.setStatus("available");
        return propertyRepository.save(propertyDetails);
    }

    public boolean updatePropertyStatus(Long propertyId, String status, Long userId) {
        Optional<Property> propertyOpt = propertyRepository.findById(propertyId);
        if (propertyOpt.isPresent() && propertyOpt.get().getUser().getUserId().equals(userId)) {
            Property property = propertyOpt.get();
            property.setStatus(status);
            propertyRepository.save(property);
            return true;
        }
        return false;
    }

    public List<Property> getUserProperties(Long userId) {
        return propertyRepository.findByUserId(userId);
    }
    public Location addLocation(Location location) {
        return locationRepository.save(location);
    }

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public Optional<Property> getPropertyById(Long propertyId) {
        return propertyRepository.findById(propertyId);
    }

    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    public Property updateProperty(Long propertyId, Property propertyDetails) {
        Optional<Property> optionalProperty = propertyRepository.findById(propertyId);

        if (optionalProperty.isPresent()) {
            Property property = optionalProperty.get();
            property.setUser(propertyDetails.getUser());
            property.setLocationId(propertyDetails.getLocationId());
            property.setPrice(propertyDetails.getPrice());
            property.setType(propertyDetails.getType());
            property.setStatus(propertyDetails.getStatus());
            return propertyRepository.save(property);
        } else {
            throw new RuntimeException("Property not found with ID: " + propertyId);
        }
    }

    public void deleteProperty(Long propertyId) {
        if (propertyRepository.existsById(propertyId)) {
            propertyRepository.deleteById(propertyId);
        } else {
            throw new RuntimeException("Property not found with ID: " + propertyId);
        }
    }
}
