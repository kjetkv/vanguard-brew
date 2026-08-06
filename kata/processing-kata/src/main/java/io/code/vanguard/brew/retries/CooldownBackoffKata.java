package io.code.vanguard.brew.retries;

import io.code.vanguard.brew.BasicKata;

import static io.code.vanguard.brew.retries.CooldownBackoffKata.IgnitionSequence;

public class CooldownBackoffKata implements BasicKata<IgnitionSequence, String> {

    public static class MotorStallException extends Exception {
        public MotorStallException(String message) {
            super(message);
        }
    }

    public static class ConveyorHaltedRuntimeException extends RuntimeException {
        public ConveyorHaltedRuntimeException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @FunctionalInterface
    public interface ThermalMotor {
        String ignite() throws MotorStallException;
    }

    public record IgnitionSequence(ThermalMotor motor, long cooldownMillis) { }

    @Override
    public String solve(IgnitionSequence sequence) {
        return null;
    }
}