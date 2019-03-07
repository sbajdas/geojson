package com.bajdas.geojson.controller;

import com.bajdas.geojson.service.GeojsonService;
import lombok.extern.slf4j.Slf4j;
import org.geojson.GeometryCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class GeojsonController {
    private final GeojsonService geojsonService;

    @Autowired
    public GeojsonController(GeojsonService geojsonService) {
        this.geojsonService = geojsonService;
    }

    @RequestMapping("/geojson/{cityName}")
    GeometryCollection getGeoJsonFromCityName(HttpServletRequest request, @PathVariable String cityName) {

        String userName = request.getRemoteUser();
        log.info(String.format("geoJSON requested by %s : looking for %s", userName, cityName));
        return geojsonService.lookForGeoJson(cityName);
    }
}
