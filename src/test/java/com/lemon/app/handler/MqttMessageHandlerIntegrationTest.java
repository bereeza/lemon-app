package com.lemon.app.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemon.app.model.SensorData;
import com.lemon.app.service.SensorDataServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("MQTT Message Handler Integration Tests")
class MqttMessageHandlerIntegrationTest {

    private MqttMessageHandler mqttMessageHandler;
    private ObjectMapper objectMapper;
    private SensorDataServiceImpl sensorDataServiceImpl;

    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        sensorDataServiceImpl = mock(SensorDataServiceImpl.class);
        mqttMessageHandler = new MqttMessageHandler(objectMapper, sensorDataServiceImpl);

        Field dataTopicField = MqttMessageHandler.class.getDeclaredField("dataTopic");
        dataTopicField.setAccessible(true);
        dataTopicField.set(mqttMessageHandler, "sensor/data");
    }

    @Test
    @DisplayName("Should process valid MQTT message successfully.")
    void shouldProcessValidMqttMessageSuccessfullyTest() {
        String value = """
                {
                    "temperature": 25.5,
                    "humidity": 60.0
                }
                """;

        Map<String, Object> headers = new HashMap<>();
        headers.put("mqtt_receivedTopic", "sensor/data");
        headers.put("id", UUID.randomUUID());
        headers.put("timestamp", System.currentTimeMillis());

        Message<?> message = MessageBuilder.createMessage(value, new MessageHeaders(headers));

        mqttMessageHandler.handleMessage(message);

        verify(sensorDataServiceImpl, times(1))
                .process(any(SensorData.class));
    }

    @Test
    @DisplayName("Should ignore message from different topic.")
    void shouldIgnoreMessageFromDifferentTopicTest() {
        String value = """
                {
                    "temperature": 25.5,
                    "humidity": 60.0
                }
                """;

        Map<String, Object> headers = new HashMap<>();
        headers.put("mqtt_receivedTopic", "abc/topic");
        headers.put("id", UUID.randomUUID());
        headers.put("timestamp", System.currentTimeMillis());

        Message<?> message = MessageBuilder.createMessage(value, new MessageHeaders(headers));

        mqttMessageHandler.handleMessage(message);

        // wrong topic should not be processed
        verify(sensorDataServiceImpl, never())
                .process(any(SensorData.class));
    }
}
