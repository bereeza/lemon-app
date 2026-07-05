package com.lemon.app.client;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.models.TableId;
import com.lemon.app.model.SensorData;
import com.lemon.app.properties.BigTableDataProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Objects;

import static com.lemon.app.model.SensorConstants.*;

@Repository
public class DHT11SensorDataClient {

    private static final Logger logger = LoggerFactory.getLogger(DHT11SensorDataClient.class);
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
        try {
            String rowKey = PREFIX + DELIMITER + data.getCreatedAt();

            String causeOfAnomaly = Objects.isNull(data.getCauseOfAnomaly()) ? "" : data.getCauseOfAnomaly();

            RowMutation mutation = RowMutation.create(TableId.of(bigTableDataProperties.getTableId()), rowKey)
                    .setCell(bigTableDataProperties.getColumnFamilyTempC(), ID.getValue(), String.valueOf(data.getId()))
                    .setCell(bigTableDataProperties.getColumnFamilyTempC(), TEMPERATURE_C.getValue(), String.valueOf(data.getTemperature()))
                    .setCell(bigTableDataProperties.getColumnFamilyHum(), HUMIDITY.getValue(), String.valueOf(data.getHumidity()))
                    .setCell(bigTableDataProperties.getColumnFamilyTempC(), CAUSE_OF_ANOMALY.getValue(), causeOfAnomaly)
                    .setCell(bigTableDataProperties.getColumnFamilyTempC(), TIMESTAMP.getValue(), data.getCreatedAt());

            dataClient.mutateRow(mutation);
            logger.info("Saved sensor data to BigTable with row key: {}", rowKey);
        } catch (Exception e) {
            logger.error("Failed to save sensor data to BigTable: {}", e.getMessage(), e);
        }
    }
}
