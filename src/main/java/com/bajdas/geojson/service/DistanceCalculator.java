package com.bajdas.geojson.service;

import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfMeasurement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ListIterator;

class DistanceCalculator {

    private DistancePoint farthestPoint = new DistancePoint(null, null, 0.0d);

    DistancePoint getFarthestPoints(List<Point> listOfPoints) {
        for (int i = 0; i < listOfPoints.size(); i++) {
            getLongestDistanceFromPoint(listOfPoints.get(i), listOfPoints.listIterator(i));
        }
        return farthestPoint;

    }

    private void getLongestDistanceFromPoint(Point startingPoint, ListIterator<Point> listIterator) {
        listIterator.forEachRemaining(pointToCompare -> compareDistance(startingPoint, pointToCompare));
    }

    private void compareDistance(Point startingPoint, Point pointToCompare) {
        double distance = TurfMeasurement.distance(startingPoint, pointToCompare);
        if (distance > farthestPoint.getDistance()) {
            farthestPoint = new DistancePoint(startingPoint, pointToCompare, distance);
        }
    }

    DistancePoint getFarthestPointsBetweenCities(List<List<Point>> listOfPointsInCities) {
        for (int currentCityIndex = 0; currentCityIndex < listOfPointsInCities.size() - 1; currentCityIndex++) {
            calculateLongestLineToOtherCities(listOfPointsInCities, currentCityIndex);
        }
        return farthestPoint;
    }

    private void calculateLongestLineToOtherCities(List<List<Point>> listOfCities, int currentCityIndex) {
        for (int j = currentCityIndex + 1; j < listOfCities.size(); j++) {
            ListIterator<Point> nextCityPointsIterator = listOfCities.get(j).listIterator();
            List<Point> currentCityPoints = listOfCities.get(currentCityIndex);
            currentCityPoints.forEach(currentCityPoint ->
                    getLongestDistanceFromPoint(currentCityPoint, nextCityPointsIterator));
        }
    }

}
