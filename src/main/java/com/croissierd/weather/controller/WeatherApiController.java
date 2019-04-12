package com.croissierd.weather.controller;

import com.croissierd.weather.domain.City;
import com.croissierd.weather.domain.WeatherReport;
import com.croissierd.weather.service.RequestLimitExceededException;
import com.croissierd.weather.service.WeatherService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class WeatherApiController {
    private static Logger logger = LogManager.getLogger(WeatherApiController.class);

    private final WeatherService service;

    public WeatherApiController(WeatherService service) {this.service = service;}

    @GetMapping(value = "/cities", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<City>> getCities(@RequestParam("q") List<String> city) {
        return ResponseEntity.ok(service.lookupCities(city));
    }

    @GetMapping("/current-weather")
    public @ResponseBody List<WeatherReport> currentWeather(@RequestParam() List<Integer> ids) {
        return service.getCurrentWeather(ids.stream().mapToInt(Integer::intValue).toArray());
    }

    @ExceptionHandler(RequestLimitExceededException.class)
    public ResponseEntity<?> handleWeatherServiceException(RequestLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"cod\":429,\"message\":\"Your account is temporary blocked due to exceeding of requests limitation of your subscription type. Please choose the proper subscription http://openweathermap.org/price\"}");
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> handleClientErrorException(HttpClientErrorException ex) {
        logger.info("Received: " + ex.getResponseBodyAsString());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(ex.getResponseBodyAsString());
    }
}
