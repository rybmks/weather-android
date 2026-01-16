package com.example.weather.models;

import androidx.annotation.NonNull;

import com.example.weather.BuildConfig;
import com.example.weather.interfaces.IForecastCallback;
import com.example.weather.interfaces.IWeatherApi;
import com.example.weather.interfaces.IWeatherCallback;
import com.example.weather.models.dto.current.WeatherResponse;
import com.example.weather.models.dto.forecast.ForecastResponse;
import com.example.weather.utils.ForecastMapper;
import com.example.weather.utils.WeatherMapper;

import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository class responsible for handling data operations related to weather.
 * <p>
 * This class acts as a single source of truth for weather data, abstracting the
 * network layer from the UI or Domain layer. It handles:
 * <ul>
 * <li>Asynchronous API calls via Retrofit.</li>
 * <li>Response validation and error handling (HTTP codes, network issues).</li>
 * <li>Mapping raw DTOs to Domain models using a mapper.</li>
 * </ul>
 * </p>
 */
public class WeatherRepository {

    private final IWeatherApi api;

    public WeatherRepository(IWeatherApi api) {
        this.api = api;
    }

    public static WeatherRepository defaultRepository() {
        return new WeatherRepository(IWeatherApi.createDefault());
    }

    public void getWeatherByCity(
            String city,
            Units units,
            IWeatherCallback callback
    ) {
        api.getCurrentWeather(city, BuildConfig.WEATHER_API_KEY, units.getValue())
                .enqueue(new Callback<>() {

                    @Override
                    public void onResponse(
                            @NonNull Call<WeatherResponse> call,
                            @NonNull Response<WeatherResponse> response
                    ) {
                        WeatherResponse body = validateResponse(response, callback);
                        if (body == null) return;

                        callback.onSuccess(
                                WeatherMapper.map(body)
                        );
                    }

                    @Override
                    public void onFailure(
                            @NonNull Call<WeatherResponse> call,
                            @NonNull Throwable t
                    ) {
                        handleNetworkError(t, callback);
                    }
                });
    }

    public void getForecastByCity(
            String city,
            Units units,
            IForecastCallback callback
    ) {
        api.getForecastByCity(city, BuildConfig.WEATHER_API_KEY, units.getValue())
                .enqueue(new Callback<>() {

                    @Override
                    public void onResponse(
                            @NonNull Call<ForecastResponse> call,
                            @NonNull Response<ForecastResponse> response
                    ) {
                        ForecastResponse body = validateResponse(response, callback);
                        if (body == null) return;

                        callback.onSuccess(
                                ForecastMapper.map(body)
                        );
                    }

                    @Override
                    public void onFailure(
                            @NonNull Call<ForecastResponse> call,
                            @NonNull Throwable t
                    ) {
                        callback.onError("Network error");
                    }
                });
    }

    public void getWeatherByCords(
            double lat,
            double lon,
            Units units,
            IWeatherCallback callback
    ) {
        api.getCurrentWeatherByCoords(
                        lat,
                        lon,
                        BuildConfig.WEATHER_API_KEY,
                        units.getValue()
                )
                .enqueue(new Callback<>() {

                    @Override
                    public void onResponse(
                            @NonNull Call<WeatherResponse> call,
                            @NonNull Response<WeatherResponse> response
                    ) {
                        WeatherResponse body = validateResponse(response, callback);
                        if (body == null) return;

                        callback.onSuccess(
                                WeatherMapper.map(body)
                        );
                    }

                    @Override
                    public void onFailure(
                            @NonNull Call<WeatherResponse> call,
                            @NonNull Throwable t
                    ) {
                        handleNetworkError(t, callback);
                    }
                });
    }

    public void getForecastByCords(
            double lat,
            double lon,
            Units units,
            IForecastCallback callback
    ) {
        api.getForecastByCoords(
                        lat,
                        lon,
                        BuildConfig.WEATHER_API_KEY,
                        units.getValue()
                )
                .enqueue(new Callback<>() {

                    @Override
                    public void onResponse(
                            @NonNull Call<ForecastResponse> call,
                            @NonNull Response<ForecastResponse> response
                    ) {
                        ForecastResponse body = validateResponse(response, callback);
                        if (body == null) return;

                        callback.onSuccess(
                                ForecastMapper.map(body)
                        );
                    }

                    @Override
                    public void onFailure(
                            @NonNull Call<ForecastResponse> call,
                            @NonNull Throwable t
                    ) {
                        callback.onError("Network error");
                    }
                });
    }

    private <T> T validateResponse(
            Response<T> response,
            IWeatherCallback callback
    ) {
        if (!response.isSuccessful()) {
            if (response.code() == 404) {
                callback.onError("City not found");
            } else {
                callback.onError("Server error: " + response.code());
            }
            return null;
        }

        if (response.body() == null) {
            callback.onError("Empty response from server");
            return null;
        }

        return response.body();
    }

    private <T> T validateResponse(
            Response<T> response,
            IForecastCallback callback
    ) {
        if (!response.isSuccessful()) {
            callback.onError("Server error: " + response.code());
            return null;
        }

        if (response.body() == null) {
            callback.onError("Empty forecast response");
            return null;
        }

        return response.body();
    }

    private void handleNetworkError(Throwable t, IWeatherCallback callback) {
        if (t instanceof UnknownHostException) {
            callback.onError("No internet connection");
        } else {
            callback.onError("Network error");
        }
    }
}
