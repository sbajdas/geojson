package com.bajdas.geojson.service;

import org.json.JSONArray;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component
public class CityIdResolver {
    final static String NO_CITY_FOUND = "0";
    private final static String API_QUERY_URL = "https://nominatim.openstreetmap.org/search?format=json&q=";
    private final Map<String, String> mapper = new HashMap<>();

    @PostConstruct
    private void fillMap() {
        mapper.put("Gdansk", "2597485");
    }

    /**
     * Method for retrieving place ID from OpenStreetMap API
     * If no city is found, default return is 0.
     *
     * @param cityName city name to look up for, from user's input
     * @return cityId
     */
    String getIdFromApi(String cityName) {
        String jsonString = getSearchResult(cityName);
        JSONArray jsonArray = new JSONArray(jsonString);
        return (!jsonArray.isNull(0)) ? osmIdFromJson(jsonArray) : NO_CITY_FOUND;
    }

    private String osmIdFromJson(JSONArray jsonArray) {
        return jsonArray.getJSONObject(0).get("osm_id").toString();
    }

    private String getSearchResult(String cityName) {
        URI uri = URI.create(API_QUERY_URL + cityName);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, String.class);
    }

    /**
     * Deprecated method for retrieving city Id from a mapper.
     *
     * @param cityName city name to search for, from user's input
     * @return cityId from map, if not found default return is 0.
     */
    String getIdFromMap(String cityName) {
        return mapper.getOrDefault(cityName, NO_CITY_FOUND);
    }
}
