package com.lemon.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemon.app.model.SensorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MqttPublishingServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(MqttPublishingServiceImpl.class);

    private final ObjectMapper objectMapper;
    private final MessageChannel mqttAnomalyOutputChannel;

    public MqttPublishingServiceImpl(
            ObjectMapper objectMapper,
            @Qualifier("mqttAnomalyOutputChannel") MessageChannel mqttAnomalyOutputChannel
    ) {
        this.mqttAnomalyOutputChannel = Objects.requireNonNull(mqttAnomalyOutputChannel, "mqttAnomalyOutputChannel is required");
        this.objectMapper = Objects.requireNonNull(objectMapper, "ObjectMapper is required");
    }

    public void publishAnomaly(SensorData data) {
        try {
            String anomalyJson = objectMapper.writeValueAsString(data);
            mqttAnomalyOutputChannel.send(MessageBuilder.withPayload(anomalyJson).build());
            logger.info("Published anomaly topic: {}", data.getCauseOfAnomaly());
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize anomaly: {}", e.getMessage());
        }
    }
}
