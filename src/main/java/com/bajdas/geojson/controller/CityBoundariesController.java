package com.bajdas.geojson.controller;

import com.bajdas.geojson.exception.RestApiException;
import com.bajdas.geojson.exception.ServiceException;
import com.bajdas.geojson.model.NeighboursDTO;
import com.bajdas.geojson.service.CityGeographyService;
import com.bajdas.geojson.service.NeighbouringService;
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
    private NeighbouringService neighbouringService;

    @Autowired
    public CityBoundariesController(CityGeographyService cityGeographyService, NeighbouringService neighbouringService) {
        this.cityGeographyService = cityGeographyService;
        this.neighbouringService = neighbouringService;
    }

    @GetMapping("/geojson/{cityNames}")
    GeometryCollection getCityBoundaries(HttpServletRequest request,
                                         @PathVariable List<String> cityNames,
                                         @RequestParam(required = false) boolean line) throws RestApiException, ServiceException {
        String userName = request.getRemoteUser();
        log.info(String.format("geoJSON city boundaries requested by %s : looking for %s%s",
                userName, String.join(",", cityNames), (line) ? " with lines" : ""));
        return (line) ? cityGeographyService.getBatchCityBoundariesWithLines(cityNames)
                : cityGeographyService.getBatchCityBoundaries(cityNames);
    }


    @GetMapping("/neighbours/{cityNames}")
    NeighboursDTO getNeighboursInfo(HttpServletRequest request,
                                    @PathVariable List<String> cityNames) throws RestApiException, ServiceException {
        String userName = request.getRemoteUser();
        if(cityNames.size()!=2) {
            throw new ServiceException("Please provide a list of 2 cities");
        }
        log.info(String.format("Neighbouring status requested by %s : for %s%n",
                userName, String.join(",", cityNames)));
        return neighbouringService.getNeighbouringInfo(cityNames);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RestApiException.class)
    public String exception(RestApiException reason) {
        return reason.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    public String serviceException(ServiceException reason) {
        return reason.getMessage();
    }

}
