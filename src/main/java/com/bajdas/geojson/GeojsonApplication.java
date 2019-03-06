package com.bajdas.geojson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Simple REST Service for retrieving GeoJson prepared from a given city name
 * City name input by path variable : /geojson/{cityName}
 *
 * @author Slawomir_Bajdas
 */
@SpringBootApplication
public class GeojsonApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeojsonApplication.class, args);
    }

}
