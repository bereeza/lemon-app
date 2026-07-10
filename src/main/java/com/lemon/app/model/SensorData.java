package com.lemon.app.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.UUID;

import static com.lemon.app.model.MessageConstants.INCORRECT_HUM;
import static com.lemon.app.model.MessageConstants.INCORRECT_TEMP;

public class SensorData extends Data {

    @Min(value = -50, message = INCORRECT_TEMP)
    @Max(value = 50, message = INCORRECT_TEMP)
    private double temperature;
    
    @Min(value = 0, message = INCORRECT_HUM)
    @Max(value = 100, message = INCORRECT_HUM)
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
