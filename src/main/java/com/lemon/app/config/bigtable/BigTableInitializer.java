package com.lemon.app.config.bigtable;

import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.lemon.app.properties.BigTableDataProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class BigTableInitializer {

    private static final Logger logger = LoggerFactory.getLogger(BigTableInitializer.class);

    private final BigtableTableAdminClient adminClient;

    private final BigTableDataProperties bigTableDataProperties;

    public BigTableInitializer(
            BigtableTableAdminClient adminClient,
            BigTableDataProperties bigTableDataProperties
    ) {
        this.adminClient = Objects.requireNonNull(adminClient, "BigtableTableAdminClient cannot be null.");
        this.bigTableDataProperties = Objects.requireNonNull(bigTableDataProperties, "BigTableDataProperties cannot be null.");
    }

    @PostConstruct
    public void initTable() {
        try {
            if (!adminClient.exists(bigTableDataProperties.getTableId())) {
                logger.info("Table {} does not exist. Creating...", bigTableDataProperties.getTableId());
                createTable();
            } else {
                logger.info("Table {} already exists.", bigTableDataProperties.getTableId());
            }
        } catch (Exception e) {
            logger.debug("BigTable initialization error.", e);
        }
    }

    private void createTable() {
        CreateTableRequest req = CreateTableRequest.of(bigTableDataProperties.getTableId())
                .addFamily(bigTableDataProperties.getColumnFamilyTempC())
                .addFamily(bigTableDataProperties.getColumnFamilyHum());

        adminClient.createTable(req);
        logger.info("Table {} created", bigTableDataProperties.getTableId());
    }
}
