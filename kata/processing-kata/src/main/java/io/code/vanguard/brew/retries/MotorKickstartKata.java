package io.code.vanguard.brew.retries;

import io.code.vanguard.brew.BasicKata;

import static io.code.vanguard.brew.retries.MotorKickstartKata.HeavyMotor;

public class MotorKickstartKata implements BasicKata<HeavyMotor, String> {

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
    public interface HeavyMotor {
        String ignite() throws MotorStallException;
    }

    @Override
    public String solve(HeavyMotor motor) {
        return null;
    }
}