package com.example.weather.interfaces;

import com.example.weather.models.DailyForecast;
import com.example.weather.models.WeatherData;

import java.util.List;
//! Interface for weather view

public interface IWeatherView {

    void showLoading();

    void showError(String message);

    void showWeather(WeatherData weatherData);

    void showForecast(List<DailyForecast> forecast);
}
