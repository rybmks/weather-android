package com.example.weather.models;

import java.time.LocalDateTime;

/**
 * Domain model representing the consolidated weather information for a specific location.
 * <p>
 * Unlike the raw DTO, this class contains flattened and processed data
 * ready for UI binding or internal logic usage.
 * </p>
 */
public class WeatherData {

    public String city;
    public String country;

    public double temperature;
    public double feelsLike;

    public int humidity;

    public String description;
    public String icon;

    public LocalDateTime date;

    public double windSpeed;

    public long sunrise;
    public long sunset;
    public String weatherMain;
}