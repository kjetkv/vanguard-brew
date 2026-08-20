package io.code.vanguard.brew.retries;

import io.code.vanguard.brew.BasicKata;

import static io.code.vanguard.brew.retries.JitteryValveKata.IgnitionSequence;

public class JitteryValveKata implements BasicKata<IgnitionSequence, String> {

    public static class ValveJammedException extends Exception {
        public ValveJammedException(String message) {
            super(message);
        }
    }

    public static class PressureOverloadRuntimeException extends RuntimeException {
        public PressureOverloadRuntimeException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @FunctionalInterface
    public interface JammedValve {
        String open() throws ValveJammedException;
    }

    public record IgnitionSequence(JammedValve valve, long baseDelayMillis, long maxJitterMillis) { }

    @Override
    public String solve(IgnitionSequence sequence) {
        return null;
    }
}