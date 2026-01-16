package com.example.weather.utils;

import com.example.weather.R;

/**
 * Utility class for mapping weather conditions to Android drawable resources.
 * <p>
 * This class acts as a bridge between the textual weather descriptions from the API
 * (e.g., "Clear", "Rain") and the visual icons stored in the application's resources.
 * </p>
 */
public class WeatherIconMapper {

    public static int getIcon(String weatherMain) {
        if (weatherMain == null) return R.drawable.ic_weather_cloudy;

        switch (weatherMain.toLowerCase()) {
            case "clear":
                return R.drawable.ic_weather_sunny;

            case "rain":
            case "drizzle":
                return R.drawable.ic_weather_rain;

            case "thunderstorm":
                return R.drawable.ic_weather_thunder;

            case "snow":
                return R.drawable.ic_weather_snow;

            case "fog":
            case "mist":
            case "haze":
                return R.drawable.ic_weather_fog;

            case "clouds":
            default:
                return R.drawable.ic_weather_cloudy;
        }
    }
}