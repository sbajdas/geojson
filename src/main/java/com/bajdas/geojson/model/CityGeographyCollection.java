package com.bajdas.geojson.model;

import lombok.Data;
import org.geojson.LineString;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class CityGeographyCollection {
    private final Map<String, CityGeography> citiesMap = new HashMap<>();
    private LineString longestLine;

    public void put(String cityName, CityGeography singleCity) {
        citiesMap.put(cityName, singleCity);
    }

    public Collection<CityGeography> getCities() {
        return citiesMap.values();
    }
}
