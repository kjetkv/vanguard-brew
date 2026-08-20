package io.code.vanguard.brew.retries;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Validators.verifyWrappedSameExceptionClassAndMessage;
import static io.code.vanguard.brew.retries.CooldownBackoffKata.ConveyorHaltedRuntimeException;
import static io.code.vanguard.brew.retries.CooldownBackoffKata.IgnitionSequence;
import static io.code.vanguard.brew.retries.CooldownBackoffKata.MotorStallException;
import static io.code.vanguard.brew.retries.CooldownBackoffKata.ThermalMotor;

@DisplayName("Retry Patterns - Cooldown Backoff")
@Tag("Resiliency")
@Tag("Retries")
public class CooldownBackoffTest extends BasicKataTestBase {

    private static class MockThermalMotor implements ThermalMotor {
        private int attempts = 0;
        private long lastIgnitionTime = 0;
        private boolean thermalSafetyMaintained = true;

        private final int requiredAttempts;
        private final long requiredCooldown;
        private final String successMsg;
        private final MotorStallException fault;

        public MockThermalMotor(int requiredAttempts, long requiredCooldown, String successMsg, MotorStallException fault) {
            this.requiredAttempts = requiredAttempts;
            this.requiredCooldown = requiredCooldown;
            this.successMsg = successMsg;
            this.fault = fault;
        }

        @Override
        public String ignite() throws MotorStallException {
            long now = System.currentTimeMillis();

            if (attempts > 0) {
                long timeElapsed = now - lastIgnitionTime;
                if (timeElapsed < (requiredCooldown - 5)) {
                    thermalSafetyMaintained = false;
                }
            }

            lastIgnitionTime = now;
            attempts++;

            if (attempts < requiredAttempts) {
                throw fault;
            }
            return successMsg;
        }

        public String getTelemetry() {
            return "Ignitions: " + attempts + " | Thermal Safety: " + (thermalSafetyMaintained ? "VERIFIED" : "COMPROMISED");
        }

        @Override
        public String toString() {
            return "ThermalMotor[" +
                    "requiredAttempts=" + requiredAttempts +
                    ", requiredCooldown=" + requiredCooldown +
                    ']';
        }
    }

    @Test
    @DisplayName("Successfully ignites pristine motor instantly with absolutely zero cooldown delay.")
    @Order(1)
    void testSucceedsFirstTimeNoDelay() {
        var motor = new MockThermalMotor(1, 50, "THERMAL_NOMINAL", new MotorStallException("Heat spike."));
        var sequence = new IgnitionSequence(motor, 50);

        verifyBasicKata(
                new CooldownBackoffKata(),
                sequence,
                "THERMAL_NOMINAL",
                Objects::equals
        );

        verifyClass(
                motor,
                MockThermalMotor::getTelemetry,
                "Ignitions: 1 | Thermal Safety: VERIFIED",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Overcomes initial friction, executing exactly one cooldown cycle before successful second ignition.")
    @Order(2)
    void testSucceedsSecondTimeOneDelay() {
        var motor = new MockThermalMotor(2, 20, "THERMAL_NOMINAL", new MotorStallException("Heat spike."));
        var sequence = new IgnitionSequence(motor, 20);

        verifyBasicKata(
                new CooldownBackoffKata(),
                sequence,
                "THERMAL_NOMINAL",
                Objects::equals
        );

        verifyClass(
                motor,
                MockThermalMotor::getTelemetry,
                "Ignitions: 2 | Thermal Safety: VERIFIED",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Pushes through severe friction, executing exactly two cooldown cycles before successful final ignition.")
    @Order(3)
    void testSucceedsThirdTimeTwoDelays() {
        var motor = new MockThermalMotor(3, 20, "THERMAL_NOMINAL", new MotorStallException("Heat spike."));
        var sequence = new IgnitionSequence(motor, 20);

        verifyBasicKata(
                new CooldownBackoffKata(),
                sequence,
                "THERMAL_NOMINAL",
                Objects::equals
        );

        verifyClass(
                motor,
                MockThermalMotor::getTelemetry,
                "Ignitions: 3 | Thermal Safety: VERIFIED",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Safely halts conveyor after three failed attempts without applying an unnecessary post-failure sleep.")
    @Order(4)
    void testFailsAfterThreeAttemptsTwoDelays() {
        var motor = new MockThermalMotor(4, 20, "THERMAL_NOMINAL", new MotorStallException("Thermal meltdown imminent."));
        var sequence = new IgnitionSequence(motor, 20);

        verifyException(
                () -> new CooldownBackoffKata().solve(sequence),
                new ConveyorHaltedRuntimeException(
                        "Motor permanently stalled after 3 attempts.",
                        new MotorStallException("Thermal meltdown imminent.")
                ),
                verifyWrappedSameExceptionClassAndMessage
        );

        verifyClass(
                motor,
                MockThermalMotor::getTelemetry,
                "Ignitions: 3 | Thermal Safety: VERIFIED",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Immediately honors an external thread interruption signal and restores the system interrupt flag.")
    @Order(5)
    void testHandlesInterruptedException() {
        var motor = new MockThermalMotor(3, 5000, "THERMAL_NOMINAL", new MotorStallException("Heat spike."));
        var sequence = new IgnitionSequence(motor, 5000);

        Thread executingThread = Thread.currentThread();

        new Thread(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
            executingThread.interrupt();
        }).start();

        verifyException(
                () -> new CooldownBackoffKata().solve(sequence),
                new ConveyorHaltedRuntimeException(
                        "Ignition interrupted by system override.",
                        new InterruptedException("sleep interrupted")
                ),
                verifyWrappedSameExceptionClassAndMessage
        );

        verifyClass(
                Thread.currentThread(),
                _ -> Thread.interrupted(),
                true,
                Objects::equals
        );
    }
}
