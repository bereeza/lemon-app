package com.lemon.app.model;

import java.util.UUID;

public class SensorData extends Data {

    private double temperature;
    private double humidity;
    private String causeOfAnomaly;

    public SensorData() {
    }

    public SensorData(
            UUID id,
            double temperature,
            double humidity,
            String causeOfAnomaly,
            long createdAt
    ) {
        super(id, createdAt);
        this.temperature = temperature;
        this.humidity = humidity;
        this.causeOfAnomaly = causeOfAnomaly;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public String getCauseOfAnomaly() {
        return causeOfAnomaly;
    }

    public void setCauseOfAnomaly(String causeOfAnomaly) {
        this.causeOfAnomaly = causeOfAnomaly;
    }
}
