package com.bajdas.geojson.service;

import com.bajdas.geojson.model.CityGeography;
import com.bajdas.geojson.model.CityGeographyCollection;
import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfMeasurement;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@Service
public class DistanceService {

    private Double longestDistance;
    private Point[] longest;

    LineString getLongestLine(List<Point> listOfPoints) {
        longestDistance = 0.0d;
        for (int i = 0; i < listOfPoints.size(); i++) {
            getLongestDistanceFromPoint(listOfPoints.get(i), listOfPoints.listIterator(i));
        }
        return getLineString();
    }

    private void getLongestDistanceFromPoint(Point startingPoint, ListIterator<Point> listIterator) {
        listIterator.forEachRemaining(pointToCompare -> compareDistance(startingPoint, pointToCompare));
    }

    private void compareDistance(Point startingPoint, Point pointToCompare) {
        double distance = TurfMeasurement.distance(startingPoint, pointToCompare);
        if (distance > longestDistance) {
            longestDistance = distance;
            longest = new Point[]{startingPoint, pointToCompare};
        }
    }

    private LineString getLineString() {
        return new LineString(getLngLatAlt(longest[0]), getLngLatAlt(longest[1]));
    }

    private LngLatAlt getLngLatAlt(Point point) {
        return new LngLatAlt(point.longitude(), point.latitude());
    }

    LineString getLongestLineBetweenCities(CityGeographyCollection response) {
        longestDistance = 0.0d;
        List<List<Point>> listOfCities = response.getCities().stream()
                .map(CityGeography::getPointList).collect(Collectors.toList());
        for (int i = 0; i < listOfCities.size() - 1; i++) {
            for (int j = i + 1; j < listOfCities.size(); j++) {
                ListIterator<Point> nextCityIterator = listOfCities.get(j).listIterator();
                listOfCities.get(i).forEach(s -> getLongestDistanceFromPoint(s, nextCityIterator));
            }
        }
        return getLineString();
    }
}
