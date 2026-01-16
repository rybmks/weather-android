package com.example.weather.views.CityInputActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weather.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class CityInputActivity extends AppCompatActivity {

    public static final String EXTRA_CITY = "extra_city";
    public static final String EXTRA_REQUEST_LOCATION = "extra_request_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_input);

        EditText editCity = findViewById(R.id.editCity);

        MaterialButton btnUpdateLocation = findViewById(R.id.btnUpdateLocation);
        MaterialButton btnConfirm = findViewById(R.id.btnConfirmCity);

        btnConfirm.setOnClickListener(v -> {
            String city = editCity.getText().toString().trim();

            if (city.isEmpty()) {
                Snackbar.make(v, "Please enter a city name", Snackbar.LENGTH_SHORT).show();
                return;
            }

            Intent result = new Intent();
            result.putExtra(EXTRA_CITY, city);
            setResult(RESULT_OK, result);
            finish();
        });

        btnUpdateLocation.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra(EXTRA_REQUEST_LOCATION, true);
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
