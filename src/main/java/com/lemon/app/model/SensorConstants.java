package com.lemon.app.model;

public enum SensorConstants {
    ID("id"),
    TEMPERATURE_C("temperature_c"),
    HUMIDITY("humidity");

    private final String value;

    SensorConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
