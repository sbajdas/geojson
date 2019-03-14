package com.bajdas.geojson.service;

import com.bajdas.geojson.model.CityGeography;
import com.bajdas.geojson.model.CityGeographyCollection;
import org.geojson.GeoJsonObject;
import org.geojson.GeometryCollection;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeometryCollectionService {
    GeometryCollection getAllPolygons(CityGeographyCollection collection) {
        GeometryCollection result = new GeometryCollection();
        List<GeoJsonObject> collect = collection.getCities().stream()
                .map(CityGeography::getCityGeometries).flatMap(Collection::stream)
                .collect(Collectors.toList());
        result.setGeometries(collect);
        addLines(collection,result);
        return result;
    }

    private void addLines(CityGeographyCollection collection, GeometryCollection result) {
        collection.getCities().stream().filter(s -> s.getLongestLine() != null)
                .map(CityGeography::getLongestLine).forEach(result::add);
        if (collection.getLongestLine() != null) {
            result.add(collection.getLongestLine());
        }
    }
}
