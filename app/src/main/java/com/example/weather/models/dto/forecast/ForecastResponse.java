package com.example.weather.models.dto.forecast;

import java.util.List;

/**
 * DTO representing the forecast data response.
 * <p>
 * This class maps the JSON response from the OpenWeatherMap
 * to a Java object using Gson annotations.
 * </p>
 */
public class ForecastResponse {
    public List<ForecastItem> list;
}
