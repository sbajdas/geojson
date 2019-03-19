package com.bajdas.geojson.service;

import com.bajdas.geojson.exception.RestApiException;
import com.bajdas.geojson.model.CityGeographyCollection;
import com.bajdas.geojson.model.NeighboursDTO;
import com.mapbox.geojson.Point;
import com.mapbox.turf.*;
import org.geojson.GeometryCollection;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class NeighbouringService {
    private CityGeographyService cityGeographyService;
    private PointListService pointListService;

    @Autowired
    public NeighbouringService(CityGeographyService cityGeographyService, PointListService pointListService) {
        this.cityGeographyService = cityGeographyService;
        this.pointListService = pointListService;
    }

    public NeighboursDTO getNeighbouringInfo(List<String> cityNames) throws RestApiException {
        List<Point> borderPoints = getBorderPoints(cityNames);
        if(borderPoints.size()==0)
            return NeighboursDTO.builder().isNeigbouring(false).build();
        return NeighboursDTO.builder()
                .isNeigbouring(true)
                .borderLength(getBorderLength(borderPoints))
                .borderCenter(getBorderCenter(borderPoints))
                .border(getLineToDisplay(borderPoints))
                .build();
    }

    private Point getBorderCenter(List<Point> borderPoints) {
        return null;
    }

    private List<Point> getBorderPoints(List<String> cityNames) throws RestApiException {
        List<Point> collect = getAllPoints(cityNames);
        HashSet<Point> allPoints = new HashSet<>();
        return collect.stream().filter(s -> !allPoints.add(s)).collect((Collectors.toList()));
    }

    private List<Point> getAllPoints(List<String> cityNames) throws RestApiException {
        CityGeographyCollection response = cityGeographyService.getCityGeographyCollection(cityNames);
        response.getCities().forEach(s -> s.setPointList(pointListService.getPointList(s)));
        return response.getCities().stream()
                .map(s -> s.getPointList().stream())
                .flatMap(Stream::distinct).collect(Collectors.toList());
    }

    private double getBorderLength(List<Point> listPoints) {
        com.mapbox.geojson.LineString lineForCalculation = com.mapbox.geojson.LineString.fromLngLats(listPoints);
        return TurfMeasurement.length(lineForCalculation, TurfConstants.UNIT_KILOMETERS);
    }

    private GeometryCollection getLineToDisplay(List<Point> listPoints) {
        LngLatAlt[] objects = listPoints.stream().map(this::getLngLatAlt).toArray(LngLatAlt[]::new);
        LineString lineToDisplay = new LineString(objects);
        return new GeometryCollection().add(lineToDisplay);
    }

    private LngLatAlt getLngLatAlt(Point point) {
        return new LngLatAlt(point.longitude(), point.latitude());
    }
}
