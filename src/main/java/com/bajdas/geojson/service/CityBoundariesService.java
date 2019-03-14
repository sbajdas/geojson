package com.bajdas.geojson.service;

import com.bajdas.geojson.exception.RestApiException;
import com.bajdas.geojson.exception.RestApiNotFoundException;
import com.bajdas.geojson.model.CityGeography;
import org.geojson.GeometryCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class CityBoundariesService {

    private static final String ID_KEY = "id";
    private final RestTemplate restTemplate;
    @Value("${geojsonSearch}")
    private String geojsonApiQuery;

    @Autowired
    public CityBoundariesService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GeometryCollection getCityBoundariesFromApi(CityGeography cityDetails) throws RestApiException {
        URI uri = UriComponentsBuilder.fromUriString(geojsonApiQuery)
                .queryParam(ID_KEY, cityDetails.getOsmId()).build().toUri();
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
}
