package com.croissierd.weather.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class WeatherReport {
    @JsonProperty
    private int id;
    @JsonProperty
    private String name;
    private String country;
    private String temperature;
    private String humidity;
    private String pressure;

    @JsonProperty("main")
    private void unpackNestedObjectMain(Map<String, String> main) {
        this.temperature = main.get("temp");
        this.humidity = main.get("humidity");
        this.pressure = main.get("pressure");
    }

    @JsonProperty("sys")
    private void unpackCountryFromNestedObject(Map<String, String> sys) {
        this.country = sys.get("country");
    }
}
