package com.lemon.app.client;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.lemon.app.model.SensorData;
import com.lemon.app.properties.BigTableDataProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.lemon.app.model.SensorConstants.HUMIDITY;
import static com.lemon.app.model.SensorConstants.TEMPERATURE_C;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DHT11 Sensor Data Client Tests")
class DHT11SensorDataClientTest {

    @Mock
    private BigtableDataClient dataClient;

    @Mock
    private BigTableDataProperties bigTableDataProperties;

    private DHT11SensorDataClient client;

    private static final String TABLE_ID = "test-table";

    @BeforeEach
    void setUp() {
        client = new DHT11SensorDataClient(dataClient, bigTableDataProperties);
    }

    @Test
    @DisplayName("Should throw NullPointerException when dataClient is null.")
    void shouldThrowNullPointerExceptionWhenDataClientIsNullTest() {
        assertThrows(
                NullPointerException.class,
                () -> new DHT11SensorDataClient(null, bigTableDataProperties)
        );
    }

    @Test
    @DisplayName("Should throw NullPointerException when bigTableDataProperties is null.")
    void shouldThrowNullPointerExceptionWhenBigTableDataPropertiesIsNullTest() {
        assertThrows(
                NullPointerException.class,
                () -> new DHT11SensorDataClient(dataClient, null)
        );
    }

    @Test
    @DisplayName("Should save sensor data to BigTable successfully.")
    void shouldSaveSensorDataToBigtableSuccessfullyTest() {
        when(bigTableDataProperties.getTableId()).thenReturn(TABLE_ID);
        when(bigTableDataProperties.getColumnFamilyTempC()).thenReturn(TEMPERATURE_C.getValue());
        when(bigTableDataProperties.getColumnFamilyHum()).thenReturn(HUMIDITY.getValue());

        SensorData sensorData = new SensorData(25.5, 60.0);
        client.save(sensorData, UUID.randomUUID(), System.currentTimeMillis());
        ArgumentCaptor<RowMutation> mutationCaptor = ArgumentCaptor.forClass(RowMutation.class);
        verify(dataClient, times(1)).mutateRow(mutationCaptor.capture());
        RowMutation capturedMutation = mutationCaptor.getValue();
        assertNotNull(capturedMutation);
    }

    @Test
    @DisplayName("Should call mutateRow with correct parameters.")
    void shouldCallMutateRowWithCorrectParametersTest() {
        when(bigTableDataProperties.getTableId()).thenReturn(TABLE_ID);
        when(bigTableDataProperties.getColumnFamilyTempC()).thenReturn(TEMPERATURE_C.getValue());
        when(bigTableDataProperties.getColumnFamilyHum()).thenReturn(HUMIDITY.getValue());

        SensorData sensorData = new SensorData(25.5, 60.0);
        client.save(sensorData, UUID.randomUUID(), System.currentTimeMillis());
        verify(dataClient, times(1)).mutateRow(any(RowMutation.class));
    }
}
