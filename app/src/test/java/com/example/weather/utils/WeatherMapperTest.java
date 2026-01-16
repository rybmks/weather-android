package com.example.weather.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.example.weather.models.WeatherData;
import com.example.weather.models.dto.current.WeatherResponse;
import com.example.weather.models.dto.shared.MainInfo;
import com.example.weather.models.dto.shared.WeatherItem;
import com.example.weather.models.dto.shared.Wind;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;

public class WeatherMapperTest {

    @Test
    public void map_mapsBasicFieldsCorrectly() {
        WeatherResponse response = createValidResponse();

        WeatherData data = WeatherMapper.map(response);

        assertEquals("Kyiv", data.city);
        assertEquals(12.5, data.temperature, 0.01);
        assertEquals("overcast clouds", data.description);
        assertEquals(5.2, data.windSpeed, 0.01);
        assertEquals(80, data.humidity);
    }

    @Test
    public void map_mapsDateCorrectlyFromUnixTime() {
        WeatherResponse response = createValidResponse();
        response.dt = 1767800660L;

        WeatherData data = WeatherMapper.map(response);

        LocalDateTime expected =
                LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(1767800660L),
                        ZoneOffset.UTC
                );

        assertEquals(expected, data.date);
    }

    @Test
    public void map_handlesMissingWeatherGracefully() {
        WeatherResponse response = createValidResponse();
        response.weather = null;

        WeatherData data = WeatherMapper.map(response);

        assertNull(data.description);
    }

    @Test
    public void map_handlesEmptyWeatherList() {
        WeatherResponse response = createValidResponse();
        response.weather = Collections.emptyList();

        WeatherData data = WeatherMapper.map(response);

        assertEquals(null, data.description);
    }


    private WeatherResponse createValidResponse() {
        WeatherResponse response = new WeatherResponse();

        response.name = "Kyiv";
        response.dt = 1767800660L;

        MainInfo main = new MainInfo();
        main.temp = 12.5;
        main.humidity = 80;
        response.main = main;

        WeatherItem weather = new WeatherItem();
        weather.description = "overcast clouds";
        response.weather = Collections.singletonList(weather);

        Wind wind = new Wind();
        wind.speed = 5.2;
        response.wind = wind;

        return response;
    }
}
