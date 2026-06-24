package com.lemon.app.properties;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sliding-window")
public class AnomalyDetectorProperties {
    @Min(value = 1L, message = "windowSize must be positive")
    private int windowSize;
    private double sigma;

    @Min(value = 0L, message = "minBounds must be non-negative")
    private double minBounds;

    public AnomalyDetectorProperties() {
    }

    public AnomalyDetectorProperties(
            int windowSize,
            double sigma,
            double minBounds
    ) {
        this.windowSize = windowSize;
        this.sigma = sigma;
        this.minBounds = minBounds;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public double getMinBounds() {
        return minBounds;
    }

    public void setMinBounds(double minBounds) {
        this.minBounds = minBounds;
    }
}
