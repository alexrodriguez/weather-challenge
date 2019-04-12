package com.croissierd.weather.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

public class WeatherResponseErrorhandler extends DefaultResponseErrorHandler {
    @Override
    protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
        if (HttpStatus.TOO_MANY_REQUESTS == statusCode) {
            throw new RequestLimitExceededException();
        } else {
            super.handleError(response, statusCode);
        }
    }
}
