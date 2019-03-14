package com.bajdas.geojson.service;

import com.bajdas.geojson.model.CityGeography;
import com.bajdas.geojson.model.CityGeographyCollection;
import org.geojson.GeoJsonObject;
import org.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointListService {

    List<Point> getPointList(CityGeography singleCity) {
        GeoJsonObject geoJsonObject = singleCity.getCityBoundaries().getGeometries().get(0);
        if (singleCity.getPointList().isEmpty() && geoJsonObject instanceof MultiPolygon) {
            return preparePointList((MultiPolygon) geoJsonObject);
        }
        return singleCity.getPointList();
    }

    private List<Point> preparePointList(MultiPolygon multiPolygon) {
        return multiPolygon.getCoordinates().stream()
                .flatMap(Collection::stream).flatMap(Collection::stream)
                .map(lngLat -> Point.fromLngLat(lngLat.getLongitude(), lngLat.getLatitude()))
                .collect(Collectors.toList());
    }

    List<Point> getAllPointList(CityGeographyCollection response) {
            return response.getCities().stream().map(CityGeography::getPointList)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
    }
}
