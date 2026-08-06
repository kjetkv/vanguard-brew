package io.code.vanguard.brew.exception;


import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Validators.verifyWrappedSameExceptionClassAndMessage;
import static io.code.vanguard.brew.exception.ValveWrapperKata.FactoryHaltedRuntimeException;
import static io.code.vanguard.brew.exception.ValveWrapperKata.LegacyValve;
import static io.code.vanguard.brew.exception.ValveWrapperKata.LowWaterPressureException;

@DisplayName("Exceptions - Valve Wrapper")
@Tag("Resiliency")
@Tag("Exceptions")
public class ValveWrapperTest extends BasicKataTestBase {

    private static class MockValve implements LegacyValve {
        private final Integer pressure;
        private final LowWaterPressureException exception;

        public MockValve(Integer pressure) {
            this.pressure = pressure;
            this.exception = null;
        }

        public MockValve(LowWaterPressureException exception) {
            this.pressure = 0;
            this.exception = exception;
        }

        @Override
        public int readPressure() throws LowWaterPressureException {
            if (exception != null) {
                throw exception;
            }
            return pressure;
        }

        @Override
        public String toString() {
            if (exception != null) {
                return "Status: FAULT, Reason: " + exception.getMessage();
            }
            return "Status: OK, Pressure: " + pressure + " PSI";
        }
    }

    @Test
    @DisplayName("Successfully reads a low but safe water pressure.")
    @Order(1)
    void testReadsLowSafePressure() {
        verifyBasicKata(
                new ValveWrapperKata(),
                new MockValve(50),
                50,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully reads standard operational water pressure.")
    @Order(2)
    void testReadsStandardPressure() {
        verifyBasicKata(
                new ValveWrapperKata(),
                new MockValve(120),
                120,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully reads high operational water pressure.")
    @Order(3)
    void testReadsHighPressure() {
        verifyBasicKata(
                new ValveWrapperKata(),
                new MockValve(300),
                300,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully reads the maximum rated water pressure.")
    @Order(4)
    void testReadsMaxPressure() {
        verifyBasicKata(
                new ValveWrapperKata(),
                new MockValve(800),
                800,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully reads industrial scale water pressure.")
    @Order(5)
    void testReadsIndustrialPressure() {
        verifyBasicKata(
                new ValveWrapperKata(),
                new MockValve(1500),
                1500,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Translates exception when city water supply is interrupted.")
    @Order(6)
    void testWrapsInterruptedSupplyException() {
        verifyException(
                () -> new ValveWrapperKata().solve(new MockValve(new LowWaterPressureException("City water supply interrupted."))),
                new FactoryHaltedRuntimeException(
                        "Pipeline halted due to legacy valve failure.",
                        new LowWaterPressureException("City water supply interrupted.")
                ),
                verifyWrappedSameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Translates exception when pressure drops below operational minimum.")
    @Order(7)
    void testWrapsPressureDropException() {
        verifyException(
                () -> new ValveWrapperKata().solve(new MockValve(new LowWaterPressureException("Pressure dropped below 20 PSI."))),
                new FactoryHaltedRuntimeException(
                        "Pipeline halted due to legacy valve failure.",
                        new LowWaterPressureException("Pressure dropped below 20 PSI.")
                ),
                verifyWrappedSameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Translates exception when cavitation is detected in the main line.")
    @Order(8)
    void testWrapsCavitationException() {
        verifyException(
                () -> new ValveWrapperKata().solve(new MockValve(new LowWaterPressureException("Cavitation detected in main line."))),
                new FactoryHaltedRuntimeException(
                        "Pipeline halted due to legacy valve failure.",
                        new LowWaterPressureException("Cavitation detected in main line.")
                ),
                verifyWrappedSameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Translates exception when the physical valve actuator is frozen.")
    @Order(9)
    void testWrapsFrozenActuatorException() {
        verifyException(
                () -> new ValveWrapperKata().solve(new MockValve(new LowWaterPressureException("Valve actuator frozen."))),
                new FactoryHaltedRuntimeException(
                        "Pipeline halted due to legacy valve failure.",
                        new LowWaterPressureException("Valve actuator frozen.")
                ),
                verifyWrappedSameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Translates exception when a vacuum state causes negative pressure.")
    @Order(10)
    void testWrapsNegativePressureException() {
        verifyException(
                () -> new ValveWrapperKata().solve(new MockValve(new LowWaterPressureException("Negative pressure vacuum state."))),
                new FactoryHaltedRuntimeException(
                        "Pipeline halted due to legacy valve failure.",
                        new LowWaterPressureException("Negative pressure vacuum state.")
                ),
                verifyWrappedSameExceptionClassAndMessage
        );
    }
}