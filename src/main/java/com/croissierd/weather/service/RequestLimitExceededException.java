package com.croissierd.weather.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class RequestLimitExceededException extends HttpStatusCodeException {
    private static final String ERROR_MESSAGE =
            "Your account is temporary blocked due to exceeding of requests limitation of your subscription type. " +
            "Please choose the proper subscription http://openweathermap.org/price";

    public RequestLimitExceededException() {
        super(HttpStatus.TOO_MANY_REQUESTS, ERROR_MESSAGE);
    }
}
