package com.lemon.app.handler;

import com.lemon.app.properties.AnomalyDetectorProperties;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SlidingWindowAnomalyDetector {
    private final Queue<Double> window = new ConcurrentLinkedDeque<>();
    private final AnomalyDetectorProperties properties;

    public SlidingWindowAnomalyDetector(AnomalyDetectorProperties properties) {
        this.properties = Objects.requireNonNull(properties, "AnomalyDetectorProperties cannot be null.");
    }

    /*
     * Checks if the given value is an anomaly based on the sliding window method.
     *
     * @param value The value to check for anomaly.
     * @return true if the value is an anomaly, false otherwise.
     */
    public synchronized boolean isAnomaly(double value) {
        if (window.size() < properties.getWindowSize()) {
            window.offer(value);
            return false;
        }

        double mean = 0.0;
        double m2 = 0.0;
        int count = 0;
        for (double i : window) {
            count++;
            double delta = i - mean;
            mean += delta / count;
            m2 += delta * (i - mean);
        }

        double variance = (count > 1) ? m2 / (count - 1) : 0.0;
        double standardDeviation = Math.sqrt(variance);

        boolean isAnomaly = Math.abs(value - mean) > properties.getSigma() * standardDeviation + properties.getMinBounds();

        window.poll();
        window.offer(value);
        return isAnomaly;
    }
}
