package com.croissierd.weather.service;

import com.croissierd.weather.domain.City;
import com.croissierd.weather.domain.WeatherReport;

import java.util.List;

public interface WeatherService {
    List<City> lookupCity(String name);

    List<City> lookupCities(List<String> cities);

    List<WeatherReport> getCurrentWeather(int[] ids);
}
