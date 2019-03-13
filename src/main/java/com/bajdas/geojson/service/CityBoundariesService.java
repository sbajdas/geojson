package com.bajdas.geojson.service;

import com.bajdas.geojson.exception.RestApiException;
import com.bajdas.geojson.exception.RestApiNotFoundException;
import com.bajdas.geojson.model.CityGeography;
import com.bajdas.geojson.model.CityGeographyCollection;
import com.bajdas.geojson.model.CityMetaData;
import org.geojson.GeometryCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class CityBoundariesService {
    private static final String ID_KEY = "id";
    private final CityIdService cityNameResolver;
    private final RestTemplate restTemplate;
    @Value("${geojsonSearch}")
    private String geojsonApiQuery;

    @Autowired
    public CityBoundariesService(CityIdService cityNameResolver, RestTemplate restTemplate) {
        this.cityNameResolver = cityNameResolver;
        this.restTemplate = restTemplate;
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

    /**
     * Base method for finding GeoJSON prepared from a given city name
     *
     * @param cityNames city name from user input
     * @return geoJSON as String
     */
    public GeometryCollection getBatchCityBoundaries(List<String> cityNames) throws RestApiException {
        CityGeographyCollection response = new CityGeographyCollection();
        for (String cityName : cityNames) {
            CityGeography singleCity = getCityGeography(cityName);
            response.put(cityName,singleCity);
        }
        return response.getAllPolygons();
    }

    private CityGeography getCityGeography(String cityName) throws RestApiException {
        CityMetaData singleCityMetaData = cityNameResolver.getCityMetaData(cityName);
        CityGeography singleCity = new CityGeography(singleCityMetaData);
        GeometryCollection cityBoundaries = getCityBoundariesFromApi(singleCity);
        singleCity.addBoundaries(cityBoundaries);
        return singleCity;
    }
}
