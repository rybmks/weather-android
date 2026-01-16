package com.example.weather.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.weather.models.DailyForecast;
import com.example.weather.models.dto.forecast.ForecastItem;
import com.example.weather.models.dto.forecast.ForecastResponse;
import com.example.weather.models.dto.shared.MainInfo;
import com.example.weather.models.dto.shared.WeatherItem;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ForecastMapperTest {

    @Test
    public void map_returnsCorrectNumberOfItems() {
        ForecastResponse response = createForecastResponse(3);

        List<DailyForecast> forecast = ForecastMapper.map(response);

        assertEquals(3, forecast.size());
    }

    @Test
    public void map_mapsTemperatureAndDescription() {
        ForecastResponse response = createForecastResponse(1);

        List<DailyForecast> result = ForecastMapper.map(response);

        assertEquals(1, result.size());

        DailyForecast day = result.get(0);

        assertEquals(10.0, day.temperature, 0.01);
        assertEquals("cloudy", day.description);
    }

    @Test
    public void map_handlesEmptyList() {
        ForecastResponse response = new ForecastResponse();
        response.list = new ArrayList<>();

        List<DailyForecast> forecast = ForecastMapper.map(response);

        assertTrue(forecast.isEmpty());
    }

    @Test
    public void map_handlesNullList() {
        ForecastResponse response = new ForecastResponse();
        response.list = null;

        List<DailyForecast> forecast = ForecastMapper.map(response);

        assertTrue(forecast.isEmpty());
    }

    private ForecastResponse createForecastResponse(int count) {
        ForecastResponse response = new ForecastResponse();
        response.list = new ArrayList<>();

        long dayStartUtc = 1767744000L;
        long middayUtc = dayStartUtc + 12 * 60 * 60;

        for (int i = 0; i < count; i++) {
            ForecastItem item = new ForecastItem();

            item.dt = middayUtc + i * 86400;

            MainInfo main = new MainInfo();
            main.temp = 10.0;
            item.main = main;

            WeatherItem weather = new WeatherItem();
            weather.description = "cloudy";
            item.weather = List.of(weather);

            response.list.add(item);
        }

        return response;
    }
}
