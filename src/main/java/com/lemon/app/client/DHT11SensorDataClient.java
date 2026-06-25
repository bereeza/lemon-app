package com.lemon.app.client;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.lemon.app.model.SensorData;
import com.lemon.app.properties.BigTableDataProperties;
import org.springframework.stereotype.Repository;

import java.util.Objects;

import static com.lemon.app.model.SensorConstants.*;

@Repository
public class DHT11SensorDataClient {

    private static final String PREFIX = "dht11";
    private static final String DELIMITER = "#";

    private final BigtableDataClient dataClient;
    private final BigTableDataProperties bigTableDataProperties;

    public DHT11SensorDataClient(
            BigtableDataClient dataClient,
            BigTableDataProperties bigTableDataProperties
    ) {
        this.dataClient = Objects.requireNonNull(dataClient, "BigtableDataClient cannot be null.");
        this.bigTableDataProperties = Objects.requireNonNull(bigTableDataProperties, "BigTableDataProperties cannot be null.");
    }

    public void save(SensorData data) {
        String rowKey = PREFIX + DELIMITER + data.getCreatedAt();

        String causeOfAnomaly = Objects.isNull(data.getCauseOfAnomaly()) ? "" : data.getCauseOfAnomaly();

        RowMutation mutation = RowMutation.create(bigTableDataProperties.getTableId(), rowKey)
                .setCell(ID.name(), ID.getValue(), String.valueOf(data.getId()))
                .setCell(TEMPERATURE_C.name(), TEMPERATURE_C.getValue(), String.valueOf(data.getTemperature()))
                .setCell(HUMIDITY.name(), HUMIDITY.getValue(), String.valueOf(data.getHumidity()))
                .setCell(CAUSE_OF_ANOMALY.name(), CAUSE_OF_ANOMALY.getValue(), causeOfAnomaly)
                .setCell(TIMESTAMP.name(), TIMESTAMP.getValue(), data.getCreatedAt());

        dataClient.mutateRow(mutation);
    }
}
