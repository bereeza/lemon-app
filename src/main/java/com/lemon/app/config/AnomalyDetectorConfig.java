package com.lemon.app.config;

import com.lemon.app.properties.AnomalyDetectorProperties;
import com.lemon.app.handler.SlidingWindowAnomalyDetector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnomalyDetectorConfig {

    @Bean
    SlidingWindowAnomalyDetector temperatureDetector(AnomalyDetectorProperties properties) {
        return new SlidingWindowAnomalyDetector(properties);
    }

    @Bean
    SlidingWindowAnomalyDetector humidityDetector(AnomalyDetectorProperties properties) {
        return new SlidingWindowAnomalyDetector(properties);
    }
}
