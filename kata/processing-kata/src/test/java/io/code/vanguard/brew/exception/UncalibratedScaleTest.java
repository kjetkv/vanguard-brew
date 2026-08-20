package io.code.vanguard.brew.exception;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.exception.UncalibratedScaleKata.HardwareScale;
import static io.code.vanguard.brew.exception.UncalibratedScaleKata.SensorCalibrationException;

@DisplayName("Exceptions - Uncalibrated Scale")
@Tag("Resiliency")
@Tag("Exceptions")
public class UncalibratedScaleTest extends BasicKataTestBase {

    private static class MockScale implements HardwareScale {
        private final Integer successfulMass;
        private final SensorCalibrationException failureException;

        public MockScale(Integer successfulMass) {
            this.successfulMass = successfulMass;
            this.failureException = null;
        }

        public MockScale(SensorCalibrationException failureException) {
            this.successfulMass = 0;
            this.failureException = failureException;
        }

        @Override
        public int readMass() throws SensorCalibrationException {
            if (failureException != null) {
                throw failureException;
            }
            return successfulMass;
        }

        @Override
        public String toString() {
            if (failureException != null) {
                return "Status: ERROR, Reason: " + failureException.getMessage();
            }
            return "Status: OK, Reading: " + successfulMass + "g";
        }
    }

    @Test
    @DisplayName("Successfully reads an empty scale returning 0g.")
    @Order(1)
    void testScaleReadsZero() {
        verifyBasicKata(
                new UncalibratedScaleKata(),
                new MockScale(0),
                0,
                Objects::equals);
    }

    @Test
    @DisplayName("Successfully reads a delicate 15g measurement.")
    @Order(2)
    void testScaleReadsSmallDose() {
        verifyBasicKata(
                new UncalibratedScaleKata(),
                new MockScale(15),
                15,
                Objects::equals);
    }

    @Test
    @DisplayName("Successfully reads a standard 250g batch.")
    @Order(3)
    void testScaleReadsStandardDose() {
        verifyBasicKata(
                new UncalibratedScaleKata(),
                new MockScale(250),
                250,
                Objects::equals);
    }

    @Test
    @DisplayName("Successfully reads a large 1000g hopper load.")
    @Order(4)
    void testScaleReadsLargeDose() {
        verifyBasicKata(
                new UncalibratedScaleKata(),
                new MockScale(1000),
                1000,
                Objects::equals);
    }

    @Test
    @DisplayName("Successfully reads a massive 5000g industrial payload.")
    @Order(5)
    void testScaleReadsIndustrialDose() {
        verifyBasicKata(
                new UncalibratedScaleKata(),
                new MockScale(5000),
                5000,
                Objects::equals);
    }

    @Test
    @DisplayName("Intercepts a minor 1mm misalignment panic and safely reports 0g.")
    @Order(6)
    void testScaleFailsWithMinorMisalignment() {
        verifyBasicKata(
                new UncalibratedScaleKata(),
                new MockScale(new SensorCalibrationException("Misaligned by 1mm")),
                0,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Intercepts a severe hardware misalignment panic and safely reports 0g.")
    @Order(7)
    void testScaleFailsWithMajorMisalignment() {
        verifyBasicKata(
                new UncalibratedScaleKata(),
                new MockScale(new SensorCalibrationException("Misaligned by 50mm")),
                0,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Intercepts a complete sensor offline panic and safely reports 0g.")
    @Order(8)
    void testScaleFailsWithDisconnectedSensor() {
        verifyBasicKata(
                new UncalibratedScaleKata(),
                new MockScale(new SensorCalibrationException("Hardware offline")),
                0,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Intercepts an impossible negative calibration panic and safely reports 0g.")
    @Order(9)
    void testScaleFailsWithNegativeCalibration() {
        verifyBasicKata(
                new UncalibratedScaleKata(),
                new MockScale(new SensorCalibrationException("Calibration offset below zero")),
                0,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Intercepts a massive power surge panic and safely reports 0g.")
    @Order(10)
    void testScaleFailsWithPowerSurge() {
        verifyBasicKata(
                new UncalibratedScaleKata(),
                new MockScale(new SensorCalibrationException("Power surge detected during read")),
                0,
                Objects::equals
        );
    }
}