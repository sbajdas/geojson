package com.bajdas.geojson.controller;

import com.bajdas.geojson.service.GeojsonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class GeojsonController {
    private final GeojsonService geojsonService;
    private Logger logger = LoggerFactory.getLogger(GeojsonController.class);

    @Autowired
    public GeojsonController(GeojsonService geojsonService) {
        this.geojsonService = geojsonService;
    }

    @RequestMapping("/geojson/{cityName}")
    String getGeoJsonFromCityName(HttpServletRequest request, @PathVariable String cityName) {

        String userName = request.getUserPrincipal().getName();
        logger.info(String.format("%s : %s", userName,cityName));
        return geojsonService.lookForGeoJson(cityName);
    }
}
