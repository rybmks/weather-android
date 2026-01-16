package com.example.weather.interfaces;

import com.example.weather.models.WeatherData;
//! Interface for weather callback

public interface IWeatherCallback {
    void onSuccess(WeatherData data);

    void onError(String message);
}
