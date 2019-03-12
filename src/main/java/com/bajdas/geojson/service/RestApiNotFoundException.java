package com.bajdas.geojson.service;

public class RestApiNotFoundException extends RestApiException {
    @Override
    public String getMessage() {
        return "City not found.";
    }
}
