package com.bajdas.geojson.controller;

import com.bajdas.geojson.service.CityBoundariesService;
import lombok.extern.slf4j.Slf4j;
import org.geojson.GeometryCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class CityBoundariesController {
    private final CityBoundariesService cityBoundariesService;

    @Autowired
    public CityBoundariesController(CityBoundariesService cityBoundariesService) {
        this.cityBoundariesService = cityBoundariesService;
    }

    @GetMapping("/geojson/{cityName}")
    GeometryCollection getCityBoundaries(HttpServletRequest request, @PathVariable String cityName) {

        String userName = request.getRemoteUser();
        log.info(String.format("geoJSON city boundaries requested by %s : looking for %s", userName, cityName));
        return cityBoundariesService.getCityBoundaries(cityName);
    }
}
