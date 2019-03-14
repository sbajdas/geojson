package com.bajdas.geojson.model;

import com.mapbox.geojson.Point;
import lombok.Data;
import org.geojson.GeoJsonObject;
import org.geojson.GeometryCollection;
import org.geojson.LineString;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
