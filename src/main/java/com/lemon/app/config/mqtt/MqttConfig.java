package com.lemon.app.config.mqtt;

import com.lemon.app.properties.MqttBrokerProperties;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.lemon.app.handler.MqttMessageHandler;

import java.util.Objects;

@Configuration
public class MqttConfig {
    private final MqttBrokerProperties mqttBrokerProperties;

    public MqttConfig(MqttBrokerProperties mqttBrokerProperties) {
        this.mqttBrokerProperties = Objects.requireNonNull(mqttBrokerProperties, "MqttBrokerProperties cannot be null.");
    }

    @Bean
    MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{mqttBrokerProperties.getBrokerUri()});
        options.setAutomaticReconnect(false);
        options.setCleanSession(true);
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                mqttBrokerProperties.getClientId() + "-" + System.currentTimeMillis(),
                mqttClientFactory(),
                mqttBrokerProperties.getBrokerUri()
        );

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(mqttBrokerProperties.getQos());
        adapter.setOutputChannel(mqttInputChannel());
        adapter.setErrorChannel(mqttErrorChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    MessageHandler handler(MqttMessageHandler mqttMessageHandler) {
        return mqttMessageHandler;
    }

    @Bean
    MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    MessageChannel mqttErrorChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttAnomalyOutputChannel")
    MessageHandler mqttAnomalyPublisher() {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(
                mqttBrokerProperties.getClientId() + "-anomaly-pub",
                mqttClientFactory()
        );

        handler.setAsync(true);
        handler.setDefaultTopic(mqttBrokerProperties.getTopicSensorAnomalies());
        handler.setConverter(new DefaultPahoMessageConverter());
        return handler;
    }

    @Bean
    MessageChannel mqttAnomalyOutputChannel() {
        return new DirectChannel();
    }
}
