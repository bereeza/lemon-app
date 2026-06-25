package com.lemon.app.model;

public enum SensorConstants {
    ID("id"),
    TEMPERATURE_C("temperature_c"),
    HUMIDITY("humidity"),
    CAUSE_OF_ANOMALY("cause_of_anomaly"),
    TIMESTAMP("timestamp");

    private final String value;

    SensorConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
