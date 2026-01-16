package com.example.weather.controllers;

import com.example.weather.interfaces.IForecastCallback;
import com.example.weather.interfaces.IWeatherCallback;
import com.example.weather.interfaces.IWeatherView;
import com.example.weather.models.DailyForecast;
import com.example.weather.models.Units;
import com.example.weather.models.WeatherData;
import com.example.weather.models.WeatherRepository;

import java.util.List;


public class WeatherController {

    private final IWeatherView view;
    private final WeatherRepository repository;

    public WeatherController(IWeatherView view, WeatherRepository repository) {
        this.view = view;
        this.repository = repository;
    }


    public void loadWeatherByCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            view.showError("City name is empty");
            return;
        }

        view.showLoading();
        loadCurrentByCity(city);
        loadForecastByCity(city);
    }

    public void loadWeatherByCords(double lat, double lon) {
        view.showLoading();
        loadCurrentByCords(lat, lon);
        loadForecastByCords(lat, lon);
    }


    private void loadCurrentByCity(String city) {
        repository.getWeatherByCity(
                city,
                Units.METRIC,
                new IWeatherCallback() {
                    @Override
                    public void onSuccess(WeatherData data) {
                        view.showWeather(data);
                    }

                    @Override
                    public void onError(String message) {
                        view.showError(message);
                    }
                }
        );
    }

    private void loadCurrentByCords(double lat, double lon) {
        repository.getWeatherByCords(
                lat,
                lon,
                Units.METRIC,
                new IWeatherCallback() {
                    @Override
                    public void onSuccess(WeatherData data) {
                        view.showWeather(data);
                    }

                    @Override
                    public void onError(String message) {
                        view.showError(message);
                    }
                }
        );
    }


    private void loadForecastByCity(String city) {
        repository.getForecastByCity(
                city,
                Units.METRIC,
                new IForecastCallback() {
                    @Override
                    public void onSuccess(List<DailyForecast> forecast) {
                        view.showForecast(forecast);
                    }

                    @Override
                    public void onError(String message) {
                    }
                }
        );
    }

    private void loadForecastByCords(double lat, double lon) {
        repository.getForecastByCords(
                lat,
                lon,
                Units.METRIC,
                new IForecastCallback() {
                    @Override
                    public void onSuccess(List<DailyForecast> forecast) {
                        view.showForecast(forecast);
                    }

                    @Override
                    public void onError(String message) {
                    }
                }
        );
    }
}
