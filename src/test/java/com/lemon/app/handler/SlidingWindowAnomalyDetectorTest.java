package com.lemon.app.handler;

import com.lemon.app.properties.AnomalyDetectorProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SlidingWindowAnomalyDetector Tests")
class SlidingWindowAnomalyDetectorTest {

    private SlidingWindowAnomalyDetector detector;

    private static final int MIN_WINDOW_SIZE = 5;
    private static final int MAX_WINDOW_SIZE = 100;
    private static final double SIGMA = 2.0;
    private static final double MIN_BOUNDS = 1.0;

    @BeforeEach
    void setUp() {
        AnomalyDetectorProperties properties = new AnomalyDetectorProperties(MIN_WINDOW_SIZE, SIGMA, MIN_BOUNDS);
        detector = new SlidingWindowAnomalyDetector(properties);
    }

    @Test
    @DisplayName("Constructor must throw NullPointerException when properties is null.")
    void constructorMustThrownNPEWhenPropertiesIsNullTest() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new SlidingWindowAnomalyDetector(null)
        );

        assertEquals("AnomalyDetectorProperties cannot be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Analyze should return false when window is not full.")
    void isAnomalyShouldReturnFalseWhenWindowIsNotFullTest() {
        for (int i = 0; i < MIN_WINDOW_SIZE - 1; i++) {
            assertFalse(detector.isAnomaly(20.0));
        }
    }

    @Test
    @DisplayName("Analyze should return true for anomalous values outside bounds.")
    void isAnomalyShouldReturnTrueForAnomalousValueTest() {
        double[] normalValues = {20.0, 21.0, 19.0, 20.5, 19.5};
        for (double value : normalValues) {
            assertFalse(detector.isAnomaly(value));
        }

        assertTrue(detector.isAnomaly(30));
    }

    @Test
    @DisplayName("Analyze should handle window sliding correctly.")
    void isAnomalyShouldHandleWindowsSlidingCorrectlyTest() {
        for (int i = 0; i < 5; i++) {
            detector.isAnomaly(20.0);
        }

        detector.isAnomaly(21.0);
        detector.isAnomaly(22.0);
        detector.isAnomaly(23.0);

        assertFalse(detector.isAnomaly(21.5));
    }

    @Test
    @DisplayName("Analyze should handle large window size.")
    void isAnomalyShouldHandleLargeWindowSizeTest() {
        AnomalyDetectorProperties largeWindowProps = new AnomalyDetectorProperties(MAX_WINDOW_SIZE, SIGMA, MIN_BOUNDS);
        SlidingWindowAnomalyDetector largeDetector = new SlidingWindowAnomalyDetector(largeWindowProps);

        for (int i = 0; i < 100; i++) {
            assertFalse(largeDetector.isAnomaly(20.0));
        }

        assertFalse(largeDetector.isAnomaly(21.0));
    }

    @Test
    @DisplayName("Analyze should handle zero sigma.")
    void isAnomalyShouldHandleZeroSigmaTest() {
        AnomalyDetectorProperties zeroSigmaProps = new AnomalyDetectorProperties(MIN_WINDOW_SIZE, 0.0, MIN_BOUNDS);
        SlidingWindowAnomalyDetector zeroSigmaDetector = new SlidingWindowAnomalyDetector(zeroSigmaProps);

        for (int i = 0; i < 5; i++) {
            zeroSigmaDetector.isAnomaly(20.0);
        }

        assertFalse(zeroSigmaDetector.isAnomaly(20.5));
        assertTrue(zeroSigmaDetector.isAnomaly(22.0));
    }

    @Test
    @DisplayName("Analyze should handle high sigma.")
    void isAnomalyShouldHandleHighSigmaTest() {
        AnomalyDetectorProperties highSigmaProps = new AnomalyDetectorProperties(MIN_WINDOW_SIZE, 10.0, MIN_BOUNDS);
        SlidingWindowAnomalyDetector highSigmaDetector = new SlidingWindowAnomalyDetector(highSigmaProps);

        double[] values = {20.0, 25.0, 15.0, 30.0, 10.0};
        for (double value : values) {
            highSigmaDetector.isAnomaly(value);
        }

        assertFalse(highSigmaDetector.isAnomaly(22.0));
    }

    @Test
    @DisplayName("Analyze should handle zero minBounds.")
    void isAnomalyShouldHandleZeroMinBoundsTest() {
        AnomalyDetectorProperties zeroMinBoundsProps = new AnomalyDetectorProperties(5, 2.0, 0.0);
        SlidingWindowAnomalyDetector zeroMinBoundsDetector = new SlidingWindowAnomalyDetector(zeroMinBoundsProps);

        double[] values = {20.0, 21.0, 19.0, 20.5, 19.5};
        for (double value : values) {
            zeroMinBoundsDetector.isAnomaly(value);
        }

        assertTrue(zeroMinBoundsDetector.isAnomaly(30));
    }

    @Test
    @DisplayName("Analyze should calculate correct mean and variance.")
    void isAnomalyShouldCalculateCorrectMeanAndVarianceTEst() {
        AnomalyDetectorProperties strictProps = new AnomalyDetectorProperties(3, 1.0, 0.1);
        SlidingWindowAnomalyDetector strictDetector = new SlidingWindowAnomalyDetector(strictProps);

        strictDetector.isAnomaly(10.0);
        strictDetector.isAnomaly(20.0);
        strictDetector.isAnomaly(30.0);

        assertTrue(strictDetector.isAnomaly(50.0));
        assertFalse(strictDetector.isAnomaly(25.0));
    }

    @Test
    @DisplayName("Analyze should handle negative values.")
    void isAnomalyShouldHandleNegativeValuesTest() {
        double[] negativeValues = {-10.0, -15.0, -12.0, -13.0, -11.0};
        for (double value : negativeValues) {
            detector.isAnomaly(value);
        }

        assertFalse(detector.isAnomaly(-12.5));
    }
}
