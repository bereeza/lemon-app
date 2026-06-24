package com.lemon.app.service;

import com.lemon.app.client.DHT11SensorDataClient;
import com.lemon.app.handler.SlidingWindowAnomalyDetector;
import com.lemon.app.model.SensorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class SensorDataService implements DataService<SensorData> {
    private static final Logger logger = LoggerFactory.getLogger(SensorDataService.class);

    private final DHT11SensorDataClient dht11SensorDataClient;
    private final SlidingWindowAnomalyDetector temperatureDetector;
    private final SlidingWindowAnomalyDetector humidityDetector;

    public SensorDataService(
            DHT11SensorDataClient dht11SensorDataClient,
            SlidingWindowAnomalyDetector temperatureDetector,
            SlidingWindowAnomalyDetector humidityDetector
    ) {
        this.dht11SensorDataClient = Objects.requireNonNull(dht11SensorDataClient, "Dht11SensorDataClient is required");
        this.temperatureDetector = Objects.requireNonNull(temperatureDetector, "Temperature detector is required");
        this.humidityDetector = Objects.requireNonNull(humidityDetector, "Humidity detector is required");
    }

    @Override
    public void process(SensorData data, UUID id, long timestamp) throws NumberFormatException {
        try {
            boolean temperatureAnomaly = temperatureDetector.isAnomaly(data.getTemperature());
            boolean humidityAnomaly = humidityDetector.isAnomaly(data.getHumidity());

            if (temperatureAnomaly) {
                // TODO: send notification
            }

            if (humidityAnomaly) {
                // TODO: send notification
            }

            dht11SensorDataClient.save(data, id, timestamp);
            logger.info("UUID: {}; t: {}; humidity: {}; timestamp: {};", id, data.getTemperature(), data.getHumidity(), timestamp);
        } catch (NumberFormatException e) {
            logger.error("Invalid format of data: {}", e.getMessage());
        }
    }
}
