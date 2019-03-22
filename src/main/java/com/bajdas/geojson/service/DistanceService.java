package com.bajdas.geojson.service;

import com.bajdas.geojson.model.CityGeography;
import com.bajdas.geojson.model.CityGeographyCollection;
import com.mapbox.geojson.Point;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@Service
public class DistanceService {
    @Autowired
    private DistanceCalculator distanceCalculator;
    @Autowired
    private PointListService pointListService;


    LineString getLongestLine(List<Point> listOfPoints) {
        DistancePoint farthestPoints = distanceCalculator.getFarthestPoints(listOfPoints);
        return getLineString(farthestPoints);
    }

    private LineString getLineString(DistancePoint farthestPoints) {
        return new LineString(getLngLatAlt(farthestPoints.getFrom()), getLngLatAlt(farthestPoints.getTo()));
    }

    private LngLatAlt getLngLatAlt(Point point) {
        return new LngLatAlt(point.longitude(), point.latitude());
    }

    LineString getLongestLineBetweenCities(CityGeographyCollection cityCollection) {
        List<List<Point>> listOfCitiesPoints = pointListService.getCitiesPointsList(cityCollection);
        DistancePoint farthestPoints = distanceCalculator.getFarthestPointsBetweenCities(listOfCitiesPoints);
        return getLineString(farthestPoints);
    }


}
