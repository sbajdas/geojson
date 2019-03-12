package com.bajdas.geojson.service;

public class RestApiException extends Throwable {
    @Override
    public String getMessage() {
        return "Excuse us, there was an API error.";
    }
}
