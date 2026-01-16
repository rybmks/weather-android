package com.example.weather.models;

/**
 * Enumeration representing the units of measurement for the weather API requests.
 */
public enum Units {
    METRIC("metric"),
    IMPERIAL("imperial"),
    STANDARD("standard");

    private final String value;

    Units(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
