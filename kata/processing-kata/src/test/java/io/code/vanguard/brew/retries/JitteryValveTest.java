package io.code.vanguard.brew.retries;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Validators.verifyWrappedSameExceptionClassAndMessage;
import static io.code.vanguard.brew.retries.JitteryValveKata.IgnitionSequence;
import static io.code.vanguard.brew.retries.JitteryValveKata.JammedValve;
import static io.code.vanguard.brew.retries.JitteryValveKata.PressureOverloadRuntimeException;
import static io.code.vanguard.brew.retries.JitteryValveKata.ValveJammedException;

@DisplayName("Retry Patterns - Jittery Valve")
@Tag("Resiliency")
@Tag("Retries")
public class JitteryValveTest extends BasicKataTestBase {


    private static class MockJammedValve implements JammedValve {
        private int attempts = 0;
        private long lastOpenTime = 0;
        private boolean jitterProfileMaintained = true;

        private final int requiredAttempts;
        private final long baseDelay;
        private final long maxJitter;
        private final String successMsg;
        private final ValveJammedException fault;

        public MockJammedValve(int requiredAttempts, long baseDelay, long maxJitter, String successMsg, ValveJammedException fault) {
            this.requiredAttempts = requiredAttempts;
            this.baseDelay = baseDelay;
            this.maxJitter = maxJitter;
            this.successMsg = successMsg;
            this.fault = fault;
        }

        @Override
        public String open() throws ValveJammedException {
            long now = System.currentTimeMillis();

            if (attempts > 0) {
                long expectedBaseDelay = baseDelay * (1L << (attempts - 1));
                long timeElapsed = now - lastOpenTime;

                long minExpected = expectedBaseDelay - 5;
                long maxExpected = expectedBaseDelay + maxJitter + 100;

                if (timeElapsed < minExpected || timeElapsed > maxExpected) {
                    jitterProfileMaintained = false;
                }
            }

            lastOpenTime = now;
            attempts++;

            if (attempts < requiredAttempts) {
                throw fault;
            }
            return successMsg;
        }

        public String getTelemetry() {
            return "Attempts: " + attempts + " | Jitter Profile: " + (jitterProfileMaintained ? "VERIFIED" : "BROKEN");
        }
    }

    @Test
    @DisplayName("Successfully opens a pristine pneumatic valve instantly with zero backoff delay.")
    @Order(1)
    void testSucceedsFirstTimeNoDelay() {
        var valve = new MockJammedValve(
                1,
                10,
                5,
                "VALVE_OPEN_500G_FLOW",
                new ValveJammedException("Air lock."));
        var sequence = new IgnitionSequence(valve, 10, 5);

        verifyBasicKata(
                new JitteryValveKata(),
                sequence,
                "VALVE_OPEN_500G_FLOW",
                Objects::equals
        );

        verifyClass(
                valve,
                MockJammedValve::getTelemetry,
                "Attempts: 1 | Jitter Profile: VERIFIED",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Overcomes initial pneumatic resistance, applying base delay plus random jitter before opening.")
    @Order(2)
    void testSucceedsSecondTimeWithJitter() {
        var valve = new MockJammedValve(
                2,
                20,
                10,
                "VALVE_OPEN_1000G_FLOW",
                new ValveJammedException("Air lock."));
        var sequence = new IgnitionSequence(valve, 20, 10);

        verifyBasicKata(
                new JitteryValveKata(),
                sequence,
                "VALVE_OPEN_1000G_FLOW",
                Objects::equals
        );

        verifyClass(
                valve,
                MockJammedValve::getTelemetry,
                "Attempts: 2 | Jitter Profile: VERIFIED",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Executes a doubled base delay combined with dynamic jitter before clearing a severe 5000g blockage.")
    @Order(3)
    void testSucceedsThirdTimeWithExponentialJitter() {
        var valve = new MockJammedValve(
                3,
                10, 10,
                "VALVE_OPEN_5000G_FLOW",
                new ValveJammedException("Physical debris."));
        var sequence = new IgnitionSequence(valve, 10, 10);

        verifyBasicKata(
                new JitteryValveKata(),
                sequence,
                "VALVE_OPEN_5000G_FLOW",
                Objects::equals
        );

        verifyClass(
                valve,
                MockJammedValve::getTelemetry,
                "Attempts: 3 | Jitter Profile: VERIFIED",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Safely halts the distribution line after four failed attempts while strictly adhering to the jitter profile.")
    @Order(4)
    void testFailsAfterFourAttemptsMaintainsJitterProfile() {
        var valve = new MockJammedValve(5,
                10,
                10,
                "VALVE_OPEN",
                new ValveJammedException("Main seal ruptured."));
        var sequence = new IgnitionSequence(valve, 10, 10);

        verifyException(
                () -> new JitteryValveKata().solve(sequence),
                new PressureOverloadRuntimeException(
                        "Pneumatic valve permanently jammed after 4 attempts.",
                        new ValveJammedException("Main seal ruptured.")
                ),
                verifyWrappedSameExceptionClassAndMessage
        );

        verifyClass(
                valve,
                MockJammedValve::getTelemetry,
                "Attempts: 4 | Jitter Profile: VERIFIED",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Immediately honors an external thread interruption signal during a randomized sleep cycle.")
    @Order(5)
    void testHandlesInterruptedException() {
        var valve = new MockJammedValve(
                3,
                5000,
                1000,
                "VALVE_OPEN",
                new ValveJammedException("Air lock."));
        var sequence = new IgnitionSequence(valve, 5000, 1000);

        Thread executingThread = Thread.currentThread();

        new Thread(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
            executingThread.interrupt();
        }).start();

        verifyException(
                () -> new JitteryValveKata().solve(sequence),
                new PressureOverloadRuntimeException(
                        "Valve sequence forcefully interrupted.",
                        new InterruptedException("sleep interrupted")
                ),
                verifyWrappedSameExceptionClassAndMessage
        );

        verifyClass(
                Thread.currentThread(),
                t -> {
                    boolean state = t.isInterrupted();
                    Thread.interrupted();
                    return state;
                },
                true,
                Objects::equals
        );
    }
}