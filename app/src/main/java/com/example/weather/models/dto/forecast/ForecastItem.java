package com.example.weather.models.dto.forecast;

import com.example.weather.models.dto.shared.MainInfo;
import com.example.weather.models.dto.shared.WeatherItem;

import java.util.List;


/**
 * DTO representing the current weather forecast item.
 */
public class ForecastItem {
    public long dt;
    public MainInfo main;
    public List<WeatherItem> weather;
}
