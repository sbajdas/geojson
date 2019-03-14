package com.bajdas.geojson.service;

import com.bajdas.geojson.model.CityGeography;
import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfMeasurement;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ListIterator;

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
}
