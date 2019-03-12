package com.bajdas.geojson.service;

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


    private GeometryCollection getCityBoundariesFromApi(String cityId) throws RestApiException {
        URI uri = UriComponentsBuilder.fromUriString(geojsonApiQuery).queryParam(ID_KEY, cityId).build().toUri();
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
        GeometryCollection response = new GeometryCollection();
        for (String s : cityNames) {
            response.getGeometries().addAll(getCityBoundaries(s).getGeometries());
        }
        return response;
    }

    private GeometryCollection getCityBoundaries(String cityName) throws RestApiException {
        String cityId = cityNameResolver.getCityMetaData(cityName).osm_id;
        return getCityBoundariesFromApi(cityId);
    }
}
