package com.bajdas.geojson.service;

import com.bajdas.geojson.exception.RestApiException;
import com.bajdas.geojson.model.CityGeography;
import com.bajdas.geojson.model.CityGeographyCollection;
import com.bajdas.geojson.model.CityMetaData;
import com.mapbox.geojson.Point;
import org.geojson.GeometryCollection;
import org.geojson.LineString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@PropertySource("classpath:application.properties")
@Service
public class CityGeographyService {
    private final CityNameResolverService cityNameResolver;
    private final DistanceService distanceService;
    private PointListService pointListService;
    private GeometryCollectionService geometryCollectionService;
    private CityBoundariesService cityBoundariesService;

    @Autowired
    public CityGeographyService(CityNameResolverService cityNameResolver, DistanceService distanceService,
                                PointListService pointListService,
                                GeometryCollectionService geometryCollectionService,
                                CityBoundariesService cityBoundariesService) {
        this.cityNameResolver = cityNameResolver;
        this.distanceService = distanceService;
        this.pointListService = pointListService;
        this.geometryCollectionService = geometryCollectionService;
        this.cityBoundariesService = cityBoundariesService;
    }


    /**
     * Base method for finding GeoJSON prepared from a given city name
     *
     * @param cityNames city name from user input
     * @return geoJSON as String
     */
    public GeometryCollection getBatchCityBoundaries(List<String> cityNames) throws RestApiException {
        CityGeographyCollection response = getCityGeographyCollection(cityNames);
        return geometryCollectionService.getAllPolygons(response);
    }

    private CityGeographyCollection getCityGeographyCollection(List<String> cityNames) throws RestApiException {
        CityGeographyCollection response = new CityGeographyCollection();
        for (String cityName : cityNames) {
            CityGeography singleCity = getCityGeography(cityName);
            response.put(cityName, singleCity);
        }
        return response;
    }

    private CityGeography getCityGeography(String cityName) throws RestApiException {
        CityMetaData singleCityMetaData = cityNameResolver.getCityMetaData(cityName);
        CityGeography singleCity = new CityGeography(singleCityMetaData);
        GeometryCollection cityBoundariesFromApi = cityBoundariesService.getCityBoundariesFromApi(singleCity);
        singleCity.setCityBoundaries(cityBoundariesFromApi);
        return singleCity;
    }

    public GeometryCollection getBatchCityBoundariesWithLines(List<String> cityNames) throws RestApiException {
        CityGeographyCollection response = getCityGeographyCollection(cityNames);
        response.getCities().forEach(this::calculateLongestLine);
        getLongestLineBetweenCities(response);
        return geometryCollectionService.getAllPolygons(response);
    }

    private void getLongestLineBetweenCities(CityGeographyCollection response) {
        List<Point> allPointList = pointListService.getAllPointList(response);
        LineString longestLine = distanceService.getLongestLine(allPointList);
        response.setLongestLine(longestLine);
    }

    private void calculateLongestLine(CityGeography singleCity) {
        List<Point> pointsFromCity = pointListService.getPointList(singleCity);
        LineString longestLine = distanceService.getLongestLine(pointsFromCity);
        singleCity.setLongestLine(longestLine);
        singleCity.setPointList(pointsFromCity);
    }


    public boolean getNeighbouringStatus(List<String> cityNames) throws RestApiException {
        CityGeographyCollection response = getCityGeographyCollection(cityNames);
        response.getCities().forEach(s->s.setPointList(pointListService.getPointList(s)));
        List<Point> collect = response.getCities().stream()
                .map(s->s.getPointList().stream()).flatMap(Stream::distinct).collect(Collectors.toList());
        int count = Math.toIntExact(collect.stream().distinct().count());
        return count != collect.size();
    }
}
