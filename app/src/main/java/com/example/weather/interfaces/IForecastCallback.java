package com.example.weather.interfaces;
//! Interface for forecast callback

import com.example.weather.models.DailyForecast;

import java.util.List;

public interface IForecastCallback {
    void onSuccess(List<DailyForecast> data);

    void onError(String message);
}
