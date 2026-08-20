package io.code.vanguard.brew.resources;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Validators.verifySameExceptionClassAndMessage;
import static io.code.vanguard.brew.resources.LegacyValveKata.FlowException;
import static io.code.vanguard.brew.resources.LegacyValveKata.LegacyValve;
import static io.code.vanguard.brew.resources.LegacyValveKata.OperationResult;

@DisplayName("Resources - Legacy Basement Valves")
@Tag("Resiliency")
@Tag("Resources")
public class LegacyValveTest extends BasicKataTestBase {

    private static class MockLegacyValve implements LegacyValve {
        private final Integer successfulPull;
        private final Exception checkedFault;
        private final RuntimeException runtimeFault;
        private final Error systemError;
        private boolean closed = false;

        public MockLegacyValve(Integer successfulPull,
                               Exception checkedFault,
                               RuntimeException runtimeFault,
                               Error systemError) {
            this.successfulPull = successfulPull == null ? 0 : successfulPull;
            this.checkedFault = checkedFault;
            this.runtimeFault = runtimeFault;
            this.systemError = systemError;
        }


        public MockLegacyValve(Exception checkedFault) {
            this(null, checkedFault, null, null);
        }

        public MockLegacyValve(RuntimeException runtimeFault) {
            this(null, null, runtimeFault, null);
        }

        public MockLegacyValve(Error systemError) {
            this(null, null, null, systemError);
        }

        public MockLegacyValve(Integer successfulPull) {
            this(successfulPull, null, null, null);
        }

        @Override
        public int pullWater() throws FlowException {
            if (systemError != null) {
                throw systemError;
            }
            if (runtimeFault != null) {
                throw runtimeFault;
            }
            if (checkedFault instanceof FlowException) {
                throw (FlowException) checkedFault;
            }
            return successfulPull;
        }

        @Override
        public void closeValve() {
            this.closed = true;
        }

        @Override
        public boolean isClosed() {
            return closed;
        }

        @Override
        public String toString() {
            return "LegacyValve[Closed: " + closed + "]";
        }
    }

    @Test
    @DisplayName("Successfully draws a standard 250g water batch and securely seals the valve afterwards.")
    @Order(1)
    void testPullsStandardWaterAndCloses() {
        verifyBasicKata(
                new LegacyValveKata(),
                new MockLegacyValve(250),
                new OperationResult(250, true),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully draws a large 1000g water batch and securely seals the valve afterwards.")
    @Order(2)
    void testPullsLargeWaterAndCloses() {
        verifyBasicKata(
                new LegacyValveKata(),
                new MockLegacyValve(1000),
                new OperationResult(1000, true),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully draws a massive 5000g industrial water batch and securely seals the valve afterwards.")
    @Order(3)
    void testPullsMassiveWaterAndCloses() {
        verifyBasicKata(
                new LegacyValveKata(),
                new MockLegacyValve(5000),
                new OperationResult(5000, true),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Safely halts water draw during a minor flow interruption, defaults to 0g, and securely seals the valve.")
    @Order(4)
    void testCatchesMinorFlowExceptionAndCloses() {
        verifyBasicKata(
                new LegacyValveKata(),
                new MockLegacyValve(new FlowException("Minor air pocket detected.")),
                new OperationResult(0, true),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Safely halts water draw during severe pressure loss, defaults to 0g, and securely seals the valve.")
    @Order(5)
    void testCatchesSevereFlowExceptionAndCloses() {
        verifyBasicKata(
                new LegacyValveKata(),
                new MockLegacyValve(new FlowException("Main reservoir empty.")),
                new OperationResult(0, true),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Guarantees the physical valve is sealed shut even if the digital pipeline abruptly crashes.")
    @Order(6)
    void testClosesOnUnexpectedPipelineCrash() {
        MockLegacyValve explosiveValve = new MockLegacyValve(new NullPointerException("Sensor data missing."));

        verifyException(
                () -> new LegacyValveKata().solve(explosiveValve),
                new NullPointerException("Sensor data missing."),
                verifySameExceptionClassAndMessage
        );

        verifyClass(
                explosiveValve,
                MockLegacyValve::isClosed,
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Guarantees the physical valve is sealed shut even if the mechanical gears completely jam.")
    @Order(7)
    void testClosesOnMechanicalJam() {
        MockLegacyValve explosiveValve = new MockLegacyValve(new IllegalStateException("Valve gears jammed."));

        verifyException(
                () -> new LegacyValveKata().solve(explosiveValve),
                new IllegalStateException("Valve gears jammed."),
                verifySameExceptionClassAndMessage
        );

        verifyClass(
                explosiveValve,
                MockLegacyValve::isClosed,
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Guarantees the physical valve is sealed shut even during a catastrophic main terminal memory failure.")
    @Order(8)
    void testClosesOnSystemMemoryFailure() {
        MockLegacyValve explosiveValve = new MockLegacyValve(new OutOfMemoryError("Terminal heap space exceeded."));

        verifyException(
                () -> new LegacyValveKata().solve(explosiveValve),
                new OutOfMemoryError("Terminal heap space exceeded."),
                verifySameExceptionClassAndMessage
        );

        verifyClass(
                explosiveValve,
                MockLegacyValve::isClosed,
                true,
                Objects::equals
        );
    }
}