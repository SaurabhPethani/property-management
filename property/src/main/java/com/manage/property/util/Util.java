package com.manage.property.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Util {

    private static final String PROPERTY_SEARCH_CACHE = "PropertySearch";
    public String generateCacheKey(Map<String, String> criteria) {
        return PROPERTY_SEARCH_CACHE + criteria.toString();
    }
}
