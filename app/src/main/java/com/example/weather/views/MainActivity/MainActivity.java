package com.example.weather.views.MainActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.controllers.WeatherController;
import com.example.weather.interfaces.ILocationCallback;
import com.example.weather.interfaces.IWeatherView;
import com.example.weather.models.DailyForecast;
import com.example.weather.models.WeatherData;
import com.example.weather.models.WeatherRepository;
import com.example.weather.services.LocationService;
import com.example.weather.utils.WeatherIconMapper;
import com.example.weather.views.CityInputActivity.CityInputActivity;
import com.google.android.material.button.MaterialButton;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements IWeatherView, ILocationCallback {
    private ImageView imageWeatherIcon;
    private TextView textStatus;
    private TextView textCity;
    private TextView textTemperature;
    private TextView textDescription;
    private TextView textWind;
    private TextView textHumidity;
    private TextView textDate;
    private ForecastAdapter forecastAdapter;
    private LocationService locationManager;

    private WeatherController controller;
    private String resolvedCity;

    private final ActivityResultLauncher<Intent> cityInputLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() != RESULT_OK || result.getData() == null) {
                            return;
                        }

                        Intent data = result.getData();

                        if (data.getBooleanExtra("extra_request_location", false)) {
                            showLoading();
                            locationManager.startLocationFlow();
                            return;
                        }

                        String city = data.getStringExtra("extra_city");
                        if (city != null) {
                            resolvedCity = city;
                            controller.loadWeatherByCity(city);
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initViews();
        initController();

        locationManager = new LocationService(this, this);
        locationManager.startLocationFlow();
    }

    private void initViews() {
        textCity = findViewById(R.id.textCity);
        textTemperature = findViewById(R.id.textTemperature);
        textDescription = findViewById(R.id.textDescription);
        textWind = findViewById(R.id.textWind);
        textHumidity = findViewById(R.id.textHumidity);
        textStatus = findViewById(R.id.textStatus);
        textDate = findViewById(R.id.textDate);
        imageWeatherIcon = findViewById(R.id.imageWeatherIcon);

        RecyclerView recyclerForecast = findViewById(R.id.recyclerForecast);
        recyclerForecast.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        forecastAdapter = new ForecastAdapter();
        recyclerForecast.setAdapter(forecastAdapter);

        MaterialButton btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(v -> {
            resolvedCity = null;
            showLoading();
            locationManager.startLocationFlow();
        });

        MaterialButton btnChangeCity = findViewById(R.id.btnChangeCity);
        btnChangeCity.setOnClickListener(v -> openCityInput());
    }

    private void initController() {
        WeatherRepository repository =
                WeatherRepository.defaultRepository();

        controller = new WeatherController(this, repository);
    }

    @Override
    public void showLoading() {
        textCity.setText("Locating...");
        textTemperature.setText("--°");
        textDescription.setText("");
    }

    @Override
    public void showError(String message) {
        textCity.setText("Error");
        textDescription.setText(message);
    }

    @Override
    public void showWeather(WeatherData weatherData) {
        if (resolvedCity != null) {
            weatherData.city = resolvedCity;
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("EEEE, d MMM", Locale.ENGLISH);

        String formatted = weatherData.date.format(formatter);

        textDate.setText(formatted);
        textStatus.setText(weatherData.city);
        textCity.setText(weatherData.city);

        textTemperature.setText(weatherData.temperature + "°");
        textDescription.setText(weatherData.description);
        textWind.setText(weatherData.windSpeed + " m/s");
        textHumidity.setText(weatherData.humidity + "%");
        int iconRes = WeatherIconMapper.getIcon(weatherData.weatherMain);
        imageWeatherIcon.setImageResource(iconRes);
    }

    @Override
    public void showForecast(List<DailyForecast> forecast) {
        forecastAdapter.setItems(forecast);
    }

    @Override
    public void onLocationResolved(double lat, double lon, String city) {
        resolvedCity = city;
        controller.loadWeatherByCords(lat, lon);
    }

    private void openCityInput() {
        Intent intent = new Intent(this, CityInputActivity.class);
        cityInputLauncher.launch(intent);
    }

    @Override
    public void onLocationUnavailable() {
        new AlertDialog.Builder(this)
                .setTitle("Location unavailable")
                .setMessage("We couldn’t determine your location.\nPlease enter the city manually or\ncheck your location permissions\nand press 'Update Location' button.")
                .setCancelable(false)
                .setPositiveButton("Enter city", (dialog, which) -> {
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @androidx.annotation.NonNull String[] permissions,
            @androidx.annotation.NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LocationService.LOCATION_REQUEST_CODE) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                showLoading();
                locationManager.startLocationFlow();

            } else {
                onLocationUnavailable();
            }
        }
    }
}