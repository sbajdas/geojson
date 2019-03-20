package com.bajdas.geojson.service;

import com.bajdas.geojson.exception.RestApiException;
import com.bajdas.geojson.model.CityGeographyCollection;
import com.bajdas.geojson.model.NeighboursDTO;
import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfConstants;
import com.mapbox.turf.TurfMeasurement;
import org.geojson.GeometryCollection;
import com.mapbox.geojson.LineString;
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
        if (borderPoints.size() == 0)
            return NeighboursDTO.builder().isNeigbouring(false).build();
        LineString lineForCalculation = LineString.fromLngLats(borderPoints);
        double borderLength = getBorderLength(lineForCalculation);
        org.geojson.Point borderCenter = getBorderCenter(lineForCalculation, borderLength);
        return NeighboursDTO.builder()
                .isNeigbouring(true)
                .borderLength(borderLength)
                .borderWithCenterPoint(getLineToDisplay(borderPoints,borderCenter))
                .build();
    }

    private org.geojson.Point getBorderCenter(LineString borderPoints, double borderLength) {
        Point along = TurfMeasurement.along(borderPoints, borderLength / 2.0d, TurfConstants.UNIT_KILOMETERS);
        return new org.geojson.Point(getLngLatAlt(along));
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

    private double getBorderLength(LineString lineForCalculation) {
        return TurfMeasurement.length(lineForCalculation, TurfConstants.UNIT_KILOMETERS);
    }

    private GeometryCollection getLineToDisplay(List<Point> listPoints, org.geojson.Point borderCenter) {
        LngLatAlt[] points = listPoints.stream().map(this::getLngLatAlt).toArray(LngLatAlt[]::new);
        return new GeometryCollection()
                .add(new org.geojson.LineString(points))
                .add(borderCenter);
    }

    private LngLatAlt getLngLatAlt(Point point) {
        return new LngLatAlt(point.longitude(), point.latitude());
    }
}
