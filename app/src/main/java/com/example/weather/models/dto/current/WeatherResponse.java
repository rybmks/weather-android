package com.example.weather.models.dto.current;

import com.example.weather.models.dto.shared.Clouds;
import com.example.weather.models.dto.shared.Coord;
import com.example.weather.models.dto.shared.MainInfo;
import com.example.weather.models.dto.shared.Sys;
import com.example.weather.models.dto.shared.WeatherItem;
import com.example.weather.models.dto.shared.Wind;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * DTO representing the current weather data response.
 * <p>
 * This class maps the JSON response from the OpenWeatherMap
 * to a Java object using Gson annotations.
 * </p>
 */
public class WeatherResponse {

    @SerializedName("coord")
    public Coord coord;

    @SerializedName("weather")
    public List<WeatherItem> weather;

    @SerializedName("main")
    public MainInfo main;

    @SerializedName("visibility")
    public int visibility;

    @SerializedName("wind")
    public Wind wind;

    @SerializedName("clouds")
    public Clouds clouds;

    @SerializedName("dt")
    public long dt;

    @SerializedName("sys")
    public Sys sys;

    @SerializedName("timezone")
    public int timezone;

    @SerializedName("id")
    public long id;

    @SerializedName("name")
    public String name;

    @SerializedName("cod")
    public int cod;
}

