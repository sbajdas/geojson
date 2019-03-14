package com.bajdas.geojson.model;

import com.mapbox.geojson.Point;
import org.geojson.GeoJsonObject;
import org.geojson.GeometryCollection;
import org.geojson.LineString;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CityGeographyCollection {
    private final Map<String, CityGeography> citiesMap = new HashMap<>();
    private LineString longestLine;

    public void put(String cityName, CityGeography singleCity) {
        citiesMap.put(cityName, singleCity);
    }

    public GeometryCollection getAllPolygons() {
        GeometryCollection result = new GeometryCollection();
        List<GeoJsonObject> collect = getCities().stream()
                .map(CityGeography::getCityGeometries).flatMap(Collection::stream)
                .collect(Collectors.toList());
        result.setGeometries(collect);
        addLines(result);
        return result;
    }

    private void addLines(GeometryCollection result) {
        getCities().stream().filter(s -> s.getLongestLine() != null).
                map(CityGeography::getLongestLine).forEach(result::add);
        if(longestLine!=null) {
            result.add(longestLine);
        }
    }

    public Collection<CityGeography> getCities() {
        return citiesMap.values();
    }

    public List<Point> getAllPointList() {
        return getCities().stream().map(CityGeography::getPointList)
                .flatMap(Collection::stream).distinct()
                .collect(Collectors.toList());
    }

    public void setLongestLine(LineString longestLine) {
        this.longestLine = longestLine;
    }
}
