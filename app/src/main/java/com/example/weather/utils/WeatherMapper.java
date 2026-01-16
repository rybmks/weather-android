package com.example.weather.utils;

import com.example.weather.models.WeatherData;
import com.example.weather.models.dto.current.WeatherResponse;

import java.time.Instant;
import java.time.ZoneId;

/**
 * Utility class for mapping raw API responses to domain models.
 * <p>
 * This class isolates the data transformation logic, ensuring that the rest of the app
 * does not depend on the specific structure of the external API (DTOs).
 * It flattens the nested JSON structure into a clean {@link WeatherData} object.
 * </p>
 */
public final class WeatherMapper {

    private WeatherMapper() {
    }

    public static WeatherData map(WeatherResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("WeatherResponse is null");
        }

        WeatherData data = new WeatherData();

        data.city = response.name;
        data.country = response.sys != null ? response.sys.country : null;

        if (response.main != null) {
            data.temperature = response.main.temp;
            data.feelsLike = response.main.feels_like;
            data.humidity = response.main.humidity;
        }

        if (response.wind != null) {
            data.windSpeed = response.wind.speed;
        }

        if (response.weather != null && !response.weather.isEmpty()) {
            data.description = response.weather.get(0).description;
            data.icon = response.weather.get(0).icon;
            data.weatherMain = response.weather.get(0).main;
        }

        if (response.sys != null) {
            data.sunrise = response.sys.sunrise;
            data.sunset = response.sys.sunset;
        }

        if (response.dt > 0) {
            long dt = response.dt;
            int timezone = response.timezone;

            Instant instant = Instant.ofEpochSecond(dt + timezone);

            data.date = instant.atZone(ZoneId.of("UTC")).toLocalDateTime();
        }

        return data;
    }
}
