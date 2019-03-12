package com.bajdas.geojson.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class CityIdService {
    final static String NO_CITY_FOUND = "0";
    private static final String FORMAT_KEY = "format";
    private static final String FORMAT_VALUE_JSON = "json";
    private static final String QUERY_KEY = "q";
    @Value("${idsearch}")
    private String apiQueryUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public CityIdService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Method for retrieving place ID_KEY from OpenStreetMap API
     * If no city is found, default return is 0.
     *
     * @param cityName city name to look up for, from user's input
     * @return cityId
     */
    String getCityId(String cityName) {
        CityMetaData[] metadataResults = getSearchResult(cityName);
        return (metadataResults.length > 0) ? osmIdFromSearchResults(metadataResults) : NO_CITY_FOUND;
    }

    private String osmIdFromSearchResults(CityMetaData[] metadataResults) {
        return metadataResults[0].osm_id;
    }

    private CityMetaData[] getSearchResult(String cityName) {
        URI uri = UriComponentsBuilder.fromUriString(apiQueryUrl).queryParam(FORMAT_KEY, FORMAT_VALUE_JSON).queryParam(QUERY_KEY, cityName).build().toUri();
        return restTemplate.getForObject(uri, CityMetaData[].class);
    }
}
