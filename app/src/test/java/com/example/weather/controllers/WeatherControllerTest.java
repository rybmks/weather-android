package com.example.weather.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.weather.interfaces.IWeatherCallback;
import com.example.weather.interfaces.IWeatherView;
import com.example.weather.models.WeatherData;
import com.example.weather.models.WeatherRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WeatherControllerTest {

    @Mock
    IWeatherView view;

    @Mock
    WeatherRepository repository;

    private WeatherController controller;

    @Before
    public void setup() {
        controller = new WeatherController(view, repository);
    }

    @Test
    public void loadWeatherByCity_emptyCity_showsError() {
        controller.loadWeatherByCity("");

        verify(view).showError("City name is empty");
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void loadWeatherByCity_success_callsShowWeather() {
        WeatherData data = new WeatherData();
        data.city = "Kyiv";

        doAnswer(invocation -> {
            IWeatherCallback callback = invocation.getArgument(2);
            callback.onSuccess(data);
            return null;
        }).when(repository).getWeatherByCity(
                eq("Kyiv"),
                any(),
                any()
        );

        controller.loadWeatherByCity("Kyiv");

        verify(view).showLoading();
        verify(view).showWeather(data);
    }

    @Test
    public void loadWeatherByCity_error_callsShowError() {
        doAnswer(invocation -> {
            IWeatherCallback callback = invocation.getArgument(2);
            callback.onError("Error");
            return null;
        }).when(repository).getWeatherByCity(
                eq("Kyiv"),
                any(),
                any()
        );

        controller.loadWeatherByCity("Kyiv");

        verify(view).showError("Error");
    }
}
