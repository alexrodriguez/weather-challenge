package com.croissierd.weather.controller;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchForm {
    private List<String> cities = new ArrayList<>();
}
