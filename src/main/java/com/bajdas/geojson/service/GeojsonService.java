package com.bajdas.geojson.service;

import org.geojson.GeometryCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@Service
public class GeojsonService {
    private final CityIdResolver cityNameResolver;
    @Value("${geojsonSearch}")
    private String geojsonApiQuery;

    @Autowired
    public GeojsonService(CityIdResolver cityNameResolver) {
        this.cityNameResolver = cityNameResolver;
    }


    /**
     * Base method for finding GeoJSON prepared from a given city name
     *
     * @param cityName city name from user input
     * @return geoJSON as String
     */
    public GeometryCollection lookForGeoJson(String cityName) {
        String cityId = cityNameResolver.getIdFromApi(cityName);
        if (cityId.equals(CityIdResolver.NO_CITY_FOUND)) {
//            return String.format("Can't find GeoJSON for the city name %s, sorry.", cityName);
            return null;
        }
        return getGeoJsonFromApi(cityId);
    }

    private GeometryCollection getGeoJsonFromApi(String cityId) {
        URI uri = UriComponentsBuilder.fromUriString(geojsonApiQuery).queryParam("id", cityId).build().toUri();
        RestTemplate template = getRestTemplateAcceptingPlainText();
        return template.getForObject(uri, GeometryCollection.class);
    }

    private RestTemplate getRestTemplateAcceptingPlainText() {
        RestTemplate template = new RestTemplate();
        MappingJackson2HttpMessageConverter mapper =
                new MappingJackson2HttpMessageConverter();
        mapper.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_PLAIN, MediaType.TEXT_HTML));
        template.getMessageConverters().add(mapper);
        return template;
    }
}
