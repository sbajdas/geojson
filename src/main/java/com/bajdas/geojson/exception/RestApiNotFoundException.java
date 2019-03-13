package com.bajdas.geojson.exception;

public class RestApiNotFoundException extends RestApiException {
    @Override
    public String getMessage() {
        return "City not found.";
    }
}
