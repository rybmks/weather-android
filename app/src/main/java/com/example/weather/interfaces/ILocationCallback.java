package com.example.weather.interfaces;
//! Interface for location callback

public interface ILocationCallback {
    void onLocationResolved(double lat, double lon, String city);

    void onLocationUnavailable();
}
