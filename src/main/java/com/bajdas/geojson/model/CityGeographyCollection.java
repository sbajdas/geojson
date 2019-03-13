package com.bajdas.geojson.model;

import org.geojson.GeoJsonObject;
import org.geojson.GeometryCollection;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CityGeographyCollection {
    private final Map<String, CityGeography> citiesMap = new HashMap<>();

    public void put(String cityName, CityGeography singleCity) {
        citiesMap.put(cityName, singleCity);
    }

    public GeometryCollection getAllPolygons() {
        GeometryCollection result = new GeometryCollection();
        List<GeoJsonObject> collect = citiesMap.values().stream().map(CityGeography::getCityGeometries).flatMap(Collection::stream).collect(Collectors.toList());
        result.setGeometries(collect);
        return result;
    }
}
