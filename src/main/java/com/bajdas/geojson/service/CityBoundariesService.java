package com.bajdas.geojson.service;

import com.bajdas.geojson.exception.RestApiException;
import com.bajdas.geojson.exception.RestApiNotFoundException;
import com.bajdas.geojson.model.CityGeography;
import com.bajdas.geojson.model.CityGeographyCollection;
import com.bajdas.geojson.model.CityMetaData;
import org.geojson.GeometryCollection;
import org.geojson.LineString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@PropertySource("classpath:application.properties")
@Service
public class CityBoundariesService {
    private static final String ID_KEY = "id";
    private final CityIdService cityNameResolver;
    private final RestTemplate restTemplate;
    private final DistanceService distanceService;
    @Value("${geojsonSearch}")
    private String geojsonApiQuery;

    @Autowired
    public CityBoundariesService(CityIdService cityNameResolver, RestTemplate restTemplate, DistanceService distanceService) {
        this.cityNameResolver = cityNameResolver;
        this.restTemplate = restTemplate;
        this.distanceService = distanceService;
    }


    /**
     * Base method for finding GeoJSON prepared from a given city name
     *
     * @param cityNames city name from user input
     * @return geoJSON as String
     */
    public GeometryCollection getBatchCityBoundaries(List<String> cityNames) throws RestApiException {
        CityGeographyCollection response = getCityGeographyCollection(cityNames);
        return response.getAllPolygons();
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
        singleCity.setCityBoundaries(getCityBoundariesFromApi(singleCity));
        return singleCity;
    }

    private GeometryCollection getCityBoundariesFromApi(CityGeography cityDetails) throws RestApiException {
        URI uri = UriComponentsBuilder.fromUriString(geojsonApiQuery).queryParam(ID_KEY, cityDetails.getOsmId()).build().toUri();
        GeometryCollection cityBoundaries;
        try {
            cityBoundaries = restTemplate.getForObject(uri, GeometryCollection.class);
        } catch (RestClientException e) {
            throw new RestApiException();
        }
        if (cityBoundaries == null || cityBoundaries.getGeometries().isEmpty()) {
            throw new RestApiNotFoundException();
        }
        return cityBoundaries;
    }

    public GeometryCollection getBatchCityBoundariesWithLines(List<String> cityNames) throws RestApiException {
        CityGeographyCollection response = getCityGeographyCollection(cityNames);
        for (CityGeography singleCity : response.getCities()) {
            LineString longestLine = distanceService.getLongestLine(singleCity.getPointList());
            singleCity.setLongestLine(longestLine);
        }
        LineString longestLine = distanceService.getLongestLine(response.getAllPointList());
        response.setLongestLine(longestLine);
        return response.getAllPolygons();
    }
}
