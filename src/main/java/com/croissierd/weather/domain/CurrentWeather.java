package com.croissierd.weather.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CurrentWeather {
    @JsonProperty
    private int cnt;
    @JsonProperty
    private List<WeatherReport> list = new ArrayList<>();
}
