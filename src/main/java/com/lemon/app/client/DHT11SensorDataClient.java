package com.lemon.app.client;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.lemon.app.model.SensorData;
import com.lemon.app.properties.BigTableDataProperties;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.UUID;

import static com.lemon.app.model.SensorConstants.ID;
import static com.lemon.app.model.SensorConstants.TEMPERATURE_C;
import static com.lemon.app.model.SensorConstants.HUMIDITY;

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

    public void save(SensorData data, UUID id, long timestamp) {
        String rowKey = PREFIX + DELIMITER + timestamp;

        RowMutation mutation = RowMutation.create(bigTableDataProperties.getTableId(), rowKey)
                .setCell(bigTableDataProperties.getColumnFamilyTempC(), ID.getValue(), id.toString())
                .setCell(bigTableDataProperties.getColumnFamilyTempC(), TEMPERATURE_C.getValue(), String.valueOf(data.getTemperature()))
                .setCell(bigTableDataProperties.getColumnFamilyHum(), HUMIDITY.getValue(), String.valueOf(data.getHumidity()));

        dataClient.mutateRow(mutation);
    }
}
