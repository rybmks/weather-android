package com.example.weather.utils;

import com.example.weather.models.DailyForecast;
import com.example.weather.models.dto.forecast.ForecastItem;
import com.example.weather.models.dto.forecast.ForecastResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Utility class responsible for transforming raw API forecast data into a simplified domain model.
 * <p>
 * This mapper selects a single data point per day
 * to represent the daily forecast.
 * </p>
 */
public class ForecastMapper {

    public static List<DailyForecast> map(ForecastResponse response) {
        List<DailyForecast> result = new ArrayList<>();

        if (response == null || response.list == null) {
            return result;
        }

        for (ForecastItem item : response.list) {
            if (isMidday(item.dt)) {
                DailyForecast forecast = new DailyForecast();
                forecast.day = formatDay(item.dt);
                forecast.temperature = item.main.temp;
                forecast.description = item.weather.get(0).description;
                forecast.weatherMain = item.weather.get(0).main;

                result.add(forecast);
            }
        }

        return result;
    }

    private static boolean isMidday(long unixSeconds) {
        long secondsInDay = unixSeconds % 86400;
        long midday = 12 * 60 * 60; // 12:00

        return secondsInDay > midday - 3600
                && secondsInDay < midday + 3600;
    }

    private static String formatDay(long unixSeconds) {
        return new SimpleDateFormat("EEE", Locale.ENGLISH)
                .format(new Date(unixSeconds * 1000));
    }
}
