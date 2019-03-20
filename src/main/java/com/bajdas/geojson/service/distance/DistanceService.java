package com.bajdas.geojson.service.distance;

import com.bajdas.geojson.model.CityGeography;
import com.bajdas.geojson.model.CityGeographyCollection;
import com.google.common.util.concurrent.AtomicDouble;
import com.mapbox.geojson.Point;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DistanceService {

    private PointsToMeasure farthestPoints;

    /*
    1. obiekt z distansem i punktami?
    2. kolejka z parami punktow
    3. runnable do obliczania odleglosci
     */

    public LineString getLongestLine(List<Point> listOfPoints) {
        LinkedBlockingQueue<PointsToMeasure> queue = new LinkedBlockingQueue<>();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.submit(() -> addToQueue(listOfPoints, queue));

        Future<PointsToMeasure> result = executorService.submit(new DistanceComputer(queue));
        Future<PointsToMeasure> result2 = executorService.submit(new DistanceComputer(queue));
        Future<PointsToMeasure> result3 = executorService.submit(new DistanceComputer(queue));
        Future<PointsToMeasure> result4 = executorService.submit(new DistanceComputer(queue));
        PointsToMeasure[] results = new PointsToMeasure[4];
        try {
            results = new PointsToMeasure[]{result.get(),result2.get(),result3.get(),result4.get()};
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Arrays.sort(results);
        return getLineString(results[0]);
    }

    private void addToQueue(List<Point> listOfPoints, LinkedBlockingQueue<PointsToMeasure> queue) {
        System.out.println(Thread.currentThread().getName() + ": Start kolejki");
        for (int i = 0; i < listOfPoints.size(); i++) {
            putConnectionsToQueue(queue, listOfPoints.get(i), listOfPoints.listIterator(i));
        }
        System.out.println(Thread.currentThread().getName() + ": zakończono robić kolejkę");
    }

    private void putConnectionsToQueue(LinkedBlockingQueue<PointsToMeasure> queue, Point startingPoint, ListIterator<Point> listIterator) {
        listIterator.forEachRemaining(pointToCompare -> {
            queue.offer(new PointsToMeasure(startingPoint, pointToCompare));
        });
    }

    private LineString getLineString(PointsToMeasure farthestLine) {
        return new LineString(getLngLatAlt(farthestLine.getFrom()), getLngLatAlt(farthestLine.getTo()));
    }

    private LngLatAlt getLngLatAlt(Point point) {
        return new LngLatAlt(point.longitude(), point.latitude());
    }

//    public LineString getLongestLineBetweenCities(CityGeographyCollection response) {
//        List<List<Point>> listOfCities = response.getCities().stream()
//                .map(CityGeography::getPointList).collect(Collectors.toList());
//        PriorityBlockingQueue<PointsToMeasure> queue = new PriorityBlockingQueue<>(2000);
//        for (int i = 0; i < listOfCities.size() - 1; i++) {
//            calculateLongestLineToOtherCities(listOfCities, i);
//        }
//        return getLineString(pointsToMeasure);
//    }
//
//    private void calculateLongestLineToOtherCities(List<List<Point>> listOfCities, int i) {
//        for (int j = i + 1; j < listOfCities.size(); j++) {
//            ListIterator<Point> nextCityIterator = listOfCities.get(j).listIterator();
//            listOfCities.get(i).forEach(s -> putConnectionsToQueue(queue, s, nextCityIterator));
//        }
//    }
}
