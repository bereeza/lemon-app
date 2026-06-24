package com.lemon.app.config;

import com.lemon.app.properties.AnomalyDetectorProperties;
import com.lemon.app.handler.SlidingWindowAnomalyDetector;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AnomalyDetectorConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    SlidingWindowAnomalyDetector temperatureDetector(AnomalyDetectorProperties properties) {
        return new SlidingWindowAnomalyDetector(properties);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    SlidingWindowAnomalyDetector humidityDetector(AnomalyDetectorProperties properties) {
        return new SlidingWindowAnomalyDetector(properties);
    }
}
