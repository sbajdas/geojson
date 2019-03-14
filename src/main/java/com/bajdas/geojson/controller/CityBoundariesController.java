package com.bajdas.geojson.controller;

import com.bajdas.geojson.exception.RestApiException;
import com.bajdas.geojson.service.CityGeographyService;
import lombok.extern.slf4j.Slf4j;
import org.geojson.GeometryCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
public class CityBoundariesController {
    private final CityGeographyService cityGeographyService;

    @Autowired
    public CityBoundariesController(CityGeographyService cityGeographyService) {
        this.cityGeographyService = cityGeographyService;
    }

    @GetMapping("/geojson/{cityNames}")
    GeometryCollection getCityBoundaries(HttpServletRequest request,
                                         @PathVariable List<String> cityNames,
                                         @RequestParam(required = false) boolean line) throws RestApiException {
        String userName = request.getRemoteUser();
        log.info(String.format("geoJSON city boundaries requested by %s : looking for %s%s",
                userName, String.join(",", cityNames), (line) ? " with lines" : ""));
        return (line) ? cityGeographyService.getBatchCityBoundariesWithLines(cityNames)
                : cityGeographyService.getBatchCityBoundaries(cityNames);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RestApiException.class)
    public String exception(RestApiException reason) {
        return reason.getMessage();
    }

}
