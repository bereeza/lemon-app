package com.lemon.app.config.bigtable;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminSettings;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.lemon.app.properties.BigTableDataProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Objects;

@Configuration
public class BigTableConfig {

    private final BigTableDataProperties bigTableDataProperties;

    public BigTableConfig(
            BigTableDataProperties bigTableDataProperties
    ) {
        this.bigTableDataProperties = Objects.requireNonNull(bigTableDataProperties, "BigTableDataProperties cannot be null.");
    }

    @Bean
    BigtableTableAdminSettings bigtableAdminDataSettings(CredentialsProvider credentialsProvider) throws IOException {
        return BigtableTableAdminSettings.newBuilder()
                .setProjectId(bigTableDataProperties.getProjectId())
                .setInstanceId(bigTableDataProperties.getInstanceId())
                .setCredentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    BigtableTableAdminClient bigtableAdminDataClient(BigtableTableAdminSettings settings) throws IOException {
        return BigtableTableAdminClient.create(settings);
    }

    @Bean
    BigtableDataSettings bigtableDataSettings(CredentialsProvider credentialsProvider) {
        return BigtableDataSettings.newBuilder()
                .setProjectId(bigTableDataProperties.getProjectId())
                .setInstanceId(bigTableDataProperties.getInstanceId())
                .setCredentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    BigtableDataClient bigtableDataClient(BigtableDataSettings settings) throws IOException {
        return BigtableDataClient.create(settings);
    }

    @Bean
    CredentialsProvider fixedCredentialsProvider() throws IOException {
        String adminCredentialsPath = bigTableDataProperties.getAdminCredentialsPath();
        if (Objects.isNull(adminCredentialsPath) || adminCredentialsPath.isBlank()) {
            return NoCredentialsProvider.create();
        }

        ClassPathResource resource = new ClassPathResource(adminCredentialsPath);
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
        return FixedCredentialsProvider.create(credentials);
    }
}
