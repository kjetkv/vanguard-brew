package io.code.vanguard.brew.retries;

import io.code.vanguard.brew.BasicKata;

import static io.code.vanguard.brew.retries.ExponentialBoilerKata.IgnitionSequence;

public class ExponentialBoilerKata implements BasicKata<IgnitionSequence, String> {

    public static class ConnectionTimeoutException extends Exception {
        public ConnectionTimeoutException(String message) {
            super(message);
        }
    }

    public static class NetworkSaturatedRuntimeException extends RuntimeException {
        public NetworkSaturatedRuntimeException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @FunctionalInterface
    public interface NetworkBoiler {
        String connect() throws ConnectionTimeoutException;
    }

    public record IgnitionSequence(NetworkBoiler boiler, long baseDelayMillis) { }

    @Override
    public String solve(IgnitionSequence sequence) {
        return null;
    }
}