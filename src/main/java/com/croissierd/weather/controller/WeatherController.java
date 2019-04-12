package com.croissierd.weather.controller;

import com.croissierd.weather.domain.City;
import com.croissierd.weather.service.RequestLimitExceededException;
import com.croissierd.weather.service.WeatherService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collection;
import java.util.Objects;

@Controller
public class WeatherController {
    private static Logger         logger = LogManager.getLogger(WeatherController.class);

    private final  WeatherService service;

    public WeatherController(WeatherService service) {
        this.service = service;
    }

    @ModelAttribute("search")
    public SearchForm formBackingObject() {
        return new SearchForm();
    }

    @GetMapping("/")
    public String search(@ModelAttribute("search") SearchForm form, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            logger.info("Cities: " + form.getCities());
            int[] ids = form.getCities()
                            .stream()
                            .map(service::lookupCity)
                            .flatMap(Collection::stream)
                            .filter(Objects::nonNull)
                            .mapToInt(City::getId)
                            .toArray();
            if (ids.length > 0) {
                getReports(ids, model);
            }
        }
        model.addAttribute("search", form);
        return "search";
    }

    private void getReports(int[] ids, Model model) {
        try {
            model.addAttribute("reports", service.getCurrentWeather(ids));
        } catch (RequestLimitExceededException e) {
            logger.error(e);
            model.addAttribute("error", "You have exceeded the number of requests allowed in an hour. Please come back later.");
        }
    }
}
