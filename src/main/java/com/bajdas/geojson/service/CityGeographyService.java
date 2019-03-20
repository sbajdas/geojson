package com.bajdas.geojson.service;

import com.bajdas.geojson.exception.RestApiException;
import com.bajdas.geojson.exception.ServiceException;
import com.bajdas.geojson.model.CityGeography;
import com.bajdas.geojson.model.CityGeographyCollection;
import com.bajdas.geojson.model.CityMetaData;
import com.bajdas.geojson.service.distance.DistanceService;
import com.mapbox.geojson.Point;
import org.geojson.GeometryCollection;
import org.geojson.LineString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.List;

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

    CityGeographyCollection getCityGeographyCollection(List<String> cityNames) throws RestApiException {
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

    public GeometryCollection getBatchCityBoundariesWithLines(List<String> cityNames) throws RestApiException, ServiceException {
        CityGeographyCollection response = getCityGeographyCollection(cityNames);
        for (CityGeography cityGeography : response.getCities()) {
            calculateLongestLine(cityGeography);
        }
//        LineString longestLineBetweenCities = distanceService.getLongestLineBetweenCities(response);
//        response.setLongestLine(longestLineBetweenCities);
        return geometryCollectionService.getAllPolygons(response);
    }

    private void calculateLongestLine(CityGeography singleCity) throws ServiceException {
        List<Point> pointsFromCity = pointListService.getPointList(singleCity);
        LineString longestLine = distanceService.getLongestLine(pointsFromCity);
        singleCity.setLongestLine(longestLine);
        singleCity.setPointList(pointsFromCity);
    }

}
