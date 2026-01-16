package com.example.weather.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.weather.interfaces.ILocationCallback;

import java.util.List;
import java.util.Locale;

/**
 * Service responsible for managing user location retrieval and reverse geocoding.
 * <p>
 * It handles the entire flow:
 * <ol>
 * <li>Permission checking and requesting.</li>
 * <li>Checking for enabled providers (GPS/Network).</li>
 * <li>Retrieving the "Last Known Location" to avoid unnecessary battery usage.</li>
 * <li>Requesting fresh updates if the cached location is stale.</li>
 * <li>Reverse geocoding (coordinates -> city name) on a background thread.</li>
 * </ol>
 * It also implements a timeout mechanism to prevent indefinite loading.
 * </p>
 */
public class LocationService implements LocationListener {

    public static final int LOCATION_REQUEST_CODE = 1001;

    private static final long TIME_OUT_MS = 8000;

    private final Activity activity;
    private ILocationCallback callback;
    private final android.location.LocationManager systemLocationManager;
    private final Handler timeoutHandler = new Handler(Looper.getMainLooper());
    private boolean isLocationFound = false;

    public LocationService(Activity activity, ILocationCallback callback) {
        this.activity = activity;
        this.callback = callback;
        this.systemLocationManager = (android.location.LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Starts the location retrieval process.
     * <p>
     * If permissions are granted, it immediately starts the scan.
     * If permissions are missing, it triggers the system permission dialog.
     * </p>
     */
    public void startLocationFlow() {
        if (hasPermissions()) {
            startNativeLocationScan();
        } else {
            requestPermissions();
        }
    }

    private boolean hasPermissions() {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_REQUEST_CODE
        );
    }

    @SuppressLint("MissingPermission")
    private void startNativeLocationScan() {
        isLocationFound = false;

        boolean gpsEnabled = systemLocationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        boolean networkEnabled = systemLocationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);

        if (!gpsEnabled && !networkEnabled) {
            callback.onLocationUnavailable();
            return;
        }

        Location lastKnownGPS = null;
        Location lastKnownNetwork = null;

        if (gpsEnabled)
            lastKnownGPS = systemLocationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
        if (networkEnabled)
            lastKnownNetwork = systemLocationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);

        Location bestLastLocation = isBetterLocation(lastKnownGPS, lastKnownNetwork) ? lastKnownGPS : lastKnownNetwork;

        if (bestLastLocation != null && System.currentTimeMillis() - bestLastLocation.getTime() < 5 * 60 * 1000) {
            handleLocationSuccess(bestLastLocation);
            return;
        }

        try {
            if (gpsEnabled) {
                systemLocationManager.requestLocationUpdates(
                        android.location.LocationManager.GPS_PROVIDER,
                        0,
                        0,
                        this
                );
            }
            if (networkEnabled) {
                systemLocationManager.requestLocationUpdates(
                        android.location.LocationManager.NETWORK_PROVIDER,
                        0,
                        0,
                        this
                );
            }

            timeoutHandler.postDelayed(timeoutRunnable, TIME_OUT_MS);

        } catch (Exception e) {
            callback.onLocationUnavailable();
        }
    }

    private final Runnable timeoutRunnable = () -> {
        if (!isLocationFound) {
            stopUpdates();
            callback.onLocationUnavailable();
        }
    };

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (!isLocationFound) {
            isLocationFound = true;
            stopUpdates();
            timeoutHandler.removeCallbacks(timeoutRunnable);
            handleLocationSuccess(location);
        }
    }

    private void stopUpdates() {
        if (systemLocationManager != null) {
            systemLocationManager.removeUpdates(this);
        }
    }

    private void handleLocationSuccess(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        new Thread(() -> {
            String city = resolveCity(lat, lon);
            activity.runOnUiThread(() -> callback.onLocationResolved(lat, lon, city));
        }).start();
    }

    private String resolveCity(double lat, double lon) {
        try {
            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
            List<Address> list = geocoder.getFromLocation(lat, lon, 1);
            if (list != null && !list.isEmpty()) {
                Address address = list.get(0);
                if (address.getLocality() != null) return address.getLocality();
                if (address.getSubAdminArea() != null) return address.getSubAdminArea();
            }
        } catch (Exception ignored) {
        }
        return "Unknown Location";
    }

    private boolean isBetterLocation(Location location1, Location location2) {
        if (location1 == null) return false;
        if (location2 == null) return true;
        return location1.getTime() > location2.getTime();
    }
}