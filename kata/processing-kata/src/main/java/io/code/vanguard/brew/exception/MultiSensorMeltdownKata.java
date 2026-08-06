package io.code.vanguard.brew.exception;


import io.code.vanguard.brew.BasicKata;

import static io.code.vanguard.brew.exception.MultiSensorMeltdownKata.BoilerSensor;

public class MultiSensorMeltdownKata implements BasicKata<BoilerSensor, String> {

    public static class OverheatingException extends RuntimeException {
        public OverheatingException(String message) {
            super(message);
        }
    }

    public static class DepressurizationException extends RuntimeException {
        public DepressurizationException(String message) {
            super(message);
        }
    }

    public static class GeneralPowerFaultException extends RuntimeException {
        public GeneralPowerFaultException(String message) {
            super(message);
        }
    }

    @FunctionalInterface
    public interface BoilerSensor {
        String readStatus() throws OverheatingException, DepressurizationException;
    }

    @Override
    public String solve(BoilerSensor sensor) {
        return null;
    }
}