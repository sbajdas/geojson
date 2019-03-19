package com.bajdas.geojson.exception;

public class RestApiException extends Throwable {

    private String message = "Excuse us, there was an API error.";

    @Override
    public String getMessage() {
        return message;
    }
}
