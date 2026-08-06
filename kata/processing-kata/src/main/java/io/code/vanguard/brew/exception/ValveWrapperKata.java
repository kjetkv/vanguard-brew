package io.code.vanguard.brew.exception;


import io.code.vanguard.brew.BasicKata;

import static io.code.vanguard.brew.exception.ValveWrapperKata.LegacyValve;

public class ValveWrapperKata implements BasicKata<LegacyValve, Integer> {

    public static class LowWaterPressureException extends Exception {
        public LowWaterPressureException(String message) {
            super(message);
        }
    }

    public static class FactoryHaltedRuntimeException extends RuntimeException {
        public FactoryHaltedRuntimeException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @FunctionalInterface
    public interface LegacyValve {
        int readPressure() throws LowWaterPressureException;
    }

    @Override
    public Integer solve(LegacyValve valve) {
        return null;
    }
}