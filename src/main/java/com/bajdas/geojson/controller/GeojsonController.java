package com.bajdas.geojson.controller;

import com.bajdas.geojson.service.GeojsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeojsonController {
    private final GeojsonService geojsonService;

    @Autowired
    public GeojsonController(GeojsonService geojsonService) {
        this.geojsonService = geojsonService;
    }

    @RequestMapping("/geojson/{cityName}")
    String getGeoJsonFromCityName(@PathVariable String cityName) {
        return geojsonService.lookForGeoJson(cityName);
    }
}
