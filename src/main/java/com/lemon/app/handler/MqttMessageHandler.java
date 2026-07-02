package com.lemon.app.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemon.app.exception.SensorDataParseException;
import com.lemon.app.model.Data;
import com.lemon.app.model.SensorData;
import com.lemon.app.service.SensorDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RegisterReflectionForBinding(classes = {SensorData.class, Data.class})
public class MqttMessageHandler implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MqttMessageHandler.class);
    private static final String MQTT_RECEIVED_TOPIC = "mqtt_receivedTopic";

    @Value("${mqtt.topic-sensor-data}")
    private String dataTopic;

    private final ObjectMapper objectMapper;
    private final SensorDataService sensorDataService;

    public MqttMessageHandler(
            ObjectMapper objectMapper,
            SensorDataService sensorDataService) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "ObjectMapper cannot be null.");
        this.sensorDataService = Objects.requireNonNull(sensorDataService, "SensorDataService cannot be null.");
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = message.getHeaders().get(MQTT_RECEIVED_TOPIC, String.class);
        if (!Objects.equals(dataTopic, topic)) {
            logger.warn("Topic mismatch. Expected: {}, Got: {}", dataTopic, topic);
            return;
        }

        try {
            SensorData sensorData = objectMapper.readValue(message.getPayload().toString(), SensorData.class);
            UUID uuid = message.getHeaders().get("id", UUID.class);
            Long timestamp = message.getHeaders().get("timestamp", Long.class);
            sensorData.setId(uuid);
            sensorData.setCreatedAt(timestamp);

            sensorDataService.process(sensorData);
        } catch (JsonProcessingException e) {
            logger.error("Failed to process message from topic '{}': {}", topic, message.getPayload());
            throw new SensorDataParseException(
                    String.format("Failed to process message from topic '%s': %s", topic, message.getPayload())
            );
        }
    }
}
