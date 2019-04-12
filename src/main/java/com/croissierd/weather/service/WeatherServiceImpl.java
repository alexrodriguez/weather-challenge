package com.croissierd.weather.service;

import com.croissierd.weather.domain.City;
import com.croissierd.weather.domain.CurrentWeather;
import com.croissierd.weather.domain.WeatherReport;
import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Predicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toUnmodifiableList;

@Service
public class WeatherServiceImpl implements WeatherService {
    private static Logger logger = LogManager.getLogger(WeatherServiceImpl.class);

    private final DocumentContext context;
    private final String weatherUrl;

    public WeatherServiceImpl(DocumentContext context, @Value("${weather.api.url}") String url) {
        this.context = context;
        this.weatherUrl = url;
    }

    @Override
    public List<City> lookupCity(String name) {
        Predicate startsWithName = ctx -> ctx.item(Map.class)
                                             .get("name")
                                             .toString()
                                             .regionMatches(true, 0, name, 0, name.length());

        City[] cities = context.read("$[?]", City[].class, Criteria.where("name").matches(startsWithName));
        logger.info(MessageFormat.format("Returned: {0}", (Object) cities));
        return Arrays.asList(cities);
    }

    @Override
    public List<City> lookupCities(List<String> names) {
        return names.stream()
                    .filter(name -> name.length() > 2)
                    .map(this::lookupCity)
                    .flatMap(Collection::stream)
                    .distinct()
                    .limit(20)
                    .collect(toUnmodifiableList());
    }

    @Override
    public List<WeatherReport> getCurrentWeather(int[] ids) {
        RestTemplate template = new RestTemplate(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
        template.setErrorHandler(new WeatherResponseErrorhandler());
        String url = MessageFormat.format(weatherUrl, Arrays.stream(Arrays.copyOfRange(ids, 0, Math.min(ids.length, 20)))
                                                            .mapToObj(Integer::toString).collect(Collectors.joining(",")));
        logger.info(url);
        CurrentWeather currentWeather = template.getForObject(url, CurrentWeather.class);
        logger.info(MessageFormat.format("Current weather: {0}", currentWeather));
        return currentWeather != null ? currentWeather.getList() : Collections.emptyList();
    }
}
