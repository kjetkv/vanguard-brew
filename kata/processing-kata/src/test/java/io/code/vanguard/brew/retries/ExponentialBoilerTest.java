package io.code.vanguard.brew.retries;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Validators.verifySameExceptionClassAndMessage;
import static io.code.vanguard.brew.Validators.verifyWrappedSameExceptionClassAndMessage;
import static io.code.vanguard.brew.retries.ExponentialBoilerKata.ConnectionTimeoutException;
import static io.code.vanguard.brew.retries.ExponentialBoilerKata.IgnitionSequence;
import static io.code.vanguard.brew.retries.ExponentialBoilerKata.NetworkBoiler;
import static io.code.vanguard.brew.retries.ExponentialBoilerKata.NetworkSaturatedRuntimeException;

@DisplayName("Retry Patterns - Exponential Boiler")
@Tag("Resiliency")
@Tag("Retries")
public class ExponentialBoilerTest extends BasicKataTestBase {

    private static class MockNetworkBoiler implements NetworkBoiler {
        private int attempts = 0;
        private long lastConnectTime = 0;
        private boolean exponentialCurveMaintained = true;

        private final int requiredAttempts;
        private final long baseDelay;
        private final String successMsg;
        private final ConnectionTimeoutException fault;

        public MockNetworkBoiler(int requiredAttempts, long baseDelay, String successMsg, ConnectionTimeoutException fault) {
            this.requiredAttempts = requiredAttempts;
            this.baseDelay = baseDelay;
            this.successMsg = successMsg;
            this.fault = fault;
        }

        @Override
        public String connect() throws ConnectionTimeoutException {
            long now = System.currentTimeMillis();

            if (attempts > 0) {
                long expectedDelay = baseDelay * (1L << (attempts - 1));
                long timeElapsed = now - lastConnectTime;

                if (timeElapsed < (expectedDelay - 5)) {
                    exponentialCurveMaintained = false;
                }
            }

            lastConnectTime = now;
            attempts++;

            if (attempts < requiredAttempts) {
                throw fault;
            }
            return successMsg;
        }

        public String getTelemetry() {
            return "Attempts: " + attempts + " | Curve: " + (exponentialCurveMaintained ? "VERIFIED" : "BROKEN");
        }

        @Override
        public String toString() {
            return "NetworkBoiler[" +
                    "requiredAttempts=" + requiredAttempts +
                    ", baseDelay=" + baseDelay +
                    ']';
        }
    }

    @Test
    @DisplayName("Successfully connects to the central server instantly on the first attempt with zero delay.")
    @Order(1)
    void testSucceedsFirstTimeNoDelay() {
        var boiler = new MockNetworkBoiler(
                1,
                10,
                "SYNC_COMPLETE",
                new ConnectionTimeoutException("Ping timeout."));

        var sequence = new IgnitionSequence(boiler, 10);

        verifyBasicKata(
                new ExponentialBoilerKata(),
                sequence,
                "SYNC_COMPLETE",
                Objects::equals
        );

        verifyClass(boiler,
                MockNetworkBoiler::getTelemetry,
                "Attempts: 1 | Curve: VERIFIED",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Overcomes initial network congestion, executing exactly one base-delay backoff before syncing.")
    @Order(2)
    void testSucceedsSecondTimeBaseDelay() {
        var boiler = new MockNetworkBoiler(
                2,
                10,
                "SYNC_COMPLETE",
                new ConnectionTimeoutException("Ping timeout."));

        var sequence = new IgnitionSequence(boiler, 10);

        verifyBasicKata(
                new ExponentialBoilerKata(),
                sequence,
                "SYNC_COMPLETE",
                Objects::equals
        );

        verifyClass(
                boiler,
                MockNetworkBoiler::getTelemetry,
                "Attempts: 2 | Curve: VERIFIED",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Executes a doubled backoff delay (2x) after the second failure before syncing on the third try.")
    @Order(3)
    void testSucceedsThirdTimeDoubledDelay() {
        var boiler = new MockNetworkBoiler(
                3,
                10,
                "SYNC_COMPLETE",
                new ConnectionTimeoutException("Ping timeout."));
        var sequence = new IgnitionSequence(boiler, 10);

        verifyBasicKata(
                new ExponentialBoilerKata(),
                sequence,
                "SYNC_COMPLETE",
                Objects::equals
        );

        verifyClass(
                boiler,
                MockNetworkBoiler::getTelemetry,
                "Attempts: 3 | Curve: VERIFIED",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Executes a quadrupled backoff delay (4x) after the third failure before syncing on the final try.")
    @Order(4)
    void testSucceedsFourthTimeQuadrupledDelay() {
        var boiler = new MockNetworkBoiler(
                4,
                10,
                "SYNC_COMPLETE",
                new ConnectionTimeoutException("Ping timeout."));
        var sequence = new IgnitionSequence(boiler, 10);

        verifyBasicKata(
                new ExponentialBoilerKata(),
                sequence,
                "SYNC_COMPLETE",
                Objects::equals
        );

        verifyClass(
                boiler,
                MockNetworkBoiler::getTelemetry,
                "Attempts: 4 | Curve: VERIFIED",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Safely halts boiler sequence after four failed attempts while strictly adhering to the exponential curve.")
    @Order(5)
    void testFailsAfterFourAttemptsMaintainsCurve() {
        var boiler = new MockNetworkBoiler(
                5,
                10,
                "SYNC_COMPLETE",
                new ConnectionTimeoutException("Server offline."));
        var sequence = new IgnitionSequence(boiler, 10);

        verifyException(
                () -> new ExponentialBoilerKata().solve(sequence),
                new NetworkSaturatedRuntimeException(
                        "Boiler network permanently saturated after 4 attempts.",
                        new ConnectionTimeoutException("Server offline.")
                ),
                verifyWrappedSameExceptionClassAndMessage
        );

        verifyClass(
                boiler,
                MockNetworkBoiler::getTelemetry,
                "Attempts: 4 | Curve: VERIFIED",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Immediately honors an external thread interruption signal during a long exponential sleep cycle.")
    @Order(6)
    void testHandlesInterruptedException() {
        var boiler = new MockNetworkBoiler(
                3,
                5000,
                "SYNC_COMPLETE",
                new ConnectionTimeoutException("Ping timeout."));
        var sequence = new IgnitionSequence(boiler, 5000);

        Thread executingThread = Thread.currentThread();

        new Thread(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
            executingThread.interrupt();
        }).start();

        verifyException(
                () -> new ExponentialBoilerKata().solve(sequence),
                new NetworkSaturatedRuntimeException(
                        "Network sync interrupted by system override.",
                        new InterruptedException()
                ),
                verifySameExceptionClassAndMessage
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