package com.bajdas.geojson.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class GeojsonService {
    private final static String GEOJSON_API_QUERY = "http://polygons.openstreetmap.fr/get_geojson.py?id=";
    private final CityIdResolver cityNameResolver;

    @Autowired
    public GeojsonService(CityIdResolver cityNameResolver) {
        this.cityNameResolver = cityNameResolver;
    }


    /**
     * Base method for finding GeoJSON prepared from a given city name
     * @param cityName city name from user input
     * @return geoJSON as String
     */
    public String lookForGeoJson(String cityName) {
        String cityId = cityNameResolver.getIdFromApi(cityName);
        if (cityId.equals(CityIdResolver.NO_CITY_FOUND)) {
            return String.format("Can't find GeoJSON for the city name %s, sorry.", cityName);
        }
        return getGeoJsonFromApi(cityId);
    }

    private String getGeoJsonFromApi(String cityId) {
        URI uri = URI.create(GEOJSON_API_QUERY + cityId);
        RestTemplate template = new RestTemplate();
        return template.getForObject(uri, String.class);
    }
}
