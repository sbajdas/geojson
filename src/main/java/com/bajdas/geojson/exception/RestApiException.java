package com.bajdas.geojson.exception;

public class RestApiException extends Throwable {
    @Override
    public String getMessage() {
        return "Excuse us, there was an API error.";
    }
}
