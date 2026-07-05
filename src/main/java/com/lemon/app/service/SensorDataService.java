package com.lemon.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemon.app.client.DHT11SensorDataClient;
import com.lemon.app.handler.SlidingWindowAnomalyDetector;
import com.lemon.app.model.SensorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SensorDataService implements DataService<SensorData> {
    private static final Logger logger = LoggerFactory.getLogger(SensorDataService.class);

    private static final String TEMP_ANOMALY = "Temperature anomaly:";
    private static final String HUMIDITY_ANOMALY = "Humidity anomaly:";

    private final DHT11SensorDataClient dht11SensorDataClient;
    private final SlidingWindowAnomalyDetector temperatureDetector;
    private final SlidingWindowAnomalyDetector humidityDetector;
    private final MessageChannel mqttAnomalyOutputChannel;
    private final ObjectMapper objectMapper;

    public SensorDataService(
            DHT11SensorDataClient dht11SensorDataClient,
            SlidingWindowAnomalyDetector temperatureDetector,
            SlidingWindowAnomalyDetector humidityDetector,
            @Qualifier("mqttAnomalyOutputChannel") MessageChannel mqttAnomalyOutputChannel,
            ObjectMapper objectMapper
    ) {
        this.dht11SensorDataClient = Objects.requireNonNull(dht11SensorDataClient, "Dht11SensorDataClient is required");
        this.temperatureDetector = Objects.requireNonNull(temperatureDetector, "Temperature detector is required");
        this.humidityDetector = Objects.requireNonNull(humidityDetector, "Humidity detector is required");
        this.mqttAnomalyOutputChannel = Objects.requireNonNull(mqttAnomalyOutputChannel, "mqttAnomalyOutputChannel is required");
        this.objectMapper = Objects.requireNonNull(objectMapper, "ObjectMapper is required");
    }

    @Override
    public void process(SensorData data) throws NumberFormatException {
        try {
            boolean temperatureAnomaly = temperatureDetector.isAnomaly(data.getTemperature());
            boolean humidityAnomaly = humidityDetector.isAnomaly(data.getHumidity());
            boolean hasAnomaly = false;

            if (temperatureAnomaly) {
                data.setCauseOfAnomaly(TEMP_ANOMALY + data.getTemperature());
                hasAnomaly = true;
            }

            if (humidityAnomaly || data.getHumidity() < 0) {
                data.setCauseOfAnomaly(HUMIDITY_ANOMALY + data.getHumidity());
                hasAnomaly = true;
            }

            dht11SensorDataClient.save(data);
            logger.info("UUID: {}; t: {}; humidity: {}; timestamp: {};",
                    data.getId(), data.getTemperature(), data.getHumidity(), data.getCreatedAt());

            if (hasAnomaly) {
                publishAnomaly(data);
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid format of data: {}", e.getMessage());
        }
    }

    private void publishAnomaly(SensorData data) {
        try {
            String anomalyJson = objectMapper.writeValueAsString(data);
            mqttAnomalyOutputChannel.send(MessageBuilder.withPayload(anomalyJson).build());
            logger.info("Published anomaly topic: {}", data.getCauseOfAnomaly());
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize anomaly: {}", e.getMessage());
        }
    }
}
