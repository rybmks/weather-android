package com.example.weather.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import com.example.weather.interfaces.IWeatherApi;
import com.example.weather.interfaces.IWeatherCallback;
import com.example.weather.models.Units;
import com.example.weather.models.WeatherData;
import com.example.weather.models.WeatherRepository;
import com.example.weather.models.dto.current.WeatherResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(MockitoJUnitRunner.class)
public class WeatherRepositoryTest {

    @Mock
    IWeatherApi api;

    @Mock
    Call<WeatherResponse> call;

    private WeatherRepository repository;

    @Before
    public void setup() {
        repository = new WeatherRepository(api);
    }

    @Test
    public void getWeatherByCity_success_returnsData() {
        WeatherResponse response = new WeatherResponse();
        response.name = "Kyiv";

        when(api.getCurrentWeather(any(), any(), any()))
                .thenReturn(call);

        doAnswer(invocation -> {
            Callback<WeatherResponse> cb = invocation.getArgument(0);
            cb.onResponse(call, Response.success(response));
            return null;
        }).when(call).enqueue(any());

        repository.getWeatherByCity("Kyiv", Units.METRIC, new IWeatherCallback() {
            @Override
            public void onSuccess(WeatherData data) {
                assertEquals("Kyiv", data.city);
            }

            @Override
            public void onError(String message) {
                fail("Should not fail");
            }
        });
    }

    @Test
    public void getWeatherByCity_noInternet_returnsError() {
        when(api.getCurrentWeather(any(), any(), any()))
                .thenReturn(call);

        doAnswer(invocation -> {
            Callback<WeatherResponse> cb = invocation.getArgument(0);
            cb.onFailure(call, new java.net.UnknownHostException());
            return null;
        }).when(call).enqueue(any());

        repository.getWeatherByCity("Kyiv", Units.METRIC, new IWeatherCallback() {
            @Override
            public void onSuccess(WeatherData data) {
                fail();
            }

            @Override
            public void onError(String message) {
                assertEquals("No internet connection", message);
            }
        });
    }
}
