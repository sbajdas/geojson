package com.bajdas.geojson.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class CityIdResolver {
    final static String NO_CITY_FOUND = "0";
    @Value("${idsearch}")
    private String apiQueryUrl;

    /**
     * Method for retrieving place ID from OpenStreetMap API
     * If no city is found, default return is 0.
     *
     * @param cityName city name to look up for, from user's input
     * @return cityId
     */
    String getIdFromApi(String cityName) {
        CityMetaData[] metadataResults = getSearchResult(cityName);
        return (metadataResults.length > 0) ? osmIdFromSearchResults(metadataResults) : NO_CITY_FOUND;
    }

    private String osmIdFromSearchResults(CityMetaData[] metadataResults) {
        return metadataResults[0].osm_id;
    }

    private CityMetaData[] getSearchResult(String cityName) {
        URI uri = UriComponentsBuilder.fromUriString(apiQueryUrl).queryParam("format", "json").queryParam("q", cityName).build().toUri();
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, CityMetaData[].class);
    }
}
