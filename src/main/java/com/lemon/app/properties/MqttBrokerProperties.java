package com.lemon.app.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttBrokerProperties {

    private String brokerUri;
    private String clientId;
    private String topicSensorData;
    private String topicSensorAnomalies;
    private String username;
    private String password;
    private int qos;

    public MqttBrokerProperties() {
    }

    public MqttBrokerProperties(
            String brokerUri,
            String clientId,
            String topicSensorData,
            String topicSensorAnomalies,
            String username,
            String password,
            int qos
    ) {
        this.brokerUri = Objects.requireNonNull(brokerUri, "Broker URI cannot be null.");
        this.clientId = Objects.requireNonNull(clientId, "Client ID cannot be null.");
        this.topicSensorData = Objects.requireNonNull(topicSensorData, "Topics sensor data cannot be null.");
        this.topicSensorAnomalies = Objects.requireNonNull(topicSensorAnomalies, "Topic sensor anomalies cannot be null.");
        this.username = Objects.requireNonNull(username, "Username cannot be null.");
        this.password = Objects.requireNonNull(password, "Password cannot be null.");
        this.qos = qos;
    }

    public String getBrokerUri() {
        return brokerUri;
    }

    public void setBrokerUri(String brokerUri) {
        this.brokerUri = brokerUri;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTopicSensorData() {
        return topicSensorData;
    }

    public void setTopicSensorData(String topicSensorData) {
        this.topicSensorData = topicSensorData;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public String getTopicSensorAnomalies() {
        return topicSensorAnomalies;
    }

    public void setTopicSensorAnomalies(String topicSensorAnomalies) {
        this.topicSensorAnomalies = topicSensorAnomalies;
    }
}
