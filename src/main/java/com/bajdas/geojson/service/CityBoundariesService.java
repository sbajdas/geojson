package com.bajdas.geojson.service;

import org.geojson.GeometryCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

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


    /**
     * Base method for finding GeoJSON prepared from a given city name
     *
     * @param cityName city name from user input
     * @return geoJSON as String
     */
    public GeometryCollection getCityBoundaries(String cityName) {
        String cityId = cityNameResolver.getCityId(cityName);
        if (cityId.equals(CityIdService.NO_CITY_FOUND)) {
            return null;
        }
        return getCityBoundariesFromApi(cityId);
    }

    private GeometryCollection getCityBoundariesFromApi(String cityId) {
        URI uri = UriComponentsBuilder.fromUriString(geojsonApiQuery).queryParam(ID_KEY, cityId).build().toUri();
        return restTemplate.getForObject(uri, GeometryCollection.class);
    }

}
